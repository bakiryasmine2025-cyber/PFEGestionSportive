package com.example.pfegestionsportive.repository;

import com.example.pfegestionsportive.model.Competition;
import com.example.pfegestionsportive.model.CompetitionCategory;
import com.example.pfegestionsportive.model.CompetitionLevel;
import com.example.pfegestionsportive.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {


    List<Competition> findAllByOrderByDateCreationDesc();


    @Query("SELECT c FROM Competition c WHERE " +
            "(COALESCE(:saison, '') = '' OR c.saison = :saison) " +
            "AND (:categorie IS NULL OR c.categorie = :categorie) " +
            "AND (:genre IS NULL OR c.genre = :genre) " +
            "AND (:niveau IS NULL OR c.niveau = :niveau) " +
            "ORDER BY c.dateCreation DESC")
    List<Competition> findWithFilters(
            @Param("saison")    String saison,
            @Param("categorie") CompetitionCategory categorie,
            @Param("genre")     Gender genre,
            @Param("niveau")    CompetitionLevel niveau
    );

    List<Competition> findBySaison(String saison);

    boolean existsByNomAndSaison(String nom, String saison);
}