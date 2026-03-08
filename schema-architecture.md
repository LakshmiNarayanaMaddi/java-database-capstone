**Arhitecture Summary:**


This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.


Here are 7 architecture flow statements for your Smart Clinic Management System:

1. The user accesses the **Admin Dashboard** or **Doctor Dashboard**, where the request is handled by a **Thymeleaf MVC controller** that renders server-side HTML templates for the browser.

2. For all other modules (patients, appointments, prescriptions, etc.), the client communicates via **REST API controllers** that receive HTTP requests and return JSON responses.

3. Both MVC and REST controllers route all incoming requests through a **common service layer**, which encapsulates the core business logic and orchestrates operations across the system.

4. When processing patient, doctor, appointment, or admin data, the service layer delegates to **JPA repositories**, which map Java entity objects to tables in the **MySQL relational database**.

5. When handling prescription data, the service layer delegates to **MongoDB repositories**, which persist and retrieve flexible **document models** from the **MongoDB NoSQL database**.

6. The dual-database architecture ensures that **structured, relational data** (such as appointments and doctor records) is stored in MySQL, while **semi-structured, variable data** (such as prescription details) is stored in MongoDB for greater flexibility.

7. The full request lifecycle — from user interface through controller → service → repository → database — is consistent across all modules, ensuring a **unified, layered architecture** that separates concerns and keeps the system maintainable and scalable.
