package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "armario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Armario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "numero", nullable = false)
	private int numero;

	@Column(name = "ocupacao", columnDefinition = "BIT DEFAULT 1")
	private boolean ocupacao;

	// Métodos getters e setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public boolean isOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(boolean ocupacao) {
		this.ocupacao = ocupacao;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Armario armario = (Armario) o;
		return numero == armario.numero && ocupacao == armario.ocupacao && Objects.equals(id, armario.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, numero, ocupacao);
	}
}
