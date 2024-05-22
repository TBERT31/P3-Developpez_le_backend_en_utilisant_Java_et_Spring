package com.chatop.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    // Chaîne de filtres de sécurité à appliquer à chaque requête HTTP.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive la protection CSRF pour permettre une communication sans état entre le client et le serveur.
                .csrf(CsrfConfigurer::disable)
                // Autorise l'accès à certaines URL sans authentification
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/uploads/**",
                                // resources pour que swagger marche correctement
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )
                // Gestion des exceptions lors de l'authentification
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                // Gestion de la session
                .sessionManagement(session -> session
                        // Indique que la session ne sera pas utilisée pour gérer l'état de l'authentification
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Ajoute un filtre pour valider les jetons JWT lors de chaque requête
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Construit et retourne la chaîne de filtres de sécurité configurée
        return http.build();
    }

    /**
     * Définit un bean pour AuthenticationManager.
     *
     * @param authenticationConfiguration Configuration de l'authentification fournie par Spring.
     * @return L'instance d'AuthenticationManager qui sera utilisée par Spring Security pour gérer l'authentification.
     * @throws Exception Si une erreur se produit lors de l'obtention de l'AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Obtient et retourne l'instance d'AuthenticationManager à partir de la configuration d'authentification fournie par Spring.
        // L'AuthenticationManager est responsable de l'authentification des utilisateurs en vérifiant leurs identifiants.
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
