package com.example.pfegestionsportive.dto;
import com.example.pfegestionsportive.model.CompetitionCategory;
import com.example.pfegestionsportive.model.CompetitionLevel;
import com.example.pfegestionsportive.model.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetitionDTO {
    public  Long id;

    @NotBlank(message = "le nom est obligatoire")
    private String nom;

    @NotBlank(message = "le saison est obligatoire ")
    private String saison;

    @NotNull(message = "la calendrier est obligatoire")
    private  CompetitionCategory categorie;

    @NotNull(message = "le genre est obligatoire")
    private Gender genre;

    @NotNull(message = "le niveau est obligatoire")
    private CompetitionLevel niveau;

    private boolean cloturee;
    private  LocalDateTime dateCreation;
    private LocalDateTime dateMiseAJour;
    private int nombreMatchs;
}
