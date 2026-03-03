package com.example.pfegestionsportive.controller;

import com.example.pfegestionsportive.dto.CalendrierDTO;
import com.example.pfegestionsportive.model.PlanningType;
import com.example.pfegestionsportive.service.CalendrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendrier")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CalendrierController {

    private final CalendrierService calendrierService;

    // GET /api/calendrier/{competitionId}
    @GetMapping("/{competitionId}")
    public ResponseEntity<CalendrierDTO> getByCompetition(
            @PathVariable("competitionId") Long competitionId) {
        return ResponseEntity.ok(calendrierService.getByCompetition(competitionId));
    }

    // POST /api/calendrier/generer?competitionId=1&typePlanning=ALLER_RETOUR
    @PostMapping("/generer")
    public ResponseEntity<CalendrierDTO> genererCalendrier(
            @RequestParam("competitionId") Long competitionId,
            @RequestParam(value = "typePlanning", defaultValue = "ALLER_RETOUR") PlanningType typePlanning) {
        return ResponseEntity.ok(
                calendrierService.genererCalendrier(competitionId, typePlanning)
        );
    }
}