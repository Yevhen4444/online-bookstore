# 📚 BookStore API

![License](https://img.shields.io/badge/license-MIT-lightgrey)

## 🚀 Introduction
BookStore is a RESTful backend application for managing an online bookstore.

This project was created to demonstrate practical backend development skills using Java and Spring technologies. It shows how to build a secure and structured API with authentication, role-based access, database migrations, pagination, sorting, and testing.

The application allows users to browse books, search books by parameters, work with a shopping cart, and place orders. Administrators can manage books and categories.

## 🛠️ Technologies Used

### 🔧 Core Technologies
- Java 17
- Spring Boot
- Spring Web
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Liquibase
- Swagger (OpenAPI)
- Maven
- Docker & Docker Compose

### 🧪 Testing
- JUnit 5
- Spring Boot Test
- MockMvc

### 🧠 Architecture & Patterns
- Layered Architecture (Controller → Service → Repository)
- DTO pattern
- Mapper layer
- Specification pattern (dynamic search)
- Custom validation
- Global exception handling
- Pagination & sorting

## ⚙️ Features

### 🔐 Authentication & Authorization
- User registration
- Login with JWT authentication
- Role-based access control (USER / ADMIN)
- Email used as username
- Password encryption

👉 A shopping cart is automatically created after user registration.

---

### 📚 Book Management
- Create, update, delete books (**ADMIN only**)
- Get book by ID
- Get all books (with pagination)
- 🔍 Search books by:
    - title
    - author
- Pagination & sorting support

---

### 🗂️ Category Management
- Create, update, delete categories (**ADMIN only**)
- Get category by ID
- Get all categories (paginated)
- Get all books by category

---

### 🛒 Shopping Cart
- Add books to cart
- Update cart items
- Remove items from cart
- View cart

---

### 📦 Orders
- Place orders
- View order history
- View order details
- Order status support

## 🔐 Security

The application uses Spring Security with JWT-based authentication.

- Users authenticate using email and password
- JWT token is used to access protected endpoints
- Role-based authorization is implemented:
    - ADMIN → manage books and categories
    - USER → browse books, manage cart, create orders

---

## 🔍 Search Implementation

The project uses the Specification pattern to implement dynamic search functionality.

Users can filter books by:
- title (partial match)
- author (partial match)

Search supports pagination and works together with sorting.

## 📊 API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui.html

---

## ▶️ How to Run the Project

### 1. Clone the repository
git clone https://github.com/Yevhen4444/online-bookstore
cd bookstore

---

### 2. Configure database

Update `application.properties`:

spring.datasource.url=jdbc:mysql://localhost:3306/bookstore  
spring.datasource.username=your_username  
spring.datasource.password=your_password

---

### 3. Run with Docker (recommended)

docker-compose up --build

---

### 4. Or run manually

mvn clean install  
mvn spring-boot:run

## 🗄️ Database

- MySQL database
- Database schema managed using Liquibase
- Migration scripts are located in:
  resources/db/changelog

---

## 🧪 Testing

The project includes tests for:
- Controller layer
- Service layer
- Repository layer

Technologies used:
- JUnit 5
- Spring Boot Test
- MockMvc

---

## 📁 Project Structure

controller/
service/
repository/
entity/
dto/
mapper/
security/
specification/
validation/
exception/
config/

## 🎥 Demo Video

Loom video (2–4 min) demonstrating the project:

👉 [Add your Loom link here]

---

## 📬 Postman Collection (optional)

You can attach a Postman collection to test API endpoints more easily.

---

## 💡 Challenges & Solutions

### ❗ Challenge:
Implementing secure authentication with JWT.

### ✅ Solution:
Configured Spring Security with JWT filters and role-based authorization.

---

### ❗ Challenge:
Implementing flexible book search.

### ✅ Solution:
Used Specification pattern to dynamically build queries based on user input.

---

### ❗ Challenge:
Maintaining clean and scalable architecture.

### ✅ Solution:
Applied layered architecture and separated responsibilities between controller, service, repository, DTO, and mapper layers.

---

## 👨‍💻 Author

Yevhen – Junior Java Developer