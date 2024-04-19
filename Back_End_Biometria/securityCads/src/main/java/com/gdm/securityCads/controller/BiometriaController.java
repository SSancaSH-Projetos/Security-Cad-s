package com.gdm.securityCads.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdm.securityCads.model.Biometria;
import com.gdm.securityCads.repository.BiometriaRepository;

@RestController
@RequestMapping("/Biometria")
public class BiometriaController {

    @Autowired
    private BiometriaRepository biometriaRepository; 

    @GetMapping
    public List<Biometria> getAllBiometria() {
        return biometriaRepository.findAll();
    }

    @PostMapping
    public Biometria createBiometria(@RequestBody Biometria biometria) {
        return biometriaRepository.save(biometria);
    }

    @GetMapping("/{id}")
    public Biometria getBiometriaById(@PathVariable long id) {
        return biometriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biometria não encontrada com id: " + id));
    }
}
