package com.gdm.securityCads.controller;

import java.util.List;

import com.gdm.securityCads.Service.AcessoService;
import com.gdm.securityCads.Service.CartaoRFIDService;
import com.gdm.securityCads.Service.RelatorioService;
import com.gdm.securityCads.model.Acesso;
import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.model.CartaoRFID;
import com.gdm.securityCads.model.Relatorio;
import com.gdm.securityCads.repository.AcessoRepository;
import com.gdm.securityCads.repository.AlunoRepository;
import com.gdm.securityCads.repository.CartaoRFIDRepository;
import com.gdm.securityCads.repository.RelatorioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

	@Autowired
	AlunoRepository alunoRepository;
	@Autowired
	AcessoRepository acessoRepository;
	@Autowired
	CartaoRFIDRepository cartaoRFIDRepository;

	@Autowired
	private RelatorioService relatorioService;

	@Autowired
	private AcessoService acessoService;
	@Autowired
	private CartaoRFIDService cartaoRFIDService;

	@GetMapping("/pegarTodos")
	public List<Aluno> getAllRelatorio() {
		return alunoRepository.findAll();
	}

	@GetMapping("pegarPorNome")
	public List<Relatorio> getByName() {
		return RelatorioRepository.findByNome();
	}

	@GetMapping("/FiltrarPorNomeDoCurso")
	public List<Acesso> getAllAcesso() {
		return acessoRepository.findAll();
	}

	@GetMapping("/filtrarPorNumeroDeMatricula")
	public List<Aluno> filterByMatricula(@RequestParam("matricula") String matricula) {
		return alunoRepository.findByMatricula(matricula);

	}

	@GetMapping("/filtrarPorCartaoRFID")
	public List<CartaoRFID> getByCartaoRFID() {
		return cartaoRFIDRepository.findAll();

	}

	@GetMapping("/filtrarPorCartaoRFIDS")
	public List<CartaoRFID> getByFilterNumCartaoRFID(@RequestParam("numeroCartao") String numeroCartao) {
		return cartaoRFIDRepository.findByNumeroCartao(numeroCartao);

	}


	@GetMapping("/filtrarPorNome")
	public List<Aluno> getByFilterNome(@RequestParam("nome") String nome) {
		return alunoRepository.findByNome(nome);
	}


}
