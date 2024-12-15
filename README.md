# Task List Application

Task List Application is a simple project that allows users to manage tasks with features like task creation, editing, filtering, and exporting statistics. The application supports two roles: User and Admin, each with specific access rights.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Access and Authentication](#access-and-authentication)

---

## Getting Started

### Preparation

1. **Java 17**: Make sure you have Java Development Kit (JDK) version 17 installed.
2. **PostgreSQL**: Install and configure PostgreSQL. 
3. **Git**: Install Git for cloning the repository.

2. Configure the application:
   - Open the `runApplication.bat` file in the root directory.
   - Replace the placeholders with your actual database credentials and JWT secret:
     ```bat
     @echo off

     set DB_URL=<your-url>
     set DB_USERNAME=<your-username>
     set DB_PASSWORD=<your-password>
     set JWT_SECRET=<your-secret-key>

     java -jar build/libs/task-list-0.0.1-SNAPSHOT.jar
     ```

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   runApplication.bat
   ```

## Access and Authentication

API documentation is available via **Swagger UI**:

- **Swagger URL**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Default Admin Credentials

By default, the application includes an admin user with the following credentials:

- **Username**: `admin`
- **Password**: `admin`

