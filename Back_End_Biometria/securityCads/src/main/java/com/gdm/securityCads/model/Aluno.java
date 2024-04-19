package com.gdm.securityCads.model;

import java.util.Objects;

import com.gdm.securityCads.dto.TodasInformacoesAlunosDTO;

import jakarta.persistence.*;

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

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "biometria")
	private Biometria biometria;

	// Construtores

	public Aluno() {
		// Construtor vazio necessário para frameworks como Spring
	}

	public Aluno(String nome, String matricula, String curso, Armario armario, CartaoRFID cartaoRFID,
			Biometria biometria) {
		this.nome = nome;
		this.matricula = matricula;
		this.curso = curso;
		this.armario = armario;
		this.cartaoRFID = cartaoRFID;
		this.biometria = biometria;
	}

	// Métodos getter e setter

	public Long getIdAluno() {
		return idAluno;
	}

	public void setIdAluno(Long idAluno) {
		this.idAluno = idAluno;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCurso() {
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Armario getArmario() {
		return armario;
	}

	public void setArmario(Armario armario) {
		this.armario = armario;
	}

	public CartaoRFID getCartaoRFID() {
		return cartaoRFID;
	}

	public void setCartaoRFID(CartaoRFID cartaoRFID) {
		this.cartaoRFID = cartaoRFID;
	}

	public Biometria getBiometria() {
		return biometria;
	}

	public void setBiometria(Biometria biometria) {
		this.biometria = biometria;
	}

	// Métodos equals e hashCode

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Aluno aluno = (Aluno) obj;
		return Objects.equals(idAluno, aluno.idAluno) && Objects.equals(nome, aluno.nome)
				&& Objects.equals(matricula, aluno.matricula) && Objects.equals(curso, aluno.curso)
				&& Objects.equals(armario, aluno.armario) && Objects.equals(cartaoRFID, aluno.cartaoRFID)
				&& Objects.equals(biometria, aluno.biometria);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idAluno, nome, matricula, curso, armario, cartaoRFID, biometria);
	}

	@Override
	public String toString() {
		return "Aluno [idAluno=" + idAluno + ", nome=" + nome + ", matricula=" + matricula + ", curso=" + curso
				+ ", armario=" + armario + ", cartaoRFID=" + cartaoRFID + ", biometria=" + biometria + "]";
	}
	

	// Método toString
	
	public static Aluno of(TodasInformacoesAlunosDTO alunoDTO) {
	Aluno aluno= new Aluno();
	aluno.setIdAluno(alunoDTO.getId());
	aluno.setNome(alunoDTO.getNome());
	aluno.setMatricula(alunoDTO.getMatricula());
	aluno.setCurso(alunoDTO.getCurso());
	aluno.setArmario(alunoDTO.getNumeroArmario());
	aluno.setBiometria(alunoDTO.getBiometria());
	aluno.setCartaoRFID(alunoDTO.getNumeroCartaoRFID());
	
	return aluno;
	}

}