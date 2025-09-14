package nl.ckarakoc.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {

  private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

  public BillingServiceGrpcClient(
      @Value("${billing.service.address:localhost}") String serverAddress,
      @Value("${billing.service.grpc.port:9001}") int serverPort
  ) {
    log.info("[Grpc] Connecting to billing service at {}:{}", serverAddress, serverPort);
    ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
        .usePlaintext()
        .build();
    blockingStub = BillingServiceGrpc.newBlockingStub(channel);
  }

  public BillingResponse createBillingAccount(String patientId, String name, String email) {
    BillingRequest billingRequest = BillingRequest.newBuilder()
        .setPatientId(patientId)
        .setName(name)
        .setEmail(email)
        .build();
    BillingResponse response = blockingStub.createBillingAccount(billingRequest);
    log.info("[Grpc] Billing account created: {}", response.toString());

    return response;
  }
}
