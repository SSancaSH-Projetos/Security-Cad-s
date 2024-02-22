package com.gdm.securityCads.repository;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gdm.securityCads.model.Aluno;
import com.mysql.cj.log.Log;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	List findByNome(String nome);

	Aluno findByMatricula(String Matricula);

	List findByCurso(String curso);

}
