package com.gdm.securityCads.controller;

import java.util.List;
import java.util.Optional;

import com.gdm.securityCads.Service.AlunoService;
import com.gdm.securityCads.dto.TodasInformacoesAlunosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

	@DeleteMapping("/delete/{id}")
	public void deletarPorNumerodeMatricula(@PathVariable String id) {
		Aluno alunoBuscado = alunoRepository.findByMatricula(id);
		alunoRepository.delete(alunoBuscado);
	}

	@PutMapping("/editarAluno/{matriculaId}")
	public void editarInformacoes(@RequestBody TodasInformacoesAlunosDTO todasInformacoesAlunosDTO,
								  @PathVariable Long matriculaId) {

		alunoService.editar(todasInformacoesAlunosDTO, matriculaId);

	}

}
