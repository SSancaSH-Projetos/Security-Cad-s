package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Relatorio")
@Getter
@Setter
public class Relatorio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRelatorio;

	@OneToOne
	@JoinColumn(name = "id_cartao")
	private CartaoRFID cartaoRFID;

	@OneToOne
	@JoinColumn(name = "id_aluno")
	private Aluno aluno;

	@OneToOne
	@JoinColumn(name = "id")
	private Armario armario;

}
