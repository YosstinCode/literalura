package com.literalura.Literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.literalura.Literalura.principal.MenuPrincipal;
import com.literalura.Literalura.repositorio.AutorRepository;
import com.literalura.Literalura.repositorio.LibroRepository;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MenuPrincipal menuPrincipal = new MenuPrincipal(libroRepository, autorRepository);
		menuPrincipal.muestraElMenu();
	}

}
