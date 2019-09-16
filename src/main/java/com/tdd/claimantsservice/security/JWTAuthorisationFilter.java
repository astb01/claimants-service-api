package com.tdd.claimantsservice.security;

import com.google.common.collect.Lists;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorisationFilter extends BasicAuthenticationFilter {

    public JWTAuthorisationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER);

        if (StringUtils.isEmpty(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER);

        if (StringUtils.isNotEmpty(token)){
            String user = Jwts.parser()
                            .setSigningKey(SecurityConstants.SECRET)
                            .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                            .getBody()
                            .getSubject();

            if (StringUtils.isNotEmpty(user)){
                return new UsernamePasswordAuthenticationToken(user, null, Lists.newArrayList());
            }

            return null;
        }

        return null;
    }
}
