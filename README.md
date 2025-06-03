
# Ticketing System

## Description
A microservices-based ticketing system built using Spring Boot, ActiveMQ, MySQL, and JSP for the frontend. It allows users to register, log in, and create support tickets. Managers can view and assign tickets, while admins can oversee the entire system. The Notification Microservice sends email notifications with dynamically generated PDF tickets. The Ticketing Gateway provides a web interface for all user roles.

## Table of Contents
1. [Architecture](#architecture)  
2. [Prerequisites](#prerequisites)  
3. [Getting Started](#getting-started)  
   - [Cloning the Repository](#cloning-the-repository)  
   - [Database Setup](#database-setup)  
   - [Running Microservices](#running-microservices)  
   - [Running the Gateway](#running-the-gateway)  
4. [Microservices](#microservices)  
   - [Ticket Microservice](#ticket-microservice)  
   - [Notification Microservice](#notification-microservice)  
5. [Ticketing Gateway](#ticketing-gateway)  
   - [Folder Structure](#folder-structure)  
   - [Key Controllers](#key-controllers)  
   - [Views](#views)  
6. [API Endpoints](#api-endpoints)  
7. [Configuration](#configuration)  
8. [File Uploads](#file-uploads)  
9. [Technologies Used](#technologies-used)  
10. [Project Structure](#project-structure)  
11. [Testing](#testing)  
12. [Contributing](#contributing)  
13. [License](#license)  
14. [Acknowledgements](#acknowledgements)  

---

## Architecture
The Ticketing System is composed of three Spring Boot microservices and a web gateway:
1. **Ticket Microservice**  
   - Manages ticket creation, updates, and history.  
   - Publishes ticket events to an ActiveMQ queue for downstream processing.  
2. **Notification Microservice**  
   - Listens to the ticket event queue.  
   - Generates a PDF ticket and emails it to the user.  
3. **Ticketing Gateway**  
   - Provides a JSP-based web interface for registration, authentication, and role-based ticket management.  
   - Forwards requests to the Ticket Microservice and interacts with the Notification Microservice via JMS.  

Refer high-level flow diagrams (located in `images/`):
  

---

## Prerequisites
Before running the system, ensure you have the following installed and configured on your machine:
- **Java 11 (or higher)**  
- **Maven 3.6+**  
- **MySQL 8.0+**  
- **ActiveMQ (5.16+)**  
- **Git**  
- A modern web browser for accessing the gateway (e.g., Chrome, Firefox)

---

## Getting Started

### Cloning the Repository
```bash
git clone https://github.com/SravyaPola/Ticketing-System.git
cd Ticketing-System
```

## Database Setup

1. **Start MySQL**

2. **Create a database named `ticketingsystem`:**
   ```sql
   CREATE DATABASE ticketingsystem;
    ```

3. **Create a MySQL user (if not already existing) and grant privileges::**
```sql
CREATE USER 'root'@'localhost' IDENTIFIED BY '12345';
GRANT ALL PRIVILEGES ON ticketingsystem.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

The default credentials in application.properties for all microservices are `username`: root and `password`: 12345. Adjust if you change them.

## Running Microservices
Each microservice is a Maven-based Spring Boot application. Open separate terminal windows or tabs for each one.

### ActiveMQ

Download and install ActiveMQ from [ActiveMQ Official Site](https://activemq.apache.org/).

Start the broker:
```bash
cd apache-activemq-5.x.x
bin/activemq start
```

Verify broker URL: `tcp://localhost:61616`.

### ActiveMQ Using Docker

```bash
docker run -d \\n  --name activemq \\n  -p 61616:61616 \\n  -p 8161:8161 \\n  rmohr/activemq:latest\n
```

### Ticket Microservice

```bash
cd TicketMicroservice
./mvnw clean package
./mvnw spring-boot:run
```

Default port: `8383`

Base URL: `http://localhost:8383/api/tickets`


### Notification Microservice

```bash
cd NotificationMicroservice
./mvnw clean package
./mvnw spring-boot:run
```

Default port: `8484`


Listens on ActiveMQ queue for ticket events.


Sends emails using the SMTP settings in application.properties.


### Ticketing Gateway

```bash
cd TicketingGateway
./mvnw clean package
./mvnw spring-boot:run
```

Default port: `8282`


Access the web interface at `http://localhost:8282/`


Note: Ensure that the MySQL server and ActiveMQ broker are running before starting the microservices so that JPA and JMS connections succeed.
Running the Gateway
Open your browser and navigate to:


`http://localhost:8282/`


**You will see the login or registration page. Use the “New User” form to register, or log in if you already have an account.**





# Microservices

## Ticket Microservice

- **Location:** `TicketMicroservice/`
- **Port:** `8383`
- **Purpose:**
  - Handles all CRUD operations for tickets.
  - Persists ticket data to the `ticketingsystem` MySQL database.
  - Publishes `TicketEvent` messages to the ActiveMQ broker when a ticket is created or updated.
- **Key Packages:**
  - `com.synex.controller` – REST controllers (e.g., `TicketController.java`)
  - `com.synex.service` – Business logic (e.g., `TicketService.java`)
  - `com.synex.repository` – Spring Data JPA repositories (e.g., `TicketRepository.java`)
  - `com.synex.domain` – JPA entities (e.g., `Ticket.java`, `TicketHistory.java`)
  - `com.synex.component` – JMS producer (`JmsProducer.java`) and scheduler (`TicketScheduler.java`)
- **Sample Endpoints:**
  - ```http
    POST /api/tickets
    ```
    Create a new ticket
  - ```http
    GET /api/tickets/{id}
    ```
    Retrieve ticket by ID
  - ```http
    PUT /api/tickets/{id}
    ```
    Update ticket status or details
  - ```http
    GET /api/tickets
    ```
    Retrieve all tickets (supports query parameters for filtering)

---

## Notification Microservice

- **Location:** `NotificationMicroservice/`
- **Port:** `8484`
- **Purpose:**
  - Listens to `TicketEvent` messages published by the Ticket Microservice.
  - Generates a PDF confirmation for each ticket using `PdfGenerator.java`.
  - Sends an email with the PDF attached to the user via `EmailClientService.java`.
- **Key Packages:**
  - `com.synex.domain` – Event model (`TicketEvent.java`)
  - `com.synex.component` – 
    - `TicketEventListener.java` (annotated with `@JmsListener`)
    - `PdfGenerator.java` (uses iText or similar library to build PDFs)
    - `EmailClientService.java` (configures `JavaMailSender`)
  - `com.synex.config` – JMS configuration (`ListenerConfig.java`)
- **Email Behavior:**
  - SMTP settings (Gmail) are defined in `application.properties`.
  - Emails are sent to the user’s address when a ticket is created or its status changes.
  - PDF attachments contain ticket details: ID, title, description, creation date, and status.

# Ticketing Gateway

## 1. Folder Structure

- TicketingGateway/
  - pom.xml
  - src/
    - main/
      - java/
        - com/
          - synex/
            - controller/
              - AdminController.java
              - AuthController.java
              - FileDownloadController.java
              - ManagerController.java
              - NotificationController.java
              - UserController.java
      - resources/
        - application.properties
        - templates/  (if any Thymeleaf files)
      - webapp/
        - WEB-INF/
          - views/
            - login.jsp
            - register.jsp
            - user-dashboard.jsp
            - admin-dashboard.jsp
            - manager-dashboard.jsp
            - create-ticket.jsp
            - view-ticket.jsp
            - user-notifications.jsp
            - (other JSPs)
        - css/
          - dashboard.css
          - ticket.css
          - view-ticket.css
          - (other CSS files)
    - test/
      - java/
        - com/
          - synex/
            - (unit tests)
- mvnw
- mvnw.cmd
- .mvn/

## 2. Key Controllers

### AuthController.java
- **GET `/login`** – Display login page
- **POST `/login`** – Process login credentials
- **GET `/register`** – Display registration page
- **POST `/register`** – Create a new user

### UserController.java (Role: USER)
- **GET `/user/dashboard`** – View user’s tickets and notifications
- **GET `/user/create-ticket`** – Display ticket submission form
- **POST `/user/create-ticket`** – Submit new ticket
- **GET `/user/view-ticket/{id}`** – View details of a specific ticket

### ManagerController.java (Role: MANAGER)
- **GET `/manager/dashboard`** – View all assigned tickets or team tickets
- **GET `/manager/view-ticket/{id}`** – View and update ticket status/assignment

### AdminController.java (Role: ADMIN)
- **GET `/admin/dashboard`** – View system metrics, user management page
- **POST `/admin/create-user`** – Create or assign roles to new users
- **DELETE `/admin/delete-user/{id}`** – Remove a user from the system

### NotificationController.java
- (Optional) Endpoints for manual notification triggering or status viewing

### FileDownloadController.java
- **GET `/files/{filename}`** – Stream the PDF file stored under `${user.home}/uploads`

---

## 3. Views

_All JSP files are located under `src/main/webapp/WEB-INF/views/`_

- `login.jsp` / `register.jsp`
- `user-dashboard.jsp` – Shows user’s submitted tickets and notifications
- `manager-dashboard.jsp` – Shows tickets assigned to the manager or pending assignment
- `admin-dashboard.jsp` – System summary and user management
- `create-ticket.jsp` – Form to submit a new ticket (title, description, priority, etc.)
- `view-ticket.jsp` – Detailed ticket view with status and history
- `user-notifications.jsp` – List of email notifications sent to the user

---

### CSS Files

_All CSS files are under `src/main/webapp/css/`_

- `dashboard.css`
- `ticket.css`
- `notification.css`

# API Endpoints

## Ticket Microservice (Port 8383)

| HTTP Method | Endpoint           | Description                                       |
|-------------|--------------------|---------------------------------------------------|
| POST        | `/api/tickets`     | Create a new ticket                               |
| GET         | `/api/tickets`     | Retrieve all tickets (supports filtering via query) |
| GET         | `/api/tickets/{id}`| Retrieve a single ticket by ID                    |
| PUT         | `/api/tickets/{id}`| Update an existing ticket (status, assignment, details) |
| DELETE      | `/api/tickets/{id}`| Delete a ticket by ID                             |

---

## Notification Microservice (Port 8484)

- Listens on ActiveMQ queue **`ticket.events`** for `TicketEvent` messages.
- No public REST endpoints by default (notification flows are triggered via JMS).

---

## Gateway (Port 8282)

| HTTP Method | Endpoint                   | Description                                                 | Role          |
|-------------|----------------------------|-------------------------------------------------------------|---------------|
| GET         | `/login`                   | Display login form                                          | Public        |
| POST        | `/login`                   | Authenticate user credentials                               | Public        |
| GET         | `/register`                | Display registration form                                   | Public        |
| POST        | `/register`                | Register new user                                           | Public        |
| GET         | `/user/dashboard`          | Show user dashboard (list tickets, notifications)           | USER          |
| GET         | `/user/create-ticket`      | Display “create ticket” form                                | USER          |
| POST        | `/user/create-ticket`      | Submit a new ticket                                         | USER          |
| GET         | `/user/view-ticket/{id}`   | View a particular ticket’s details                          | USER          |
| GET         | `/manager/dashboard`       | Show manager dashboard (tickets assigned to team)           | MANAGER       |
| GET         | `/manager/view-ticket/{id}`| Manager view/update of a ticket                             | MANAGER       |
| GET         | `/admin/dashboard`         | Admin dashboard (user management & system overview)         | ADMIN         |
| POST        | `/admin/create-user`       | Create a new user or assign roles                           | ADMIN         |
| DELETE      | `/admin/delete-user/{id}`  | Delete a user                                               | ADMIN         |
| GET         | `/files/{filename}`        | Download a generated PDF ticket                             | Authenticated |

# Configuration

Each application has its own `application.properties` under `src/main/resources/`:

## Common Properties

### Spring Datasource Properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ticketingsystem
spring.datasource.username=root
spring.datasource.password=12345
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### ActiveMQ (JMS)

```properties
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
spring.jms.pub-sub-domain=false  # Use point-to-point (queue) model
```

### Ticket Microservice 
Location : `(TicketMicroservice/src/main/resources/application.properties)`

```properties
spring.application.name=TicketMicroservice
server.port=8383

spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
spring.jms.pub-sub-domain=false

logging.level.org.springframework.scheduling=DEBUG
```

### Notification Microservice 
Location : `(NotificationMicroservice/src/main/resources/application.properties)`
``` properties

spring.application.name=NotificationMicroservice
server.port=8484

spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=sravyapola2000@gmail.com
spring.mail.password=<your-email-password-or-app-password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=50000
spring.mail.properties.mail.smtp.timeout=50000
spring.mail.properties.mail.smtp.writetimeout=50000
spring.mail.properties.mail.smtp.from=sravyapola2000@gmail.com
spring.mail.default-encoding=UTF-8

logging.level.org.springframework.mail=DEBUG
logging.level.com.sun.mail=DEBUG
``` 
**Important:** Replace spring.mail.password with your actual Gmail app password or valid SMTP credentials.

### Ticketing Gateway 
Location: `(TicketingGateway/src/main/resources/application.properties)`
``` properties
spring.application.name=TicketingGateway
server.port=8282

file.upload.dir=${user.home}/uploads
file.base.dir=${user.home}/uploads

spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
spring.jms.pub-sub-domain=false
```

# File Uploads

Generated PDF tickets are saved under the folder defined by `file.upload.dir` (default: `~/uploads`).

The `FileDownloadController` streams files from that directory when users click the “Download Ticket” link in the UI.

# Technologies Used

- Java 11
- Spring Boot 2.5+
- Spring Data JPA (Hibernate)
- ActiveMQ 5.x (JMS)
- MySQL 8.0+
- Maven 3.6+
- JSP / JSTL
- JavaMail (Spring Mail)
- iText (or equivalent) for PDF generation
- HTML, CSS, Bootstrap (for basic styling in JSP pages)
- Git

# Testing

### Register a “USER”, log in, and verify dashboard loads correctly.

### Create Ticket (USER)

- Go to “Create Ticket” page, fill in title, description, priority, click Submit.
- Confirm that the new ticket appears in the user’s dashboard.
- Check MySQL `ticket` and `ticket_history` tables for a new entry.

### Notification Email

- After creating a ticket, check your email inbox for a new message from sravyapola2000@gmail.com.
- Confirm that the email contains correct ticket details.

### Manager Workflow

- Log in as a “MANAGER” (must be created by an ADMIN).
- View the list of pending tickets, assign yourself or update status.
- Verify that ticket status changes and a new notification email is triggered.

### Admin Workflow

- Log in as ADMIN.
- Resolve the Tickets assigned to you.
- Upon Resolution Close/Reopen Ticket.

# Contributing
Contributions are welcome! To get started:

Fork the repository.

Create a new branch for your feature or bugfix:

```bash
git checkout -b feature/your-feature-name
```
Make your changes and commit with clear messages:
``` bash
git commit -m "Add feature X to Ticket Microservice"
```
Push to your fork:
``` bash
git push origin feature/your-feature-name
```

Open a pull request against the main branch.

Please follow these guidelines:

Write clear, concise commit messages.

Ensure all unit tests pass before opening a PR.

Document any new configuration or environment variables.

# License
This project is licensed under the MIT License. See `LICENSE` for details.

# Acknowledgements

- Spring Boot Guides for foundational examples.

- ActiveMQ Documentation for JMS configuration.

- Bootstrap for quick front-end styling.

- iText (or equivalent) for PDF generation libraries.

- Flowchart Diagrams created to visualize microservice interactions.

- Original project requirements and design by the course instructor.
