# Portal de Vagas - MVP

[![Build Status](https://github.com/felipemacedo1/portal-vagas/workflows/CI/badge.svg)](https://github.com/felipemacedo1/portal-vagas/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/felipemacedo1/portal-vagas/releases)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-green.svg)](https://spring.io/projects/spring-boot)

API REST para portal de vagas com autenticação JWT e controle de acesso baseado em roles.

> 🎯 **Projeto Social**: Sistema gratuito para ONGs conectarem candidatos a oportunidades de emprego.

## Tecnologias

- Java 17
- Spring Boot 3.3.x
- PostgreSQL + Flyway
- Spring Security + JWT
- Springdoc OpenAPI (Swagger)

## Roles

- **ADMIN**: Modera vagas e verifica empresas
- **EMPLOYER**: Cria empresa e publica vagas
- **CANDIDATE**: Aplica para vagas

## Setup

1. Subir banco de dados:
```bash
docker-compose up -d
```

2. Configurar variáveis de ambiente para email (opcional):
```bash
export MAIL_USERNAME=seu-email@gmail.com
export MAIL_PASSWORD=sua-senha-de-app
export MAIL_FROM=noreply@portalvagas.com
```

3. Executar aplicação:
```bash
./mvnw spring-boot:run
```

4. Acessar Swagger UI: http://localhost:8080/swagger-ui/index.html

### Configuração de Email

Para habilitar notificações por email:
1. Crie uma senha de app no Gmail
2. Configure as variáveis de ambiente acima
3. As notificações serão enviadas automaticamente para:
   - Nova candidatura (para empregador)
   - Vaga aprovada/rejeitada (para empregador)
   - Status de candidatura alterado (para candidato)

## Endpoints Principais

### Autenticação
- `POST /auth/register` - Registro (CANDIDATE|EMPLOYER)
- `POST /auth/login` - Login

### Público
- `GET /jobs/public` - Listar vagas aprovadas (com filtros)
- `GET /jobs/public/{id}` - Detalhes da vaga

### EMPLOYER
- `POST /companies` - Criar empresa
- `POST /jobs` - Criar vaga (DRAFT)
- `POST /jobs/{id}/submit` - Submeter vaga (PENDING)
- `GET /jobs/applications` - Ver candidaturas
- `PUT /jobs/applications/{id}/status` - Atualizar status de candidatura

### CANDIDATE
- `PUT /candidates/me` - Atualizar perfil + CV
- `POST /candidates/applications?jobId=X` - Candidatar-se
- `GET /candidates/applications` - Minhas candidaturas

### ADMIN
- `GET /admin/jobs/pending` - Vagas pendentes
- `POST /admin/jobs/{id}/approve` - Aprovar vaga
- `POST /admin/jobs/{id}/reject` - Rejeitar vaga
- `POST /admin/companies/{id}/verify` - Verificar empresa

## Fluxo MVP

1. Employer registra e cria empresa
2. Employer cria vaga (DRAFT) e submete (PENDING)
3. Admin aprova vaga (APPROVED) - fica visível publicamente
4. Candidate registra, atualiza perfil e se candidata
5. Employer visualiza candidaturas

## Usuário Admin Padrão

- Email: admin@portalvagas.com
- Senha: password (hash BCrypt já inserido na migration)