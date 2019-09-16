package com.tdd.claimantsservice.builders;

import com.tdd.claimantsservice.domain.Claimant;

import java.util.Calendar;

/**
 * Helper class to allow us to build pre-defined Claimant POJOs for testing support
 * purposes.
 */
public class ClaimantTestBuilder {

    public static Claimant.ClaimantBuilder aPersistedClaimant(){
        Claimant.ClaimantBuilder builder = aClaimant();

        return builder.id(1L);
    }

    public static Claimant.ClaimantBuilder aClaimant(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1976);
        cal.set(Calendar.MONTH, 2);
        cal.set(Calendar.DATE, 1);

        return Claimant.builder()
                .firstName("John")
                .lastName("Doe")
                .street("1st Street")
                .city("Newcastle")
                .postCode("NE1 3RT")
                .refNo("AB123456C")
                .dob(cal.getTime());
    }
}
