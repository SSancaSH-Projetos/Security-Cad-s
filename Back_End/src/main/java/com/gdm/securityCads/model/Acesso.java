package com.gdm.securityCads.model;

import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "acessos")
@NoArgsConstructor
@AllArgsConstructor

public class Acesso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_acesso;

	@ManyToOne
	@JoinColumn(name = "id_aluno")
	private Aluno aluno;

	@ManyToOne
	@JoinColumn(name = "id_cartao")
	private CartaoRFID cartao;

	@Column(name = "data_hora_acesso")
	private Date data_hora_acesso;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Acesso acesso = (Acesso) o;
		return Objects.equals(id_acesso, acesso.id_acesso) && Objects.equals(aluno, acesso.aluno) && Objects.equals(cartao, acesso.cartao) && Objects.equals(data_hora_acesso, acesso.data_hora_acesso);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_acesso, aluno, cartao, data_hora_acesso);
	}

	public Date setDataHoraAcesso() {
		return data_hora_acesso;

	}

	public Aluno setAluno(Aluno aluno) {
		return aluno;

	}

	public CartaoRFID setCartaoRFID(CartaoRFID cartao) {
		return cartao;

	}

}
