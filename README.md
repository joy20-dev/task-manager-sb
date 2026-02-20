# 📝 Task Manager

A full-stack task management application with secure JWT-based authentication, built with Spring Boot and PostgreSQL.

🔗 **[Live Demo](https://your-live-link-here)**

---

## 🚀 Features

- **JWT Authentication** — Secure login and registration using JSON Web Tokens
- **Token-scoped Data Access** — Users can only access their own tasks; zero cross-user data leakage by design
- **Full CRUD** — Create, read, update, and delete tasks
- **RESTful API** — Clean, well-structured endpoints following REST conventions
- **Persistent Storage** — PostgreSQL via Supabase with JPA/Hibernate ORM
- **Responsive UI** — Built with HTML, CSS, and JavaScript; works across devices

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Security | Spring Security, JWT |
| Database | PostgreSQL (Supabase), JPA/Hibernate |
| Frontend | HTML, CSS, JavaScript |
| Build Tool | Maven |

---

## 🏗️ Architecture

```
Client (HTML/CSS/JS)
        │
        ▼
 Spring Boot REST API
        │
        ├── JwtAuthenticationFilter  ← validates token on every request
        │
        ├── Controller Layer         ← handles HTTP requests/responses
        │
        ├── Service Layer            ← business logic
        │
        └── Repository Layer         ← JPA/Hibernate → PostgreSQL (Supabase)
```

---

## 🔐 Security Flow

```
1. User registers   → password encoded with BCrypt → saved to DB
2. User logs in     → credentials verified → JWT token issued
3. Every request    → JwtFilter extracts token from Authorization header
                    → validates signature and expiry
                    → sets Authentication in SecurityContext
4. Data access      → user ID extracted from JWT claims
                    → queries scoped to that user only
```

---

## 📡 API Endpoints

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login and get JWT token | ❌ |
| GET | `/api/tasks` | Get all tasks for logged-in user | ✅ |
| POST | `/api/tasks` | Create a new task | ✅ |
| GET | `/api/tasks/{id}` | Get a specific task | ✅ |
| PUT | `/api/tasks/{id}` | Update a task | ✅ |
| DELETE | `/api/tasks/{id}` | Delete a task | ✅ |

---

## ⚙️ Running Locally

### Prerequisites
- Java 17+
- Maven
- PostgreSQL (or a free Supabase account)

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/task-manager.git
cd task-manager

# 2. Configure application.properties
spring.datasource.url=your_supabase_url
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_secret_key
jwt.expiration=900000

# 3. Build and run
mvn spring-boot:run
```

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/taskmanager/
│   │       ├── controller/      # REST controllers
│   │       ├── service/         # Business logic
│   │       ├── repository/      # JPA repositories
│   │       ├── model/           # Entity classes
│   │       ├── security/        # JWT filter, config
│   │       └── dto/             # Request/Response objects
│   └── resources/
│       └── application.properties
```

---

## 🔑 Key Implementation Details

**JWT Filter** — Every request passes through `JwtAuthenticationFilter` which extends `OncePerRequestFilter`. It extracts the Bearer token, validates the signature using the secret key, checks expiry, and sets the `Authentication` object in `SecurityContextHolder`.

**Data Isolation** — The user ID is extracted directly from JWT claims, not from a request parameter. A user cannot pass someone else's ID to access their data — the token itself is the source of truth.

**Password Security** — All passwords encoded using `BCryptPasswordEncoder`. Plain-text passwords are never stored or logged.

---

## 🤝 Connect

**Joyace Tuscano**
- 📧 Joyacetuscano20@gmail.com
- 💼 [LinkedIn](https://linkedin.com/in/your-profile)
- 🐙 [GitHub](https://github.com/yourusername)
