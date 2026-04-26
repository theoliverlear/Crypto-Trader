# Crypto-Trader Deployment Guide

This guide provides minimal, step-by-step instructions for deploying the Crypto-Trader ecosystem using Docker. The architecture is designed for distributed deployment, where each container can run on its own server.

---

### 1. Prerequisites
Ensure the following are installed on each host machine:
*   **Docker Engine** (v24.0+)
*   **Docker Compose** (v2.20+)
*   **PostgreSQL** (External service, not containerized)
*   **Java 23** (Only required if running modules locally instead of Docker)

---

### 2. Infrastructure Setup (Centralized Services)
Start the shared Kafka cluster first. The PostgreSQL database is assumed to be running as an external service on your infrastructure.

1.  Navigate to the `infra/kafka/` directory:
    ```bash
    cd infra/kafka/
    ```
2.  Launch Kafka:
    ```bash
    docker-compose up -d
    ```
    *This starts `cryptotrader-kafka` (9092).*

---

### 3. Environment Configuration
Each module uses environment variables to communicate across the network.

1.  Copy the `.env.template` from the repository root to a new file named `.env`:
    ```bash
    cp .env.template .env
    ```
2.  Edit `.env` and replace `localhost` with the actual IP addresses of your infrastructure and service nodes.

---

### 4. Deploying Application Modules

#### A. Spring Boot Modules (Api, Data, Engine, etc.)
These modules use a centralized build pattern with a single `Dockerfile`. Build and run them from the **repository root**.

**Build a module:**
```bash
docker build -t ct-<module-name> --build-arg MODULE_NAME=Crypto-Trader-<ModuleName> -f Dockerfile .
```
*(Example: `docker build -t ct-api --build-arg MODULE_NAME=Crypto-Trader-Api -f Dockerfile .`)*

**Run a module:**
```bash
docker run -d --name ct-<module-name> --env-file .env -p <port>:<port> ct-<module-name>
```
*(Common Ports: Api: 8080, Security: 8081, Data: 8082, Health: 8083, Chat: 8084)*

#### B. Python Analysis Module
**Build & Run:**
```bash
docker build -t ct-analysis -f Crypto-Trader-Analysis/Dockerfile .
docker run -d --name ct-analysis --env-file .env -p 8000:8000 ct-analysis
```

#### C. Website Module (Production)
**Build & Run:**
```bash
docker build --target production -t ct-website -f Crypto-Trader-Website/Dockerfile .
docker run -d --name ct-website -p 80:80 ct-website
```

---

### 5. Specialized Deployments

#### Mobile Module (CI/CD Build)
Use the builder image to generate an APK without local Android SDKs.
```bash
docker build -t ct-mobile-builder -f Crypto-Trader-Mobile/Dockerfile.build .
```

---

### 6. Quick Deployment (Single Node)
To run all primary modules on a single server for testing:
```bash
docker-compose up -d
```

---

### 7. Troubleshooting
*   **Connectivity**: If a module fails to start, ensure the IP addresses in `.env` are reachable.
*   **Logs**: View real-time logs: `docker logs -f ct-<module-name>`.
*   **Build Failures**: Ensure you are in the repository root when running `docker build`.
