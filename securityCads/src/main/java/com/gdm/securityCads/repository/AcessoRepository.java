package com.gdm.securityCads.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdm.securityCads.model.Acesso;
import com.gdm.securityCads.model.Aluno;
import org.springframework.stereotype.Repository;

@Repository
public interface AcessoRepository extends JpaRepository<Acesso, Long> {

	List<Acesso> findByAluno(Aluno aluno);

	//List<Acesso> findByFilterName();


}
