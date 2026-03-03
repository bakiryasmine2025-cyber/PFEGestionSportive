package com.example.pfegestionsportive.repository;
import com.example.pfegestionsportive.model.Calendrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendrierRepository extends JpaRepository<Calendrier, Long>{
    Optional<Calendrier> findByCompetitionId(Long competitionId);

    boolean existsByCompetitionId(Long competitionId);
}
