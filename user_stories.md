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
