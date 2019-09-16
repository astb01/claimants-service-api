package com.tdd.claimantsservice.controllers;

import com.google.common.collect.ImmutableList;
import com.tdd.claimantsservice.domain.Claimant;
import com.tdd.claimantsservice.services.ClaimantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.tdd.claimantsservice.utils.MappingConstants.CLAIMANTS_API_BASE_PATH;

@RestController
@RequestMapping(value = CLAIMANTS_API_BASE_PATH)
public class ClaimantsController {
    /* Inject the ClaimantsService so we can access our database */
    private ClaimantsService claimantsService;

    @Autowired
    public ClaimantsController(ClaimantsService claimantsService) {
        this.claimantsService = claimantsService;
    }

    @GetMapping(value = "/{claimantId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Claimant getClaimantById(final @PathVariable("claimantId") Long claimantId){
        return claimantsService.getClaimantById(claimantId);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseStatus(HttpStatus.CREATED) Claimant create(final @Valid @RequestBody Claimant claimantToCreate){
        return claimantsService.createClaimant(claimantToCreate);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteClaimant(final @PathVariable("id") Long id){
        claimantsService.deleteClaimant(id);
    }

    @GetMapping(value = "/refno/{refNo}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Claimant getByNINO(final @PathVariable("refNo") String refNo){
        return claimantsService.getClaimantByRefNo(refNo);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ImmutableList<Claimant> getAll(){
        return claimantsService.fetchAll();
    }

}
