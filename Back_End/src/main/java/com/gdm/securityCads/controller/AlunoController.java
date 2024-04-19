package com.gdm.securityCads.controller;

import java.util.List;
import java.util.Optional;

import com.gdm.securityCads.Service.AlunoService;
import com.gdm.securityCads.dto.TodasInformacoesAlunosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncWebRequest;

import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.repository.AlunoRepository;

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
	
	  @DeleteMapping("/aluno/delete/{matricula}")
	    public void excluirAluno(@PathVariable String matricula) {
		Aluno alunoBuscado = (Aluno) alunoRepository.findByMatricula(matricula);
		alunoRepository.delete(alunoBuscado);
	}

	
	  @PutMapping("/editarInformacoes")
	    public String editarInformacoes(@RequestBody Aluno aluno) {
	        if (aluno.getId() == null || !aluno.containsKey(aluno.getId())) {
	            return "Aluno não encontrado!";
	        }
			return null;
	  }
}
