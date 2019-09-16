package com.tdd.claimantsservice.services;

import com.google.common.collect.Lists;
import com.tdd.claimantsservice.domain.AppUser;
import com.tdd.claimantsservice.dto.AppUserDTO;
import com.tdd.claimantsservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    private AppUserRepository appUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthService(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public AppUser createUser(AppUserDTO dto){
        AppUser appUser = new AppUser(dto.getUsername(), bCryptPasswordEncoder.encode(dto.getPassword()));

        return appUserRepository.saveAndFlush(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);

        if (appUser == null){
            throw new UsernameNotFoundException(username);
        }

        return new User(appUser.getUsername(), appUser.getPassword(), Lists.newArrayList());
    }
}
