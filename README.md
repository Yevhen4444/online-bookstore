# 📚 BookStore API

![License](https://img.shields.io/badge/license-MIT-lightgrey)
![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Docker](https://img.shields.io/badge/docker-ready-blue)

---

**BookStore** — demo backend application that simulates real e-commerce bookstore flows: secure authentication, product management, shopping cart and order processing.

This project demonstrates how to build a secure and maintainable REST API using Spring Boot ecosystem technologies.

---

# 🚀 Table of Contents

* Introduction
* Quick Start
* Environment Variables
* Technologies
* Features
* Model Diagram
* Usage
* API Endpoints
* Example Requests
* Example Responses
* Docker and Database
* Testing
* Demo
* License
* Author

---

# 🔥 Introduction

BookStore includes:

* JWT authentication
* Role-based authorization (USER / ADMIN)
* Book and category management
* Shopping cart functionality
* Order processing
* Pagination and sorting
* Liquibase database migrations
* Docker support
* Swagger/OpenAPI documentation

---

# ⚡ Quick Start

## Clone repository

```bash
git clone https://github.com/Yevhen4444/online-bookstore
cd online-bookstore
```

---

## Create `.env`

```bash
cp .env.template .env
```

---

## Run application

```bash
docker compose down -v
docker compose up --build
```

---

## Open Swagger UI

```text
http://localhost:8081/api/swagger-ui/index.html
```

---

# ⚙️ Environment Variables

```env
MYSQLDB_USER=bookstore_user
MYSQLDB_ROOT_PASSWORD=bookstore_pass
MYSQLDB_DATABASE=bookstore
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306

SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080

SPRING_DATASOURCE_URL=jdbc:mysql://mysqldb:3306/bookstore?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=bookstore_user
SPRING_DATASOURCE_PASSWORD=bookstore_pass
```

> Do not commit `.env`

---

# 🛠️ Technologies

* Java 17
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* MySQL
* Liquibase
* Docker
* Swagger / OpenAPI
* JUnit
* Mockito

---

# ✅ Features

* JWT authentication
* Role-based access control
* Book CRUD
* Category CRUD
* Shopping cart
* Orders
* Pagination and sorting
* Request validation
* Liquibase migrations
* Dockerized environment

---

# 🗂️ Model Diagram

The following diagram illustrates the main entities and relationships used in the BookStore application.

![Model Diagram](docs/model-diagram.png)

---

# 🔐 Usage

1. Register a new user
2. Login and receive JWT token
3. Use token in Authorization header:

```http
Authorization: Bearer <JWT_TOKEN>
```

---

# 📌 API Endpoints

## 🔐 Authentication

| Method | Endpoint               | Description           |
| ------ | ---------------------- | --------------------- |
| POST   | /api/auth/registration | Register user         |
| POST   | /api/auth/login        | Login and receive JWT |

---

## 📚 Books

| Method | Endpoint        | Description         |
| ------ | --------------- | ------------------- |
| GET    | /api/books      | Get all books       |
| GET    | /api/books/{id} | Get book by id      |
| POST   | /api/books      | Create book (ADMIN) |
| PUT    | /api/books/{id} | Update book (ADMIN) |
| DELETE | /api/books/{id} | Delete book (ADMIN) |

### Query parameters

* `title` — filter by title
* `author` — filter by author
* `sort` — sorting example: `sort=price,asc`
* `page` — page number
* `size` — page size

### Example

```http
GET /api/books?page=0&size=10&sort=price,asc&title=clean
```

---

## 🛒 Shopping Cart & Orders

| Method | Endpoint         | Description       |
| ------ | ---------------- | ----------------- |
| POST   | /api/cart/items  | Add item to cart  |
| GET    | /api/cart        | Get shopping cart |
| POST   | /api/orders      | Create order      |
| GET    | /api/orders/{id} | Get order by id   |

---

# 🔑 Example Requests

## Login

```bash
curl -X POST "http://localhost:8081/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'
```

---

## Authorized request

```bash
curl -H "Authorization: Bearer <JWT_TOKEN>" \
http://localhost:8081/api/books
```

---

## Create book

```bash
curl -X POST "http://localhost:8081/api/books" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Clean Code",
    "author":"Robert Martin",
    "price":29.99
  }'
```

---

## Add item to cart

```bash
curl -X POST "http://localhost:8081/api/cart/items" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "bookId":1,
    "quantity":1
  }'
```

---

# 📊 Example Responses

## Success response

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert Martin",
  "price": 29.99
}
```

---

## Paginated response

```json
{
  "content": [],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 123,
  "totalPages": 13
}
```

---

## Validation error

```json
{
  "timestamp": "2026-04-19T12:00:00",
  "status": 400,
  "errors": [
    "email must not be blank",
    "password must be at least 6 characters"
  ],
  "path": "/api/auth/registration"
}
```

---

# 🐳 Docker and Database

## Run containers

```bash
docker compose down -v
docker compose up --build
```

---

## Notes

* If port is busy → change `MYSQLDB_LOCAL_PORT`
* If database fails → run:

```bash
docker compose down -v
```

* Liquibase migrations run automatically on application startup

---

# 🧪 Testing

```bash
mvn test
```

Includes:

* controller tests
* service tests
* repository tests

---

# 🎥 Demo

https://www.loom.com/share/dd37daf4446e44a8b77a23beba9faed2

### Timeline

* 0:00 — Intro
* 0:15 — Registration
* 0:30 — Login
* 1:40 — Category
* 2:00 — Book
* 2:30 — Cart
* 3:00 — Order
* 3:15 — Order History

---

# 👨‍💻 Author

Yevhen — Junior Java Developer
