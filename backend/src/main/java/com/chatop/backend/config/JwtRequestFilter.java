package com.chatop.backend.config;

import com.chatop.backend.service.auth.MyUserDetailsService;
import com.chatop.backend.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Cette méthode est appelée pour chaque requête HTTP et vérifie la présence
     * d'un jeton JWT dans l'en-tête d'autorisation.
     *
     * @param request  L'objet HttpServletRequest contenant les informations sur la requête.
     * @param response L'objet HttpServletResponse pour envoyer la réponse.
     * @param chain    L'objet FilterChain pour continuer la chaîne de filtres.
     * @throws ServletException Si une erreur de servlet se produit.
     * @throws IOException      Si une erreur d'entrée/sortie se produit.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {

        // Récupère l'en-tête Authorization de la requête HTTP.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        try {
            // Vérifie si l'en-tête Authorization contient un jeton JWT.
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extrait le jeton JWT de l'en-tête.
                jwt = authorizationHeader.substring(7);
                // Extrait le username du jeton JWT.
                username = jwtUtil.extractUsername(jwt);
            }

            // Si un username a été extrait et qu'il n'y a pas encore d'utilisateur authentifié dans le contexte de sécurité.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Charge les détails de l'utilisateur à partir du service myUserDetailsService.
                UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(username);

                // Valide le jeton JWT.
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    // Crée un UsernamePasswordAuthenticationToken avec les détails de l'utilisateur.
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    // Définit des détails supplémentaires pour l'authentification.
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Définit l'authentification dans le contexte de sécurité.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            // Passe la requête et la réponse à la chaîne de filtres suivante.
            chain.doFilter(request, response);
        }
        // Capture les exceptions spécifiques liées au JWT
        catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            // Log l'exception
            logger.error(e.getMessage());

            // Paramètre le statut de la réponse et le message (401)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Invalid or expired JWT token\"}");
        }
    }
}
