package com.gdm.securityCads.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdm.securityCads.model.CartaoRFID;
import com.gdm.securityCads.repository.CartaoRFIDRepository;

@RestController
@RequestMapping("/CartaoRFID")
public class CartaoRFIDController {

	
@Autowired
private CartaoRFIDRepository cartaoRFIDRepository ;

@GetMapping
public List<CartaoRFID>getALLCartoesRFID(){
	return cartaoRFIDRepository.findAll();
	
}
@PostMapping
public CartaoRFID createCartaoRFID(@RequestBody CartaoRFID cartaoRFID) {
	return cartaoRFIDRepository.save(cartaoRFID);

}
@GetMapping("/{id}")
public CartaoRFID getCartaoRFIDByID(@PathVariable long id) {
return cartaoRFIDRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Cartao RFID nao encontrado com id:"+ id));

}
}