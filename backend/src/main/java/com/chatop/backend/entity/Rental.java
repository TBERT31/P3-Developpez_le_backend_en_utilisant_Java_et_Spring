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
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "surface", precision = 10, scale = 2) // Jusqu'à 10 chiffres au total et 2 nombre après la virgule max
    private Double surface;

    @Column(name = "price", precision = 10, scale = 2)
    private Double price;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

}
