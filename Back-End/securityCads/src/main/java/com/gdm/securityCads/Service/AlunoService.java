package com.gdm.securityCads.Service;

import com.gdm.securityCads.model.Armario;
import com.gdm.securityCads.model.CartaoRFID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdm.securityCads.dto.TodasInformacoesAlunosDTO;
import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.repository.AlunoRepository;

@Service
public class AlunoService {

	private final AlunoRepository alunoRepository;

	@Autowired
	public AlunoService(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public void salvarTudo(TodasInformacoesAlunosDTO todasInformacoesAlunosDTO) {

		Aluno alunoParaSalvar = new Aluno();
		Armario armarioParaSalvar = new Armario();
		CartaoRFID cartaoRFIDParaSalvar = new CartaoRFID();

		alunoParaSalvar.setNome(todasInformacoesAlunosDTO.getNome());
		alunoParaSalvar.setMatricula(todasInformacoesAlunosDTO.getMatricula());
		alunoParaSalvar.setCurso(todasInformacoesAlunosDTO.getCurso());

		armarioParaSalvar.setNumero(todasInformacoesAlunosDTO.getNumeroArmario());

		cartaoRFIDParaSalvar.setNumeroCartao(todasInformacoesAlunosDTO.getNumeroCartaoRFID());
		//cartaoRFIDParaSalvar.setAluno();

		alunoParaSalvar.setArmario(armarioParaSalvar);
		alunoParaSalvar.setCartaoRFID(cartaoRFIDParaSalvar);

		alunoRepository.save(alunoParaSalvar);
	}

}



