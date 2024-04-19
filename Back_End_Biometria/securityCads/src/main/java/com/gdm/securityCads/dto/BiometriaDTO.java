package com.gdm.securityCads.dto;

import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.model.Armario;
import com.gdm.securityCads.model.Biometria;
import com.gdm.securityCads.model.CartaoRFID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BiometriaDTO {
	
	private Long idBiometria;
    private int numeroBiometria;
    private Aluno aluno;
    private Armario armario;
    private CartaoRFID cartaoRFID;
	
    
    
}
