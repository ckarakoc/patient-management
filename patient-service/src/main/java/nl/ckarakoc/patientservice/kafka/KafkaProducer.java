package nl.ckarakoc.patientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import nl.ckarakoc.patientservice.model.Patient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {

  private final KafkaTemplate<String, byte[]> kafkaTemplate;

  public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendEvent(Patient patient) {
    PatientEvent patientEvent = PatientEvent.newBuilder()
        .setPatientId(patient.getId().toString())
        .setName(patient.getName())
        .setEmail(patient.getEmail())
        .setEventType("PATIENT_CREATED")
        .build();

    try {
      kafkaTemplate.send("patient", patientEvent.toByteArray());
    } catch (Exception e) {
      log.error("[Kafka] Error sending PatientEvent to Kafka: {}", e.getMessage());
    }
  }
}
