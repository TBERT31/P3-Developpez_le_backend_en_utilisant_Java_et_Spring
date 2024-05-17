package com.chatop.backend.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

import static com.chatop.backend.constant.JWTUtils.SECRET_KEY;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())  // Désactive la protection CSRF
                .sessionManagement(
                        session -> session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ) // Indique que l'application est stateless et n'utilise pas de sessions HTTP
                )
                .authorizeHttpRequests(
                        auth -> auth.anyRequest().authenticated()
                ) // Exige que toutes les requêtes HTTP soient authentifiées
                .oauth2ResourceServer(
                        (oauth2) -> oauth2.jwt(Customizer.withDefaults())
                ) // Configure le serveur de ressources OAuth2 pour utiliser des jetons JWT
                .httpBasic(Customizer.withDefaults())  // Utilise l'authentification HTTP Basic par défaut
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // On utilise BCrypt pour encoder et sécuriser les mdp.
    }

    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder().username("tbert31").password(passwordEncoder().encode("1234")).roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(SECRET_KEY.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(
                SECRET_KEY.getBytes(),
                0,
                SECRET_KEY.getBytes().length,
                "RSA"
        );

        return NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256).build();
    }
}
