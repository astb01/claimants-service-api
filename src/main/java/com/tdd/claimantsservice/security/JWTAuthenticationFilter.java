package com.tdd.claimantsservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tdd.claimantsservice.domain.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import static com.tdd.claimantsservice.security.SecurityConstants.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            AppUser appUser = mapper.readValue(request.getInputStream(), AppUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            appUser.getUsername(),
                            appUser.getPassword(),
                            Lists.newArrayList()
                    )
            );
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                            .setSubject(((User) authResult.getPrincipal()).getUsername())
                            .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_TIME))
                            .signWith(SignatureAlgorithm.HS256, SECRET)
                            .compact();

        response.addHeader(HEADER, TOKEN_PREFIX + token);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter writer = response.getWriter();
        writer.print("{\"token\": \"" + token + "\"}");
        writer.flush();
    }
}
