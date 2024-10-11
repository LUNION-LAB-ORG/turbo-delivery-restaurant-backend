package com.lunionlab.turbo_restaurant.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthFilterService implements Filter {

    @Autowired
    UsersDetailService usersDetailService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException, UsernameNotFoundException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String bearerToken = httpServletRequest.getHeader("Authorization");
        String tokenName = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(tokenName)) {
            String token = bearerToken.substring(tokenName.length());
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    UserDetails userDetail = usersDetailService.loadUserByUsername(token);
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetail, userDetail.getUsername(), userDetail.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } catch (UsernameNotFoundException e) {
                    System.out.println(e);
                }
            }
        }
        chain.doFilter(httpServletRequest, response);
    }
}
