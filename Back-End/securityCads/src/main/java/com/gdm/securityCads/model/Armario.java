package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "armario")
public class Armario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_armario")
	private Long id_armarios;

	@Column(name = "numero_armario", nullable = false)
	private int numero_armario;

	@Column(name = "ocupacao_armario", columnDefinition = "BIT DEFAULT 1")
	private boolean ocupacao;

	@Override
	public String toString() {
		return "Armario [id_armarios=" + id_armarios + ", numero_armario=" + numero_armario + ", ocupacao=" + ocupacao
				+ "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_armarios, numero_armario, ocupacao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Armario other = (Armario) obj;
		return Objects.equals(id_armarios, other.id_armarios) && numero_armario == other.numero_armario
				&& ocupacao == other.ocupacao;
	}

}
