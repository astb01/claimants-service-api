package com.tdd.claimantsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDTO {
    @NotNull(message = "Username cannot be empty")
    private String username;
    @NotNull(message = "Password cannot be empty")
    private String password;
}
