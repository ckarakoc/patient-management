package nl.ckarakoc.patientservice.mapper;

import java.time.LocalDate;
import nl.ckarakoc.patientservice.dto.PatientRequestDTO;
import nl.ckarakoc.patientservice.dto.PatientResponseDTO;
import nl.ckarakoc.patientservice.model.Patient;

public class PatientMapper {

  public static PatientResponseDTO toDTO(Patient patient) {
    PatientResponseDTO patientDTO = new PatientResponseDTO();
    patientDTO.setId(patient.getId().toString());
    patientDTO.setName(patient.getName());
    patientDTO.setAddress(patient.getAddress());
    patientDTO.setEmail(patient.getEmail());
    patientDTO.setDateOfBirth(patient.getDateOfBirth().toString());
    return patientDTO;
  }

  public static Patient toModel(PatientRequestDTO patientDTO) {
    Patient patient = new Patient();
    patient.setName(patientDTO.getName());
    patient.setAddress(patientDTO.getAddress());
    patient.setEmail(patientDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse(patientDTO.getDateOfBirth()));
    patient.setRegisteredDate(LocalDate.parse(patientDTO.getRegisteredDate()));
    return patient;
  }

}
