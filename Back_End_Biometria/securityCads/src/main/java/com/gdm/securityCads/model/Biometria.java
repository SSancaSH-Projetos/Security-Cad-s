package com.gdm.securityCads.model;

import com.gdm.securityCads.dto.BiometriaDTO;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "biometria")
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_biometria")
    private Long idBiometria;

    @Column(name = "numero_biometria", nullable = false)
    private int numeroBiometria;

    @OneToOne(mappedBy = "biometria")
    private Aluno aluno;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_armario")
    private Armario armario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_cartaoRFID")
    private CartaoRFID cartaoRFID;
    
    
    public static Biometria of(BiometriaDTO biometria) {
    	Biometria bio = new Biometria();
    	bio.setIdBiometria(biometria.getIdBiometria());
    	bio.setNumeroBiometria(biometria.getNumeroBiometria());
    	bio.setCartaoRFID(biometria.getCartaoRFID());
    	bio.setAluno(biometria.getAluno());
    	bio.setArmario(biometria.getArmario());
    	return bio;
    }

}
