package com.example.pfegestionsportive.repository;
import com.example.pfegestionsportive.model.Licence;
import com.example.pfegestionsportive.model.LicenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LicenceRepository extends JpaRepository<Licence, UUID>{
    Optional<Licence> findByMembreId(Long membreId);

    List<Licence> findByStatut(LicenseStatus statut);

    boolean existsByMembreIdAndStatut(Long membreId, LicenseStatus statut);

}
