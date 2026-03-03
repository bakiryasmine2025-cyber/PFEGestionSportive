package com.example.pfegestionsportive.service;

import com.example.pfegestionsportive.dto.CalendrierDTO;
import com.example.pfegestionsportive.dto.MatchDTO;
import com.example.pfegestionsportive.model.*;
import com.example.pfegestionsportive.repository.CompetitionRepository;
import com.example.pfegestionsportive.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendrierService {

    private final MatchRepository matchRepository;
    private final CompetitionRepository competitionRepository;

    @Transactional(readOnly = true)
    public CalendrierDTO getByCompetition(Long competitionId) {
        // 1. Njibou el matches mel repository
        List<Match> matchsEntities = matchRepository.findByCompetitionId(competitionId);

        // 2. Mapping mel Entities lel DTOs
        List<MatchDTO> matchDTOs = matchsEntities.stream()
                .map(this::toMatchDTO)
                .collect(Collectors.toList());

        // 3. El Builder tawa meryel khater zedna 'matchs' f'el CalendrierDTO
        return CalendrierDTO.builder()
                .competitionId(competitionId)
                .matchs(matchDTOs)
                .nombreMatchsGeneres(matchDTOs.size())
                .dateGeneration(LocalDateTime.now())
                .build();
    }

    @Transactional
    public CalendrierDTO genererCalendrier(Long competitionId, PlanningType type) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RuntimeException("Compétition non trouvée"));

        // Verifier si la compétition n'est pas clôturée
        if (competition.isCloturee()) {
            throw new IllegalStateException("Impossible de générer un calendrier pour une compétition clôturée.");
        }

        // TODO: Houni t-zid el logic mta3 el Round-Robin pour générer les matchs réels
        // Exemple d'un match de test pour vérifier que ça marche
        Match matchTest = Match.builder()
                .competition(competition)
                .equipeDomicile("Equipe A")
                .equipeExterieur("Equipe B")
                .dateMatch(LocalDateTime.now().plusDays(7))
                .lieu("Terrain Central") // Champ qui peut être NULL f'el DB ba3d el ALTER
                .statut(MatchStatus.PLANIFIE)
                .build();

        matchRepository.save(matchTest);

        return getByCompetition(competitionId);
    }

    // ✅ La méthode de mapping qui corrigeait les erreurs rouges
    private MatchDTO toMatchDTO(Match m) {
        return MatchDTO.builder()
                .id(m.getId())
                .equipeDomicile(m.getEquipeDomicile())
                .equipeExterieur(m.getEquipeExterieur())
                .dateMatch(m.getDateMatch())
                .lieu(m.getLieu())
                .scoreDomicile(m.getScoreDomicile())
                .scoreExterieur(m.getScoreExterieur())
                // 🔥 On passe l'Enum directement, pas de .toString()
                .statut(m.getStatut())
                .competitionId(m.getCompetition() != null ? m.getCompetition().getId() : null)
                // 🔥 Ces champs nécessitent d'être présents dans MatchDTO
                .dateCreation(m.getDateCreation())
                .dateMiseAJour(m.getDateMiseAJour())
                .build();
    }
}