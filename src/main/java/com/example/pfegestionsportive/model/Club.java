package com.example.pfegestionsportive.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clubs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String ville;
    private String region;
    private Integer anneeFondation;
    private String statut;
    private String Urllogo ;



    @ManyToOne
    @JoinColumn(name = "federation_id")
    @JsonIgnore
    private Federation federation;
}