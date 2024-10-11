package com.lunionlab.turbo_restaurant.services;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthEntryPointService implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest resquest, HttpServletResponse response, AuthenticationException auth)
            throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized");
    }
}
