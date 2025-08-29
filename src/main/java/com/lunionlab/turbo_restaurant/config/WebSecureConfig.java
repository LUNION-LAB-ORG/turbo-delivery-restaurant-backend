package com.lunionlab.turbo_restaurant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lunionlab.turbo_restaurant.services.AuthEntryPointService;
import com.lunionlab.turbo_restaurant.services.AuthFilterService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecureConfig {
    @Autowired
    AuthFilterService authFilterService;
    @Autowired
    AuthEntryPointService authEntryPointService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.cors(cor -> {

        }).csrf(AbstractHttpConfigurer::disable);

        security.formLogin(AbstractHttpConfigurer::disable);

        security.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/test/**", "/error", "/api/V1/turbo/resto/user/login",
                    "/api/V1/turbo/resto/user/register/stepfirst", "/api/V1/turbo/resto/user/register/stepsecond",
                    "/api/V1/turbo/resto/user/register/finalstep", "/api/V1/turbo/resto/user/change/password",
                    "/api/V1/turbo/resto/user/forget/password", "/api/V1/turbo/resto/user/new/password",
                    "/api/V1/turbo/restaurant/not/validated/**", "/api/V1/turbo/restaurant/validated/authservice/**",
                    "/api/V1/turbo/restaurant/validated/opsmanager/**",
                    "/api/V1/turbo/restaurant/approved/authservice/**",
                    "/api/V1/turbo/restaurant/approved/opsmanager/**", "/api/V1/turbo/restaurant/optional/erp/**",
                    "/api/V1/turbo/restaurant/detail/erp/**", "/api/serve/file/**", "/api/V1/turbo/resto/plat/filter",
                    "/api/V1/turbo/restaurant/search", "/api/V1/turbo/resto/plat/search",
                    "/api/V1/turbo/resto/plat/detail/**", "/api/V1/turbo/resto/plat/all/price",
                    "/api/V1/turbo/restaurant/check/opening/**", "/api/V1/turbo/restaurant/save/order",
                    "/api/V1/turbo/resto/plat/get/all", "/api/V1/turbo/resto/plat/get/by/**",
                    "/api/V1/turbo/resto/plat/get/collection/by/**", "/api/V1/turbo/restaurant/reject",
                    "/api/V1/turbo/resto/plat/get/by/collection/**", "/api/V1/turbo/resto/boisson/get/**",
                    "/api/V1/turbo/resto/boisson/get/by/resto/**", "/api/turbo/resto/collection/get/by/customer",
                    "api/V1/turbo/restaurant/{restoId}/users", "api/V1/turbo/restaurant/update-commission" 
                    )
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
