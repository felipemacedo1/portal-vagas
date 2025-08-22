# Portal de Vagas - MVP

[![Build Status](https://github.com/felipemacedo1/portal-vagas/workflows/CI/badge.svg)](https://github.com/felipemacedo1/portal-vagas/actions)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue.svg)](https://github.com/felipemacedo1/portal-vagas/releases)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-green.svg)](https://spring.io/projects/spring-boot)

API REST para portal de vagas com autenticação JWT e controle de acesso baseado em roles.

> 🎯 **Projeto Social**: Sistema gratuito para ONGs conectarem candidatos a oportunidades de emprego.

## Tecnologias

- Java 17
- Spring Boot 3.3.2
- PostgreSQL + Hibernate (JPA)
- Spring Security + JWT
- Springdoc OpenAPI (Swagger)
- BCrypt (Password Encoding)

## Roles

- **ADMIN**: Modera vagas e verifica empresas
- **EMPLOYER**: Cria empresa e publica vagas
- **CANDIDATE**: Aplica para vagas

## Setup

### 🚀 Execução Rápida
```bash
# Opção 1: Script automático
./run-app.sh

# Opção 2: Manual
docker-compose up -d
mvn spring-boot:run
```

### 📋 Passo a Passo

1. **Subir banco de dados:**
```bash
docker-compose up -d
```

2. **Configurar email (opcional):**
```bash
export MAIL_USERNAME=seu-email@gmail.com
export MAIL_PASSWORD=sua-senha-de-app
export MAIL_FROM=noreply@portalvagas.com
```

3. **Executar aplicação:**
```bash
mvn spring-boot:run
```

4. **Acessar aplicação:**
   - API: http://localhost:8081
   - Swagger: http://localhost:8081/swagger-ui/index.html

### 🔐 Configurações de Segurança
- **CORS**: Configurado para permitir todas as origens (`allowCredentials: true`)
- **JWT**: Access token (1h) + Refresh token (24h)
- **Passwords**: BCrypt com salt automático
- **Endpoints públicos**: `/auth/**`, `/jobs/public/**`, `/swagger-ui/**`, `/actuator/**`

### ⚠️ Importante
- **Para rodar aplicação**: Use `mvn spring-boot:run`
- **Para rodar testes**: Use `mvn test` (usa H2 em memória)
- **PostgreSQL**: Deve estar rodando (docker-compose)
- **Schema**: Hibernate com `ddl-auto: create-drop` - recria schema a cada startup
- **Dados iniciais**: Admin user e dados de teste criados automaticamente via DataLoader

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
- `GET /admin/companies` - Listar empresas
- `POST /admin/companies/{id}/verify` - Verificar empresa
- `POST /admin/companies/{id}/unverify` - Desverificar empresa

## Fluxo MVP

1. Employer registra e cria empresa
2. Employer cria vaga (DRAFT) e submete (PENDING)
3. Admin aprova vaga (APPROVED) - fica visível publicamente
4. Candidate registra, atualiza perfil e se candidata
5. Employer visualiza candidaturas

## Usuário Admin Padrão

- Email: admin@portalvagas.com
- Senha: password (criado automaticamente pelo DataLoader com hash BCrypt)