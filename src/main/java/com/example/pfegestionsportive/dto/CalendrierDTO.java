package com.example.pfegestionsportive.dto;

import com.example.pfegestionsportive.model.PlanningType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List; // 🔥 N'oublie pas l'import

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendrierDTO {
    private Long id;
    private String nom;
    private LocalDateTime dateGeneration;
    private PlanningType typePlanning;

    @NotNull(message = "ID de la compétition est obligatoire")
    private Long competitionId;

    private int nombreMatchsGeneres;

    // 🔥 Zid el champ hedha bedhabt bech el Service ma3ach ya3mel erreur
    private List<MatchDTO> matchs;
}