package com.tralaleritos.inventario;

import org.springframework.boot.SpringApplication; // <<--- ESTE IMPORT
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventarioApplication.class, args);
		System.out.println("Aplicación de Inventario iniciada!");
	}

}