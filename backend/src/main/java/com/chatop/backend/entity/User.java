package com.chatop.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

// Annotations Lombok,  très utiles pour réduire le boilerplate de code dans les classes Java (getter, setter etc...)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
// Annotation Jakarta, sert à définir l'architecture de notre base de données.
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 45, unique = true)
    private String email;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "password", nullable = false, length = 64)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updated_at;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL, /*  toutes les opérations de cycle de vie effectuées sur l'entité parent
                                            seront propagées à l'entité enfant. Cela inclut les opérations persist,
                                            remove, refresh, merge, et detach. */
            orphanRemoval = true
    )
    private List<Rental> rentals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

}
