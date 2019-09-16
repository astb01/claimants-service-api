package com.tdd.claimantsservice.integration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.repository.AppUserRepository;
import com.tdd.claimantsservice.repository.ClaimantsRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.*;
import static com.tdd.claimantsservice.security.SecurityConstants.HEADER;
import static com.tdd.claimantsservice.utils.MappingConstants.CLAIMANTS_API_BASE_PATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DrivingServiceIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private ClaimantsRepository claimantsRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Before
    public void setUp() {
        baseURI = "http://localhost";
        RestAssured.port = port;
        defaultParser = Parser.JSON;

        registerUser();
        loginAndExtractToken();
    }

    @After
    public void tearDown() {
        claimantsRepository.deleteAll();
        claimantsRepository.flush();

        appUserRepository.deleteAll();
        appUserRepository.flush();
    }

    @Test
    public void shouldValidateDrivingLicenceOnCreateClaimant() {
        Claimant claimant = ClaimantTestBuilder.aClaimant().drivingLicenceNo("CAMERON610096DWDXYA").build();

        successfulServiceCallStub(claimant);

        given()
                    .log().all()
                    .contentType(ContentType.JSON)
                    .body(claimant)
                    .header(HEADER, getAuthenticationHeaderValue())
                .when()
                    .post(CLAIMANTS_API_BASE_PATH)
                .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", Matchers.notNullValue());

        verify(postRequestedFor(urlPathEqualTo(dvlaServiceVerifyEndPoint))
                .withHeader("Content-Type", equalTo(APPLICATION_JSON_UTF8_VALUE)));
    }
}
