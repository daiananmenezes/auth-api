# 🔐 Auth API (Spring Boot + JWT)

API robusta de autenticação desenvolvida em **Java Spring Boot**, utilizando **JWT (JSON Web Token)** para autenticação stateless e autorização segura. Esta solução está totalmente integrada ao **Azure API Management**, que atua como gateway inteligente para controle de acesso, políticas de segurança e monitoramento em tempo real.

## 🌟 Principais características

- ✅ **Autenticação JWT** - Tokens seguros e stateless
- ✅ **Integração Azure** - Deploy em nuvem com API Management
- ✅ **Documentação OpenAPI** - Swagger UI integrado
- ✅ **Segurança robusta** - Spring Security com bcrypt
- ✅ **Base de dados flexível** - H2 para desenvolvimento

---

## 🚀 Stack tecnológica

| Tecnologia | Versão | Propósito |
|------------|--------|-----------|
| [Java](https://openjdk.org/projects/jdk/17/) | 17 | Linguagem base |
| [Spring Boot](https://spring.io/projects/spring-boot) | 3.x | Framework principal |
| [Spring Security](https://spring.io/projects/spring-security) | - | Autenticação e autorização |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | - | Persistência de dados |
| [H2 Database](https://www.h2database.com/) | - | Banco em memória (dev/test) |
| [JWT](https://jwt.io/) | - | Tokens de autenticação |
| [Lombok](https://projectlombok.org/) | - | Redução de boilerplate |
| [Springdoc OpenAPI](https://springdoc.org/) | - | Documentação automática |

---

## 📂 Arquitetura do projeto


---

## 📖 API Endpoints

### 🔑 Autenticação

#### Registrar usuário
```http
POST /auth/register
Content-Type: application/json

{
  "username": "usuario_exemplo",
  "password": "senha_forte_123",
  "email": "usuario@exemplo.com"
}
```

**Resposta (201):**
```json
{
  "message": "Usuário registrado com sucesso",
  "username": "usuario_exemplo",
  "createdAt": "2025-08-26T10:30:00Z"
}
```

#### Fazer login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "usuario_exemplo",
  "password": "senha_forte_123"
}
```

**Resposta (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 3600,
  "username": "usuario_exemplo",
  "authorities": ["ROLE_USER"]
}
```

### 🔒 Endpoints protegidos

#### Validar token
```http
GET /auth/validate
Authorization: Bearer {seu_jwt_token}
```

#### Renovar token
```http
POST /auth/refresh
Authorization: Bearer {seu_jwt_token}
```

---

## 📘 Documentação interativa

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health

---

## ☁️ Integração com Azure


## 🏃‍♂️ Executando localmente

### Pré-requisitos

- **Java 17+** - [Download aqui](https://openjdk.org/projects/jdk/17/)
- **Maven 3.8+** - [Guia de instalação](https://maven.apache.org/install.html)

### Passos para execução

3. **Compile e execute**

   ```bash
   # Baixar dependências e compilar
   mvn clean install
   
   # Executar a aplicação
   mvn spring-boot:run

   ```

### Configuração de banco de dados

**Desenvolvimento (H2):**
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:authdb
    driver-class-name: org.h2.Driver
  h2:
    console:
      enabled: true
      path: /h2-console
```





