package com.tdd.claimantsservice.authentication;

import com.tdd.claimantsservice.security.SecurityConstants;
import org.junit.Test;
import org.springframework.http.MediaType;

import static com.tdd.claimantsservice.utils.MappingConstants.CLAIMANTS_API_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorizationTest extends AuthenticationBase {

    @Test
    public void givenValidUserWhenLoggedInCanAccessEndpoint() throws Exception {

        String token = getToken(login(appUserDTO).andReturn());

        mockMvc.perform(get(CLAIMANTS_API_BASE_PATH)
                    .header(SecurityConstants.HEADER, getTokenAsHeaderValue(token))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoLoginCredentialsWhenAccessToEndpointThenForbidden() throws Exception {
        mockMvc.perform(get(CLAIMANTS_API_BASE_PATH))
                .andExpect(status().isForbidden());
    }
}
