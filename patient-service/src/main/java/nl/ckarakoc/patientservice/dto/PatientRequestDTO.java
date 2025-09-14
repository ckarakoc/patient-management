package nl.ckarakoc.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import nl.ckarakoc.patientservice.dto.validators.CreatePatientValidationGroup;

@Getter
@Setter
public class PatientRequestDTO {

  @NotBlank(message = "Name is required")
  @Size(max = 100, message = "Name cannot exceed 100 characters")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Email should be valid")
  private String email;

  @NotBlank(message = "Address is required")
  private String address;

  @NotBlank(message = "Date of birth is required")
  private String dateOfBirth;

  @NotBlank(message = "Registered date is required", groups = CreatePatientValidationGroup.class)
  private String registeredDate;
}
