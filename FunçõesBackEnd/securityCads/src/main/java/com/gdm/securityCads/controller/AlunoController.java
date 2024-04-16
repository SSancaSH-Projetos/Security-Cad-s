package com.gdm.securityCads.controller;

import java.util.List;
import java.util.Optional;

import com.gdm.securityCads.Service.AlunoService;
import com.gdm.securityCads.dto.TodasInformacoesAlunosDTO;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.repository.AlunoRepository;

import jakarta.persistence.PostUpdate;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private AlunoService alunoService;

	@PostMapping("/inserir")
	public Aluno inserirAluno(@RequestBody Aluno aluno) {
		return alunoRepository.save(aluno);
	}

	@PostMapping("/cadastrarInformacoesAluno")
	public void inserirAlunoCompleto(@RequestBody TodasInformacoesAlunosDTO todasInformacoesAlunosDTO) {
        alunoService.salvarTudo(todasInformacoesAlunosDTO);
    }
	
	@DeleteMapping("/delete/{id}")
	public void deletarPorNumerodeMatricula(@PathVariable String id ) {
		Aluno alunoBuscado = alunoRepository.findByMatricula(id);
		alunoRepository.delete(alunoBuscado);
	}

	 @PutMapping("/aluno/editarinformacoes/{id}")
	    public void editarInformacoes(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
	        Aluno alunoExistente = alunoRepository.findById(id)
	            .orElseThrow();

	        // Atualize os dados do aluno com as informações recebidas no corpo da solicitação
	        alunoExistente.setNome(alunoAtualizado.getNome());
	        alunoExistente.getArmario().setNumero(alunoAtualizado.getArmario().getNumero());
	        alunoExistente.setCurso(alunoAtualizado.getCurso());
	        alunoExistente.setMatricula(alunoAtualizado.getMatricula());
	        alunoExistente.getCartaoRFID().setNumeroCartao(alunoAtualizado.getCartaoRFID().getNumeroCartao());

	        // Salve as alterações no banco de dados
	        alunoRepository.save(alunoExistente);
	    }

}
