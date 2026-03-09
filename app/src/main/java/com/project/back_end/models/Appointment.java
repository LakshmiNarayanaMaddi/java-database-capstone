package com.project.back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Appointment {

    // ─── Primary Key ──────────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ─── Relationships ────────────────────────────────────────────────────────

    // Many appointments can belong to one doctor
    @ManyToOne
    @NotNull(message = "Doctor cannot be null")
    private Doctor doctor;

    // Many appointments can belong to one patient
    @ManyToOne
    @NotNull(message = "Patient cannot be null")
    private Patient patient;

    // ─── Fields ───────────────────────────────────────────────────────────────

    // Appointment must be scheduled at a future date and time
    @Future(message = "Appointment time must be in the future")
    @NotNull(message = "Appointment time cannot be null")
    private LocalDateTime appointmentTime;

    // 0 = Scheduled, 1 = Completed
    @NotNull(message = "Status cannot be null")
    private int status;

    // ─── Helper Methods (not persisted) ───────────────────────────────────────

    // Returns the end time: 1 hour after the appointment start time
    @Transient
    public LocalDateTime getEndTime() {
        return appointmentTime.plusHours(1);
    }

    // Returns only the date portion of the appointment (e.g., 2025-06-15)
    @Transient
    public LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }

    // Returns only the time portion of the appointment (e.g., 10:30)
    @Transient
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public int getStatus() {
        return status;
    }

    // ─── Setters ──────────────────────────────────────────────────────────────

    public void setId(Long id) {
        this.id = id;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}