package com.example.pfegestionsportive.repository;
import com.example.pfegestionsportive.model.Match;
import com.example.pfegestionsportive.model.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>  {
    List<Match> findByCompetitionId(Long competitionId);

    List<Match> findByCompetitionIdAndStatut(Long competitionId, MatchStatus statut);

    boolean existsByCompetitionIdAndStatut(Long competitionId, MatchStatus statut);
}
