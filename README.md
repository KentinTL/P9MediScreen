# 🏥 Mediscreen

![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)
![Spring](https://img.shields.io/badge/Spring_Boot-3.X-green?style=for-the-badge&logo=spring)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=for-the-badge&logo=docker)
![Maven](https://img.shields.io/badge/Maven-red?style=for-the-badge&logo=apache-maven)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange?style=for-the-badge&logo=mysql)
![MongoDB](https://img.shields.io/badge/MongoDB-green?style=for-the-badge&logo=mongodb)

**Mediscreen** is a medical record management and risk assessment application developed as part of the **OpenClassrooms Java Developer Path (Project 9)**.  
The project is built on a **Spring Boot microservices architecture**, orchestrated with **Docker Compose**, with authentication handled by **Spring Security** and code simplified using **Lombok**.

---

## 🚀 Prerequisites

Before running the application, make sure you have the following installed:

- **Docker** and **Docker Compose**
- **Java 17** (optional – required only if running services manually)

---

## 🧩 Project Architecture

The project is divided into multiple independent microservices that communicate with each other:

| Microservice | Port | Description |
|--------------|------|-------------|
| `gateway-microservice` | 8080 | Acts as an API gateway, routing requests between the frontend and backend. |
| `frontend-microservice` | 8083 | Web interface built with Spring MVC + Thymeleaf. Handles authentication. |
| `patient-microservice` | 8081 | Manages patient data (stored in MySQL). |
| `notes-microservice` | 8082 | Manages patient medical notes (stored in MongoDB). |
| `assessment-microservice` | 8084 | Performs diabetes risk assessment for patients. |
| MySQL / MongoDB | 3306 / 27017 | Databases used by backend microservices. |

---

## 🧠 System Overview

Mediscreen evaluates a patient's **risk of diabetes** based on personal information and medical notes.

### How it works:

- 🧍‍♂️ **Patient Service**  
  - Stores basic patient information (name, birth date, gender, etc.)  
  - Data is persisted in **MySQL**

- 🗒️ **Notes Service**  
  - Stores and manages doctors’ notes for each patient  
  - Data is persisted in **MongoDB**

- ⚕️ **Assessment Service**  
  - Fetches patient data from **Patient Service** and notes from **Notes Service**  
  - Analyzes trigger terms in notes to determine a **diabetes risk level**  
  - Possible results: `None`, `Borderline`, `In Danger`, `Early Onset`

- 🧩 **Gateway Service**  
  - Single entry point for all HTTP requests  
  - Routes requests from frontend to the appropriate backend service

- 💻 **Frontend Service**  
  - Provides a web interface for healthcare professionals  
  - Handles login and redirection using **Spring Security**  
  - Allows listing patients, viewing notes, and generating risk assessments

---

## 🔒 Security

- **Frontend-service** uses form-based authentication (`/login`) and redirects to `/patients` after login  
- Each backend microservice (patient, notes, assessment) uses **HTTP Basic authentication**  

Credentials for all services:

username: user
password: password

- **Gateway-service** intentionally has **no security configuration** — its only role is to route requests

---

## 💡 Main Technologies

| Category | Technology |
|----------|------------|
| Framework | Spring Boot 3.4.9 |
| Security | Spring Security |
| Data / ORM | Spring Data JPA, Spring Data MongoDB |
| API Gateway | Spring Cloud Gateway |
| Databases | MySQL (patients), MongoDB (notes) |
| Frontend | Spring MVC, Thymeleaf |
| Utilities | Lombok |
| Containerization | Docker & Docker Compose |

---

## ⚙️ Running the Application with Docker

1. **Clone the repository**

```bash
git clone https://github.com/KentinTL/P9MediScreen.git
cd P9MediScreen
```

## ⚙️ Build and Start All Services

Build and start all services using Docker Compose:
```bash
docker-compose up --build
```
## 🌐 Access the Application

- **Frontend UI** → [http://localhost:8083](http://localhost:8083)  
- **API Gateway** → [http://localhost:8080](http://localhost:8080)  
- **Patient API** → [http://localhost:8081/api/patient/patients](http://localhost:8081/api/patient/patients)  
- **Notes API** → [http://localhost:8082/api/notes](http://localhost:8082/api/notes)  
- **Assessment API** → [http://localhost:8084/assess](http://localhost:8084/assess)  

---

## 🧪 Database Initialization

### 🗄️ MySQL

The `patient-db` container automatically initializes a database named `mediscreen_patient_db` and applies table migrations and test data via **Liquibase**.

### 🍃 MongoDB

The `mongo-init.js` script runs on first startup of the `notes-db` container to populate the database with sample medical notes.

---

## 📂 Project Structure

P9MediScreen/

--/assessment_microservice/

--/frontend-microservice/

--/ gateway-microservice/

--/ notes-microservice/

--/ patient-microservice/

--/ docker-compose.yaml

--/ README.md

---

## 🧰 Local Development (without Docker)

To run a microservice manually:

```bash
cd patient-microservice
mvn spring-boot:run
```

⚠️ Make sure to update the database connection URLs in each service’s application.properties if running outside Docker.

---

## 👨‍💻 Author

**Kentin T.**  
Java Developer | OpenClassrooms Project 9 — Mediscreen

---

## 📄 License

This project is licensed under the **MIT License**.  
You are free to use, modify, and distribute it for educational or personal purposes.
