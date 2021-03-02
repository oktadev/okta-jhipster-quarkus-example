package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TestUtil;
import io.quarkus.oidc.IdToken;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class LogoutResourceTest {

    @InjectMock
    @IdToken
    JsonWebToken idToken;

    @BeforeEach
    public void setup() {
        // idToken is available only when Quarkus OIDC application type is "web-app".
        // This is why we need to mock it for test purpose.
        // https://stackoverflow.com/questions/62692829/how-to-use-idtoken-in-quarkus-apps-when-application-type-service
        Mockito.when(idToken.getRawToken()).thenReturn("1234");
    }

    @Test
    public void getLogoutInformation() {
        String expectedLogoutUrl = "some-dummy-logoutUrl";

        var jsonPath = given()
            .auth()
            .preemptive()
            .oauth2(TestUtil.getAdminToken())
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .when()
            .post("/api/logout")
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .jsonPath();

        assertThat(jsonPath.getString("logoutUrl")).isEqualTo(expectedLogoutUrl);
        assertThat(jsonPath.getString("idToken")).isEqualTo("1234");
    }
}
