package com.gdm.securityCads.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdm.securityCads.model.Acesso;
import com.gdm.securityCads.model.Aluno;
import com.gdm.securityCads.model.CartaoRFID;
import com.gdm.securityCads.repository.AcessoRepository;

@Service
public class AcessoService {
	@Autowired
    private final AcessoRepository acessoRepository;

    public AcessoService(AcessoRepository acessoRepository) {
        this.acessoRepository = acessoRepository;
    }

    public List<Acesso> buscarTodosAcessos() {
        return acessoRepository.findAll();
    }

    public List<Acesso> buscarAcessosPorAluno(Aluno aluno) {
        return acessoRepository.findByAluno(aluno);
    }

    public void registrarAcesso(Aluno aluno, CartaoRFID cartao) {
        Acesso acesso = new Acesso();
        acesso.setAluno(aluno);
        acesso.setCartaoRFID(cartao);

        acesso.setDataHoraAcesso();
        acessoRepository.save(acesso);
    }
}
