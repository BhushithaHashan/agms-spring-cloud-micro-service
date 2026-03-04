
# 🌿 AGMS: Automated Greenhouse Management System

 microservices ecosystem designed to monitor crop health, manage cultivation zones, and automate climate responses using real time IoT data.

##  Architecture Overview

AGMS is built using a **Spring Cloud** distributed architecture:

* **API Gateway:** The single entry point. Handles **JWT Validation** and request routing.
* **Auth Service:** The security hub. Manages user registration, login, and fetches tokens from external IoT providers.
* **Zone Service:** Manages physical greenhouse sectors and device mappings.
* **Crop Service:** Maintains the "Planting Plan"—optimal thresholds for different plant types.
* **Sensor Service:** The "Brain." An automated service using **OpenFeign** and **Scheduled Tasks** to compare real-time IoT data against crop requirements.
* **Config Server:** Centralized configuration management via Git.
* **Eureka Server:** Service discovery and health monitoring.



---

## Getting Started

### 1. Prerequisites
* Java 17+
* Maven 3.8+
* MySQL / MariaDB
* A running Config Repository (Git)

### 2. Configuration (CRITICAL)
For security reasons, the **JWT Secret Key** is not included in the source code. You must add it to your centralized config file:

1. Open your `application.yml` in your config repository.
2. Add the following entry:
   ```yaml
   jwt:
     secret: "ENTER_YOUR_32_CHARACTER_SECRET_HERE"
   ```
3. Ensure this secret is shared between the **Auth Service** and the **API Gateway**.

### 3. Deployment Order
To ensure the mesh connects correctly, start the services in this order:
1. `eureka-server` (Port 8761)
2. `config-service` (Port 8888)
3. `auth-service` (Port 8081)
4. `zone-service` & `crop-service`
5. `api-gateway` (Port 8080)
6. `sensor-service` (The automation will begin immediately upon startup)

---
![eureka dash](image.png)
---

## 🛡️ Security Flow
AGMS uses a **Stateless JWT** authentication pattern:
1. User logs in via `POST /api/auth/login`.
2. Auth Service issues a signed JWT.
3. User includes JWT in `Authorization: Bearer <token>` header.
4. API Gateway verifies the token, extracts the `username`, and injects it into the `X-User-Id` header for downstream services.

---

##  Automation Logic
The **Sensor Service** runs a pulse every 10 seconds:
1. **Fetch:** Gets all active crops and their optimal temperatures.
2. **Lookup:** Finds the corresponding `deviceId` for the zone.
3. **Poll:** Calls the external IoT API for real time temperature.
4. **Evaluate:** Compares `actual` vs `target`. If variance > 2°C, it triggers a system alert in the logs.

---

##  API Endpoints
| Service | Endpoint | Method | Security |
| :--- | :--- | :--- | :--- |
| **Auth** | `/api/auth/register` | POST | Public |
| **Auth** | `/api/auth/login` | POST | Public |
| **Zone** | `/api/zones` | GET/POST | JWT Required |
| **Crop** | `/api/crops` | GET/POST | JWT Required |

---

