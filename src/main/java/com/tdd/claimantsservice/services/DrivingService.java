package com.tdd.claimantsservice.services;

import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.dto.DrivingServiceRequestDTO;
import com.tdd.claimantsservice.dto.DrivingServiceResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;

@Service
public class DrivingService {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    @Value("${dvla-service.host:localhost}")
    String dvlaServiceHost;

    @Value("${dvla-service.port:9000}")
    int dvlaServicePort;

    @Value("${dvla-service.uri:/dvla-verify}")
    String dvlaServiceUri;

    private RestTemplate restTemplate;

    @Autowired
    public DrivingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DrivingServiceResponseDTO validate(Claimant claimant, String url) {
        DrivingServiceRequestDTO requestDTO = DrivingServiceRequestDTO.builder()
                .drivingLicenceNo(claimant.getDrivingLicenceNo())
                .dob(DATE_FORMATTER.format(claimant.getDob()))
                .firstName(claimant.getFirstName())
                .lastName(claimant.getLastName())
                .build();

        ResponseEntity<DrivingServiceResponseDTO> responseEntity =
                restTemplate.postForEntity(url, requestDTO, DrivingServiceResponseDTO.class);

        return responseEntity.getBody();
    }

    public DrivingServiceResponseDTO validate(Claimant claimant) {
        StringBuilder url = new StringBuilder(dvlaServiceHost);
        url.append(":").append(dvlaServicePort).append(dvlaServiceUri);

        return validate(claimant, url.toString());
    }
}
