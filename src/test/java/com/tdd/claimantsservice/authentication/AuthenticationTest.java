package com.tdd.claimantsservice.authentication;

import com.tdd.claimantsservice.dto.AppUserDTO;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationTest extends AuthenticationBase {

    @Test
    public void shouldLoginUser() throws Exception {
        ResultActions loginResult = login(appUserDTO);

        loginResult
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void invalidLoginAttemptResultsInAuthorized() throws Exception {
        AppUserDTO invalidUser = AppUserDTO.builder().
                username("invaliduser")
                .password("asdad")
                .build();

        login(invalidUser)
                .andExpect(status().isUnauthorized());
    }
}
