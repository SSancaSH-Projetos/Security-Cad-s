package com.gdm.securityCads.model;

import java.sql.Date;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "acessos")
@NoArgsConstructor
@AllArgsConstructor
public class Acessos {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_acesso;

	@Column(name = "id_aluno")
	private Long id_aluno;
	@Column(name = "id-cartao")
	private Long id_cartao;
	@Column(name = "data_hora_acesso")
	private Date data_hora_acesso;

	
	
	@Override
	public String toString() {
		return "Acessos [id_acesso=" + id_acesso + ", id_aluno=" + id_aluno + ", id_cartao=" + id_cartao
				+ ", data_hora_acesso=" + data_hora_acesso + "]";
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Acessos other = (Acessos) obj;
		return Objects.equals(data_hora_acesso, other.data_hora_acesso) && Objects.equals(id_acesso, other.id_acesso)
				&& Objects.equals(id_aluno, other.id_aluno) && Objects.equals(id_cartao, other.id_cartao);
	}




	@Override
	public int hashCode() {
		return Objects.hash(data_hora_acesso, id_acesso, id_aluno, id_cartao);
	}

}
