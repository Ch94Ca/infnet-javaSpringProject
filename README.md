# [WIP] Simple Expense Manager API

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Security](https://img.shields.io/badge/Security-JWT-purple)
![License](https://img.shields.io/badge/License-MIT-yellow)

A backend application built with Spring Boot for managing expenses. It features a secure REST API with JWT-based authentication and role-based access control (USER and ADMIN).

## ✨ Core Features


* Secure Authentication: Stateless authentication flow using Spring Security and JSON Web Tokens (JWT).
* Role-Based Authorization: Clear distinction between USER and ADMIN roles, with endpoints protected using method-level security.
* User Management: Endpoints for user registration, login, and self-updating of data.
* Admin Module: A dedicated set of endpoints for administrators to manage all users, including deletion and role changes.
* API Documentation: Integrated Swagger UI for easy exploration and testing of all API endpoints.
* Clean Architecture: Follows best practices with a clear separation of concerns (Controllers, Services, Repositories).

## 🛠️ Tech Stack


* Framework: Spring Boot 3
* Language: Java 21
* Security: Spring Security 6
* Authentication: JSON Web Tokens (JWT)
* Database: Spring Data JPA / Hibernate (designed for PostgreSQL)
* Build Tool: Gradle
* API Documentation: SpringDoc / Swagger UI
* Mapping: MapStruct

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine for development and testing purposes.

Prerequisites

* Java Development Kit (JDK) - Version 21 or higher
* Gradle - Version 8.x or higher (The project includes the Gradle Wrapper, so you don't need to install it manually).
* PostgreSQL - A running instance of a PostgreSQL database. You can also use the docker-compose included.
* An IDE like IntelliJ IDEA or VS Code.

Installation & Running

1. **Clone the repository:**

2. **Make the Gradle Wrapper executable (This step is only needed once on Linux/macOS):**
```console
chmod +x ./gradlew
```

3. **Configure the application in .env file:**
```console
# PostgreSQL
DB_URL=jdbc:postgresql://localhost:5432/expense_manager_db
DB_USERNAME=user_dev
DB_PASSWORD=password_dev

# JWT Secret Key - IMPORTANT: Use a long, random, and secret string here!
# You can generate one at https://www.javainuse.com/jwtgenerator#google_vignette
JWT_SECRET_KEY=your-super-secret-and-long-jwt-key
```

4. **Run the docker-compose file in docker folder to start postgreSQL database:**
```console
dokcer-compose up -d
```

5. **Build the project:**
```console
# On Linux/macOS
./gradlew build

# On Windows
gradlew.bat build
```
     
6. **Run the application:**
```console
# On Linux/macOS
./gradlew bootRun

# On Windows
gradlew.bat bootRun
```
The application will start on `http://localhost:8080`.

7. **Access API Documentation:**

Once the application is running, you can access the Swagger UI to see all available endpoints and test them:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🔐 Security Model

The API is secured using JWT.

Register or Login: A user must first register via POST /api/auth/register or log in via POST /api/auth/login. A successful login returns a JWT.
Send Token: For all subsequent requests to protected endpoints, the client must include the JWT in the Authorization header.
Authorization: Bearer <your_jwt_token>
Roles:

*   `ROLE_USER`: Can manage their own data.
*   `ROLE_ADMIN`: Can manage all users and system settings.
