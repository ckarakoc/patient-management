package nl.ckarakoc.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

  @KafkaListener(topics = "patient", groupId = "analytics-service")
  public void consumeEvent(byte[] event) {
    try {
      PatientEvent patientEvent = PatientEvent.parseFrom(event);
      // ... perform business logic on the event

      log.info("[Kafka] PatientEvent received: {}", patientEvent.toString());
    } catch (InvalidProtocolBufferException e) {
      log.error("[Kafka] Error parsing PatientEvent from Kafka: {}", e.getMessage());
    }
  }

}
