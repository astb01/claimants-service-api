package com.tdd.claimantsservice.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tdd.claimantsservice.dto.AppUserDTO;
import com.tdd.claimantsservice.repository.AppUserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.tdd.claimantsservice.security.SecurityConstants.SIGN_UP_URL;
import static com.tdd.claimantsservice.security.SecurityConstants.TOKEN_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class AuthenticationBase {

    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private AppUserRepository appUserRepository;

    protected AppUserDTO appUserDTO;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        appUserDTO = AppUserDTO.builder()
                .username("user")
                .password("password")
                .build();

        // Verify that we can register a new user:
        MockHttpServletResponse response = register(appUserDTO)
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @After
    public void tearDown() {
        appUserRepository.deleteAll();
        appUserRepository.flush();
    }

    protected String applicationUserDtoAsJson(AppUserDTO dto) throws Exception {
        return mapper.writeValueAsString(dto);
    }

    protected ResultActions register(final AppUserDTO appUserDTO) throws Exception {
        return mockMvc.perform(post(SIGN_UP_URL)
                        .contentType(APPLICATION_JSON)
                        .content(applicationUserDtoAsJson(appUserDTO)));
    }

    protected ResultActions login(final AppUserDTO appUserDTO) throws Exception {
        return mockMvc.perform(post(
                "/login"
        )
                .contentType(APPLICATION_JSON)
                .content(applicationUserDtoAsJson(appUserDTO)));
    }

    /**
     * Reads a token property from a Json response body.
     * @param mvcResult the object containing the response.
     * @return a Bearer token as a {@link String}.
     * @throws Exception if parsing JSON.
     */
    protected String getToken(MvcResult mvcResult) throws Exception {
        return JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.token");
    }

    protected String getTokenAsHeaderValue(String token){
        return new StringBuilder().append(TOKEN_PREFIX).append(token).toString();
    }
}
