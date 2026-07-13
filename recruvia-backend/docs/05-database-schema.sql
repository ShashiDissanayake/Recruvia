/****************************************************************************************
* Project    : Recruvia
* Version    : 1.0
* Database   : PostgreSQL
* Description: Initial Database Schema
****************************************************************************************/

-- =============================================================================
-- EXTENSIONS
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =============================================================================
-- DROP TABLES (Development Only)
-- =============================================================================

DROP TABLE IF EXISTS audit_logs CASCADE;
DROP TABLE IF EXISTS refresh_tokens CASCADE;
DROP TABLE IF EXISTS email_verification_tokens CASCADE;
DROP TABLE IF EXISTS password_reset_tokens CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS interviews CASCADE;
DROP TABLE IF EXISTS applications CASCADE;
DROP TABLE IF EXISTS job_skills CASCADE;
DROP TABLE IF EXISTS user_skills CASCADE;
DROP TABLE IF EXISTS resumes CASCADE;
DROP TABLE IF EXISTS skills CASCADE;
DROP TABLE IF EXISTS jobs CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- =============================================================================
-- ENUM TYPES
-- =============================================================================

CREATE TYPE account_status_enum AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'SUSPENDED'
);

CREATE TYPE employment_type_enum AS ENUM (
    'FULL_TIME',
    'PART_TIME',
    'CONTRACT',
    'INTERN',
    'FREELANCE'
);

CREATE TYPE job_status_enum AS ENUM (
    'DRAFT',
    'OPEN',
    'CLOSED'
);

CREATE TYPE proficiency_level_enum AS ENUM (
    'BEGINNER',
    'INTERMEDIATE',
    'ADVANCED',
    'EXPERT'
);

CREATE TYPE application_status_enum AS ENUM (
    'PENDING',
    'REVIEWING',
    'SHORTLISTED',
    'INTERVIEW',
    'ACCEPTED',
    'REJECTED',
    'WITHDRAWN'
);

CREATE TYPE interview_status_enum AS ENUM (
    'SCHEDULED',
    'COMPLETED',
    'CANCELLED',
    'RESCHEDULED'
);

CREATE TYPE notification_type_enum AS ENUM (
    'EMAIL',
    'SYSTEM',
    'APPLICATION',
    'INTERVIEW'
);

-- =============================================================================
-- TABLE : roles
-- =============================================================================

CREATE TABLE roles
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(50) NOT NULL UNIQUE,

    description VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- TABLE : users
-- =============================================================================

CREATE TABLE users
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    role_id UUID NOT NULL,

    first_name VARCHAR(100) NOT NULL,

    last_name VARCHAR(100) NOT NULL,

    email VARCHAR(255) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    phone VARCHAR(20),

    profile_image VARCHAR(500),

    account_status account_status_enum
        NOT NULL
                        DEFAULT 'ACTIVE',

    email_verified BOOLEAN
        NOT NULL
                        DEFAULT FALSE,

    last_login TIMESTAMP,

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id)
            REFERENCES roles(id)
            ON DELETE RESTRICT
);

-- =============================================================================
-- TABLE : companies
-- =============================================================================

CREATE TABLE companies
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    recruiter_id UUID NOT NULL,

    company_name VARCHAR(255)
                      NOT NULL
        UNIQUE,

    description TEXT,

    website VARCHAR(255),

    logo_url VARCHAR(500),

    industry VARCHAR(100),

    location VARCHAR(255),

    company_size VARCHAR(50),

    created_at TIMESTAMP
                      NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
                      NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_company_recruiter
        FOREIGN KEY (recruiter_id)
            REFERENCES users(id)
            ON DELETE RESTRICT
);

-- =============================================================================
-- SEED DATA : ROLES
-- =============================================================================

INSERT INTO roles (name, description)
VALUES
    ('ADMIN', 'System Administrator'),
    ('RECRUITER', 'Recruiter'),
    ('CANDIDATE', 'Job Candidate');

-- =============================================================================
-- END OF PART 01
-- =============================================================================

-- =============================================================================
-- TABLE : jobs
-- =============================================================================

CREATE TABLE jobs
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    company_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    description TEXT NOT NULL,

    employment_type employment_type_enum NOT NULL,

    experience_level VARCHAR(30),

    salary_min DECIMAL(10,2),

    salary_max DECIMAL(10,2),

    location VARCHAR(255),

    application_deadline DATE,

    status job_status_enum
        NOT NULL
                        DEFAULT 'OPEN',

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_jobs_company
        FOREIGN KEY (company_id)
            REFERENCES companies(id)
            ON DELETE CASCADE,

    CONSTRAINT chk_salary_range
        CHECK (
            salary_min IS NULL
                OR salary_max IS NULL
                OR salary_min <= salary_max
            )
);

-- =============================================================================
-- TABLE : skills
-- =============================================================================

CREATE TABLE skills
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(100)
        NOT NULL
        UNIQUE,

    category VARCHAR(100),

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================================
-- TABLE : resumes
-- =============================================================================

CREATE TABLE resumes
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    file_name VARCHAR(255) NOT NULL,

    file_url VARCHAR(500) NOT NULL,

    file_type VARCHAR(20) NOT NULL,

    file_size BIGINT NOT NULL,

    summary TEXT,

    ai_processed BOOLEAN
        NOT NULL
                        DEFAULT FALSE,

    is_active BOOLEAN
        NOT NULL
                        DEFAULT TRUE,

    uploaded_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_resume_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT chk_file_type
        CHECK (
            file_type IN ('PDF', 'DOCX')
            ),

    CONSTRAINT chk_file_size
        CHECK (
            file_size > 0
            )
);

-- =============================================================================
-- TABLE : user_skills
-- =============================================================================

CREATE TABLE user_skills
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    skill_id UUID NOT NULL,

    proficiency_level proficiency_level_enum,

    years_of_experience DECIMAL(4,1),

    CONSTRAINT fk_user_skill_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_user_skill_skill
        FOREIGN KEY (skill_id)
            REFERENCES skills(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_user_skill
        UNIQUE (user_id, skill_id),

    CONSTRAINT chk_years_experience
        CHECK (
            years_of_experience IS NULL
                OR years_of_experience >= 0
            )
);

-- =============================================================================
-- TABLE : job_skills
-- =============================================================================

CREATE TABLE job_skills
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    job_id UUID NOT NULL,

    skill_id UUID NOT NULL,

    required BOOLEAN
        NOT NULL
                        DEFAULT TRUE,

    CONSTRAINT fk_job_skill_job
        FOREIGN KEY (job_id)
            REFERENCES jobs(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_job_skill_skill
        FOREIGN KEY (skill_id)
            REFERENCES skills(id)
            ON DELETE CASCADE,

    CONSTRAINT uq_job_skill
        UNIQUE (job_id, skill_id)
);

-- =============================================================================
-- END OF PART 02
-- =============================================================================

-- =============================================================================
-- TABLE : applications
-- =============================================================================

CREATE TABLE applications
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    job_id UUID NOT NULL,

    user_id UUID NOT NULL,

    resume_id UUID NOT NULL,

    application_status application_status_enum
        NOT NULL
                        DEFAULT 'PENDING',

    ai_match_score DECIMAL(5,2),

    applied_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_application_job
        FOREIGN KEY (job_id)
            REFERENCES jobs(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_application_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_application_resume
        FOREIGN KEY (resume_id)
            REFERENCES resumes(id)
            ON DELETE RESTRICT,

    CONSTRAINT uq_application
        UNIQUE (job_id, user_id),

    CONSTRAINT chk_match_score
        CHECK (
            ai_match_score IS NULL
                OR (ai_match_score >= 0 AND ai_match_score <= 100)
            )
);

-- =============================================================================
-- TABLE : interviews
-- =============================================================================

CREATE TABLE interviews
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    application_id UUID NOT NULL UNIQUE,

    meeting_link VARCHAR(500),

    scheduled_at TIMESTAMP,

    interview_status interview_status_enum
                        NOT NULL
                        DEFAULT 'SCHEDULED',

    notes TEXT,

    created_at TIMESTAMP
                        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP
                        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_interview_application
        FOREIGN KEY (application_id)
            REFERENCES applications(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- TABLE : notifications
-- =============================================================================

CREATE TABLE notifications
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    message TEXT NOT NULL,

    notification_type notification_type_enum
        NOT NULL,

    is_read BOOLEAN
        NOT NULL
                        DEFAULT FALSE,

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- TABLE : refresh_tokens
-- =============================================================================

CREATE TABLE refresh_tokens
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL UNIQUE,

    token VARCHAR(500)
                 NOT NULL
        UNIQUE,

    expires_at TIMESTAMP NOT NULL,

    revoked BOOLEAN
        NOT NULL
                        DEFAULT FALSE,

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- TABLE : email_verification_tokens
-- =============================================================================

CREATE TABLE email_verification_tokens
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL UNIQUE,

    token VARCHAR(500)
                 NOT NULL
        UNIQUE,

    expires_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_email_verification_token_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- TABLE : password_reset_tokens
-- =============================================================================

CREATE TABLE password_reset_tokens
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID NOT NULL UNIQUE,

    token VARCHAR(500)
                 NOT NULL
        UNIQUE,

    expires_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_password_reset_token_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE
);

-- =============================================================================
-- TABLE : audit_logs
-- =============================================================================

CREATE TABLE audit_logs
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    user_id UUID,

    action VARCHAR(255) NOT NULL,

    entity_name VARCHAR(100) NOT NULL,

    entity_id UUID,

    ip_address VARCHAR(50),

    created_at TIMESTAMP
        NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_user
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE SET NULL
);

-- =============================================================================
-- END OF PART 03
-- =============================================================================

-- =============================================================================
-- INDEXES
-- =============================================================================

CREATE INDEX idx_users_email
    ON users(email);

CREATE INDEX idx_users_role
    ON users(role_id);

CREATE INDEX idx_users_status
    ON users(account_status);

CREATE INDEX idx_companies_name
    ON companies(company_name);

CREATE INDEX idx_companies_recruiter
    ON companies(recruiter_id);

CREATE INDEX idx_jobs_company
    ON jobs(company_id);

CREATE INDEX idx_jobs_title
    ON jobs(title);

CREATE INDEX idx_jobs_status
    ON jobs(status);

CREATE INDEX idx_jobs_location
    ON jobs(location);

CREATE INDEX idx_jobs_deadline
    ON jobs(application_deadline);

CREATE INDEX idx_skills_name
    ON skills(name);

CREATE INDEX idx_resumes_user
    ON resumes(user_id);

CREATE INDEX idx_resumes_active
    ON resumes(is_active);

CREATE INDEX idx_user_skills_user
    ON user_skills(user_id);

CREATE INDEX idx_user_skills_skill
    ON user_skills(skill_id);

CREATE INDEX idx_job_skills_job
    ON job_skills(job_id);

CREATE INDEX idx_job_skills_skill
    ON job_skills(skill_id);

CREATE INDEX idx_applications_job
    ON applications(job_id);

CREATE INDEX idx_applications_user
    ON applications(user_id);

CREATE INDEX idx_applications_resume
    ON applications(resume_id);

CREATE INDEX idx_applications_status
    ON applications(application_status);

CREATE INDEX idx_interviews_application
    ON interviews(application_id);

CREATE INDEX idx_interviews_schedule
    ON interviews(scheduled_at);

CREATE INDEX idx_notifications_user
    ON notifications(user_id);

CREATE INDEX idx_notifications_read
    ON notifications(is_read);

CREATE INDEX idx_notifications_type
    ON notifications(notification_type);

CREATE INDEX idx_refresh_tokens_user
    ON refresh_tokens(user_id);

CREATE INDEX idx_refresh_tokens_expiry
    ON refresh_tokens(expires_at);

CREATE INDEX idx_email_verification_tokens_user
    ON email_verification_tokens(user_id);

CREATE INDEX idx_email_verification_tokens_expiry
    ON email_verification_tokens(expires_at);

CREATE INDEX idx_password_reset_tokens_user
    ON password_reset_tokens(user_id);

CREATE INDEX idx_password_reset_tokens_expiry
    ON password_reset_tokens(expires_at);

CREATE INDEX idx_audit_logs_user
    ON audit_logs(user_id);

CREATE INDEX idx_audit_logs_entity
    ON audit_logs(entity_name);

CREATE INDEX idx_audit_logs_created
    ON audit_logs(created_at);

-- =============================================================================
-- COMMENTS
-- =============================================================================

COMMENT ON TABLE roles IS
'Stores system roles.';

COMMENT ON TABLE users IS
'Stores all registered users.';

COMMENT ON TABLE companies IS
'Stores recruiter companies.';

COMMENT ON TABLE jobs IS
'Stores published job vacancies.';

COMMENT ON TABLE skills IS
'Stores master skill records.';

COMMENT ON TABLE resumes IS
'Stores uploaded resumes.';

COMMENT ON TABLE user_skills IS
'Many-to-many relationship between users and skills.';

COMMENT ON TABLE job_skills IS
'Many-to-many relationship between jobs and required skills.';

COMMENT ON TABLE applications IS
'Stores candidate job applications.';

COMMENT ON TABLE interviews IS
'Stores interview information.';

COMMENT ON TABLE notifications IS
'Stores user notifications.';

COMMENT ON TABLE refresh_tokens IS
'Stores JWT refresh tokens.';

COMMENT ON TABLE email_verification_tokens IS
'Stores email verification tokens.';

COMMENT ON TABLE password_reset_tokens IS
'Stores password reset tokens.';

COMMENT ON TABLE audit_logs IS
'Stores security and audit events.';

-- =============================================================================
-- SEED DATA
-- =============================================================================

INSERT INTO roles (name, description)
VALUES
    ('ADMIN', 'System Administrator'),
    ('RECRUITER', 'Recruiter'),
    ('CANDIDATE', 'Job Candidate')
    ON CONFLICT (name) DO NOTHING;

-- =============================================================================
-- SAMPLE VALIDATION QUERIES
-- =============================================================================

-- List all tables
-- SELECT table_name
-- FROM information_schema.tables
-- WHERE table_schema = 'public'
-- ORDER BY table_name;

-- List all foreign keys
-- SELECT
--     tc.table_name,
--     kcu.column_name,
--     ccu.table_name AS referenced_table,
--     ccu.column_name AS referenced_column
-- FROM information_schema.table_constraints tc
-- JOIN information_schema.key_column_usage kcu
--      ON tc.constraint_name = kcu.constraint_name
-- JOIN information_schema.constraint_column_usage ccu
--      ON ccu.constraint_name = tc.constraint_name
-- WHERE tc.constraint_type = 'FOREIGN KEY';

-- =============================================================================
-- DATABASE INITIALIZATION COMPLETED
-- =============================================================================

/*
===============================================================================

Recruvia Database Schema
Version : 1.0

Summary

✓ PostgreSQL Extension Enabled
✓ UUID Primary Keys
✓ ENUM Types
✓ 15 Database Tables
✓ Primary Keys
✓ Foreign Keys
✓ Composite Unique Constraints
✓ CHECK Constraints
✓ Default Values
✓ Performance Indexes
✓ Seed Data
✓ Production Ready

Database Objects

1. roles
2. users
3. companies
4. jobs
5. skills
6. resumes
7. user_skills
8. job_skills
9. applications
10. interviews
11. notifications
12. refresh_tokens
13. email_verification_tokens
14. password_reset_tokens
15. audit_logs

===============================================================================
END OF FILE
===============================================================================
*/