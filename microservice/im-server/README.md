# ISTAD IAM Server (Identity & Access Management)

[![Java 25](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring Authorization Server](https://img.shields.io/badge/Spring%20Authorization%20Server-1.4+-blue.svg)](https://spring.io/projects/spring-authorization-server)
[![JTE](https://img.shields.io/badge/Template%20Engine-JTE%203.2-blueviolet.svg)](https://jte.gg/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal%20%2F%20DDD-lightgrey.svg)]()

A production-grade, enterprise-ready **Identity & Access Management (IAM)** and **OAuth2 & OpenID Connect 1.0 (OIDC)** Authorization Server built with **Spring Boot 3**, **Spring Security 7**, and **Java 25**. Designed using **Domain-Driven Design (DDD)** and **Hexagonal (Ports & Adapters) Clean Architecture**.

---

## Table of Contents
- [Architecture and Modules](#architecture-and-modules)
- [Key Features](#key-features)
- [Pre-configured Accounts and Clients](#pre-configured-accounts-and-clients)
- [Environment Configuration](#environment-configuration)
- [Getting Started and Running](#getting-started-and-running)
- [OAuth2 and OIDC Endpoints](#oauth2-and-oidc-endpoints)
- [REST API Reference](#rest-api-reference)
- [JTE Web Pages and UI Routes](#jte-web-pages-and-ui-routes)
- [Testing the OAuth2 Authorization Code Flow](#testing-the-oauth2-authorization-code-flow)

---

## Architecture and Modules

The `im-server` follows strict **Hexagonal / Clean Architecture (Ports & Adapters)** principles to ensure high maintainability, testability, and decoupling of core business logic from framework and infrastructure code:

```
im-server/
├── iam-domain/
│   ├── iam-domain-core/           # Pure Domain Layer: Entities, Value Objects, Domain Exceptions
│   └── iam-application-service/   # Application Layer: Use Cases, Inbound/Outbound Ports, DTOs, Mappers
├── iam-persistence/               # Secondary Adapter: Spring Data JPA, Entities, Specifications, Adapters
├── iam-infrastructure/            # Security Config, Token Claims, OAuth2/OIDC Services, Seeders
├── iam-rest-api/                  # Primary Adapter: REST Controllers, Request/Response DTOs, Validation
├── iam-web/                       # Primary Adapter: JTE Web Views, Thin Controllers, i18n, Telegram
└── iam-main/                      # Entry Point: SpringBootApplication, Liquibase migrations, application.yaml
```

| Module | Responsibility |
| :--- | :--- |
| **`iam-domain-core`** | Framework-independent domain models (`User`, `Role`, `Permission`, `Identity`, Value Objects like `Email`, `Username`). |
| **`iam-application-service`** | Orchestrates domain use cases (User CRUD, Role/Permission assignment, Federated provisioning). |
| **`iam-persistence`** | Database persistence using **Spring Data JPA** (Hibernate) & **Liquibase** for schema migrations. |
| **`iam-infrastructure`** | Spring Security OAuth2 Authorization Server, JWT token customization, Federated OAuth2/OIDC login handlers. |
| **`iam-rest-api`** | RESTful APIs for administrative operations (Users, Roles, Permissions, Current Actor). |
| **`iam-web`** | Server-Side Rendered Web Pages powered by **100% Pure JTE (Java Template Engine)** and Thin Controllers. |
| **`iam-main`** | Application launcher, root configurations, Liquibase master changelog. |

---

## Key Features

### 1. Centralized OAuth2 & OpenID Connect 1.0 (OIDC)
- **Grant Types Supported**:
  - `authorization_code` (with PKCE support)
  - `client_credentials`
  - `refresh_token`
- **Scopes**: `openid`, `profile`, `email`, `client.create`, `client.read`, and granular domain permissions.
- **Dynamic Client Registration (RFC 7591 / OIDC 1.0)**: Supports dynamic OAuth2 client registration via `/connect/register`.
- **JWT Customization**: Access tokens are enriched with user attributes, tenant context, active roles, and granted authorities/permissions.

### 2. Federated Social & External Logins
Seamless federated account provisioning and linking:
- **Google OAuth2 / OIDC**
- **GitHub OAuth2**
- **Facebook OAuth2**
- **Telegram Login Widget** (Instant cryptographic HMAC validation via Telegram Bot API)

### 3. Fine-Grained Authorization (RBAC & PBAC)
- **Role-Based Access Control (RBAC)**: Manage roles (`ADMIN`, `USER`, etc.) with hierarchical permissions.
- **Permission-Based Access Control (PBAC)**: Assign or revoke direct permissions on a per-user basis with `GRANT` or `REVOKE` effects.
- **Dynamic Filtering**: Advanced search, filtering, and pagination on users, roles, and permissions using `JpaSpecificationExecutor`.

### 4. Modern JTE Web Pages & Internationalization (i18n)
- **100% Pure JTE (Java Template Engine)**: Fast, type-safe, compiled templates with zero runtime overhead.
- **Clean Thin Controllers**: Zero business logic, zero entity transformation, zero `try/catch` or `if/else` branching in web controllers.
- **Modern Light Theme**: Clean, responsive UI inspired by modern design systems (Tailwind CSS / Stripe).
- **Full Bilingual i18n Support**: Switch between **English (`en`)** and **Khmer (`km`)** seamlessly via `?lang=en|km`.

---

## Pre-configured Accounts and Clients

Upon application startup, the system automatically initializes the following seed data:

### Seed Administrator Account
| Property | Value |
| :--- | :--- |
| **Username** | `ratanak` |
| **Password** | `Ratanak@168` |
| **Email** | `ratanak@gmail.com` |
| **Roles** | `ADMIN` |
| **Permissions** | Full administrative privileges (`user:*`, `role:*`, `permission:*`) |

### Seed OAuth2 Registered Clients
| Client ID | Client Secret | Grant Types | Redirect URIs | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **`oidc-client`** | `secret` | `authorization_code`, `refresh_token`, `client_credentials` | `http://localhost:9000/callback`<br>`http://localhost:9000/login/oauth2/code/oidc-client`<br>`https://oauth.pstmn.io/v1/callback` | Default Client App for browser & Postman testing |
| **`katana-client`** | `secret` | `client_credentials` | — | Registrar Client for Dynamic Client Registration (`client.create`) |

---

## Environment Configuration

Create a `.env` file in `microservice/im-server/.env` (or copy from `.env.example`):

```env
# GitHub OAuth2
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret

# Google OAuth2
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret

# Facebook OAuth2
FACEBOOK_CLIENT_ID=your-facebook-client-id
FACEBOOK_CLIENT_SECRET=your-facebook-client-secret

# PostgreSQL Database
DB_URL=jdbc:postgresql://localhost:5432/db_auth
DB_USERNAME=iteuser
DB_PASSWORD=your-db-password

# Server Port
SERVER_PORT=9000

# Telegram Bot Authentication (Optional)
TELEGRAM_BOT_TOKEN=your-telegram-bot-token
TELEGRAM_BOT_USERNAME=your-telegram-bot-username
```

---

## Getting Started and Running

### Prerequisites
- **Java 25** (or compatible JDK 21+)
- **PostgreSQL 15+** running on `localhost:5432` with a database named `db_auth`

### 1. Database Setup
```bash
# Using Docker (optional quick start):
docker run --name postgres-iam -p 5432:5432 -e POSTGRES_DB=db_auth -e POSTGRES_USER=iteuser -e POSTGRES_PASSWORD=qwerqwer -d postgres:16-alpine
```

### 2. Build and Compile
From the repository root:
```bash
./gradlew :microservice:im-server:iam-main:classes
```

### 3. Run the Application
```bash
./gradlew :microservice:im-server:iam-main:bootRun
```
The server will start up on **`http://localhost:9000`**.

---

## OAuth2 and OIDC Endpoints

| Protocol | Endpoint | Description |
| :--- | :--- | :--- |
| **OIDC Discovery** | `GET /.well-known/openid-configuration` | OpenID Connect server metadata & configuration |
| **JWKS Keyset** | `GET /oauth2/jwks` | Public RSA JSON Web Keys for verifying token signatures |
| **Authorize** | `GET /oauth2/authorize` | OAuth2 Authorization endpoint |
| **Token** | `POST /oauth2/token` | OAuth2 Token endpoint (code exchange, refresh token, client credentials) |
| **Token Revocation** | `POST /oauth2/revoke` | Revokes an issued Access Token or Refresh Token |
| **Token Introspection**| `POST /oauth2/introspect` | Inspects the active state and metadata of a token |
| **User Consent** | `GET /oauth2/consent`<br>`POST /oauth2/authorize` | Custom JTE Consent page and consent submission |
| **Client Registration**| `POST /connect/register`<br>`GET /connect/register?client_id={id}` | Dynamic Client Registration (RFC 7591) |

---

## JTE Web Pages and UI Routes

All web views are built with **JTE** with full English & Khmer language switching (`?lang=en|km`):

| Page | Route | Description |
| :--- | :--- | :--- |
| **Sign In** | `GET /login` | Form login, social OAuth2 buttons (Google, GitHub, Facebook), and Telegram widget. |
| **Sign Up** | `GET /register`<br>`POST /register` | User self-registration with live validation and password matching. |
| **User Dashboard** | `GET /` or `GET /home` | Displays profile overview, active roles, direct permissions, and OIDC claims. |
| **Edit Profile** | `GET /profile/edit`<br>`POST /profile/edit` | Update given name, family name, phone number, and address details. |
| **Change Password** | `GET /profile/password`<br>`POST /profile/password` | Change user account password with old password verification. |
| **Linked Accounts** | `GET /profile/accounts`<br>`POST /profile/accounts/unlink` | View and unlink external federated accounts (Google, GitHub, Telegram). |
| **Connected Apps** | `GET /profile/apps`<br>`POST /profile/apps/revoke` | View and revoke third-party client application consents. |
| **OAuth2 Consent** | `GET /oauth2/consent` | Authorize requested scopes when connecting third-party client applications. |

---

## REST API Reference

All administrative REST APIs are secured under `/api/**` with Role and Permission-Based Access Control:

### 1. Current Authenticated User (`/api/me/**`)
| Method | Endpoint | Description | Required Authority |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/me` | Get current user's profile | Authenticated |
| `PUT` | `/api/me/password` | Change current user password | Authenticated |
| `GET` | `/api/me/identities` | List linked identities | Authenticated |
| `DELETE` | `/api/me/identities/{id}` | Unlink a linked identity | Authenticated |

### 2. User Management (`/api/users/**`)
| Method | Endpoint | Description | Required Authority |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/users` | Create a new user account | `user:create` |
| `GET` | `/api/users` | List users with filtering & pagination | `user:read` |
| `GET` | `/api/users/{uuid}` | Get user details by UUID | `user:read` |
| `PATCH` | `/api/users/{uuid}` | Update user profile | `user:update` |
| `DELETE`| `/api/users/{uuid}` | Delete user (soft delete) | `user:delete` |
| `POST` | `/api/users/{uuid}/enable` | Enable a disabled user | `user:update` |
| `POST` | `/api/users/{uuid}/disable` | Disable an active user | `user:update` |
| `POST` | `/api/users/{uuid}/lock` | Lock user account | `user:update` |
| `POST` | `/api/users/{uuid}/unlock` | Unlock user account | `user:update` |
| `PUT` | `/api/users/{uuid}/password` | Reset user password (admin) | `user:update` |
| `GET` | `/api/users/{uuid}/authorities` | Get effective user roles & permissions | `user:read` |
| `POST` | `/api/users/{uuid}/roles` | Assign roles to user | `user:assign` |
| `DELETE`| `/api/users/{uuid}/roles/{roleUuid}` | Revoke role from user | `user:assign` |
| `POST` | `/api/users/{uuid}/permissions` | Grant/Revoke direct user permissions | `user:assign` |
| `DELETE`| `/api/users/{uuid}/permissions/{permUuid}` | Delete user permission override | `user:assign` |

### 3. Role Management (`/api/roles/**`)
| Method | Endpoint | Description | Required Authority |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/roles` | Create a new role | `role:create` |
| `GET` | `/api/roles` | List roles with search, filter & pagination | `role:read` |
| `GET` | `/api/roles/{uuid}` | Get role details with permissions | `role:read` |
| `PATCH` | `/api/roles/{uuid}` | Update role name/description | `role:update` |
| `DELETE`| `/api/roles/{uuid}` | Delete role | `role:delete` |
| `POST` | `/api/roles/{uuid}/activate` | Activate role | `role:update` |
| `POST` | `/api/roles/{uuid}/deactivate` | Deactivate role | `role:update` |
| `POST` | `/api/roles/{uuid}/permissions` | Assign permissions to role | `role:assign` |
| `DELETE`| `/api/roles/{uuid}/permissions/{permUuid}` | Revoke permission from role | `role:assign` |

### 4. Permission Management (`/api/permissions/**`)
| Method | Endpoint | Description | Required Authority |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/permissions` | Create a new permission | `permission:create` |
| `GET` | `/api/permissions` | List permissions with filtering | `permission:read` |
| `GET` | `/api/permissions/{uuid}` | Get permission details | `permission:read` |
| `PATCH` | `/api/permissions/{uuid}` | Update permission details | `permission:update` |
| `DELETE`| `/api/permissions/{uuid}` | Delete permission | `permission:delete` |
| `POST` | `/api/permissions/{uuid}/activate` | Activate permission | `permission:update` |
| `POST` | `/api/permissions/{uuid}/deactivate` | Deactivate permission | `permission:update` |

---

## Testing the OAuth2 Authorization Code Flow

### Step 1: Request Authorization Code in Browser
Navigate to the following URL in your web browser:
```http
http://localhost:9000/oauth2/authorize?response_type=code&client_id=oidc-client&scope=openid%20profile%20email&redirect_uri=http://localhost:9000/callback&state=mystate123
```

1. You will be redirected to the **JTE Sign In** page (`/login`).
2. Log in with `ratanak` / `Ratanak@168` (or via Google/GitHub/Facebook).
3. On the **JTE Consent** page, approve the requested scopes.
4. You will be redirected to `http://localhost:9000/callback?code=YOUR_AUTH_CODE&state=mystate123` where the authorization code will be neatly displayed.

### Step 2: Exchange Code for Access & Refresh Tokens
Open your terminal and run:
```bash
curl -X POST http://localhost:9000/oauth2/token \
  -u oidc-client:secret \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=authorization_code" \
  -d "code=YOUR_AUTH_CODE" \
  -d "redirect_uri=http://localhost:9000/callback"
```

**Example JSON Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIs...",
  "refresh_token": "eyJhbGciOiJSUzI1NiIs...",
  "scope": "openid profile email",
  "id_token": "eyJhbGciOiJSUzI1NiIs...",
  "token_type": "Bearer",
  "expires_in": 17999
}
```

---

## Author and Acknowledgements
- **Developer**: Thai Ratanak
- **Institution**: ISTAD
- **Project**: Microservices Architecture - Identity & Access Management (Assignment 06)
