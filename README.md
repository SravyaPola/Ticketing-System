# Ticketing Gateway System

**A Microservices-Based Support Ticket Management Application**

---

## Table of Contents

- [Overview](#overview)  
- [User Roles & Workflows](#user-roles--workflows)  
- [Data Model](#data-model)  
- [Key Features](#key-features)  
- [Architecture & Components](#architecture--components)  
- [UI Pages](#ui-pages)  
  - [Register & Login](#register--login)  
  - [User Dashboard](#user-dashboard)  
  - [Manager Dashboard](#manager-dashboard)  
  - [Admin Dashboard](#admin-dashboard)  
  - [Notifications Dashboard](#notifications-dashboard)  
  - [Ticket History](#ticket-history)  
- [Email & Notifications](#email--notifications)  
- [Scheduled Jobs](#scheduled-jobs)  
- [Getting Started](#getting-started)  
- [Contributing](#contributing)  
- [License](#license)  

---

## Overview

Ticketing Gateway System is a full‑stack, microservices‑based application designed to streamline support ticket lifecycles across organizational roles (USER, MANAGER, ADMIN). Built with Spring Boot, JMS (ActiveMQ), and a lightweight HTML/jQuery frontend, it delivers secure authentication, role‑based dashboards, and robust workflows for creating, approving, resolving, and closing tickets.

---

## User Roles & Workflows

- **User**  
  - Raise new support tickets via the dashboard.  
  - View, close, or reopen their tickets.  
  - Receive email confirmations and feedback requests.

- **Manager**  
  - Review pending tickets and either approve or reject them.  
  - Provide rejection reasons when applicable.  
  - Receive daily reminders for tickets older than 7 days.

- **Admin**  
  - Handle approved tickets by resolving, closing, or reopening them.  
  - Generate and download PDF summaries of resolutions.  
  - Send feedback emails post‑resolution.

---

## Data Model

- **Employee**  
  - Fields: `id`, `name`, `email`, `roles` (USER, MANAGER, ADMIN)  
  - Relationships:  
    - Many-to-many with `Role`  
    - One-to-many as ticket creator and assignee  
    - One-to-many as actor in ticket history  

- **Role**  
  - Fields: `id`, `name`  

- **Ticket**  
  - Fields: `id`, `title`, `description`, `priority`, `status`, `createdDate`  
  - Relationships:  
    - `createdBy` (Employee)  
    - `assignee` (Employee)  
    - History (one-to-many with `TicketHistory`)  

- **TicketHistory**  
  - Records every status change with `actionBy`, `timestamp`, and `comments`.  

---

## Key Features

- Secure, JWT‑based authentication and role‑based access.  
- Modular microservices communicating via JMS (ActiveMQ).  
- Automated email notifications on ticket actions.  
- PDF generation of ticket resolutions.  
- Feedback workflow capturing user ratings (1–5).  
- Daily scheduled jobs for approval reminders and stale ticket cleanup.  

---

## Architecture & Components

```
[ Client Browser ] ↔ [Gateway Service] ↔ JMS Broker (ActiveMQ)
                       ↕                ↕
             [Ticket Microservice]    [Notification Microservice]
```

- **Gateway Service** (Spring Cloud Gateway)  
  - Authentication, authorization, and API routing.  
  - Publishes ticket events to JMS.

- **Ticket Microservice**  
  - CRUD operations and business logic for tickets.  
  - Schedules overdue alerts and publishes update events.

- **Notification Microservice**  
  - Listens for ticket events on JMS.  
  - Sends templated emails with PDF attachments.  
  - Stores notification records.

---

## UI Pages

### Register & Login

- User registration with secure password hashing.  
- Role-based login redirects to appropriate dashboard.

### User Dashboard

- Create new tickets with title, description, and attachments.  
- View “Your Tickets” table: ID, title, priority, status, assignee.  
- Actions: View details, close, or reopen tickets.

### Manager Dashboard

- Icon-driven landing page with ticket approvals.  
- “Tickets to Approve” list: approve or reject with comments.

### Admin Dashboard

- View assigned tickets and take actions: Resolve, Close, Reopen.  
- Generate PDF resolution summaries.

### Notifications Dashboard

- Real‑time feed of ticket events: creation, approval, assignment, resolution, closure.  
- Unread badge indicators and navigation back to dashboard.

### Ticket History

- Full audit trail of ticket lifecycle events.  
- Displays actor, role, timestamp, and comments.

---

## Email & Notifications

- **Confirmation & Updates** via `NotificationMicroservice`.  
- **PDF Summaries** generated on resolution and attached to closing emails.  
- **Feedback Emails** prompt users to rate support (via clickable links).

---

## Scheduled Jobs

- **Daily at 9:00 PM**: Remind managers of tickets older than 7 days awaiting approval.  
- **Daily at 9:00 AM**: Auto-close tickets in `RESOLVED` status for more than 5 days, notifying users.

---

## Getting Started

1. **Clone the repo**  
   ```bash
   git clone <repo-url>
   cd Ticketing-System-main
   ```

2. **Start ActiveMQ**  
   Ensure JMS broker is running (`activemq start`).

3. **Configure Services**  
   - Copy `application.properties.example` to `application.properties` in each service.  
   - Set database, JMS, and SMTP credentials.

4. **Build & Run**  
   ```bash
   mvn clean install

   # In separate terminals:
   cd TicketingGateway && mvn spring-boot:run
   cd TicketMicroservice && mvn spring-boot:run
   cd NotificationMicroservice && mvn spring-boot:run
   ```

5. **Access UI**  
   Open `http://localhost:8080` in your browser.

---

## Contributing

1. Fork the repository  
2. Create a feature branch (`git checkout -b feature/YourFeature`)  
3. Commit your changes (`git commit -m "Add feature"`)  
4. Push to your branch (`git push origin feature/YourFeature`)  
5. Open a Pull Request  

---

## License

This project is licensed under the MIT License.  

