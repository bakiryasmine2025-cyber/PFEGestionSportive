package com.example.pfegestionsportive.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calendriers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calendrier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private LocalDateTime dateGeneration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanningType typePlanning;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competitionId", nullable = false, unique = true)
    @ToString.Exclude
    private Competition competition;

    @PrePersist
    public void prePersist() {
        this.dateGeneration = LocalDateTime.now();
    }
}
