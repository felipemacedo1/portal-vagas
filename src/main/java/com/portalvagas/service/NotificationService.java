package com.portalvagas.service;

import com.portalvagas.domain.Application;
import com.portalvagas.domain.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${mail.from}")
    private String fromEmail;

    @Async
    public void notifyNewApplication(Application application) {
        try {
            String employerEmail = application.getJob().getCompany().getUser().getEmail();
            String subject = "Nova candidatura recebida - " + application.getJob().getTitle();
            String body = buildNewApplicationEmail(application);
            
            sendEmail(employerEmail, subject, body);
            log.info("Email de nova candidatura enviado para: {}", employerEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email de nova candidatura", e);
        }
    }

    @Async
    public void notifyJobApproved(Job job) {
        try {
            String employerEmail = job.getCompany().getUser().getEmail();
            String subject = "Vaga aprovada - " + job.getTitle();
            String body = buildJobApprovedEmail(job);
            
            sendEmail(employerEmail, subject, body);
            log.info("Email de vaga aprovada enviado para: {}", employerEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email de vaga aprovada", e);
        }
    }

    @Async
    public void notifyJobRejected(Job job) {
        try {
            String employerEmail = job.getCompany().getUser().getEmail();
            String subject = "Vaga rejeitada - " + job.getTitle();
            String body = buildJobRejectedEmail(job);
            
            sendEmail(employerEmail, subject, body);
            log.info("Email de vaga rejeitada enviado para: {}", employerEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email de vaga rejeitada", e);
        }
    }

    @Async
    public void notifyApplicationStatusChange(Application application) {
        try {
            String candidateEmail = application.getCandidate().getUser().getEmail();
            String subject = "Status da candidatura atualizado - " + application.getJob().getTitle();
            String body = buildApplicationStatusEmail(application);
            
            sendEmail(candidateEmail, subject, body);
            log.info("Email de status de candidatura enviado para: {}", candidateEmail);
        } catch (Exception e) {
            log.error("Erro ao enviar email de status de candidatura", e);
        }
    }

    private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
        mailSender.send(message);
    }

    private String buildNewApplicationEmail(Application application) {
        return String.format("""
            Olá!
            
            Você recebeu uma nova candidatura para a vaga "%s".
            
            Candidato: %s
            Empresa: %s
            Data da candidatura: %s
            
            Acesse o portal para visualizar o perfil completo do candidato.
            
            Atenciosamente,
            Portal de Vagas
            """,
            application.getJob().getTitle(),
            application.getCandidate().getFullName(),
            application.getJob().getCompany().getName(),
            application.getAppliedAt()
        );
    }

    private String buildJobApprovedEmail(Job job) {
        return String.format("""
            Parabéns!
            
            Sua vaga "%s" foi aprovada e já está visível no portal.
            
            Empresa: %s
            Data de aprovação: %s
            
            Os candidatos já podem se candidatar à sua vaga.
            
            Atenciosamente,
            Portal de Vagas
            """,
            job.getTitle(),
            job.getCompany().getName(),
            job.getUpdatedAt()
        );
    }

    private String buildJobRejectedEmail(Job job) {
        return String.format("""
            Informamos que sua vaga "%s" não foi aprovada.
            
            Empresa: %s
            
            Por favor, revise os dados da vaga e tente novamente.
            Em caso de dúvidas, entre em contato conosco.
            
            Atenciosamente,
            Portal de Vagas
            """,
            job.getTitle(),
            job.getCompany().getName()
        );
    }

    private String buildApplicationStatusEmail(Application application) {
        return String.format("""
            Olá %s!
            
            O status da sua candidatura foi atualizado.
            
            Vaga: %s
            Empresa: %s
            Novo status: %s
            
            Acesse o portal para mais detalhes.
            
            Atenciosamente,
            Portal de Vagas
            """,
            application.getCandidate().getFullName(),
            application.getJob().getTitle(),
            application.getJob().getCompany().getName(),
            application.getStatus()
        );
    }
}