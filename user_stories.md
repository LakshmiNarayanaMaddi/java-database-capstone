# Smart Clinic Management System - User Stories

---

## 👤 Admin User Stories

| Story ID | User Story | Acceptance Criteria |
|---|---|---|
| US-A01 | As an admin, I want to log in securely so that I can access the dashboard. | Admin can log in with valid credentials; invalid login shows error. |
| US-A02 | As an admin, I want to add a new doctor so that they can be available for appointments. | Doctor is saved in MySQL with all required fields. |
| US-A03 | As an admin, I want to view all registered doctors so that I can manage them. | A list of all doctors is displayed on the dashboard. |
| US-A04 | As an admin, I want to update doctor details so that information stays accurate. | Changes are saved and reflected immediately. |
| US-A05 | As an admin, I want to delete a doctor so that inactive doctors are removed from the system. | Doctor is removed from the database and no longer appears in listings. |
| US-A06 | As an admin, I want to view all patients so that I can monitor who is registered. | Full patient list is visible on the admin dashboard. |
| US-A07 | As an admin, I want to view all appointments so that I can oversee clinic activity. | All appointments across all doctors and patients are listed. |
| US-A08 | As an admin, I want to cancel any appointment so that scheduling conflicts are resolved. | Appointment status changes to "Cancelled" and patient is notified. |

---

## 🧑‍⚕️ Patient User Stories

| Story ID | User Story | Acceptance Criteria |
|---|---|---|
| US-P01 | As a patient, I want to register an account so that I can access the clinic system. | Patient account is created and stored in MySQL. |
| US-P02 | As a patient, I want to log in so that I can manage my appointments. | Patient is authenticated and redirected to their dashboard. |
| US-P03 | As a patient, I want to view available doctors so that I can choose who to book. | A list of doctors with specializations is displayed. |
| US-P04 | As a patient, I want to book an appointment so that I can consult a doctor. | Appointment is saved with status "Pending" and confirmation is shown. |
| US-P05 | As a patient, I want to view my upcoming appointments so that I can keep track of them. | All booked appointments are listed with date, time, and doctor name. |
| US-P06 | As a patient, I want to cancel my appointment so that I can free up the slot if not needed. | Appointment status updates to "Cancelled" and slot becomes available. |
| US-P07 | As a patient, I want to view my prescriptions so that I can follow my treatment. | Prescriptions from MongoDB are displayed with medicine and dosage details. |
| US-P08 | As a patient, I want to update my profile so that my personal details stay current. | Updated details are saved and reflected in the system. |

---

## 🩺 Doctor User Stories

| Story ID | User Story | Acceptance Criteria |
|---|---|---|
| US-D01 | As a doctor, I want to log in so that I can access my dashboard. | Doctor is authenticated and redirected to their Thymeleaf dashboard. |
| US-D02 | As a doctor, I want to view my appointments so that I know which patients I need to see. | All upcoming appointments assigned to the doctor are listed. |
| US-D03 | As a doctor, I want to accept or reject an appointment so that I can manage my schedule. | Appointment status updates to "Confirmed" or "Rejected" accordingly. |
| US-D04 | As a doctor, I want to set my available time slots so that patients can book accordingly. | Availability is saved and reflects on the patient booking page. |
| US-D05 | As a doctor, I want to write a prescription for a patient so that their treatment is recorded. | Prescription is saved as a document in MongoDB and linked to the patient. |
| US-D06 | As a doctor, I want to view a patient's prescription history so that I can make informed decisions. | All past prescriptions for the selected patient are retrieved from MongoDB. |
| US-D07 | As a doctor, I want to update my profile so that my specialization and contact info are accurate. | Updated details are saved in MySQL and visible to patients and admin. |
