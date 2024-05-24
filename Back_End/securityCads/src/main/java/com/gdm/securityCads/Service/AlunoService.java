package com.gdm.securityCads.Service;

import com.gdm.securityCads.model.Armario;
import com.gdm.securityCads.model.Biometria;
import com.gdm.securityCads.model.CartaoRFID;
import com.gdm.securityCads.repository.CartaoRFIDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdm.securityCads.dto.TodasInformacoesAlunosDTO;
import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.repository.AlunoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

	@Autowired
	private  AlunoRepository alunoRepository;

	@Autowired
	private CartaoRFIDRepository cartaoRFIDRepository;


	public AlunoService(AlunoRepository alunoRepository) {
		this.alunoRepository = alunoRepository;
	}

	public void salvarTudo(TodasInformacoesAlunosDTO todasInformacoesAlunosDTO) {

		Aluno alunoParaSalvar = new Aluno();
		Armario armarioParaSalvar = new Armario();
		CartaoRFID cartaoRFIDParaSalvar = new CartaoRFID();
		Biometria biometriaParaSalvar = new Biometria();

		alunoParaSalvar.setNome(todasInformacoesAlunosDTO.getNome());
		alunoParaSalvar.setMatricula(todasInformacoesAlunosDTO.getMatricula());
		alunoParaSalvar.setCurso(todasInformacoesAlunosDTO.getCurso());

		armarioParaSalvar.setNumero(todasInformacoesAlunosDTO.getNumeroArmario());

		cartaoRFIDParaSalvar.setNumeroCartao(todasInformacoesAlunosDTO.getNumeroCartaoRFID());
		//cartaoRFIDParaSalvar.setAluno();

		biometriaParaSalvar.setNumeroBiometria(todasInformacoesAlunosDTO.getNumeroDaBiometria());

		alunoParaSalvar.setArmario(armarioParaSalvar);
		alunoParaSalvar.setCartaoRFID(cartaoRFIDParaSalvar);
		alunoParaSalvar.setBiometria(biometriaParaSalvar);

		alunoRepository.save(alunoParaSalvar);
	}

	public void editar(TodasInformacoesAlunosDTO todasInformacoesAlunosDTO, Long matriculaId) {

		Optional<Aluno> alunoExistente = alunoRepository.findById(matriculaId);

		if (alunoExistente.isPresent()) {
			Aluno alunoParaEditar = alunoExistente.get();

			// Atualiza os dados do aluno existente
			alunoParaEditar.setNome(todasInformacoesAlunosDTO.getNome());
			alunoParaEditar.setMatricula(todasInformacoesAlunosDTO.getMatricula());
			alunoParaEditar.setCurso(todasInformacoesAlunosDTO.getCurso());

			// Atualiza outros campos, como armário, biometria, etc.

			alunoRepository.save(alunoParaEditar);
		} else {
			// Cria um novo aluno se não existir um com o ID fornecido
			Aluno novoAluno = new Aluno();
			novoAluno.setIdAluno(matriculaId);
			novoAluno.setNome(todasInformacoesAlunosDTO.getNome());
			novoAluno.setMatricula(todasInformacoesAlunosDTO.getMatricula());
			novoAluno.setCurso(todasInformacoesAlunosDTO.getCurso());

			// Define outros campos, como armário, biometria, etc.

			alunoRepository.save(novoAluno);
		}
	}


	public boolean deletarPorNumerodeMatricula(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean editarInformacoes(TodasInformacoesAlunosDTO todasInformacoesAlunosDTO) {
		// TODO Auto-generated method stub
		return false;
	}

}



