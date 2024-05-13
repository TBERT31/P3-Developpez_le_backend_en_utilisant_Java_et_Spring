package com.chatop.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    /*

        Boilerplate de code grandement réduit grâce à la dépendence Lombok.

        @Data de Lombok permet de regrouper un ensemble d'annotations telles que @ToString,
        @EqualsAndHashCode, @Getter sur tous les champs,
        @Setter sur les champs non finaux, et @RequiredArgsConstructor.
        En utilisant @Data, on automatise la création de méthodes getter et setter pour chaque champ,
        ainsi que les méthodes toString(), equals(Object other), et hashCode().

        @SuperBuilder est une extension de @Builder.
        Elle permet de créer automatiquement des patterns de builder complexes
        avec une API fluide pour l'instanciation d'objets.
        Elle supporte les classes héritées en permettant à chaque classe
        dans la hiérarchie de spécifier ses propres paramètres dans le pattern de builder.

        @AllArgsConstructor génère un constructeur avec un argument pour chaque champ dans la classe.
        Cela est utile lorsqu'il faut initialiser tous les attributs d'une instance
        de la classe via le constructeur plutôt que par des setters.

        @NoArgsConstructor crée un constructeur sans argument (constructeur par défaut).
        Cela est souvent requis par JPA et par d'autres frameworks qui utilisent
        la réflexion pour instancier des classes sans fournir d'arguments spécifiques.

     */
}
