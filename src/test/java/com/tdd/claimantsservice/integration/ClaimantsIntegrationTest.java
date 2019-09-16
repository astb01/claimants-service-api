package com.tdd.claimantsservice.integration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Response;
import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.repository.AppUserRepository;
import com.tdd.claimantsservice.repository.ClaimantsRepository;
import com.tdd.claimantsservice.security.SecurityConstants;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static com.tdd.claimantsservice.builders.ClaimantTestBuilder.aClaimant;
import static com.tdd.claimantsservice.utils.MappingConstants.CLAIMANTS_API_BASE_PATH;
import static org.springframework.http.HttpStatus.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClaimantsIntegrationTest extends BaseIntegrationTest {
    private final static Claimant A_CLAIMANT = ClaimantTestBuilder.aClaimant().build();

    /*
        Use @LocalServerPort as we have defined RANDOM_PORT above so we
        know which port Spring Boot Test will run our application on
    */
    @LocalServerPort
    private int port;

    /* Inject our repository so we can persists and retrieve Claimants */
    @Autowired
    private ClaimantsRepository claimantsRepository;

    /* Inject application user repository so we can clear down user access. */
    @Autowired
    private AppUserRepository appUserRepository;

    private Header header;

    @Before
    public void setUp() {
        // Set up RestAssured to allow us to make REST calls
        baseURI = "http://localhost";
        RestAssured.port = port;
        defaultParser = Parser.JSON;

        registerUser();
        loginAndExtractToken();

        header = new Header(SecurityConstants.HEADER, getAuthenticationHeaderValue());
    }

    @After
    public void tearDown() {
        // Clear our database down after we run each test
        claimantsRepository.deleteAll();
        claimantsRepository.flush();

        appUserRepository.deleteAll();
        appUserRepository.flush();
    }

    @Test
    public void shouldReturnAListOfClaimants() {
        Claimant createdClaimant = createClaimant(A_CLAIMANT)
                .then().extract().response().as(Claimant.class);

        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .get(CLAIMANTS_API_BASE_PATH)
                .then()
                .log().all()
                .statusCode(OK.value())
                .body("$", Matchers.hasSize(1))
                .body("id", Matchers.hasItem(getAsInt(createdClaimant.getId())));
    }

    @Test
    public void givenClaimantsId_WhenGet_ThenClaimantReturned() {
        Claimant createdClaimant = createClaimant(A_CLAIMANT)
                .then().extract().response().as(Claimant.class);

        Long claimantId = createdClaimant.getId();

        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .get(String.format("%s/{claimantId}", CLAIMANTS_API_BASE_PATH), claimantId)
                .then()
                .log().all()
                .body("id", Matchers.is(getAsInt(claimantId)));
    }

    @Test
    public void givenClaimantId_WhenGet_Then404Returned() {
        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .get(String.format("%s/{claimantId}", CLAIMANTS_API_BASE_PATH), Long.MAX_VALUE)
                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void shouldCreateClaimant() {
        given()
                .log().all()
                .contentType(JSON)
                .body(A_CLAIMANT)
                .header(header)
                .when()
                .post(CLAIMANTS_API_BASE_PATH)
                .then()
                .statusCode(CREATED.value())
                .body("id", Matchers.notNullValue())
                .body("firstName", Matchers.equalTo("John"))
                .body("lastName", Matchers.equalTo("Doe"))
                .body("dob", Matchers.equalTo("01-03-1976"))
                .body("street", Matchers.equalTo("1st Street"))
                .body("city", Matchers.equalTo("Newcastle"))
                .body("postCode", Matchers.equalTo("NE1 3RT"))
                .body("refNo", Matchers.equalTo("AB123456C"));
    }

    @Test
    public void shouldDeleteAClaimantById() {
        Claimant createdClaimant = createClaimant(A_CLAIMANT)
                .then().extract().response().as(Claimant.class);

        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .delete(String.format("%s/{id}", CLAIMANTS_API_BASE_PATH), createdClaimant.getId())
                .then()
                .log().all()
                .statusCode(OK.value());

        getClaimant(createdClaimant.getId())
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void shouldReturn404WhenDeletingNonExistingClaimant() {
        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .delete(String.format("%s/{id}", CLAIMANTS_API_BASE_PATH), Long.MAX_VALUE)
                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    public void shouldFindClaimantByNINO() {
        Claimant createdClaimant = createClaimant(A_CLAIMANT)
                .then().extract().response().as(Claimant.class);

        String nino = createdClaimant.getRefNo();

        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .get(String.format("%s/refno/{refNo}", CLAIMANTS_API_BASE_PATH), nino)
                .then()
                .log().all()
                .body("id", Matchers.is(getAsInt(createdClaimant.getId())));
    }

    @Test
    public void shouldReturn404WhenNINONotFound() {
        String nino = "SD234567G";

        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .get(String.format("%s/refno/{refNo}", CLAIMANTS_API_BASE_PATH), nino)
                .then()
                .log().all()
                .statusCode(NOT_FOUND.value());
    }

    /* ---------------------------------------- VALIDATION ---------------------------------------- */

    @Test
    public void shouldReturn400WhenNinoNotValid() {
        String invalidNino = "ABC1231";

        given()
                .log().all()
                .contentType(JSON)
                .header(header)
                .when()
                .get(String.format("%s/refno/{refNo}", CLAIMANTS_API_BASE_PATH), invalidNino)
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void shouldReturn400WhenNinoIsMissing() {
        Claimant claimant = aClaimant().refNo(null).build();

        createClaimant(claimant)
                .then()
                .statusCode(400);
    }

    @Test
    public void shouldValidateDrivingLicenceWhenClaimantIsCreated() {
        Claimant claimant = aClaimant().drivingLicenceNo("WQE").build();

        createClaimant(claimant)
                .then()
                .statusCode(400);
    }

    /* ---------------------------------------- PRIVATE METHODS ---------------------------------------- */

    private Response getClaimant(Long id) {
        return given()
                .log().all()
                .header(header)
                .contentType(JSON)
                .when()
                .get(String.format("%s/{id}", CLAIMANTS_API_BASE_PATH), id);
    }

    private Response createClaimant(Claimant claimant) {
        return given()
                .log().all()
                .header(header)
                .contentType(JSON)
                .body(claimant)
                .when()
                .post(CLAIMANTS_API_BASE_PATH);
    }


    private Integer getAsInt(Long val) {
        return Math.toIntExact(val);
    }
}
