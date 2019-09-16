package com.tdd.claimantsservice.repository;

import com.tdd.claimantsservice.domain.AppUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser = new AppUser("John", "1234");

    @Before
    public void setUp() {
        appUserRepository.deleteAll();
        appUserRepository.flush();
    }

    @Test
    public void shouldSaveAUser() {
        AppUser result = appUserRepository.saveAndFlush(appUser);

        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("username", appUser.getUsername());
    }

    @Test
    public void shouldFindUserByUsername() {
        entityManager.persist(appUser);

        AppUser result = appUserRepository.findByUsername(appUser.getUsername());
        assertThat(result).isNotNull();
        assertThat(result).hasFieldOrPropertyWithValue("username", appUser.getUsername());
    }
}