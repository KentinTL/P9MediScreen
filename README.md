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

---

## ♻️ Green Code Recommendations

Software ecodesign, or "Green Code," aims to develop applications that minimize their resource consumption (CPU, RAM, network, storage) to reduce their overall energy footprint.

For the Mediscreen project, several concrete actions could be implemented to align the architecture with these principles.

### 1. Architecture and Deployment Optimization

-   **Optimize Docker Images:**
    -   Use minimalist base images like `alpine` for the JRE. For example, `eclipse-temurin:17-jre-alpine` is significantly lighter than the standard image.
    -   Implement **multi-stage builds** in each `Dockerfile`. This allows the code to be compiled in a heavy image (containing Maven and the full JDK) and then copies only the final `.jar` artifact into a lightweight runtime image. The reduction in final image size is often over 50%, which decreases storage space and network consumption during deployments.

-   **Adapt Infrastructure in Production:**
    -   In a production environment, use an orchestrator like **Kubernetes** to implement **auto-scaling**. Services like `assessment-service` could then be scaled to zero when not in use and the number of instances could dynamically adjust to the load, avoiding unnecessary resource consumption.

### 2. Backend and Data Optimization

-   **Strategic Caching:**
    -   The `assessment-service` systematically calls the `patient-service` and `notes-service`. A patient's core data (age, gender) rarely changes. Implementing a cache (with **Redis** or **Caffeine**) would allow for temporary storage of this information, drastically reducing the number of network calls and the load on other services.

-   **Query Optimization:**
    -   **Pagination:** The page listing all patients (`/patients`) could eventually become very slow if the clinic has thousands of patients. Implementing pagination in the `patient-service` and `frontend-service` would ensure that only the data visible on the screen is loaded, saving memory, CPU, and bandwidth.
    -   **Indexing:** Ensure that frequently queried fields in the databases (like `patientId` in MongoDB) are properly **indexed** to prevent full collection scans, which are very resource-intensive.

### 3. Frontend Frugality

-   **Resource Optimization:**
    -   Compress and minify static resources like CSS and JavaScript files to reduce their size and speed up load times for the user, thereby decreasing bandwidth consumption.
-   **Limiting API Calls:**
    -   Ensure the user interface only makes the API calls that are strictly necessary to render a view. For example, the patient list should only fetch the data required for the table (name, date of birth), not the complete medical history for every patient in the list.

## 👨‍💻 Author

**Kentin T.**  
Java Developer | OpenClassrooms Project 9 — Mediscreen

---

## 📄 License

This project is licensed under the **MIT License**.  
You are free to use, modify, and distribute it for educational or personal purposes.
