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
@Table(name = "cartoesRFID")
public class CartoesRFID {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "id_Cartao")
	private Long id_cartao;

	@Column(name = "numero_cartao", nullable = false)
	private int numero_cartao;

	@Column(name = "id_aluno")
	private Long id_aluno;

	@Override
	public String toString() {
		return "CartoesRFID [id_cartao=" + id_cartao + ", numero_cartao=" + numero_cartao + ", id_aluno=" + id_aluno
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartoesRFID other = (CartoesRFID) obj;
		return Objects.equals(id_aluno, other.id_aluno) && Objects.equals(id_cartao, other.id_cartao)
				&& numero_cartao == other.numero_cartao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_aluno, id_cartao, numero_cartao);
	}

}
