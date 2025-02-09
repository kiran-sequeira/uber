# uber
Uber backend simulation

## Overview

This project is a backend service for an Uber-like ride-hailing application. It includes functionalities for managing drivers, rides, fares, and notifications.

## Features

- **Driver Management**: Update driver location and status.
- **Ride Management**: Request and accept rides.
- **Fare Calculation**: Mock implementation for fare calculation.
- **Notifications**: Mock notifications for drivers and users.

## Technologies Used

- **Spring Boot**: Framework for building the backend service.
- **DynamoDB**: AWS NoSQL database for storing data.
- **Redis**: In-memory data store for caching.

### Prerequisites

- Java env
- Maven
- Docker

### Running the Application

1. **Start DynamoDB and Redis using Docker Compose**:
    ```sh
    docker-compose up -d
    ```

2. **Build the project**:
    ```sh
    ./mvnw clean install
    ```

3. **Run the application**:
    ```sh
    ./mvnw spring-boot:run
    ```

### Configuration

The application configuration is located in [`src/main/resources/application.properties`](src/main/resources/application.properties ):
```properties
spring.main.allow-bean-definition-overriding=true
aws.dynamodb.endpoint=http://localhost:8000
aws.region=us-east-1