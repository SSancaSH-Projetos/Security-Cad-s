package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "aluno")
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_aluno")
	private Long idAluno;

	@Column(name = "nome", nullable = false, length = 50)
	private String nome;

	@Column(name = "matricula", nullable = false, length = 10)
	private String matricula;

	@Column(name = "curso", nullable = false, length = 50)
	private String curso;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_armario")
	private Armario armario;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_cartaoRFID")
	private CartaoRFID cartaoRFID;

	public Object getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsKey(Object id) {
		// TODO Auto-generated method stub
		return false;
	}


}

