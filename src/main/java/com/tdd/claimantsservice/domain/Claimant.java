package com.tdd.claimantsservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdd.claimantsservice.validators.ValidDrivingLicence;
import com.tdd.claimantsservice.validators.ValidRefNo;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "claimants")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Claimant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dob;

    private String street;
    private String city;
    private String postCode;

    @NotNull(message = "Reference Number is required")
    @ValidRefNo
    private String refNo;

    @ValidDrivingLicence
    private String drivingLicenceNo;
}
