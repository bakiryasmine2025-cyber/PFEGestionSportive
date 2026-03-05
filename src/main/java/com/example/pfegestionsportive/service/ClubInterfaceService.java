package com.example.pfegestionsportive.service;

import com.example.pfegestionsportive.model.Club;
import java.util.List;

public interface ClubInterfaceService {
    Club createClub( Club club);
    List<Club> getAllClubs();
    List<Club> getClubsByFederation(Long federationId);
    Club getClubById(Long id);
    Club updateClub(Long id, Club club);
    void deleteClub(Long id);
}
