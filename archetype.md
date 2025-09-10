# Spring Boot Application Archetype Guide

## Overview

This is a Spring Boot 3.5.5 application archetype with Java 21, PostgreSQL, JPA, Flyway migrations, and Lombok. It follows a clean layered architecture pattern for building REST APIs.

## Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java          # Main Spring Boot application class
│   │   ├── controller/                   # REST Controllers (API endpoints)
│   │   ├── dto/                          # Data Transfer Objects (Request/Response objects)
│   │   ├── entity/                       # JPA Entities (Database models)
│   │   ├── enums/                        # Enum definitions
│   │   ├── repository/                   # Data Access Layer (JPA Repositories)
│   │   └── service/                      # Business Logic Layer
│   └── resources/
│       ├── application.properties       # Application configuration
│       ├── db/migration/                # Flyway database migration scripts
│       ├── static/                      # Static web resources
│       └── templates/                   # Template files
└── test/                                # Test classes
```

## Architecture Layers

### 1. Controller Layer (`/controller`)

- **Purpose**: Handle HTTP requests and responses
- **Responsibilities**:
  - Define REST endpoints
  - Validate request data
  - Call service layer methods
  - Return appropriate HTTP responses

**Example Structure:**

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, 
                                                  @RequestBody UpdateUserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 2. DTO Layer (`/dto`)

- **Purpose**: Data Transfer Objects for API communication
- **Responsibilities**:
  - Request DTOs for incoming data
  - Response DTOs for outgoing data
  - Data validation annotations

**Example Structure:**

```java
// Request DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}

// Update DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserStatus status;
}

// Response DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private UserStatus status;
    private String statusDisplayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 3. Service Layer (`/service`)

- **Purpose**: Business logic and orchestration
- **Responsibilities**:
  - Implement business rules
  - Coordinate between repositories
  - Handle transactions
  - Data transformation

**Example Structure:**

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with email already exists");
        }
        
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(UserStatus.ACTIVE);
        
        User savedUser = userRepository.save(user);
        return convertToResponse(savedUser);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToResponse);
    }
    
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getStatus() != null) user.setStatus(request.getStatus());
        
        User updatedUser = userRepository.save(user);
        return convertToResponse(updatedUser);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToResponse);
    }
    
    private UserResponse convertToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setStatus(user.getStatus());
        response.setStatusDisplayName(user.getStatus().getDisplayName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}
```

### 4. Repository Layer (`/repository`)

- **Purpose**: Data access abstraction
- **Responsibilities**:
  - Database operations
  - Custom queries
  - JPA repository interfaces

**Example Structure:**

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Page<User> findByStatus(UserStatus status, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> findByNameContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= :since")
    List<User> findRecentUsers(@Param("since") LocalDateTime since);
    
    long countByStatus(UserStatus status);
}
```

### 5. Entity Layer (`/entity`)

- **Purpose**: Database model representation
- **Responsibilities**:
  - JPA entity definitions
  - Database table mappings
  - Relationships between entities

**Example Structure:**

```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status = UserStatus.ACTIVE;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

### 6. Enums (`/enums`)

- **Purpose**: Define constant values and types
- **Responsibilities**:
  - Status enums
  - Category types
  - Configuration constants

**Example Structure:**

```java
public enum UserStatus {
    
    ACTIVE("Active"),
    SUSPENDED("Suspended"),
    INACTIVE("Inactive"),
    PENDING("Pending");
    
    private final String displayName;
    
    UserStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean canLogin() {
        return this == ACTIVE;
    }
    
    public boolean canModify() {
        return this == ACTIVE || this == SUSPENDED;
    }
}
```

## Getting Started

### Stack Requirements

- Java 21 or higher
- Maven 3.6+
- PostgreSQL database

## Development Guidelines

### Creating a New Feature

#### 1. Entity First (`/entity`)

Create your database model:

```java
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
}
```

#### 2. Database Migration (`/resources/db/migration`)

Create a Flyway migration script:

**Example Structure:**

```sql
-- V1__Create_users_table.sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. Repository (`/repository`)

Define data access interface:

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

#### 4. DTOs (`/dto`)

Create request and response objects:

```java
// Request DTO
@Data
public class CreateUserRequest {
    @NotBlank
    private String name;
    
    @Email
    @NotBlank
    private String email;
}

// Response DTO
@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
}
```

#### 5. Service (`/service`)

Implement business logic following your business rules:

```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    public UserResponse createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        
        User savedUser = userRepository.save(user);
        
        return new UserResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }
}
```

#### 6. Controller (`/controller`)

Create REST endpoints:

```java
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
```

### Migration File Naming Convention

- Format: `V{version}__{description}.sql`
- Examples:
  - `V1__Create_users_table.sql`
  - `V2__Add_created_at_to_users.sql`
  - `V3__Create_orders_table.sql`

### Best Practices

1. **Separation of Concerns**: Each layer should have a single responsibility
2. **DTO Usage**: Never expose entities directly in controllers
3. **Validation**: Use Bean Validation annotations in DTOs
4. **Error Handling**: Implement proper exception handling if needed
5. **Database Migrations**: Always use Flyway for schema changes

## Code Generation Patterns for AI Agents

### Required Imports by Layer

**Entity Layer:**

```java
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
```

**Repository Layer:**

```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
```

**Service Layer:**

```java
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
```

**Controller Layer:**

```java
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
```

**DTO Layer:**

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
```

### Naming Conventions

- **Entities**: PascalCase (e.g., `User`, `Order`, `Product`)
- **Tables**: snake_case (e.g., `users`, `orders`, `products`)
- **Columns**: snake_case (e.g., `first_name`, `created_at`)
- **Request DTOs**: `Create{Entity}Request`, `Update{Entity}Request`
- **Response DTOs**: `{Entity}Response`
- **Controllers**: `{Entity}Controller`
- **Services**: `{Entity}Service`
- **Repositories**: `{Entity}Repository`
- **Enums**: PascalCase (e.g., `UserStatus`, `OrderStatus`)
- **Migration Files**: `V{number}__{Description}.sql`

### Standard Annotations

- **@Entity** + **@Table(name = "table_name")**: For JPA entities
- **@Service**: For service classes
- **@Repository**: For repository interfaces
- **@RestController** + **@RequestMapping("/api/entity")**: For controllers
- **@Data** + **@NoArgsConstructor** + **@AllArgsConstructor**: For DTOs and entities
- **@RequiredArgsConstructor**: For dependency injection
- **@Slf4j**: For logging
- **@Transactional**: For service methods that modify data
- **@Transactional(readOnly = true)**: For read-only operations

## Available Dependencies

- **Spring Boot Web**: REST API development
- **Spring Boot Data JPA**: Database operations
- **PostgreSQL Driver**: Database connectivity
- **Flyway**: Database migrations
- **Lombok**: Reduce boilerplate code
- **Spring Boot Actuator**: Application monitoring
- **Spring Boot DevTools**: Development utilities
