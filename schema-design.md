# Schema Design — java-database-capstone

This document defines the database schema for the Healthcare Portal project.  
The system uses **MySQL** for structured relational data (users, appointments, doctors)  
and **MongoDB** for flexible, document-based data (prescriptions, feedback, logs).

---

## MySQL Relational Database Design

MySQL is chosen for entities that have well-defined relationships and require strict
data integrity — such as admin accounts, doctor profiles, patient records, and appointments.

---

### Table 1: `admin`

Stores administrator credentials used to manage the platform.

```sql
CREATE TABLE admin (
    id          INT             AUTO_INCREMENT PRIMARY KEY,  -- Unique admin identifier
    username    VARCHAR(100)    NOT NULL UNIQUE,             -- Login username, must be unique
    password    VARCHAR(255)    NOT NULL,                    -- Bcrypt-hashed password
    email       VARCHAR(150)    NOT NULL UNIQUE,             -- Admin email, must be unique
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP   -- Record creation timestamp
);
```

| Column      | Type         | Constraints              |
|-------------|--------------|--------------------------|
| id          | INT          | PRIMARY KEY, AUTO_INCREMENT |
| username    | VARCHAR(100) | NOT NULL, UNIQUE         |
| password    | VARCHAR(255) | NOT NULL                 |
| email       | VARCHAR(150) | NOT NULL, UNIQUE         |
| created_at  | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP |

---

### Table 2: `doctors`

Stores doctor profiles managed by the admin.

```sql
CREATE TABLE doctors (
    id              INT             AUTO_INCREMENT PRIMARY KEY,  -- Unique doctor identifier
    full_name       VARCHAR(150)    NOT NULL,                    -- Doctor's full name
    email           VARCHAR(150)    NOT NULL UNIQUE,             -- Unique email per doctor
    phone           VARCHAR(20)     NOT NULL,                    -- Contact number
    specialisation  VARCHAR(100)    NOT NULL,                    -- e.g., Cardiology, Dermatology
    available       BOOLEAN         DEFAULT TRUE,                -- TRUE if accepting appointments
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,  -- Profile creation timestamp
    deleted_at      TIMESTAMP       NULL                        -- Soft-delete timestamp; NULL = active
    -- Soft-delete pattern used so appointment history is preserved after profile removal
);
```

| Column         | Type         | Constraints                  |
|----------------|--------------|------------------------------|
| id             | INT          | PRIMARY KEY, AUTO_INCREMENT  |
| full_name      | VARCHAR(150) | NOT NULL                     |
| email          | VARCHAR(150) | NOT NULL, UNIQUE             |
| phone          | VARCHAR(20)  | NOT NULL                     |
| specialisation | VARCHAR(100) | NOT NULL                     |
| available      | BOOLEAN      | DEFAULT TRUE                 |
| created_at     | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP    |
| deleted_at     | TIMESTAMP    | NULL (soft-delete flag)      |

---

### Table 3: `patients`

Stores registered patient accounts.

```sql
CREATE TABLE patients (
    id          INT             AUTO_INCREMENT PRIMARY KEY,  -- Unique patient identifier
    full_name   VARCHAR(150)    NOT NULL,                    -- Patient's full name
    email       VARCHAR(150)    NOT NULL UNIQUE,             -- Login email, must be unique
    password    VARCHAR(255)    NOT NULL,                    -- Bcrypt-hashed password
    phone       VARCHAR(20),                                 -- Optional contact number
    dob         DATE            NOT NULL,                    -- Date of birth for medical context
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP   -- Account creation timestamp
);
```

| Column     | Type         | Constraints                 |
|------------|--------------|-----------------------------|
| id         | INT          | PRIMARY KEY, AUTO_INCREMENT |
| full_name  | VARCHAR(150) | NOT NULL                    |
| email      | VARCHAR(150) | NOT NULL, UNIQUE            |
| password   | VARCHAR(255) | NOT NULL                    |
| phone      | VARCHAR(20)  | NULL (optional)             |
| dob        | DATE         | NOT NULL                    |
| created_at | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP   |

---

### Table 4: `appointments`

Links patients to doctors for a scheduled appointment slot.

```sql
CREATE TABLE appointments (
    id              INT             AUTO_INCREMENT PRIMARY KEY,     -- Unique appointment ID
    patient_id      INT             NOT NULL,                       -- FK → patients.id
    doctor_id       INT             NOT NULL,                       -- FK → doctors.id
    appointment_date DATE           NOT NULL,                       -- Scheduled date
    start_time      TIME            NOT NULL,                       -- Start time of the slot
    end_time        TIME            NOT NULL,                       -- End time (always start + 1 hour)
    status          ENUM(
                        'PENDING',
                        'CONFIRMED',
                        'CANCELLED',
                        'COMPLETED'
                    )               DEFAULT 'PENDING',              -- Appointment lifecycle status
    reason          VARCHAR(255),                                   -- Optional reason for visit
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,     -- Booking timestamp

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id) REFERENCES patients(id)
        ON DELETE CASCADE,                                          -- Remove appointments if patient is deleted

    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id) REFERENCES doctors(id)
        ON DELETE RESTRICT,                                         -- Block doctor deletion if appointments exist

    CONSTRAINT chk_time_order
        CHECK (end_time > start_time),                             -- Ensure valid time range

    CONSTRAINT uq_doctor_slot
        UNIQUE (doctor_id, appointment_date, start_time)           -- Prevent double-booking same slot
);
```

| Column           | Type         | Constraints                               |
|------------------|--------------|-------------------------------------------|
| id               | INT          | PRIMARY KEY, AUTO_INCREMENT               |
| patient_id       | INT          | NOT NULL, FK → patients(id) ON DELETE CASCADE |
| doctor_id        | INT          | NOT NULL, FK → doctors(id) ON DELETE RESTRICT |
| appointment_date | DATE         | NOT NULL                                  |
| start_time       | TIME         | NOT NULL                                  |
| end_time         | TIME         | NOT NULL                                  |
| status           | ENUM         | DEFAULT 'PENDING'                         |
| reason           | VARCHAR(255) | NULL (optional)                           |
| created_at       | TIMESTAMP    | DEFAULT CURRENT_TIMESTAMP                 |

> **Design note:** The `UNIQUE (doctor_id, appointment_date, start_time)` constraint enforces
> no double-booking at the database level, acting as a safety net beyond application-layer validation.

---

### Stored Procedure: `GetAppointmentsPerMonth`

Used by admins to track monthly usage statistics via MySQL CLI.

```sql
DELIMITER $$

CREATE PROCEDURE GetAppointmentsPerMonth(IN filter_year INT)
BEGIN
    -- If no year is passed (NULL), default to the current year
    IF filter_year IS NULL THEN
        SET filter_year = YEAR(CURDATE());
    END IF;

    SELECT
        MONTHNAME(appointment_date)  AS Month,
        YEAR(appointment_date)       AS Year,
        COUNT(*)                     AS Total_Appointments
    FROM appointments
    WHERE YEAR(appointment_date) = filter_year
    GROUP BY MONTH(appointment_date), MONTHNAME(appointment_date), YEAR(appointment_date)
    ORDER BY MONTH(appointment_date);
END$$

DELIMITER ;

-- Usage:
-- CALL GetAppointmentsPerMonth(NULL);    → current year
-- CALL GetAppointmentsPerMonth(2025);    → filter by 2025
```

---

## MongoDB Collection Design

MongoDB is chosen for data that is unstructured, varies per record, or benefits from
document nesting — such as prescriptions (which vary by condition), patient feedback,
and system activity logs.

---

### Collection 1: `prescriptions`

Stores prescriptions issued by doctors after a consultation. Each prescription
is tightly coupled to a specific appointment and may contain multiple medications.
A document model is ideal here because the number of medications varies per visit.

```json
{
  "_id": "ObjectId('64f3a2c9e4b07d1a2c3d4e5f')",

  "appointment_id": 102,
  "doctor_id": 7,
  "patient_id": 23,
  "issued_at": "2025-06-15T10:30:00Z",

  "diagnosis": "Acute Bronchitis",
  "notes": "Patient advised to rest and increase fluid intake.",

  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "3 times a day",
      "duration_days": 7,
      "instructions": "Take after meals"
    },
    {
      "name": "Paracetamol",
      "dosage": "650mg",
      "frequency": "As needed",
      "duration_days": 5,
      "instructions": "Do not exceed 4 doses in 24 hours"
    }
  ],

  "follow_up_required": true,
  "follow_up_date": "2025-06-22"
}
```

> **Design note:** Medications are stored as an array of embedded documents rather than
> a separate collection. This avoids expensive joins and keeps a complete prescription
> snapshot intact even if medication records are later updated globally.

---

### Collection 2: `feedback`

Stores ratings and comments left by patients after a completed appointment.
Schema flexibility allows optional fields (e.g., tags, anonymous flag) without migrations.

```json
{
  "_id": "ObjectId('64f3b1d2e4b07d1a2c3d5a9c')",

  "appointment_id": 102,
  "patient_id": 23,
  "doctor_id": 7,
  "submitted_at": "2025-06-15T14:20:00Z",

  "rating": 5,
  "comment": "Dr. Smith was very thorough and explained everything clearly.",

  "tags": ["professional", "on-time", "clear-explanation"],

  "anonymous": false
}
```

> **Design note:** `tags` is an array field, making it simple to query common feedback
> themes (e.g., `db.feedback.find({ tags: "on-time" })`). The `anonymous` flag allows
> patients to hide their identity while still associating feedback with an appointment.

---

### Collection 3: `logs`

Captures system activity events (logins, logouts, admin actions) for auditing and debugging.
Log structures vary by event type, making a flexible document model preferable to a rigid table.

```json
{
  "_id": "ObjectId('64f3c9a7e4b07d1a2c3d6b1e')",

  "event_type": "ADMIN_DELETE_DOCTOR",
  "performed_by": {
    "user_id": 1,
    "role": "admin",
    "username": "admin_root"
  },
  "target": {
    "entity": "doctor",
    "entity_id": 14,
    "entity_name": "Dr. John Carter"
  },
  "timestamp": "2025-06-15T09:45:00Z",
  "ip_address": "192.168.1.10",
  "status": "SUCCESS",
  "meta": {
    "reason": "Doctor requested account removal",
    "affected_appointments": 3
  }
}
```

> **Design note:** Logs embed contextual metadata (`performed_by`, `target`, `meta`) as
> nested objects because the shape differs per event type. Storing logs in MongoDB means
> new event types can be added without altering a relational schema.

---

## Design Decision Summary

| Decision | Rationale |
|---|---|
| MySQL for core entities | Patients, doctors, and appointments have fixed schemas and require referential integrity |
| Soft-delete on `doctors` | Preserves historical appointment data and supports audit trails |
| `UNIQUE` constraint on appointment slot | Enforces no double-booking at the database level as a hard constraint |
| `ON DELETE CASCADE` for patient → appointments | Automatically cleans up appointments when a patient account is removed |
| `ON DELETE RESTRICT` for doctor → appointments | Prevents accidental data loss; admin must reassign appointments first |
| MongoDB for prescriptions | Medication lists vary in length and structure per visit — document arrays are a natural fit |
| MongoDB for logs | Event shapes differ by type; schema flexibility avoids constant table migrations |
| Embedded medications in prescriptions | Keeps prescription snapshot self-contained; immune to future medication data changes |
