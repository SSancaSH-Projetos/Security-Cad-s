package com.gdm.securityCads.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdm.securityCads.model.Aluno;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	List<Aluno> findByMatricula(String matricula);

	List<Aluno> findByNome(String nome);
	List findByCurso(String curso);

}
