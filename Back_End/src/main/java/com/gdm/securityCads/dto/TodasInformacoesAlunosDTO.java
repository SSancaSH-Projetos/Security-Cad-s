package com.gdm.securityCads.dto;

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
    private int numeroArmario;
    private String numeroCartaoRFID;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public int getNumeroArmario() {
        return numeroArmario;
    }

    public void setNumeroArmario(int numeroArmario) {
        this.numeroArmario = numeroArmario;
    }

    public String getNumeroCartaoRFID() {
        return numeroCartaoRFID;
    }

    public void setNumeroCartaoRFID(String numeroCartaoRFID) {
        this.numeroCartaoRFID = numeroCartaoRFID;
    }

}


