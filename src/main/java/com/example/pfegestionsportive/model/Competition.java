package com.example.pfegestionsportive.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "competitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nom;

    @NotBlank
    @Column(nullable = false)
    private String saison;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetitionCategory categorie;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender genre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetitionLevel niveau;

    @Column(nullable = false)
    private boolean cloturee = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateMiseAJour;

    // Relation avec les matchs
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Match> matchs = new ArrayList<>();

    // Relation avec le calendrier
    @OneToOne(mappedBy = "competition", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Calendrier calendrier;

    @PrePersist
    public void prePersist() {
        this.dateCreation  = LocalDateTime.now();
        this.dateMiseAJour = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dateMiseAJour = LocalDateTime.now();
    }

    // ─── Logique métier ───────────────────────────────────────────────────────

    public void ajouterMatch(Match match) {
        match.setCompetition(this);
        this.matchs.add(match);
    }

    public void cloturerSaison() {
        boolean matchsActifs = this.matchs.stream()
                .anyMatch(m -> m.getStatut() == MatchStatus.PLANIFIE
                        || m.getStatut() == MatchStatus.EN_COURS);
        if (matchsActifs) {
            throw new IllegalStateException(
                    "Impossible de clôturer : des matchs sont encore planifiés ou en cours."
            );
        }
        this.cloturee      = true;
        this.dateMiseAJour = LocalDateTime.now();
    }
}