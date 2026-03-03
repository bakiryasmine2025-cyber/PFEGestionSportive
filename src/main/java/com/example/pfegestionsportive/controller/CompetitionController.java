package com.example.pfegestionsportive.controller;

import com.example.pfegestionsportive.dto.CompetitionDTO;
import com.example.pfegestionsportive.service.CompetitionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/competitions")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CompetitionController {

    private final CompetitionService competitionService;

    @GetMapping
    public ResponseEntity<List<CompetitionDTO>> getAll(
            @RequestParam(defaultValue = "") String saison,
            @RequestParam(defaultValue = "") String categorie,
            @RequestParam(defaultValue = "") String genre,
            @RequestParam(defaultValue = "") String niveau
    ) {
        return ResponseEntity.ok(
                competitionService.getAll(saison, categorie, genre, niveau)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetitionDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(competitionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<CompetitionDTO> create(@Valid @RequestBody CompetitionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(competitionService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetitionDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CompetitionDTO dto) {
        return ResponseEntity.ok(competitionService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        competitionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cloturer")
    public ResponseEntity<Map<String, Object>> cloturerSaison(@PathVariable Long id) {
        CompetitionDTO dto = competitionService.cloturerSaison(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Compétition '" + dto.getNom() + "' clôturée avec succès.");
        response.put("competition", dto);
        return ResponseEntity.ok(response);
    }
}