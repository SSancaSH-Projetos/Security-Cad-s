package com.gdm.securityCads.controller;

import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.model.Armario;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gdm.securityCads.Service.ArmarioService;

@RestController
@RequestMapping("/armario")
public class ArmarioController {


	@Autowired
	private ArmarioService armarioService;

	@GetMapping
	public List getAllArmarios() {
		return armarioService.getAllArmarios();
	}

	@PostMapping("/inserir")
	public void inserirArmario(@RequestBody Armario armario) {
		armarioService.salvarArmario(armario);
	}


}


	

