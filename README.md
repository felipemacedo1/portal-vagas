# Portal de Vagas - MVP

API REST para portal de vagas com autenticação JWT e controle de acesso baseado em roles.

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

2. Executar aplicação:
```bash
./mvnw spring-boot:run
```

3. Acessar Swagger UI: http://localhost:8080/swagger-ui/index.html

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