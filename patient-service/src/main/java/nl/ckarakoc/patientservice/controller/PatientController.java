package nl.ckarakoc.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.patientservice.dto.PatientRequestDTO;
import nl.ckarakoc.patientservice.dto.PatientResponseDTO;
import nl.ckarakoc.patientservice.dto.validators.CreatePatientValidationGroup;
import nl.ckarakoc.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient", description = "API for managing Patients")
@RequiredArgsConstructor
public class PatientController {

  private final PatientService patientService;

  @GetMapping
  @Operation(summary = "Get all patients")
  public ResponseEntity<List<PatientResponseDTO>> getPatients() {
    return ResponseEntity.ok(patientService.getPatients());
  }

  @PostMapping
  @Operation(summary = "Create a new patient")
  public ResponseEntity<PatientResponseDTO> createPatient(
      @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO
  ) {
    return ResponseEntity.ok(patientService.createPatient(patientRequestDTO));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an existing patient")
  public ResponseEntity<PatientResponseDTO> updatePatient(
      @PathVariable UUID id,
      @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO
  ) {
    return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an existing patient")
  public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
    patientService.deletePatient(id);
    return ResponseEntity.noContent().build();
  }
}
