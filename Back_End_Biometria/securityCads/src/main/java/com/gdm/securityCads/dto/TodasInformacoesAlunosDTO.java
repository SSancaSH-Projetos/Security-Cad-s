package com.gdm.securityCads.dto;

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
public class TodasInformacoesAlunosDTO {

    private String nome;
    private String matricula;
    private String curso;
    private Armario numeroArmario;
    private CartaoRFID numeroCartaoRFID;
    private Biometria biometria;
    private Long id;


}