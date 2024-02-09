package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "aluno")
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_aluno")

	private Long id_aluno;

	@Column(name = "nome", nullable = false, length = 50)
	private String nome;

	@Column(name = "matricula", nullable = false, length = 10)
	private String matricula;

	@Column(name = "curso", nullable = false, length = 50)
	private String curso;

	@Override
	public String toString() {
		return "Aluno [id_aluno=" + id_aluno + ", nome=" + nome + ", matricula=" + matricula + ", curso=" + curso + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		return Objects.equals(curso, other.curso) && Objects.equals(id_aluno, other.id_aluno)
				&& Objects.equals(matricula, other.matricula) && Objects.equals(nome, other.nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(curso, id_aluno, matricula, nome);
	}

}
