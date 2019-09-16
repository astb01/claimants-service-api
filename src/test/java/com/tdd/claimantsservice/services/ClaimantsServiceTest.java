package com.tdd.claimantsservice.services;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.exceptions.ClaimantNotFoundException;
import com.tdd.claimantsservice.repository.ClaimantsRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClaimantsServiceTest {

    @Mock
    private ClaimantsRepository claimantsRepository;

    @Mock
    private DrivingService drivingService;

    private ClaimantsService claimantsService;

    @Before
    public void setUp() {
        claimantsService = new ClaimantsService(claimantsRepository, drivingService);
    }

    @Test
    public void shouldReturnClaimantWhenSearchedById() {
        Claimant persistedClaimant = ClaimantTestBuilder.aClaimant()
                .id(1L)
                .build();

        given(claimantsRepository.findOne(persistedClaimant.getId()))
                .willReturn(persistedClaimant);

        Claimant result = claimantsService.getClaimantById(persistedClaimant.getId());
        Assertions.assertThat(result.getId()).isEqualTo(persistedClaimant.getId());
    }

    @Test(expected = ClaimantNotFoundException.class)
    public void shouldReturnExceptionWhenClaimantByIdNotFound() {
        given(claimantsRepository.findOne(MAX_VALUE)).willReturn(null);

        claimantsService.getClaimantById(MAX_VALUE);
    }

    @Test
    public void shouldCreateClaimant() {
        Claimant claimant = ClaimantTestBuilder.aClaimant().build();

        given(claimantsRepository.saveAndFlush(claimant))
                .willReturn(claimant);

        Claimant result = claimantsService.createClaimant(claimant);
        Assertions.assertThat(result).isEqualTo(claimant);
    }

    @Test
    public void shouldDeleteClaimant() {
        Claimant claimant = ClaimantTestBuilder.aClaimant().build();

        given(claimantsRepository.findOne(claimant.getId())).willReturn(claimant);

        doNothing().when(claimantsRepository).delete(claimant);

        Boolean result = claimantsService.deleteClaimant(claimant.getId());

        Assertions.assertThat(result).isTrue();
    }

    @Test(expected = ClaimantNotFoundException.class)
    public void shouldReturnClaimantNotFoundExceptionWhenDeletingNonExistingClaimant() {
        given(claimantsRepository.findOne(anyLong()))
                .willReturn(null);

        claimantsService.deleteClaimant(MAX_VALUE);
    }

    @Test
    public void shouldRetrieveClaimantByRefNo() {
        Claimant claimant = ClaimantTestBuilder.aPersistedClaimant()
                .refNo("HT234567T")
                .build();

        given(claimantsRepository.findOptionalByRefNo(claimant.getRefNo()))
                .willReturn(Optional.ofNullable(claimant));

        Claimant result = claimantsService.getClaimantByRefNo(claimant.getRefNo());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getRefNo()).isEqualTo(claimant.getRefNo());
    }

    @Test(expected = ClaimantNotFoundException.class)
    public void shouldReturnClaimantNotFoundExceptionWhenRefNoNotFound() {
        given(claimantsRepository.findOptionalByRefNo(anyString()))
                .willReturn(Optional.empty());

        claimantsService.getClaimantByRefNo("AS123456T");
    }

    @Test
    public void shouldReturnAllClaimants() {
        Claimant claimant = ClaimantTestBuilder.aPersistedClaimant().build();

        given(claimantsRepository.findAll()).willReturn(Lists.newArrayList(claimant));

        ImmutableList<Claimant> results = claimantsService.fetchAll();
        Assertions.assertThat(results).isNotEmpty();
        Assertions.assertThat(results).extracting("id").contains(claimant.getId());
    }
}