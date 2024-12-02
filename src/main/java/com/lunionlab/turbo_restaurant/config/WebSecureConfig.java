package com.lunionlab.turbo_restaurant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lunionlab.turbo_restaurant.services.AuthEntryPointService;
import com.lunionlab.turbo_restaurant.services.AuthFilterService;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class WebSecureConfig {
    @Autowired
    AuthFilterService authFilterService;
    @Autowired
    AuthEntryPointService authEntryPointService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.cors(cor -> {

        }).csrf(csrfProtect -> {
            csrfProtect.disable();
        });

        security.formLogin(form -> {
            form.disable();
        });

        security.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/test/**", "/error", "/api/V1/turbo/resto/user/login",
                    "/api/V1/turbo/resto/user/register/stepfirst", "/api/V1/turbo/resto/user/register/stepsecond",
                    "/api/V1/turbo/resto/user/register/finalstep", "/api/V1/turbo/resto/user/change/password",
                    "/api/V1/turbo/resto/user/forget/password", "/api/V1/turbo/resto/user/new/password",
                    "/api/V1/turbo/restaurant/not/validated/**", "/api/V1/turbo/restaurant/validated/authservice/**",
                    "/api/V1/turbo/restaurant/validated/opsmanager/**",
                    "/api/V1/turbo/restaurant/approved/authservice/**",
                    "/api/V1/turbo/restaurant/approved/opsmanager/**",
                    "/api/V1/turbo/restaurant/detail/erp/**", "/api/serve/file/**", "/api/V1/turbo/resto/plat/filter",
                    "/api/V1/turbo/restaurant/search", "/api/V1/turbo/resto/plat/search",
                    "/api/V1/turbo/resto/plat/detail/**", "/api/V1/turbo/resto/plat/all/price",
                    "/api/V1/turbo/restaurant/check/opening/**", "/api/V1/turbo/restaurant/save/order",
                    "/api/V1/turbo/resto/plat/get/all")
                    .permitAll().anyRequest().authenticated();
        });

        security.exceptionHandling(exception -> {
            exception.authenticationEntryPoint(authEntryPointService);
        });

        security.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        security.addFilterBefore(authFilterService, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }
}
