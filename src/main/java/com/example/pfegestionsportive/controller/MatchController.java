package com.example.pfegestionsportive.controller;

import com.example.pfegestionsportive.dto.MatchDTO;
import com.example.pfegestionsportive.dto.ResultatDTO;
import com.example.pfegestionsportive.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MatchController {

    private final MatchService matchService;

    // GET /api/competitions/{competitionId}/matchs
    @GetMapping("/api/competitions/{competitionId}/matchs")
    public ResponseEntity<List<MatchDTO>> getByCompetition(
            @PathVariable("competitionId") Long competitionId) {
        return ResponseEntity.ok(matchService.getByCompetition(competitionId));
    }

    // GET /api/matchs/{id}
    @GetMapping("/api/matchs/{id}")
    public ResponseEntity<MatchDTO> getById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(matchService.getById(id));
    }

    // POST /api/competitions/{competitionId}/matchs
    @PostMapping("/api/competitions/{competitionId}/matchs")  // ✅ أضف /
    public ResponseEntity<MatchDTO> ajouterMatch(
            @PathVariable("competitionId") Long competitionId,  // ✅ أضف ("competitionId")
            @Valid @RequestBody MatchDTO dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(matchService.ajouterMatch(competitionId, dto));
    }

    // PUT /api/matchs/{id}/resultat
    @PutMapping("/api/matchs/{id}/resultat")
    public ResponseEntity<MatchDTO> enregistrerResultat(
            @PathVariable("id") Long id,  // ✅ أضف ("id")
            @Valid @RequestBody ResultatDTO dto) {
        return ResponseEntity.ok(matchService.enregistrerResultat(id, dto));
    }

    // PATCH /api/matchs/{id}/resultat
    @PatchMapping("/api/matchs/{id}/resultat")
    public ResponseEntity<MatchDTO> modifierResultat(
            @PathVariable("id") Long id,
            @Valid @RequestBody ResultatDTO dto) {
        return ResponseEntity.ok(matchService.modifierResultat(id, dto));
    }

    // DELETE /api/matchs/{id}
    @DeleteMapping("/api/matchs/{id}")
    public ResponseEntity<Void> supprimerMatch(
            @PathVariable("id") Long id) {
        matchService.supprimerMatch(id);
        return ResponseEntity.noContent().build();
    }
}