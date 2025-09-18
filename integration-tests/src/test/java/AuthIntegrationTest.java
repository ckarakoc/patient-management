import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AuthIntegrationTest {

  @BeforeAll
  static void setup() {
    RestAssured.baseURI = "http://localhost:4004"; // Api Gateway port
  }

  @Test
  public void shouldReturnOKWithValidToken() {
    String loginPayload = """
          {
            "email": "testuser@test.com",
            "password": "password123"
          }
        """;
    Response response = RestAssured.given()
        .contentType("application/json")
        .body(loginPayload)
        .when()
        .post("/auth/login")
        .then()
        .statusCode(200)
        .body("token", notNullValue())
        .extract().response();

    System.out.println("Generated token: " + response.jsonPath().getString("token"));
  }

  @Test
  public void shouldReturnUnauthorizedWithInvalidLogin() {
    String loginPayload = """
          {
            "email": "invalid_user@test.com",
            "password": "wrongpassword"
          }
        """;
    RestAssured.given()
        .contentType("application/json")
        .body(loginPayload)
        .when()
        .post("/auth/login")
        .then()
        .statusCode(401);
  }
}
