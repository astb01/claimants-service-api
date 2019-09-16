package com.tdd.claimantsservice.services;

import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.dto.DrivingServiceResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DrivingServiceTest {

    @Mock
    private RestTemplate mockedRestTemplate;

    private DrivingService drivingService;

    @Before
    public void setUp() {
        this.drivingService = new DrivingService(mockedRestTemplate);
    }

    @Test
    public void shouldValidateDrivingLicence() {
        Claimant claimant = ClaimantTestBuilder.aClaimant().drivingLicenceNo("CAMERON610096DWDXYA").build();

        DrivingServiceResponseDTO responseDto = DrivingServiceResponseDTO.builder()
                .status(HttpStatus.OK.value())
                .message("Driving Licence is valid")
                .build();

        ResponseEntity<DrivingServiceResponseDTO> entity = ResponseEntity.ok(responseDto);

        given(
                mockedRestTemplate.postForEntity(anyString(), any(Claimant.class),
                        Mockito.eq(DrivingServiceResponseDTO.class)))
                .willReturn(entity);

        DrivingServiceResponseDTO result =  drivingService.validate(claimant, "someUrl");
        Assertions.assertThat(result.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}