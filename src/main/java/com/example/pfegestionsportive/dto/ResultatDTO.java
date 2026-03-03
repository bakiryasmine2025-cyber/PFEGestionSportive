package com.example.pfegestionsportive.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultatDTO {

    @NotNull(message = "Le score domicile est obligatoire")
    @Min(value = 0, message = "Le score ne peut pas être négatif")
    private Integer scoreDomicile;

    @NotNull(message = "Le score extérieur est obligatoire")
    @Min(value = 0, message = "Le score ne peut pas être négatif")
    private Integer scoreExterieur;
}