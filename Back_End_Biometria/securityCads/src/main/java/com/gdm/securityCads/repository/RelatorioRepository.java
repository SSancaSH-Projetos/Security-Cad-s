package com.gdm.securityCads.repository;

import com.gdm.securityCads.model.Relatorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

	static List<Relatorio> findByNome() {
		return null;
	}

}
