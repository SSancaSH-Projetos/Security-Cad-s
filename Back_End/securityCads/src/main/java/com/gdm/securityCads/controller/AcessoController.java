package com.gdm.securityCads.controller;

import java.util.ArrayList;
import java.util.List;

import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/acesso")
public class AcessoController {

	@Autowired
	private AlunoRepository alunoRepository;

	@GetMapping("/consultar/{numeroArmario}/{rfid}")
	public ResponseEntity<String> consultarAcesso(@PathVariable("numeroArmario") String numeroArmario,
												  @PathVariable("rfid") String rfid) {

		List<Aluno> alunosNaBase = alunoRepository.findAll();

		for (Aluno aluno : alunosNaBase) {
			if (aluno.getArmario().getNumero() == Integer.parseInt(numeroArmario) &&
					aluno.getCartaoRFID().getNumeroCartao().equals(rfid)) {
				System.out.println("Liberado o Acesso!");
				return ResponseEntity.ok("ok");
			}
		}

		System.out.println("Acesso negado!");
		return ResponseEntity.notFound().build();
	}

	private final List<String> rfidList = new ArrayList<>();

	@PostMapping("/armazenar")
	public ResponseEntity<String> armazenarRFID(@RequestBody String rfid) {
		try {

			rfidList.add(rfid);

			return ResponseEntity.ok("RFID armazenado com sucesso!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao armazenar o RFID: " + e.getMessage());
		}
	}

	@GetMapping("/ultimoRFID")
	public ResponseEntity<String> obterUltimoRFID() {
		try {
			if (!rfidList.isEmpty()) {
				String ultimoRFID = rfidList.get(rfidList.size() - 1);
				return ResponseEntity.ok(ultimoRFID);
			} else {
				return ResponseEntity.ok("Nenhum RFID armazenado ainda.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao obter o último RFID: " + e.getMessage());
		}
	}


	@GetMapping("/consultar/biometria/{numeroArmario}/{numeroBiometria}")
	public ResponseEntity<String> consultarBiometria(@PathVariable("numeroArmario") String numeroArmario,
													 @PathVariable("numeroBiometria") String biometria){
		List<Aluno> alunosNaBase = alunoRepository.findAll();

		for (Aluno aluno : alunosNaBase) {
			if (aluno.getArmario().getNumero() == Integer.parseInt(numeroArmario) &&
					aluno.getBiometria().getNumeroBiometria().equals(biometria)) {
				System.out.println("Acesso Liberado biometria!");
				return ResponseEntity.ok("ok");
			}
		}
		System.out.println("Acesso Negado biometria!");
		return ResponseEntity.notFound().build();
	}


	private boolean cadastroIniciado = false;
	private Integer posicaoRecebida = -1;

	@PostMapping("/iniciarCadastroBiometria")
	public Integer iniciaCadastroBiometria(@RequestBody Integer posicao) {
		// Exibe a flag de iniciar cadastro no console
		System.out.println("Iniciar cadastro: " + cadastroIniciado);
				cadastroIniciado = true;
				posicaoRecebida = posicao;
				return posicaoRecebida;
	}

	@GetMapping("/pegarPosicaoBiometria")
	public Integer pegarPosicaoBiometria() {
		return posicaoRecebida;
	}

	@GetMapping("/modoCadastroIniciado")
	public boolean modoCadastroIniciado() {
		return cadastroIniciado;
	}

	@PostMapping("/encerrarCadastroBiometria")
	public boolean  encerrarCadastroBiometria(@RequestBody boolean cadastroEncerrado) {
		cadastroIniciado = cadastroEncerrado;
		return cadastroIniciado;
	}

	@GetMapping("/verificarCadastroBiometria")
	public boolean verificarCadastro() {
		return cadastroIniciado;
	}

}
