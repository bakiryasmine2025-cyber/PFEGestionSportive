package com.example.pfegestionsportive.dto;

import com.example.pfegestionsportive.model.MatchStatus;
import lombok.*; // 🔥 Lezem hedhi mawjouda
import java.time.LocalDateTime;

@Data            // 🔥 Te-generi el Getters/Setters
@NoArgsConstructor
@AllArgsConstructor
@Builder         // 🔥 Hedhi elli t-khalli .builder() yemshi f'el Service
public class MatchDTO {
    private Long id;
    private LocalDateTime dateMatch;
    private String lieu;
    private String equipeDomicile;
    private String equipeExterieur;
    private Integer scoreDomicile;
    private Integer scoreExterieur;
    private MatchStatus statut; // Enum match m3a el DB
    private Long competitionId;
    private LocalDateTime dateCreation;
    private LocalDateTime dateMiseAJour;
}