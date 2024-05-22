package com.chatop.backend.service.auth;

import com.chatop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.chatop.backend.entity.User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        /**
             Si l'utilisateur est trouvé, un objet UserDetails est créé et retourné
             UserDetails est une interface fournie par Spring Security
             org.springframework.security.core.userdetails.User est une implémentation de cette interface
             Les paramètres sont l'email, le mot de passe et une liste de rôles/permissions (vide dans notre cas),
             car nous n'avons pas implémenter le concept d'Authorization dans ce projet.
         */
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}
