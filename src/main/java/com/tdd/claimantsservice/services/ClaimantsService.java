package com.tdd.claimantsservice.services;

import com.google.common.collect.ImmutableList;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.dto.DrivingServiceResponseDTO;
import com.tdd.claimantsservice.exceptions.ClaimantNotFoundException;
import com.tdd.claimantsservice.exceptions.InvalidDrivingLicenceException;
import com.tdd.claimantsservice.exceptions.InvalidRefNoException;
import com.tdd.claimantsservice.repository.ClaimantsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tdd.claimantsservice.validators.RefNoValidator.REFNO_REGEX;

@Service
public class ClaimantsService {
    /* Inject our repository so we can make access our data layer */
    private ClaimantsRepository claimantsRepository;

    private DrivingService drivingService;

    @Autowired
    public ClaimantsService(ClaimantsRepository claimantsRepository, DrivingService drivingService) {
        this.claimantsRepository = claimantsRepository;
        this.drivingService = drivingService;
    }

    public Claimant getClaimantById(long claimantId) {
        Claimant claimant = claimantsRepository.findOne(claimantId);

        if (null == claimant){
            throw new ClaimantNotFoundException("Claimant with ID " +
                    claimantId + " not found");
        }

        return claimant;
    }

    public Claimant createClaimant(Claimant claimant) {
        if (StringUtils.isNotEmpty(claimant.getDrivingLicenceNo())){
            DrivingServiceResponseDTO drivingServiceResponseDTO = drivingService.validate(claimant);

            if (null == drivingServiceResponseDTO || (drivingServiceResponseDTO.getStatus() != HttpStatus.OK.value())){
                throw new InvalidDrivingLicenceException(drivingServiceResponseDTO.getMessage());
            }
        }

        return claimantsRepository.saveAndFlush(claimant);
    }

    public Boolean deleteClaimant(Long id) {
        Claimant claimant = claimantsRepository.findOne(id);

        if (null == claimant){
            throw new ClaimantNotFoundException(
                    String.format("Claimant with ID [%d] does not exist!", id)
            );
        }

        claimantsRepository.delete(id);

        return true;
    }

    public Claimant getClaimantByRefNo(String refNo) {
        if (!refNo.matches(REFNO_REGEX)){
            throw new InvalidRefNoException("Reference Number format is invalid");
        }

        return claimantsRepository.findOptionalByRefNo(refNo)
                .orElseThrow(() -> new ClaimantNotFoundException("Claimant for Reference Number not found"));
    }

    public ImmutableList<Claimant> fetchAll() {
        List<Claimant> claimants = claimantsRepository.findAll();
        return ImmutableList.copyOf(claimants);
    }
}
