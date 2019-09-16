package com.tdd.claimantsservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.tdd.claimantsservice.authentication.AuthenticationBase;
import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.exceptions.ClaimantNotFoundException;
import com.tdd.claimantsservice.services.ClaimantsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.tdd.claimantsservice.security.SecurityConstants.HEADER;
import static com.tdd.claimantsservice.utils.MappingConstants.CLAIMANTS_API_BASE_PATH;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClaimantsControllerTest extends AuthenticationBase {
    @MockBean
    private ClaimantsService claimantsService;

    @Autowired
    private ObjectMapper mapper;

    private String token;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        token = getToken(login(appUserDTO).andReturn());
    }

    @Test
    public void givenGetClaimant_WhenRequest_ThenClaimantReturned() throws Exception {
        Claimant claimant = ClaimantTestBuilder.aClaimant().id(1L).build();

        given(claimantsService.getClaimantById(BDDMockito.anyLong()))
                .willReturn(claimant);

        mockMvc.perform(get(CLAIMANTS_API_BASE_PATH + "/" + claimant.getId())
                    .header(HEADER, getTokenAsHeaderValue(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(claimant.getId()));
    }

    @Test
    public void shouldCreateClaimant() throws Exception {
        Claimant claimant = ClaimantTestBuilder.aClaimant().build();

        given(claimantsService.createClaimant(Matchers.any(Claimant.class)))
                .willReturn(claimant);

        String request = mapper.writeValueAsString(claimant);

        mockMvc.perform(post(CLAIMANTS_API_BASE_PATH)
                    .content(request)
                    .contentType(APPLICATION_JSON_UTF8)
                    .header(HEADER, getTokenAsHeaderValue(token)))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldDeleteClaimant() throws Exception {
        Claimant claimant = ClaimantTestBuilder.aPersistedClaimant().build();

        given(claimantsService.deleteClaimant(claimant.getId()))
                .willReturn(true);

        mockMvc.perform(delete(CLAIMANTS_API_BASE_PATH + "/" + claimant.getId())
                    .header(HEADER, getTokenAsHeaderValue(token))
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnWith404WhenDeletingNonExistingClaimant() throws Exception {
        given(claimantsService.deleteClaimant(Long.MAX_VALUE))
                .willThrow(ClaimantNotFoundException.class);

        mockMvc.perform(delete(CLAIMANTS_API_BASE_PATH + "/" + Long.MAX_VALUE)
                    .header(HEADER, getTokenAsHeaderValue(token))
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldRetrieveClaimantByRefNo() throws Exception {
        Claimant persistedClaimant = ClaimantTestBuilder.aPersistedClaimant().build();

        given(claimantsService.getClaimantByRefNo(persistedClaimant.getRefNo()))
                .willReturn(persistedClaimant);

        mockMvc.perform(get(CLAIMANTS_API_BASE_PATH + "/refno/" + persistedClaimant.getRefNo())
                    .header(HEADER, getTokenAsHeaderValue(token))
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("refNo").value(persistedClaimant.getRefNo()));
    }

    @Test
    public void shouldReturnAListOfClaimants() throws Exception {
        Claimant claimant = ClaimantTestBuilder.aPersistedClaimant().build();

        given(claimantsService.fetchAll()).willReturn(ImmutableList.of(claimant));

        mockMvc.perform(get(CLAIMANTS_API_BASE_PATH)
                .header(HEADER, getTokenAsHeaderValue(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(1)));
    }
}