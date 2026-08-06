# FinTrack — Backend

API REST do sistema de controle financeiro pessoal FinTrack, desenvolvida em Java com Spring Boot.

## 🚀 Tecnologias

- **Java 25** — linguagem
- **Spring Boot 4.1.0** — framework principal
- **Spring Security + JWT** — autenticação e autorização
- **Spring Data JPA + Hibernate** — persistência de dados
- **PostgreSQL** — banco de dados
- **Lombok** — redução de boilerplate
- **Swagger / OpenAPI** — documentação interativa da API
- **Bean Validation** — validação de dados de entrada
- **Maven** — gerenciamento de dependências

## ⚙️ Como rodar localmente

### Pré-requisitos

- Java 25
- Maven
- PostgreSQL rodando localmente

### Configuração do banco

Crie um banco de dados no PostgreSQL:

```sql
CREATE DATABASE financeiro_db;
```

### Configuração da aplicação

Crie o arquivo `src/main/resources/application.properties` com o seguinte conteúdo:

```properties
spring.application.name=financeiro-api
server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/financeiro_db
spring.datasource.username=postgres
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

jwt.secret=seu_secret_aqui
jwt.expiration=86400000
```

### Instalação

```bash
# Clone o repositório
git clone https://github.com/pedro-sky/FinTrack-api.git
cd fintrack-api

# Rode o projeto
.\mvnw spring-boot:run
```

A API ficará disponível em: http://localhost:8080

Documentação Swagger: http://localhost:8080/swagger-ui.html

## 📡 Endpoints

### Autenticação

POST /auth/registro → cadastrar novo usuário
POST /auth/login → autenticar e receber token JWT

### Categorias (requer token)

GET /categorias → listar categorias do usuário
POST /categorias → criar categoria
PUT /categorias/{id} → atualizar categoria
DELETE /categorias/{id} → deletar categoria

### Transações (requer token)

GET /transacoes → listar com filtros e paginação
GET /transacoes/{id} → buscar por ID
GET /transacoes/resumo → resumo financeiro (saldo, receitas, despesas)
POST /transacoes → criar transação
PUT /transacoes/{id} → atualizar transação
DELETE /transacoes/{id} → deletar transação

## 🏗️ Arquitetura

Controller → Service → Repository → PostgreSQL

| Camada     | Responsabilidade                   |
| ---------- | ---------------------------------- |
| Controller | Recebe e responde requisições HTTP |
| Service    | Lógica de negócio e regras         |
| Repository | Acesso ao banco de dados           |
| Model      | Entidades JPA                      |
| DTO        | Transferência de dados             |
| Security   | JWT e autenticação                 |
| Exception  | Tratamento centralizado de erros   |

## 🔗 Repositório do Frontend

[fintrack-front](https://github.com/pedro-sky/FinTrack-front)
