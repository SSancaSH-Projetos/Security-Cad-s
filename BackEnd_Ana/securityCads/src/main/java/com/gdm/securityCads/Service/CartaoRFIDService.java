package com.gdm.securityCads.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gdm.securityCads.controller.CartaoRFIDController;
import com.gdm.securityCads.model.CartaoRFID;
import com.gdm.securityCads.repository.CartaoRFIDRepository;

@Service
public class CartaoRFIDService {

	 private final CartaoRFIDRepository cartaoRFIDRepository;

	    public CartaoRFIDService(CartaoRFIDRepository cartaoRFIDRepository) {
	        this.cartaoRFIDRepository = cartaoRFIDRepository;
	    }

	    public List<CartaoRFID> buscarTodosCartoesRFID() {
	        return cartaoRFIDRepository.findAll();
	    }

//	    public CartaoRFID buscarCartaoPorNumero(int numero) {
//	        return (CartaoRFID) cartaoRFIDRepository.findByNumeroCartao(numero);
//	    }

		public List<CartaoRFIDController> getAllCartoesRFID() {
			return null;
			
	
		}
	
}
