# Recruvia Table Specifications

**Version:** 1.0

**Project:** Recruvia – AI-Powered Recruitment & Resume Screening Platform

**Database:** PostgreSQL

**ORM:** Spring Data JPA (Hibernate)

---

# Table of Contents

1. Introduction
2. Database Standards
3. Naming Conventions
4. Table Specifications
    - 4.1 roles
    - 4.2 users
    - 4.3 companies
    - 4.4 jobs
    - 4.5 resumes
    - 4.6 skills
    - 4.7 user_skills
    - 4.8 job_skills
    - 4.9 applications
    - 4.10 interviews
    - 4.11 notifications
    - 4.12 refresh_tokens
    - 4.13 audit_logs
5. Summary

---

# 1. Introduction

This document defines the complete database table specifications for the Recruvia platform.

Each table includes:

- Purpose
- Columns
- PostgreSQL Data Types
- Primary Keys
- Foreign Keys
- Constraints
- Default Values

This document serves as the technical blueprint for implementing the database schema and Spring Boot JPA entities.

---

# 2. Database Standards

## Database Engine

PostgreSQL

## ORM

Spring Data JPA (Hibernate)

## Primary Key Strategy

All tables use **UUID** as the Primary Key.

Advantages:

- Globally Unique
- Secure
- Microservice Friendly
- Easy Horizontal Scaling

---

## Timestamp Standard

The following audit columns are used whenever applicable.

- created_at
- updated_at

Both fields are managed automatically by Spring Data JPA.

---

## Password Storage

Passwords are stored only as BCrypt hashes.

---

## Status Fields

Status fields are stored as VARCHAR in PostgreSQL and mapped to Java Enums inside Spring Boot.

---

# 3. Naming Conventions

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
created_at
updated_at
profile_image
```

---

## Primary Key

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
skill_id
recruiter_id
```

---

# 4. Table Specifications

---

# 4.1 roles

## Purpose

Stores all system roles.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Role ID |
| name | VARCHAR(50) | UNIQUE, NOT NULL | Role Name |
| description | VARCHAR(255) | NULL | Description |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |

### Default Records

- ADMIN
- RECRUITER
- CANDIDATE

---

# 4.2 users

## Purpose

Stores all registered users.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | User ID |
| first_name | VARCHAR(100) | NOT NULL | First Name |
| last_name | VARCHAR(100) | NOT NULL | Last Name |
| email | VARCHAR(255) | UNIQUE, NOT NULL | Email Address |
| password | VARCHAR(255) | NOT NULL | BCrypt Password |
| phone | VARCHAR(20) | NULL | Phone Number |
| profile_image | VARCHAR(500) | NULL | Profile Image URL |
| role_id | UUID | FOREIGN KEY, NOT NULL | User Role |
| account_status | VARCHAR(20) | DEFAULT 'ACTIVE' | Account Status |
| email_verified | BOOLEAN | DEFAULT FALSE | Email Verification |
| last_login | TIMESTAMP | NULL | Last Login |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |
| updated_at | TIMESTAMP | DEFAULT NOW() | Updated Time |

### Allowed Account Status

- ACTIVE
- INACTIVE
- SUSPENDED

---

# 4.3 companies

## Purpose

Stores recruiter companies.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Company ID |
| recruiter_id | UUID | FOREIGN KEY, NOT NULL | Company Owner |
| company_name | VARCHAR(255) | UNIQUE, NOT NULL | Company Name |
| description | TEXT | NULL | Description |
| website | VARCHAR(255) | NULL | Website |
| logo_url | VARCHAR(500) | NULL | Logo URL |
| industry | VARCHAR(100) | NULL | Industry |
| location | VARCHAR(255) | NULL | Location |
| company_size | VARCHAR(50) | NULL | Company Size |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |
| updated_at | TIMESTAMP | DEFAULT NOW() | Updated Time |

---

# 4.4 jobs

## Purpose

Stores job vacancies.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Job ID |
| company_id | UUID | FOREIGN KEY, NOT NULL | Company |
| title | VARCHAR(255) | NOT NULL | Job Title |
| description | TEXT | NOT NULL | Job Description |
| employment_type | VARCHAR(30) | NOT NULL | Employment Type |
| experience_level | VARCHAR(30) | NULL | Experience Level |
| salary_min | DECIMAL(10,2) | NULL | Minimum Salary |
| salary_max | DECIMAL(10,2) | NULL | Maximum Salary |
| location | VARCHAR(255) | NULL | Job Location |
| application_deadline | DATE | NULL | Deadline |
| status | VARCHAR(20) | DEFAULT 'OPEN' | Job Status |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |
| updated_at | TIMESTAMP | DEFAULT NOW() | Updated Time |

### Allowed Employment Types

- FULL_TIME
- PART_TIME
- CONTRACT
- INTERN
- FREELANCE

### Allowed Job Status

- DRAFT
- OPEN
- CLOSED

---

# 4.5 resumes

## Purpose

Stores uploaded resumes.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Resume ID |
| user_id | UUID | FOREIGN KEY, NOT NULL | Candidate |
| file_name | VARCHAR(255) | NOT NULL | File Name |
| file_url | VARCHAR(500) | NOT NULL | File URL |
| file_type | VARCHAR(20) | NOT NULL | PDF / DOCX |
| file_size | BIGINT | NOT NULL | File Size |
| summary | TEXT | NULL | AI Summary |
| ai_processed | BOOLEAN | DEFAULT FALSE | AI Processed |
| is_active | BOOLEAN | DEFAULT TRUE | Active Resume |
| uploaded_at | TIMESTAMP | DEFAULT NOW() | Uploaded Time |

---

# 4.6 skills

## Purpose

Stores master skill records.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Skill ID |
| name | VARCHAR(100) | UNIQUE, NOT NULL | Skill Name |
| category | VARCHAR(100) | NULL | Skill Category |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |

---

# 4.7 user_skills

## Purpose

Stores candidate skills.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Record ID |
| user_id | UUID | FOREIGN KEY, NOT NULL | User |
| skill_id | UUID | FOREIGN KEY, NOT NULL | Skill |
| proficiency_level | VARCHAR(30) | NULL | Skill Level |
| years_of_experience | DECIMAL(4,1) | NULL | Years of Experience |

### Composite Unique Constraint

```
(user_id, skill_id)
```

### Allowed Proficiency Levels

- BEGINNER
- INTERMEDIATE
- ADVANCED
- EXPERT

---

# 4.8 job_skills

## Purpose

Stores required skills for jobs.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Record ID |
| job_id | UUID | FOREIGN KEY, NOT NULL | Job |
| skill_id | UUID | FOREIGN KEY, NOT NULL | Skill |
| required | BOOLEAN | DEFAULT TRUE | Required Skill |

### Composite Unique Constraint

```
(job_id, skill_id)
```

---

# 4.9 applications

## Purpose

Stores candidate job applications.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Application ID |
| job_id | UUID | FOREIGN KEY, NOT NULL | Job |
| user_id | UUID | FOREIGN KEY, NOT NULL | Candidate |
| resume_id | UUID | FOREIGN KEY, NOT NULL | Resume Used |
| application_status | VARCHAR(30) | DEFAULT 'PENDING' | Application Status |
| ai_match_score | DECIMAL(5,2) | NULL | AI Match Score |
| applied_at | TIMESTAMP | DEFAULT NOW() | Applied Time |

### Composite Unique Constraint

```
(job_id, user_id)
```

### Allowed Status

- PENDING
- REVIEWING
- SHORTLISTED
- INTERVIEW
- ACCEPTED
- REJECTED
- WITHDRAWN

---

# 4.10 interviews

## Purpose

Stores interview details.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Interview ID |
| application_id | UUID | FOREIGN KEY, UNIQUE, NOT NULL | Application |
| meeting_link | VARCHAR(500) | NULL | Meeting URL |
| scheduled_at | TIMESTAMP | NULL | Scheduled Date |
| interview_status | VARCHAR(30) | DEFAULT 'SCHEDULED' | Interview Status |
| notes | TEXT | NULL | Interview Notes |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |

### Allowed Status

- SCHEDULED
- COMPLETED
- CANCELLED
- RESCHEDULED

---

# 4.11 notifications

## Purpose

Stores user notifications.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Notification ID |
| user_id | UUID | FOREIGN KEY, NOT NULL | User |
| title | VARCHAR(255) | NOT NULL | Title |
| message | TEXT | NOT NULL | Message |
| notification_type | VARCHAR(50) | NOT NULL | Notification Type |
| is_read | BOOLEAN | DEFAULT FALSE | Read Status |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |

### Notification Types

- EMAIL
- SYSTEM
- APPLICATION
- INTERVIEW

---

# 4.12 refresh_tokens

## Purpose

Stores JWT Refresh Tokens.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Token ID |
| user_id | UUID | FOREIGN KEY, UNIQUE, NOT NULL | User |
| token | VARCHAR(500) | UNIQUE, NOT NULL | Refresh Token |
| expires_at | TIMESTAMP | NOT NULL | Expiration Time |
| revoked | BOOLEAN | DEFAULT FALSE | Revoked Status |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |

---

# 4.13 audit_logs

## Purpose

Stores audit history of important system activities.

### Columns

| Column | Data Type | Constraints | Description |
|----------|-----------|------------|-------------|
| id | UUID | PRIMARY KEY | Log ID |
| user_id | UUID | FOREIGN KEY | User |
| action | VARCHAR(255) | NOT NULL | Performed Action |
| entity_name | VARCHAR(100) | NOT NULL | Target Entity |
| entity_id | UUID | NULL | Entity ID |
| ip_address | VARCHAR(50) | NULL | Client IP |
| created_at | TIMESTAMP | DEFAULT NOW() | Created Time |

---

# 5. Summary

This document defines the complete database table specifications for the Recruvia platform.

## Deliverables

- 13 Database Tables
- UUID Primary Key Strategy
- PostgreSQL Data Types
- Column Definitions
- Primary Keys
- Foreign Keys
- Composite Unique Constraints
- Default Values
- Business Rules
- Enum-Compatible Status Fields
- Audit Columns

This document is the foundation for the next phase:

- Entity Relationship Diagram (ERD)
- DBML Design
- SQL Schema Generation
- Spring Boot Entity Implementation