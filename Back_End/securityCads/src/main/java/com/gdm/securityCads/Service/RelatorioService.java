package com.gdm.securityCads.Service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import com.gdm.securityCads.dto.RelatorioDTO1;

@Service
public class RelatorioService {

	public void gerarRelatorio() {

		RelatorioDTO1 relatorio = new RelatorioDTO1("Nome do Aluno", "Curso do Aluno", 1L,
				Timestamp.valueOf("2024-02-13 10:30:00"), "Informação adicional relevante");

		System.out.println("Relatório Gerado: " + relatorio);

	}

}
