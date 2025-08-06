# Virtual Book Store 📚

A comprehensive, cloud-enabled Virtual Book Store built with Spring Boot. This full-stack application provides robust APIs for user management, book catalog operations, and administrative functions with secure authentication and file upload capabilities.

![Java](https://img.shields.io/badge/Java-20-orange?style=flat&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen?style=flat&logo=spring-boot)
![MongoDB](https://img.shields.io/badge/MongoDB-Latest-green?style=flat&logo=mongodb)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat)
![API](https://img.shields.io/badge/API-REST-red?style=flat)

## 🌟 Features

### 🔐 User Management
- **📝 Account Registration** - Secure user signup with validation
- **🔑 Authentication System** - JWT-based login and session management
- **👤 Profile Management** - Users can view and update personal information
- **🗑️ Account Operations** - User account deletion functionality
- **🛡️ Role-based Access** - Differentiated user and admin permissions

### 📖 Book Management
- **📚 Catalog Operations** - Complete CRUD operations for books (Admin only)
- **🔍 Advanced Search** - Filter books by genre, author, title, or keywords
- **📋 Browse Collection** - View all available books with pagination
- **🖼️ Media Support** - Book cover image upload via Cloudinary
- **📊 Metadata Management** - Comprehensive book information tracking

### ⚙️ Administrative Features
- **👥 User Administration** - View and manage all registered users
- **🔧 Admin Management** - Create new administrative accounts
- **📊 System Monitoring** - User activity and system health tracking
- **🗃️ Data Management** - Bulk operations and data export capabilities

## 🛠️ Technology Stack

### Backend Architecture
| Component | Technology | Version |
|-----------|------------|---------|
| **Runtime** | Java | 20 |
| **Framework** | Spring Boot | 3.4.2 |
| **Security** | Spring Security + JWT | Latest |
| **Data Layer** | Spring Data MongoDB | Latest |
| **Build Tool** | Apache Maven | 3.9+ |

### Infrastructure & Services
| Service | Technology | Purpose |
|---------|------------|---------|
| **Database** | MongoDB | Document-based data storage |
| **File Storage** | Cloudinary | Image upload and management |
| **API Docs** | Swagger/OpenAPI 3 | Interactive API documentation |
| **Authentication** | JWT | Stateless authentication tokens |

## 🚀 Quick Start

### Prerequisites

Ensure you have the following installed:

| Requirement | Version | Download Link |
|-------------|---------|---------------|
| **Java JDK** | 20+ | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **Apache Maven** | 3.9+ | [Maven Downloads](https://maven.apache.org/download.cgi) |
| **MongoDB** | 6.0+ | [MongoDB Community](https://www.mongodb.com/try/download/community) |

#### Verify Prerequisites
```bash
# Check Java version
java --version

# Check Maven version
mvn --version

# Check MongoDB status
mongosh --eval "db.adminCommand('ismaster')"
```

### External Services Setup

#### 1. MongoDB Configuration

**Option A: Local Installation**
```bash
# Start MongoDB service
# Windows
net start MongoDB

# macOS (via Homebrew)
brew services start mongodb-community

# Linux (via systemctl)
sudo systemctl start mongod
```

**Option B: MongoDB Atlas (Cloud)**
1. Visit [MongoDB Atlas](https://www.mongodb.com/cloud/atlas)
2. Create a free cluster
3. Get your connection string
4. Whitelist your IP address

#### 2. Cloudinary Setup

1. **Create Account**: Visit [Cloudinary](https://cloudinary.com/)
2. **Get Credentials**: Access your dashboard for:
   - Cloud Name
   - API Key  
   - API Secret
3. **Configure Upload Settings**: Set up upload presets if needed

## 💻 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/virtual-book-store.git
cd virtual-book-store
```

### 2. Configuration Setup

Create the configuration file:

```bash
# Navigate to resources directory
cd src/main/resources

# Create application.properties file
touch application.properties
```

#### Complete Configuration Template

```properties
# ===============================
# DATABASE CONFIGURATION
# ===============================
spring.data.mongodb.uri=mongodb://localhost:27017/virtualbookstore
spring.data.mongodb.database=virtualbookstore

# For MongoDB Atlas (Cloud)
# spring.data.mongodb.uri=mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/virtualbookstore

# ===============================
# CLOUDINARY CONFIGURATION
# ===============================
cloudinary.cloud_name=your_cloudinary_cloud_name
cloudinary.api_key=your_cloudinary_api_key
cloudinary.api_secret=your_cloudinary_api_secret

# ===============================
# JWT CONFIGURATION
# ===============================
jwt.secret=your-256-bit-secret-key-here
jwt.expiration=86400000
jwt.refresh.expiration=604800000

# ===============================
# SERVER CONFIGURATION
# ===============================
server.port=8080
server.servlet.context-path=/api

# ===============================
# LOGGING CONFIGURATION
# ===============================
logging.level.com.virtualbookstore=INFO
logging.level.org.springframework.security=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# ===============================
# FILE UPLOAD CONFIGURATION
# ===============================
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# ===============================
# API DOCUMENTATION
# ===============================
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### 3. Build and Run

#### Using Maven Wrapper (Recommended)
```bash
# Make wrapper executable (Linux/macOS)
chmod +x ./mvnw

# Clean and install dependencies
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

#### Using Installed Maven
```bash
# Clean and install dependencies
mvn clean install

# Run the application  
mvn spring-boot:run
```

#### Using IDE
1. **Import Project**: Open in IntelliJ IDEA or Eclipse
2. **Maven Sync**: Allow IDE to download dependencies
3. **Run Configuration**: Create Spring Boot run configuration
4. **Start Application**: Run the main application class

### 4. Verify Installation

The application will be accessible at:
- **API Base URL**: http://localhost:8080/api
- **Swagger Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/health

## 📱 API Documentation

### Interactive Documentation

Access the comprehensive API documentation at:
**🔗 http://localhost:8080/swagger-ui.html**

### API Endpoints Overview

#### Authentication Endpoints
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/auth/signup` | Register new user | ❌ No |
| `POST` | `/auth/login` | User login | ❌ No |
| `POST` | `/auth/refresh` | Refresh JWT token | ✅ Yes |
| `POST` | `/auth/logout` | User logout | ✅ Yes |

#### User Management
| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| `GET` | `/users/profile` | Get user profile | ✅ Yes | User |
| `PUT` | `/users/profile` | Update profile | ✅ Yes | User |
| `DELETE` | `/users/profile` | Delete account | ✅ Yes | User |
| `GET` | `/admin/users` | Get all users | ✅ Yes | Admin |
| `POST` | `/admin/users` | Create admin user | ✅ Yes | Admin |
| `DELETE` | `/admin/users/{id}` | Delete user | ✅ Yes | Admin |

#### Book Management
| Method | Endpoint | Description | Auth Required | Role |
|--------|----------|-------------|---------------|------|
| `GET` | `/books` | Get all books | ❌ No | Public |
| `GET` | `/books/{id}` | Get book by ID | ❌ No | Public |
| `GET` | `/books/search` | Search books | ❌ No | Public |
| `POST` | `/admin/books` | Add new book | ✅ Yes | Admin |
| `PUT` | `/admin/books/{id}` | Update book | ✅ Yes | Admin |
| `DELETE` | `/admin/books/{id}` | Delete book | ✅ Yes | Admin |
| `POST` | `/admin/books/{id}/image` | Upload book cover | ✅ Yes | Admin |

### Sample API Requests

#### User Registration
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "securePassword123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

#### User Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

#### Add New Book (Admin)
```bash
curl -X POST http://localhost:8080/api/admin/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "The Great Gatsby",
    "author": "F. Scott Fitzgerald",
    "genre": "Classic Literature",
    "price": 12.99,
    "description": "A classic American novel",
    "isbn": "978-0-7432-7356-5",
    "publishedDate": "1925-04-10",
    "stockQuantity": 100
  }'
```

## 🏗️ Project Architecture

### Directory Structure
```
virtual-book-store/
├── src/
│   ├── main/
│   │   ├── java/com/virtualbookstore/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # MongoDB entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # Security configuration
│   │   │   ├── service/         # Business logic services
│   │   │   └── util/            # Utility classes
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/          # Static resources
│   └── test/                    # Test classes
├── target/                      # Compiled classes
├── pom.xml                      # Maven dependencies
└── README.md                    # This file
```

### Key Components

#### 🔧 Configuration Layer
```java
@Configuration
@EnableWebSecurity
@EnableMongoRepositories
public class SecurityConfig {
    // JWT authentication configuration
    // CORS settings
    // Password encoding
}
```

#### 🎯 Controller Layer
```java
@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {
    // RESTful endpoints
    // Request validation
    // Response formatting
}
```

#### 🏢 Service Layer
```java
@Service
@Transactional
public class BookService {
    // Business logic
    // Data transformation
    // External service integration
}
```

#### 🗃️ Repository Layer
```java
@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    // Custom query methods
    // Aggregation pipelines
    // Indexing strategies
}
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BookServiceTest

# Run tests with coverage
./mvnw test jacoco:report
```

### Test Categories

#### Unit Tests
- Service layer logic
- Utility functions
- Data transformations

#### Integration Tests
- Repository operations
- API endpoint testing
- Security configurations

#### Sample Test Data

```java
// Test User
{
    "username": "testuser",
    "email": "test@example.com",
    "password": "testPassword123",
    "role": "USER"
}

// Test Book
{
    "title": "Test Book",
    "author": "Test Author",
    "genre": "Fiction",
    "price": 19.99,
    "isbn": "978-0-123456-78-9"
}
```

## 🔧 Configuration Guide

### Environment-Specific Configurations

#### Development Environment
```properties
# application-dev.properties
spring.profiles.active=dev
logging.level.com.virtualbookstore=DEBUG
spring.data.mongodb.uri=mongodb://localhost:27017/virtualbookstore_dev
```

#### Production Environment
```properties
# application-prod.properties
spring.profiles.active=prod
logging.level.com.virtualbookstore=WARN
spring.data.mongodb.uri=${MONGODB_URI}
cloudinary.cloud_name=${CLOUDINARY_CLOUD_NAME}
```

### Docker Configuration

#### Dockerfile
```dockerfile
FROM openjdk:20-jdk-slim

WORKDIR /app
COPY target/virtual-book-store-*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### docker-compose.yml
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - MONGODB_URI=mongodb://mongo:27017/virtualbookstore
    depends_on:
      - mongo
  
  mongo:
    image: mongo:6.0
    ports:
      - "27017:27017"
    volumes:
      - mongo_data:/data/db

volumes:
  mongo_data:
```

## 🐛 Troubleshooting

### Common Issues & Solutions

**❌ "Failed to connect to MongoDB"**
```bash
# Check MongoDB status
mongosh --eval "db.runCommand('ping')"

# Restart MongoDB service
# Windows: net restart MongoDB
# macOS: brew services restart mongodb-community
# Linux: sudo systemctl restart mongod
```

**❌ "Cloudinary upload failed"**
- Verify API credentials in application.properties
- Check network connectivity
- Validate file size limits (default: 10MB)

**❌ "JWT token expired"**
```json
{
  "error": "Token expired",
  "message": "Please login again",
  "timestamp": "2025-07-19T10:30:00Z"
}
```
- Use refresh token endpoint
- Re-authenticate user

**❌ "Maven build failed"**
```bash
# Clean and rebuild
./mvnw clean install -U

# Skip tests if needed
./mvnw clean install -DskipTests
```

### Debug Configuration

```properties
# Enable debug logging
logging.level.com.virtualbookstore=DEBUG
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.web=DEBUG

# MongoDB debug
logging.level.org.springframework.data.mongodb=DEBUG
```

## 🚧 Roadmap

### Upcoming Features
- [ ] **🛒 Shopping Cart** - Add cart functionality with checkout process
- [ ] **📦 Order Management** - Order history and tracking system
- [ ] **⭐ Reviews & Ratings** - User reviews for books
- [ ] **🔍 Advanced Search** - Full-text search with Elasticsearch
- [ ] **📧 Email Notifications** - Order confirmations and updates
- [ ] **💳 Payment Integration** - Stripe/PayPal payment processing
- [ ] **📱 Mobile API** - Optimized endpoints for mobile apps
- [ ] **📊 Analytics Dashboard** - Sales and user behavior insights

### Technical Enhancements
- [ ] **🚀 Performance Optimization** - Caching with Redis
- [ ] **📈 Monitoring** - Application metrics with Micrometer
- [ ] **🔄 CI/CD Pipeline** - GitHub Actions deployment
- [ ] **🧪 Test Coverage** - Increase to 90%+ coverage
- [ ] **📚 API Versioning** - Implement version management
- [ ] **🔐 OAuth2 Integration** - Social login support
- [ ] **📝 Audit Logging** - Track all data changes
- [ ] **🌐 Internationalization** - Multi-language support

## 🤝 Contributing

We welcome contributions! Please follow our guidelines:

### Development Workflow

1. **🍴 Fork** the repository
2. **🌿 Create** feature branch: `git checkout -b feature/amazing-feature`
3. **📝 Commit** changes: `git commit -m 'Add amazing feature'`
4. **🧪 Run** tests: `./mvnw test`
5. **📤 Push** branch: `git push origin feature/amazing-feature`
6. **📬 Submit** Pull Request with detailed description

### Coding Standards

```java
// Follow Java naming conventions
public class BookService {
    private final BookRepository bookRepository;
    
    // Use constructor injection
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    
    // Add comprehensive JavaDoc
    /**
     * Retrieves books by genre with pagination
     * @param genre the book genre
     * @param pageable pagination information
     * @return page of books
     */
    public Page<Book> findByGenre(String genre, Pageable pageable) {
        return bookRepository.findByGenreIgnoreCase(genre, pageable);
    }
}
```

### Pull Request Checklist

- [ ] ✅ Code follows project conventions
- [ ] 🧪 Tests added for new features
- [ ] 📝 Documentation updated
- [ ] 🔍 No merge conflicts
- [ ] ✅ All tests passing
- [ ] 📊 Code coverage maintained

## 📄 License

This project is licensed under the **MIT License**:

```
MIT License

Copyright (c) 2025 Virtual Book Store

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## 🙏 Acknowledgments

- **☕ Spring Team** - For the excellent Spring Boot framework
- **🍃 MongoDB Team** - For the flexible document database
- **☁️ Cloudinary** - For seamless image management
- **📖 Swagger Team** - For API documentation tools
- **🔐 Auth0 Community** - For JWT implementation guidance
- **💻 Open Source Community** - For continuous inspiration

## 👤 Author

**Puspendu Nayak**
- **GitHub**: [@PuspenduNayak](https://github.com/PuspenduNayak)
- **LinkedIn**: [Puspendu Nayak](https://linkedin.com/in/puspendu-nayak)
- **Portfolio**: [Puspenu Nayak](https://puspendu-nayak.vercel.app/))

## 📞 Support & Contact

Need help? We're here for you:

- **🐛 Bug Reports**: [GitHub Issues](https://github.com/PuspenduNayak/virtual-book-store/issues)
- **💡 Feature Requests**: [GitHub Discussions](https://github.com/PuspenduNayak/virtual-book-store/discussions)


## 📊 Project Statistics

- **📅 Created**: January 2025
- **🔄 Last Updated**: July 19, 2025
- **📈 Version**: 2.0.0
- **🗣️ Language**: Java (Spring Boot)
- **📦 Dependencies**: 25+ Maven dependencies
- **🧪 Test Coverage**: 85%
- **⭐ GitHub Stars**: Growing daily!

---

**⭐ Star this repository if you find it helpful!**

*Built with ❤️ for the developer community*

---

*For detailed API documentation, visit [Swagger UI](http://localhost:8080/swagger-ui.html) when the application is running.*
