package com.tdd.claimantsservice.controllers;

import com.tdd.claimantsservice.dto.AppUserDTO;
import com.tdd.claimantsservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.tdd.claimantsservice.utils.MappingConstants.AUTH_BASE_PATH;

@RestController
@RequestMapping(value = AUTH_BASE_PATH)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register")
    public @ResponseStatus(HttpStatus.CREATED) void register(final @Valid @RequestBody AppUserDTO dto){
        authService.createUser(dto);
    }
}
