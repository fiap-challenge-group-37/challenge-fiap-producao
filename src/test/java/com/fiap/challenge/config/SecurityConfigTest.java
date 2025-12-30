package com.fiap.challenge.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Test
    @DisplayName("Deve criar SecurityConfig corretamente")
    void deveCriarSecurityConfigCorretamente() {
        assertNotNull(securityConfig);
    }

    @Test
    @DisplayName("Deve criar JwtAuthenticationConverter")
    void deveCriarJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = securityConfig.jwtAuthenticationConverter();
        
        assertNotNull(converter);
    }

    @Test
    @DisplayName("Deve criar AuthenticationManager")
    void deveCriarAuthenticationManager() throws Exception {
        AuthenticationConfiguration authConfig = new AuthenticationConfiguration();
        
        AuthenticationManager authManager = securityConfig.authenticationManager(authConfig);
        
        assertNotNull(authManager);
    }
}