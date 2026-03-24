# 💳 Banking Microservices Platform

A scalable banking platform built with **Spring Boot 3** and **Spring Cloud**, designed using a microservices architecture. It handles authentication, account management, money transfers, fraud detection, and event-driven processing.

---

## 🏗️ Architecture

```
                          ┌─────────────────┐
                          │   API Gateway    │
                          │     :8085        │
                          └────────┬────────┘
                                   │
                          ┌────────┴────────┐
                          │ Discovery Server │
                          │  (Eureka) :8761  │
                          └────────┬────────┘
                                   │
        ┌──────────┬──────────┬────┴────┬──────────┬──────────┐
        │          │          │         │          │          │
   ┌────┴───┐ ┌───┴────┐ ┌──┴────┐ ┌───┴────┐ ┌───┴────┐ ┌───┴────┐
   │  Auth  │ │Account │ │Transfer│ │Transaction│ │Notification│ │ Fraud │
   │Service │ │Service │ │Service │ │ Service   │ │ Service     │ │Detect │
   │ :9090  │ │ :9091  │ │ :9092  │ │ :9093     │ │ :9094       │ │ :9095 │
   └────────┘ └────────┘ └────┬───┘ └────┬────┘ └────┬────┘ └────┬────┘
                              │           │            │           │
                           RabbitMQ    RabbitMQ     RabbitMQ     Redis
                             :5672       :5672        :5672      :6379
```

---

## 🚀 Tech Stack

- **Backend:** Java 21, Spring Boot 3, Spring Cloud  
- **Database:** PostgreSQL  
- **Cache:** Redis  
- **Message Broker:** RabbitMQ  
- **Service Discovery:** Eureka  
- **API Gateway:** Spring Cloud Gateway  
- **Security:** JWT, Spring Security  
- **Resilience:** Resilience4j  
- **Inter-service Communication:** OpenFeign  
- **Build Tool:** Gradle  
- **Containerization:** Docker, Docker Compose  

---

## 🧩 Microservices

| Service | Port | Description |
|--------|------|------------|
| API Gateway | 8085 | Entry point for all requests |
| Discovery Server | 8761 | Eureka service registry |
| Auth Service | 9090 | Authentication & JWT |
| Account Service | 9091 | Account & balance management |
| Transfer Service | 9092 | Money transfer operations |
| Transaction Service | 9093 | Transaction logging (event-driven) |
| Notification Service | 9094 | User notifications |
| Fraud Detection | 9095 | Fraud analysis & IBAN blacklist |
| Config Server | 8888 | Centralized config (optional) |

---

## ⚙️ How It Works

1. User registers and logs in via **Auth Service** → receives JWT  
2. Accounts are created via **Account Service** (IBAN generated)  
3. **Transfer Service** checks fraud via **Fraud Detection Service**  
4. If valid → balances updated via **Account Service** (Feign)  
5. Transfer event is published to **RabbitMQ**  
6. **Transaction Service** logs the transaction  
7. **Notification Service** sends notifications  

---

## 🚨 Fraud Rules

- Blacklisted IBAN → rejected  
- Daily limit → 50,000 TL  
- Hourly limit → 5 transfers  
- Night (02:00–06:00) + high amount → flagged  

---

## ▶️ Run with Docker

```bash
./gradlew clean bootJar
docker-compose up --build
```

---

## ▶️ Run Locally

```bash
export DB_PASSWORD=your_password

./gradlew :discovery-server:bootRun
./gradlew :auth-service:bootRun
./gradlew :account-service:bootRun
./gradlew :fraud-detection-service:bootRun
./gradlew :transfer-service:bootRun
./gradlew :transaction-service:bootRun
./gradlew :notification-service:bootRun
./gradlew :api-gateway:bootRun
```

---

## 🌐 API Gateway

Base URL:

```
http://localhost:8085
```

---

## 📡 API Endpoints

### Auth
```
POST /api/v1/auth/register
POST /api/v1/auth/login
```

### Accounts
```
POST   /api/v1/accounts
GET    /api/v1/accounts/{iban}
GET    /api/v1/accounts/user/{userId}
PATCH  /api/v1/accounts/{iban}/status
PUT    /api/v1/accounts/balance
```

### Transfers
```
POST /api/v1/transfers
GET  /api/v1/transfers/{id}
GET  /api/v1/transfers/account/{iban}
```

### Transactions
```
GET /api/v1/transactions/account/{iban}
```

### Notifications
```
GET /api/v1/notifications/user/{userId}
GET /api/v1/notifications/user/{userId}/unread
```

### Fraud Detection
```
POST   /api/v1/fraud/check
GET    /api/v1/fraud/alerts
GET    /api/v1/fraud/alerts/pending
POST   /api/v1/fraud/blacklist
DELETE /api/v1/fraud/blacklist/{iban}
```

---

## 📁 Project Structure

```
banking-microservices/
├── api-gateway/
├── discovery-server/
├── config-server/
├── auth-service/
├── account-service/
├── transfer-service/
├── transaction-service/
├── notification-service/
├── fraud-detection-service/
├── docker-compose.yml
├── Dockerfile
├── init-db.sql
├── build.gradle
├── settings.gradle
└── dependencies.gradle
```

---

## 🗄️ Databases

| Database | Service |
|----------|--------|
| BankingAuthDB | Auth Service |
| BankingAccountDB | Account Service |
| BankingTransferDB | Transfer Service |
| BankingTransactionDB | Transaction Service |
| BankingNotificationDB | Notification Service |
| BankingFraudDB | Fraud Detection |
