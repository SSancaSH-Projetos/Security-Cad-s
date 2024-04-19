package com.gdm.securityCads.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodasInfromacoesRelatorioDTO {

	private Long id_relatorio;
	private Long id_armario;
	private Long id_cartaRFID;
	private Long id_aluno;
	private Long id_biometria;

	public Long getIdRelatorio() {
		return id_relatorio;
	}

	public void setIdRelatorio(Long id_relatorio) {
		this.id_relatorio = id_relatorio;
	}

	public Long getId_armario() {
		return id_armario;
	}

	public void setMatricula(Long id_armario) {
		this.id_armario = id_armario;
	}

	public Long getId_cartaRFID() {
		return id_cartaRFID;
	}

	public void setIdCartaRFID(Long id_cartaRFID) {
		this.id_cartaRFID = id_cartaRFID;
	}

	public Long getIdAluno() {
		return id_cartaRFID;
	}

	public void setIdAluno(Long id_aluno) {
		this.id_aluno = id_aluno;
	}
	
	public Long getIdBiometria() {
		return id_biometria;
	}
	
	public void setIdBiometria(Long id_biometria) {
		this.id_biometria = id_biometria;
	}

}
