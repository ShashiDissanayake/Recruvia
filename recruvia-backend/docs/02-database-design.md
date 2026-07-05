# Recruvia Database Design

**Version:** 1.0

**Project:** Recruvia – AI-Powered Recruitment & Resume Screening Platform

**Database:** PostgreSQL

**ORM:** Spring Data JPA (Hibernate)

---

# 8. Database Design Approach

## Overview

The Recruvia database is designed using relational database principles to ensure scalability, consistency, security, and maintainability.

The design follows industry best practices and supports future system growth without major structural changes.

## Design Principles

- Third Normal Form (3NF)
- Referential Integrity
- Minimal Data Redundancy
- High Data Consistency
- Performance Optimization
- Scalability
- Maintainability
- Secure Data Storage

---

# 9. Database Technology Stack

| Component | Technology |
|-----------|------------|
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Migration Tool | Flyway |
| Cache | Redis |
| File Storage | MinIO / AWS S3 |
| API | Spring Boot REST API |

---

# 10. Entity Identification

The system consists of three categories of entities.

## Core Entities

These represent the primary business objects.

- Role
- User
- Company
- Job
- Resume
- Skill
- Application
- Interview
- Notification
- RefreshToken
- AuditLog

---

## Junction Entities

These resolve many-to-many relationships.

- UserSkill
- JobSkill

---

## Future Entities (Version 2)

The following entities are planned for future releases.

- Education
- Experience
- Certificate
- Bookmark
- Message
- Comment
- Review

---

# 11. Entity Relationship Overview

The high-level relationships between entities are shown below.

```
Role
    │
    ▼
Users
    │
    ├──────────────► Companies
    │                    │
    │                    ▼
    │                  Jobs
    │                    │
    ▼                    ▼
 Resumes          JobSkills
    │
    ▼
UserSkills

Users ─────► Applications ◄──── Jobs
                  │
                  ▼
             Interviews

Users ─────► Notifications

Users ─────► RefreshTokens

Users ─────► AuditLogs
```

The detailed ER Diagram will be created in the next phase.

---

# 12. Entity Specifications

Every entity must define the following information.

- Table Name
- Purpose
- Attributes
- Data Types
- Primary Key
- Foreign Keys
- Constraints
- Default Values
- Relationships

Example

| Attribute | Type | Constraint |
|-----------|------|------------|
| id | UUID | Primary Key |
| email | VARCHAR(255) | UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| created_at | TIMESTAMP | NOT NULL |

Detailed specifications for every entity will be completed in the next phase.

---

# 13. Relationship Analysis

| Relationship | Cardinality |
|--------------|-------------|
| User → Role | Many-to-One |
| Recruiter → Company | One-to-Many |
| Company → Job | One-to-Many |
| User → Resume | One-to-Many |
| User ↔ Skill | Many-to-Many |
| Job ↔ Skill | Many-to-Many |
| User ↔ Job | Many-to-Many (Application) |
| Application → Interview | One-to-One (Optional) |
| User → Notification | One-to-Many |
| User → RefreshToken | One-to-One |
| User → AuditLog | One-to-Many |

---

# 14. Database Business Rules

The following business rules govern the database structure.

1. Every user must have exactly one role.
2. Only recruiters can create companies.
3. A recruiter may own multiple companies.
4. A company may publish multiple jobs.
5. A candidate may upload multiple resumes, but only one resume can be active at a time.
6. Users and Skills have a many-to-many relationship.
7. Jobs and Skills have a many-to-many relationship.
8. A candidate may apply for multiple jobs.
9. A job may receive multiple applications.
10. An application may have zero or one interview.
11. A user may receive multiple notifications.
12. Each user may have only one active refresh token.
13. Passwords must always be stored using BCrypt hashing.
14. Email addresses must be unique across all users.

---

# 15. Database Normalization

The database is designed according to Third Normal Form (3NF).

The objectives of normalization are:

- Eliminate duplicate data
- Reduce redundancy
- Improve data consistency
- Maintain referential integrity
- Simplify maintenance
- Improve scalability

---

# 16. Naming Conventions

To maintain consistency, the following naming conventions are used.

## Tables

- snake_case
- plural names

Examples

```
users
roles
companies
jobs
applications
```

---

## Columns

- snake_case

Examples

```
first_name
last_name
profile_image
created_at
```

---

## Primary Keys

```
id
```

---

## Foreign Keys

```
user_id
role_id
company_id
job_id
resume_id
application_id
```

---

# 17. Database Constraints

The database will use the following constraints.

- Primary Keys
- Foreign Keys
- UNIQUE Constraints
- NOT NULL Constraints
- CHECK Constraints
- DEFAULT Values
- Cascade Rules

Examples

- Email must be unique.
- Password cannot be null.
- Job status must contain only valid values.
- Foreign keys maintain referential integrity.

---

# 18. Indexing Strategy

Indexes will be created on frequently queried columns.

Primary indexes

- email
- company_name
- title
- application_status
- created_at

Composite indexes may be introduced later based on application performance.

---

# 19. Future Database Extensions

Future versions of the platform may include the following entities.

- Education
- Experience
- Certifications
- Messaging
- Reviews
- AI Feedback History
- Saved Jobs
- Job Recommendations

---

# 20. Deliverables

At the end of this phase, the following outputs are completed.

- Database Design Principles
- Technology Stack
- Entity Identification
- High-Level Relationships
- Business Rules
- Naming Conventions
- Database Constraints
- Indexing Strategy

The next phase will include:

- Complete Table Specifications
- Primary Keys
- Foreign Keys
- ER Diagram (DBML)
- SQL Schema