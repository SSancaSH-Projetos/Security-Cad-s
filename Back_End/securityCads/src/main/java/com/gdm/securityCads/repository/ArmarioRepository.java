package com.gdm.securityCads.repository;

import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gdm.securityCads.model.Armario;

public interface ArmarioRepository extends JpaRepository<Armario, Long> {

//	Armario findByNumeroArmario(Long numeroArmario);
//
//    List findByStatus(Boolean ocupado);
//
//    List findByStatusAndNumeroArmario(Boolean ocupado, Long numeroArmario);

//    List findByAlunoId(Long idAluno);

	

}
