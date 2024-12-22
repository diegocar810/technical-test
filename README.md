# Franchise Management API

## Description

This API allows managing franchises, branches, and products. You can create franchises, add branches and products, update stock, delete products, and query the product with the highest stock by branch.

---

## Technologies

- **Spring Boot**
- **MongoDB**
- **Java 23**

---

## Setup

### Prerequisites

- JDK 23 or higher
- MongoDB installed and running at `localhost:27017`

### MongoDB Configuration

Ensure your `application.properties` file is configured correctly:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/franchise_db
