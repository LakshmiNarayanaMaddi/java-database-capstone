
# Admin User Stories

---

## US-ADM-001: Admin Login

**Title:**
_As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely._

**Acceptance Criteria:**
1. Admin can access a login page with username and password fields.
2. The system authenticates credentials and grants access on success.
3. An error message is shown for invalid credentials without revealing which field is wrong.
4. The account is locked after 5 consecutive failed login attempts.
5. A secure session token is generated upon successful login.

**Priority:** High
**Story Points:** 5

**Notes:**
- Passwords must be stored as hashed values (bcrypt or equivalent).
- Login page must be served over HTTPS only.
- Consider adding two-factor authentication (2FA) in a future sprint.

---

## US-ADM-002: Admin Logout

**Title:**
_As an admin, I want to log out of the portal, so that I can protect system access when I am done._

**Acceptance Criteria:**
1. A visible "Logout" button is available on all authenticated pages.
2. Clicking logout invalidates the current session token on the server side.
3. Admin is redirected to the login page immediately after logout.
4. Back-button navigation after logout does not restore the authenticated session.
5. The system logs the logout event with a timestamp for audit purposes.

**Priority:** High
**Story Points:** 3

**Notes:**
- Session should auto-expire after 30 minutes of inactivity.
- Ensure browser cache is cleared of sensitive data on logout.
- Edge case: logout must work consistently across all supported browsers.

---

## US-ADM-003: Add Doctor

**Title:**
_As an admin, I want to add a new doctor to the portal, so that the doctor can be listed and assigned appointments._

**Acceptance Criteria:**
1. Admin can access an "Add Doctor" form with fields: name, specialisation, email, phone, and availability.
2. All required fields are validated before submission (no empty fields, valid email format).
3. The system prevents duplicate entries based on email address.
4. Upon successful creation, the new doctor appears in the doctor listing page.
5. A confirmation notification is displayed after the doctor is successfully added.

**Priority:** High
**Story Points:** 5

**Notes:**
- Email address must be unique across all doctor records.
- Consider sending a welcome email to the doctor upon profile creation.
- Edge case: handle special characters in doctor names gracefully.

---

## US-ADM-004: Delete Doctor Profile

**Title:**
_As an admin, I want to delete a doctor's profile from the portal, so that inactive or incorrect profiles are not visible to patients._

**Acceptance Criteria:**
1. Admin can select a doctor profile and choose the "Delete" option.
2. A confirmation dialog is displayed before the deletion is finalised.
3. The system checks for upcoming appointments linked to the doctor before deletion.
4. If linked appointments exist, admin is warned and must cancel or reassign them first.
5. Deleted profiles are soft-deleted (archived) rather than permanently removed from the database.

**Priority:** High
**Story Points:** 5

**Notes:**
- Soft-delete preserves appointment history and allows data recovery.
- An audit log must record which admin performed the deletion and when.
- Edge case: block deletion of a doctor with an active, in-progress appointment.

---

## US-ADM-005: Monthly Appointments Report via Stored Procedure

**Title:**
_As an admin, I want to run a stored procedure in MySQL CLI to get the number of appointments per month, so that I can track platform usage statistics._

**Acceptance Criteria:**
1. A stored procedure named `GetAppointmentsPerMonth` is available in the database.
2. The procedure accepts an optional year parameter to filter results by year.
3. Results return the columns: `Month`, `Year`, and `Total_Appointments`.
4. Admin can execute the procedure from MySQL CLI using `CALL GetAppointmentsPerMonth();`.
5. Output is accurate and matches the records in the appointments table.

**Priority:** Medium
**Story Points:** 3

**Notes:**
- Example CLI command: `CALL GetAppointmentsPerMonth(2025);`
- Months with zero appointments should return `0`, not `NULL`.
- Edge case: ensure the procedure handles leap years and months with no data correctly.
- Consider scheduling automated exports to a reporting table in a future sprint.


  # Patient User Stories

---

## US-PAT-001: View Doctor List Without Login

**Title:**
_As a patient, I want to view a list of doctors without logging in, so that I can explore available options before deciding to register._

**Acceptance Criteria:**
1. A public-facing doctor listing page is accessible without authentication.
2. Each doctor card displays name, specialisation, and available time slots.
3. Patients can filter the list by specialisation or availability.
4. No sensitive doctor contact information is shown to unauthenticated users.
5. A "Sign Up to Book" call-to-action button is visible on the listing page.

**Priority:** Medium
**Story Points:** 3

**Notes:**
- Page should be SEO-friendly to allow search engine indexing.
- Edge case: display an appropriate empty state message if no doctors are currently listed.
- Pagination or lazy loading should be implemented for large doctor lists.

---

## US-PAT-002: Patient Registration

**Title:**
_As a patient, I want to sign up using my email and password, so that I can book appointments with doctors._

**Acceptance Criteria:**
1. A registration form is available with fields: full name, email, password, and confirm password.
2. Email format is validated and must be unique in the system.
3. Password must meet minimum security requirements (at least 8 characters, one number, one special character).
4. A verification email is sent to the patient after successful registration.
5. Patient is redirected to the login page or dashboard upon successful sign-up.

**Priority:** High
**Story Points:** 5

**Notes:**
- Passwords must be stored as hashed values (bcrypt or equivalent).
- Edge case: gracefully handle registration attempts with an already-registered email.
- Consider adding an optional phone number field for appointment reminders in a future sprint.

---

## US-PAT-003: Patient Login

**Title:**
_As a patient, I want to log into the portal, so that I can manage my bookings and appointments._

**Acceptance Criteria:**
1. A login page is available with email and password fields.
2. The system authenticates credentials and creates a secure session on success.
3. A clear error message is shown for invalid credentials without specifying which field is wrong.
4. The patient is redirected to their personal dashboard after a successful login.
5. A "Forgot Password" link is available on the login page for account recovery.

**Priority:** High
**Story Points:** 3

**Notes:**
- Session should expire after 30 minutes of inactivity.
- Login must be served over HTTPS only.
- Edge case: handle locked accounts with an appropriate message directing the patient to support.

---

## US-PAT-004: Patient Logout

**Title:**
_As a patient, I want to log out of the portal, so that I can secure my account when I am finished._

**Acceptance Criteria:**
1. A "Logout" button is visible on all authenticated patient pages.
2. Clicking logout terminates the session and invalidates the session token on the server.
3. The patient is redirected to the home or login page after logout.
4. Back-button navigation after logout does not restore the session.
5. Sensitive personal data is not accessible in browser history after logout.

**Priority:** High
**Story Points:** 2

**Notes:**
- Auto-logout should trigger after 30 minutes of inactivity.
- Edge case: if a patient has an unconfirmed booking in progress, prompt them before logging out.

---

## US-PAT-005: Book an Appointment

**Title:**
_As a patient, I want to log in and book an hour-long appointment with a doctor, so that I can consult with them at a convenient time._

**Acceptance Criteria:**
1. Authenticated patients can browse available doctors and select one to book.
2. The booking form displays the doctor's available time slots in the patient's local timezone.
3. Patient can select a date and a 60-minute time slot from the available options.
4. A confirmation screen summarises the booking details before final submission.
5. A confirmation notification (email or in-app) is sent to the patient after the booking is confirmed.

**Priority:** High
**Story Points:** 8

**Notes:**
- Already-booked slots must not be selectable to prevent double-booking.
- Edge case: handle concurrent booking attempts for the same slot using a locking mechanism.
- Consider allowing patients to add a brief reason for visit during booking in a future sprint.

---

## US-PAT-006: View Upcoming Appointments

**Title:**
_As a patient, I want to view my upcoming appointments, so that I can prepare accordingly._

**Acceptance Criteria:**
1. An "My Appointments" section is available on the patient dashboard.
2. The list displays all upcoming appointments with: doctor name, specialisation, date, and time.
3. Past appointments are shown in a separate "History" tab.
4. Patient can cancel an upcoming appointment directly from this view.
5. Each appointment entry shows any notes or reason for visit added at the time of booking.

**Priority:** Medium
**Story Points:** 3

**Notes:**
- Appointments should be sorted by date ascending (nearest first) by default.
- Edge case: show a clear empty state message if no upcoming appointments exist.
- Consider adding a 24-hour reminder notification before each appointment in a future sprint.
# Doctor User Stories

---

## US-DOC-001: Doctor Login

**Title:**
_As a doctor, I want to log into the portal, so that I can manage my appointments and profile._

**Acceptance Criteria:**
1. A login page is available with email and password fields for doctors.
2. The system authenticates credentials and creates a secure session on success.
3. A clear error message is shown for invalid credentials without specifying which field is incorrect.
4. The doctor is redirected to their personal dashboard after a successful login.
5. Account is temporarily locked after 5 consecutive failed login attempts.

**Priority:** High
**Story Points:** 5

**Notes:**
- Doctors may share a login page with other roles but are routed to a role-specific dashboard.
- Session must expire after 30 minutes of inactivity.
- Login must be served over HTTPS only.

---

## US-DOC-002: Doctor Logout

**Title:**
_As a doctor, I want to log out of the portal, so that I can protect my data and patient information._

**Acceptance Criteria:**
1. A "Logout" button is clearly visible on all authenticated doctor pages.
2. Clicking logout invalidates the session token on the server side immediately.
3. The doctor is redirected to the login page after logout.
4. Back-button navigation after logout does not restore the session.
5. The system logs the logout event with a timestamp for audit purposes.

**Priority:** High
**Story Points:** 2

**Notes:**
- Auto-logout should trigger after 30 minutes of inactivity.
- Edge case: if the doctor has unsaved profile changes, prompt before logging out.

---

## US-DOC-003: View Appointment Calendar

**Title:**
_As a doctor, I want to view my appointment calendar, so that I can stay organised and manage my schedule effectively._

**Acceptance Criteria:**
1. An appointment calendar view is available on the doctor's dashboard.
2. Calendar supports daily, weekly, and monthly views.
3. Each appointment entry displays: patient name, appointment time, and duration.
4. Upcoming appointments are visually distinguished from past ones.
5. Doctor can click an appointment to view full details.

**Priority:** High
**Story Points:** 5

**Notes:**
- Calendar should display in the doctor's local timezone.
- Edge case: handle days with no appointments by showing an appropriate empty state.
- Consider allowing the doctor to export the calendar to an external format (e.g., iCal) in a future sprint.

---

## US-DOC-004: Mark Unavailability

**Title:**
_As a doctor, I want to mark my unavailability, so that patients can only see and book slots when I am available._

**Acceptance Criteria:**
1. Doctor can access an "Availability" or "Schedule" settings page.
2. Doctor can block out specific dates and time ranges as unavailable.
3. Blocked slots are immediately removed from the patient-facing booking view.
4. Doctor can add a recurring unavailability pattern (e.g., every Sunday).
5. Doctor can remove or edit previously set unavailability blocks.

**Priority:** High
**Story Points:** 5

**Notes:**
- Edge case: if a patient has already booked a slot that the doctor then marks unavailable, the patient must be notified and offered a rebooking option.
- Unavailability should be visible to admins for scheduling oversight.

---

## US-DOC-005: Update Profile

**Title:**
_As a doctor, I want to update my profile with my specialisation and contact information, so that patients have access to accurate and up-to-date details._

**Acceptance Criteria:**
1. Doctor can access a profile settings page with editable fields: name, specialisation, bio, phone, and clinic address.
2. All required fields are validated before submission.
3. Changes are saved and reflected immediately on the public doctor listing page.
4. Doctor receives a confirmation message after a successful profile update.
5. Profile update history is logged for admin audit purposes.

**Priority:** Medium
**Story Points:** 3

**Notes:**
- Email address should not be editable by the doctor directly; changes must go through admin to prevent identity issues.
- Edge case: handle concurrent profile edits (e.g., admin and doctor editing at the same time) with a last-write-wins or conflict warning approach.

---

## US-DOC-006: View Patient Details for Upcoming Appointments

**Title:**
_As a doctor, I want to view patient details for my upcoming appointments, so that I can be prepared before each consultation._

**Acceptance Criteria:**
1. Doctor can select any upcoming appointment from the calendar to view full patient details.
2. Patient details displayed include: full name, contact number, and reason for visit.
3. Doctor can view the patient's appointment history for previous consultations.
4. Patient details are only accessible to the assigned doctor for that appointment.
5. Sensitive patient data is masked in logs and not cached in the browser.

**Priority:** High
**Story Points:** 5

**Notes:**
- Access to patient details must comply with data privacy standards (e.g., HIPAA or GDPR as applicable).
- Edge case: if a patient has not provided a reason for visit, display a placeholder rather than an error.
- Doctor must not be able to view details of appointments belonging to other doctors.
