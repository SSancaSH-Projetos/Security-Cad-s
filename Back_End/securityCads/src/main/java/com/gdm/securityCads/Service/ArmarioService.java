package com.gdm.securityCads.Service;

import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdm.securityCads.model.Armario;
import com.gdm.securityCads.repository.ArmarioRepository;

@Service
public class ArmarioService {
	
	private final ArmarioRepository armarioRepository;

    @Autowired
    public ArmarioService(ArmarioRepository armarioRepository) {
        this.armarioRepository = armarioRepository;
    }

//    public Armario buscarPorNumero(Long numeroArmario) {
//        return armarioRepository.findByNumeroArmario(numeroArmario);
//    }

//    public org.hibernate.mapping.List buscarPorStatus(Boolean ocupado) {
//        return armarioRepository.findByStatus(ocupado);
//    }

//    public org.hibernate.mapping.List buscarPorStatusENumero(Boolean ocupado, Long numeroArmario) {
//        return armarioRepository.findByStatusAndNumeroArmario(ocupado, numeroArmario);
//    }

//    public org.hibernate.mapping.List buscarOcupadosPorAluno(Long idAluno) {
//        return armarioRepository.findByAlunoId(idAluno);
//    }

	public void salvarArmario(Armario armario) {
		armarioRepository.save(armario);
	}

	public List getAllArmarios() {
		return getAllArmarios();
	}

	   
	}

