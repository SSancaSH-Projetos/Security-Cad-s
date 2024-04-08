package com.gdm.securityCads.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdm.securityCads.model.CartaoRFID;

public interface CartaoRFIDRepository  extends JpaRepository <CartaoRFID, Long>{

//	List<CartaoRFID> findAllByOrderByNumeroCartaoAsc();
//
//    List<CartaoRFID> findByNumeroCartao(int numero);


}
