package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cartaoRFID")
public class CartaoRFID {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_Cartao")
	private Long idCartao;

	@Column(name = "numero_cartao", nullable = false)
	private String numeroCartao;

	@Column(name = "id_aluno")
	private Long idAluno;

	// Métodos getters e setters

	public Long getIdCartao() {
		return idCartao;
	}

	public void setIdCartao(Long idCartao) {
		this.idCartao = idCartao;
	}

	public String getNumeroCartao() {
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public Long getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(Long idAluno) {
		this.idAluno = idAluno;
	}

	@Override
	public String toString() {
		return "CartaoRFID [id_cartao=" + idCartao + ", numero_cartao=" + numeroCartao + ", id_aluno=" + idAluno
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
		CartaoRFID other = (CartaoRFID) obj;
		return Objects.equals(idAluno, other.idAluno) && Objects.equals(idCartao, other.idCartao)
				&& numeroCartao == other.numeroCartao;
	}

	@Override
	public int hashCode() {
		return Objects.hash(idAluno, idCartao, numeroCartao);
	}

}
