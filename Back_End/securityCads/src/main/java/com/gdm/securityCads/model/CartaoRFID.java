package com.gdm.securityCads.model;

import java.util.Objects;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "cartaoRFID")
public class CartaoRFID {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_Cartao")
	private Long idCartao;

	@Column(name = "numero_cartao", nullable = false)
	private String numeroCartao;

	@OneToOne(mappedBy = "cartaoRFID")
	private Aluno aluno;

}
