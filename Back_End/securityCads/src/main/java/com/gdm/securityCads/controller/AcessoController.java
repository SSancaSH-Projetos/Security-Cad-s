package com.gdm.securityCads.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdm.securityCads.model.Acesso;
import com.gdm.securityCads.repository.AcessoRepository;

@RestController
@RequestMapping("/acesso")
public class AcessoController {
	@Autowired
	private AcessoRepository acessoRepositry;

@GetMapping
public List<Acesso> getAllAcesso(){
	return acessoRepositry.findAll();
	
}

@PostMapping
public Acesso createAcesso(@RequestBody Acesso acesso) {
	return acessoRepositry.save(acesso);
	
}
@GetMapping("/{id}")
public Optional<Acesso> getAcessoById(@PathVariable Long id) {
	return Optional.ofNullable(acessoRepositry.findById(id)
.orElseThrow(()-> new RuntimeException("Acesso não encontrado com id : " + id )));
	
}
}
