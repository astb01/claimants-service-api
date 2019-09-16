package com.tdd.claimantsservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DrivingServiceRequestDTO {
    private String drivingLicenceNo;
    private String firstName;
    private String lastName;
    private String dob;
}
