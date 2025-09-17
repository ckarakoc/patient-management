package nl.ckarakoc.patientservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import nl.ckarakoc.patientservice.dto.PatientRequestDTO;
import nl.ckarakoc.patientservice.dto.PatientResponseDTO;
import nl.ckarakoc.patientservice.exception.EmailAlreadyExistsException;
import nl.ckarakoc.patientservice.exception.PatientNotFoundException;
import nl.ckarakoc.patientservice.grpc.BillingServiceGrpcClient;
import nl.ckarakoc.patientservice.kafka.KafkaProducer;
import nl.ckarakoc.patientservice.mapper.PatientMapper;
import nl.ckarakoc.patientservice.model.Patient;
import nl.ckarakoc.patientservice.repository.PatientRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;

  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();
    return patients.stream().map(PatientMapper::toDTO).toList();
  }

  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDTO.getEmail());
    }
    Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

    billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
    kafkaProducer.sendEvent(newPatient);

    return PatientMapper.toDTO(newPatient);
  }

  public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
    Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));
    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException("A patient with this email already exists " + patientRequestDTO.getEmail());
    }

    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient);
    return PatientMapper.toDTO(updatedPatient);
  }

  public void deletePatient(UUID id) {
    patientRepository.deleteById(id);
  }
}
