package com.tdd.claimantsservice.repository;

import com.tdd.claimantsservice.builders.ClaimantTestBuilder;
import com.tdd.claimantsservice.domain.Claimant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClaimantsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClaimantsRepository claimantsRepository;

    private Claimant claimant;

    @Before
    public void setUp() {
        claimant = ClaimantTestBuilder.aClaimant().build();
    }

    @Test
    public void shouldRetrieveAClaimantById() {
        // act
        entityManager.persist(claimant);

        // assert
        Claimant result = claimantsRepository.findOne(claimant.getId());
        assertThat(result).isEqualTo(claimant);
    }

    @Test
    public void shouldPersistClaimant() {
        Claimant result = claimantsRepository.saveAndFlush(claimant);

        assertThat(result)
                .hasFieldOrPropertyWithValue("firstName", claimant.getFirstName());
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void shouldDeleteAClaimant() {
        claimant = entityManager.persist(this.claimant);

        claimantsRepository.delete(claimant.getId());

        Claimant result = claimantsRepository.findOne(claimant.getId());
        assertThat(result).isNull();
    }

    @Test
    public void shouldRetrieveClaimantByRefNo() {
        claimant = entityManager.persist(this.claimant);

        Optional<Claimant> result = claimantsRepository.findOptionalByRefNo(this.claimant.getRefNo());

        assertThat(result.get()).isNotNull();
        assertThat(result.get().getRefNo()).isEqualTo(this.claimant.getRefNo());
    }
}