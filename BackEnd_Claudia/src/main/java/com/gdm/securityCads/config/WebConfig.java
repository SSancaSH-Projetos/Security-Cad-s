package com.gdm.securityCads.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
	   @Override
	    public void addCorsMappings(CorsRegistry registry) {
	        registry.addMapping("/**")
	                .allowedOrigins("*") // Permitir solicitações de todas as origens
	                .allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos HTTP permitidos
	                .allowedHeaders("*"); // Headers permitidos
	    }
	}


