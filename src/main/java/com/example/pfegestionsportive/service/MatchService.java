package com.example.pfegestionsportive.service;

import com.example.pfegestionsportive.dto.MatchDTO;
import com.example.pfegestionsportive.dto.ResultatDTO;
import com.example.pfegestionsportive.exception.ResourceNotFoundException;
import com.example.pfegestionsportive.model.Competition;
import com.example.pfegestionsportive.model.Match;
import com.example.pfegestionsportive.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository     matchRepository;
    private final CompetitionService  competitionService;


    @Transactional(readOnly = true)
    public List<MatchDTO> getByCompetition(Long competitionId) {
        competitionService.findOrThrow(competitionId);
        return matchRepository.findByCompetitionId(competitionId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    public MatchDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }


    @Transactional
    public MatchDTO ajouterMatch(Long competitionId, MatchDTO dto) {
        Competition competition = competitionService.findOrThrow(competitionId);

        if (competition.isCloturee()) {
            throw new IllegalStateException(
                    "Impossible d'ajouter un match : la compétition est clôturée."
            );
        }
        Match match = Match.builder()
                .dateMatch(dto.getDateMatch())
                .lieu(dto.getLieu())
                .equipeDomicile(dto.getEquipeDomicile())
                .equipeExterieur(dto.getEquipeExterieur())
                .build();

        competition.ajouterMatch(match);
        return toDTO(matchRepository.save(match));
    }


    @Transactional
    public MatchDTO enregistrerResultat(Long matchId, ResultatDTO dto) {
        Match match = findOrThrow(matchId);

        if (match.getCompetition().isCloturee()) {
            throw new IllegalStateException(
                    "Impossible d'enregistrer un résultat : la compétition est clôturée."
            );
        }
        match.enregistrerResultat(dto.getScoreDomicile(), dto.getScoreExterieur());
        return toDTO(matchRepository.save(match));
    }


    @Transactional
    public MatchDTO modifierResultat(Long matchId, ResultatDTO dto) {
        Match match = findOrThrow(matchId);

        if (match.getCompetition().isCloturee()) {
            throw new IllegalStateException(
                    "Impossible de modifier un résultat : la compétition est clôturée."
            );
        }
        match.modifierResultat(dto.getScoreDomicile(), dto.getScoreExterieur());
        return toDTO(matchRepository.save(match));
    }


    @Transactional
    public void supprimerMatch(Long matchId) {
        Match match = findOrThrow(matchId);
        if (match.getCompetition().isCloturee()) {
            throw new IllegalStateException(
                    "Impossible de supprimer un match d'une compétition clôturée."
            );
        }
        matchRepository.delete(match);
    }


    public Match findOrThrow(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match", id));
    }

    public MatchDTO toDTO(Match m) {
        return MatchDTO.builder()
                .id(m.getId())
                .dateMatch(m.getDateMatch())
                .lieu(m.getLieu())
                .equipeDomicile(m.getEquipeDomicile())
                .equipeExterieur(m.getEquipeExterieur())
                .scoreDomicile(m.getScoreDomicile())
                .scoreExterieur(m.getScoreExterieur())
                .statut(m.getStatut())
                .competitionId(m.getCompetition() != null ? m.getCompetition().getId() : null)
                .dateCreation(m.getDateCreation())
                .dateMiseAJour(m.getDateMiseAJour())
                .build();
    }

}
