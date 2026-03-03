package com.example.pfegestionsportive.service;

import com.example.pfegestionsportive.dto.CompetitionDTO;
import com.example.pfegestionsportive.exception.ResourceNotFoundException;
import com.example.pfegestionsportive.model.*;
import com.example.pfegestionsportive.repository.CompetitionRepository;
import com.example.pfegestionsportive.repository.MatchRepository; // 🔥 Import jdid
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final MatchRepository matchRepository; // 🔥 Zid hedha houni

    @Transactional(readOnly = true)
    public List<CompetitionDTO> getAll(String saison, String categorie, String genre, String niveau) {
        if (saison == null && categorie == null && genre == null && niveau == null) {
            return competitionRepository.findAllByOrderByDateCreationDesc()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }

        CompetitionCategory cat = null;
        Gender gen = null;
        CompetitionLevel niv = null;

        try { if (categorie != null) cat = CompetitionCategory.valueOf(categorie.toUpperCase().trim()); } catch (Exception ignored) {}
        try { if (genre != null) gen = Gender.valueOf(genre.toUpperCase().trim()); } catch (Exception ignored) {}
        try { if (niveau != null) niv = CompetitionLevel.valueOf(niveau.toUpperCase().trim()); } catch (Exception ignored) {}

        return competitionRepository.findWithFilters(saison, cat, gen, niv)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void genererCalendrier(Long competitionId, String type) {
        Competition competition = findOrThrow(competitionId);

        // 1. Thabbet elli el compétition mouch msakra
        if (competition.isCloturee()) {
            throw new IllegalStateException("Impossible de générer un calendrier pour une compétition clôturée.");
        }

        // 2. Logic simple pour créer des matchs si la liste est vide
        // Houni nasta3mlou el 'lieu' bech el DB ma t-blockich
        List<Match> matchsExistant = matchRepository.findByCompetitionId(competitionId);

        if (matchsExistant.isEmpty()) {
            // Exemple: Crée un match de test si rien n'existe
            Match testMatch = Match.builder()
                    .competition(competition)
                    .equipeDomicile("Equipe A")
                    .equipeExterieur("Equipe B")
                    .dateMatch(LocalDateTime.now().plusDays(7))
                    .lieu("Stade Municipal") // 🔥 Indispensable pour éviter Error 1364
                    .statut(MatchStatus.PLANIFIE)
                    .build();
            matchRepository.save(testMatch);
        } else {
            // Mise à jour des matchs existants qui n'ont pas de lieu
            for (Match m : matchsExistant) {
                if (m.getLieu() == null || m.getLieu().isEmpty()) {
                    m.setLieu("Stade à définir");
                    matchRepository.save(m);
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public CompetitionDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    @Transactional
    public CompetitionDTO create(CompetitionDTO dto) {
        if (competitionRepository.existsByNomAndSaison(dto.getNom(), dto.getSaison())) {
            throw new IllegalArgumentException("Une compétition existe déjà pour cette saison.");
        }
        Competition competition = Competition.builder()
                .nom(dto.getNom())
                .saison(dto.getSaison())
                .categorie(dto.getCategorie())
                .genre(dto.getGenre())
                .niveau(dto.getNiveau())
                .cloturee(false)
                .build();
        return toDTO(competitionRepository.save(competition));
    }

    @Transactional
    public CompetitionDTO update(Long id, CompetitionDTO dto) {
        Competition competition = findOrThrow(id);
        competition.setNom(dto.getNom());
        competition.setSaison(dto.getSaison());
        competition.setCategorie(dto.getCategorie());
        competition.setGenre(dto.getGenre());
        competition.setNiveau(dto.getNiveau());
        return toDTO(competitionRepository.save(competition));
    }

    @Transactional
    public void delete(Long id) {
        if (!competitionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Compétition", id);
        }
        competitionRepository.deleteById(id);
    }

    @Transactional
    public CompetitionDTO cloturerSaison(Long id) {
        Competition competition = findOrThrow(id);
        competition.cloturerSaison();
        return toDTO(competitionRepository.save(competition));
    }

    public Competition findOrThrow(Long id) {
        return competitionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compétition", id));
    }

    public CompetitionDTO toDTO(Competition c) {
        return CompetitionDTO.builder()
                .id(c.getId())
                .nom(c.getNom())
                .saison(c.getSaison())
                .categorie(c.getCategorie())
                .genre(c.getGenre())
                .niveau(c.getNiveau())
                .cloturee(c.isCloturee())
                .dateCreation(c.getDateCreation())
                .dateMiseAJour(c.getDateMiseAJour())
                .nombreMatchs(c.getMatchs() != null ? c.getMatchs().size() : 0)
                .build();
    }
}