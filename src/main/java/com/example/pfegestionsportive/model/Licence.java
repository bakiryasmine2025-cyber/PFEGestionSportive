package com.example.pfegestionsportive.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "Licences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Licence {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseType type;

    @Column(nullable = false)
    private LocalDate dateEmission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseStatus statut;

    @Column(nullable = false)
    private Boolean aptitudeMedicale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membre_id",nullable = false)
    private User membre;

}
