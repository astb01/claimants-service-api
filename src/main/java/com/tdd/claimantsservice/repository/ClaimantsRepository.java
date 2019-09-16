package com.tdd.claimantsservice.repository;

import com.tdd.claimantsservice.domain.Claimant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClaimantsRepository extends JpaRepository<Claimant, Long> {

    Optional<Claimant> findOptionalByRefNo(String refNo);
}
