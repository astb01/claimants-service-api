package com.tdd.claimantsservice.config;

import com.tdd.claimantsservice.security.JWTAuthenticationFilter;
import com.tdd.claimantsservice.security.JWTAuthorisationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.tdd.claimantsservice.security.SecurityConstants.SIGN_UP_URL;

@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                // disable Cross Site Request forgery as we are only a service
                .csrf().disable()
                // we want to authorize all requests
                .authorizeRequests()
                // provide this url as the authorization end point
                // by default Spring Security expects the URL to be /login
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                // any subsequent requests should be authenticated
                .anyRequest().authenticated()
                .and()
                // list of filters to create and very Bearer Token
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorisationFilter(authenticationManager()))
                // set session management for security and token management
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // provide Spring the mechanisms to use for authentication and storing passwords:
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }
}
