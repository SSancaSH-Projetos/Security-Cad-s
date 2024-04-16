package com.gdm.securityCads.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdm.securityCads.model.CartaoRFID;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoRFIDRepository  extends JpaRepository <CartaoRFID, Long>{

	List<CartaoRFID> findByNumeroCartao(String numeroCartao);

//	List<CartaoRFID> findAllByOrderByNumeroCartaoAsc();
//
//    List<CartaoRFID> findByNumeroCartao(int numero);


}
