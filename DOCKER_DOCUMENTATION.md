# Docker Setup

This documentation provides an overview on the Docker image for this project. Each section will explain various aspects such as requirements and, in particular, the commands to get the Docker image and container running on your personal machine.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Clone the Repository](#clone-the-repository)
3. [Input Database Information](#input-database-information)
4. [Build the Backend JAR](#build-the-backend-jar)
5. [Build and Run Docker](#build-and-run-docker)
6. [Check if Docker is Running](#check-if-docker-is-running)
7. [Stopping Docker](#stopping-docker)
8. [Troubleshooting](#troubleshooting)
---

## Prerequisites
- Docker Desktop
- Docker Compose
- Git
- Java 21
- Maven

---

## Clone the Repository
- Clone the project
    - `git clone <repo-url>`
    - `cd COSC310_GroupProject`

---

## Input Database Information
- Inside the file `docker-compose.yml`, change the database credentials to a valid username and password:
    - `SPRING_DATASOURCE_USERNAME: DATABASE_USERNAME`
    - `SPRING_DATASOURCE_PASSWORD: DATABASE_PASSWORD`

---

## Build the Backend JAR
- Before running Docker you must build the backend JAR
    - `cd backend/task-manager`
    - `mvn clean package`

---

## Build and Run Docker
- Navigate to the root directory where `docker-compose.yml` is located

### Build the Docker Image
- `docker-compose build`

### Run the Containers
- `docker-compose up --build`

---

## Check if Docker is Running
- `docker ps`
- If needed you can check the logs using this:
    - `docker logs task-manager`

---

## Stopping Docker
- To stop Docker run: 
    - `docker-compose down`
- To restart Docker run:
    - `docker-compose up --build`

---

## Troubleshooting
### Check if the container is already running
- `docker ps -a`  
  - If `task-manager` is already running, restart it with:
  - `docker start task-manager`

### Remove Old Containers (if needed)
- If a container already exists but you need to rebuild it:
  - `docker rm task-manager`
  - `docker rm <other-container-name>`

### If Ports Are Blocked
- Run:  
  - `netstat -ano | findstr :8080`
- If another process is using the port, kill it -- **BE CAREFUL, CHECK WHAT IS RUNNING ON THE PORT FIRST**:
  - `taskkill /PID <process_id> /F`