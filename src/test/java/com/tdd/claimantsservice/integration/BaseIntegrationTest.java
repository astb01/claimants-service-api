package com.tdd.claimantsservice.integration;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.tdd.claimantsservice.authentication.AuthToken;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.dto.AppUserDTO;
import com.tdd.claimantsservice.security.SecurityConstants;
import com.tdd.claimantsservice.services.DrivingService;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.tdd.claimantsservice.utils.MappingConstants.AUTH_BASE_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public abstract class BaseIntegrationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseIntegrationTest.class.getName());
    /**
     * Directory for storing our Wiremock stub files.
     */
    private static final String RESPONSES_DIR = File.separator + "drivingService" + File.separator +
            "responses";

    @ClassRule
    public static WireMockRule wireMockRule = new WireMockRule(9000);

    protected static AuthToken authToken;

    protected AppUserDTO appUserDTO = AppUserDTO.builder()
            .username("admin")
            .password("admin").build();

    @Value("${dvla-service.uri}")
    protected String dvlaServiceVerifyEndPoint;

    /**
     * Stub to mock out successful responses from the Driving service end point.
     *
     * @param claimant {@link Claimant} to use to provide dynamic values for the request.
     */
    public static void successfulServiceCallStub(Claimant claimant) {
        String jsonRequest = createRequest(claimant);

        stubFor(post(urlEqualTo("/dvla-verify"))
                .withHeader("Content-Type", equalTo("application/json;charset=UTF-8"))
                .withRequestBody(equalToJson(jsonRequest, true, true))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBodyFile(RESPONSES_DIR + File.separator + "success.json")));
    }

    private static String createRequest(Claimant claimant) {
        StringBuilder json = new StringBuilder();

        json.append("{\"drivingLicenceNo\":").append(
                StringUtils.isNotEmpty(claimant.getDrivingLicenceNo()) ? "\"" +
                        claimant.getDrivingLicenceNo() + "\"" : null);
        json.append(",\"firstName\":\"").append(claimant.getFirstName());
        json.append("\",\"lastName\":\"").append(claimant.getLastName());
        json.append("\",\"dob\":\"").append(claimant.getDob() != null ?
                DrivingService.DATE_FORMATTER.format(claimant.getDob()) + "\"" : null);
        json.append("}");

        LOGGER.info("Driving Service JSON Request:\n{}", json.toString());

        return json.toString();
    }

    protected String getAuthenticationHeaderValue() {
        return SecurityConstants.TOKEN_PREFIX + authToken.getToken();
    }

    protected void registerUser() {
        given()
                .log().all()
                .contentType(JSON)
                .body(appUserDTO)
                .when()
                .post(AUTH_BASE_PATH + "/register")
                .then()
                .log().all()
                .statusCode(CREATED.value());
    }

    protected void loginAndExtractToken() {
        authToken = given()
                .log().all()
                .contentType(JSON)
                .body(appUserDTO)
                .when()
                .post(SecurityConstants.LOGIN_URL)
                .then()
                .log().all()
                .statusCode(OK.value())
                .body("token", Matchers.notNullValue())
                .extract().response().as(AuthToken.class);
    }
}
