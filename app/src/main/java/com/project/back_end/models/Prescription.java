package com.project.back_end.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Maps this class to the "prescriptions" collection in MongoDB
@Document(collection = "prescriptions")
public class Prescription {

    // ─── Primary Key ──────────────────────────────────────────────────────────

    // MongoDB auto-generates this as ObjectId (_id field)
    @Id
    private String id;

    // ─── Fields ───────────────────────────────────────────────────────────────

    // Patient's full name — required, between 3 and 100 characters
    @NotNull(message = "Patient name cannot be null")
    @Size(min = 3, max = 100, message = "Patient name must be between 3 and 100 characters")
    private String patientName;

    // Reference to the MySQL Appointment entity by its ID — required
    @NotNull(message = "Appointment ID cannot be null")
    private Long appointmentId;

    // Name of the prescribed medication — required, between 3 and 100 characters
    @NotNull(message = "Medication cannot be null")
    @Size(min = 3, max = 100, message = "Medication name must be between 3 and 100 characters")
    private String medication;

    // Dosage instructions — required, between 3 and 20 characters (e.g., "500mg twice daily")
    @NotNull(message = "Dosage cannot be null")
    @Size(min = 3, max = 20, message = "Dosage must be between 3 and 20 characters")
    private String dosage;

    // Optional notes from the doctor — max 200 characters
    @Size(max = 200, message = "Doctor notes must not exceed 200 characters")
    private String doctorNotes;

    // ─── Constructors ─────────────────────────────────────────────────────────

    // Default constructor required by Spring Data MongoDB
    public Prescription() {
    }

    // Convenience constructor for creating prescriptions with core required fields
    public Prescription(String patientName, Long appointmentId, String medication, String dosage) {
        this.patientName   = patientName;
        this.appointmentId = appointmentId;
        this.medication    = medication;
        this.dosage        = dosage;
    }

    // Full constructor including optional doctorNotes
    public Prescription(String patientName, Long appointmentId, String medication, String dosage, String doctorNotes) {
        this.patientName   = patientName;
        this.appointmentId = appointmentId;
        this.medication    = medication;
        this.dosage        = dosage;
        this.doctorNotes   = doctorNotes;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public String getPatientName() {
        return patientName;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public String getMedication() {
        return medication;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDoctorNotes() {
        return doctorNotes;
    }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setId(String id) {
        this.id = id;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setDoctorNotes(String doctorNotes) {
        this.doctorNotes = doctorNotes;
    }
}