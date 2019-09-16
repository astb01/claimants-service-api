package com.tdd.claimantsservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonDeserialize(builder = DrivingServiceResponseDTO.DrivingServiceResponseDTOBuilder.class)
public class DrivingServiceResponseDTO {
    private String message;
    private int status;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class DrivingServiceResponseDTOBuilder {
    }
}
