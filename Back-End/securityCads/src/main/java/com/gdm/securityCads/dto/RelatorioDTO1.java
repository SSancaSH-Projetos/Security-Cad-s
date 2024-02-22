package com.gdm.securityCads.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RelatorioDTO1 {

	private Long idAluno;
	private String nomeAluno;
	private String matriculaAluno;
	private String cursoAluno;
	private Long numeroArmario;
	private Timestamp dataHoraAcesso;

	public RelatorioDTO1(Long idAluno, String nomeAluno, String matriculaAluno, String cursoAluno, Long numeroArmario,
			Timestamp dataHoraAcesso) {
		this.idAluno = idAluno;
		this.nomeAluno = nomeAluno;
		this.matriculaAluno = matriculaAluno;
		this.cursoAluno = cursoAluno;
		this.numeroArmario = numeroArmario;
		this.dataHoraAcesso = dataHoraAcesso;
	}

	public RelatorioDTO1(String string, String string2, long l, Timestamp valueOf, String string3) {

	}
}
