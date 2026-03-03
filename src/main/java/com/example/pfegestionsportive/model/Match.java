package com.example.pfegestionsportive.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "matchs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dateMatch;

    @NotBlank
    @Column(nullable = false)
    private String lieu;

    @Column(nullable = false)
    private String equipeDomicile;

    @Column(nullable = false)
    private String equipeExterieur;

    private Integer scoreDomicile;

    private Integer scoreExterieur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private MatchStatus statut = MatchStatus.PLANIFIE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_Id", nullable = false)
    @ToString.Exclude
    private Competition competition;

    @PrePersist
    public void prePersist() {
        this.dateCreation  = LocalDateTime.now();
        this.dateMiseAJour = LocalDateTime.now();
        if (this.statut == null) this.statut = MatchStatus.PLANIFIE;
    }

    @PreUpdate
    public void preUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }



    public void enregistrerResultat(Integer scoreDomicile, Integer scoreExterieur) {
        if (this.statut == MatchStatus.TERMINE) {
            throw new IllegalStateException("Ce match est déjà terminé.");
        }
        if (scoreDomicile < 0 || scoreExterieur < 0) {
            throw new IllegalArgumentException("Les scores ne peuvent pas être négatifs.");
        }
        this.scoreDomicile  = scoreDomicile;
        this.scoreExterieur = scoreExterieur;
        this.statut         = MatchStatus.TERMINE;
        this.dateMiseAJour  = LocalDateTime.now();
    }

    public void modifierResultat(Integer scoreDomicile, Integer scoreExterieur) {
        if (this.statut != MatchStatus.TERMINE) {
            throw new IllegalStateException(
                    "Impossible de modifier : le match n'est pas encore terminé."
            );
        }
        if (scoreDomicile < 0 || scoreExterieur < 0) {
            throw new IllegalArgumentException("Les scores ne peuvent pas être négatifs.");
        }
        this.scoreDomicile  = scoreDomicile;
        this.scoreExterieur = scoreExterieur;
        this.dateMiseAJour  = LocalDateTime.now();
    }
}
