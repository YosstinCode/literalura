package com.literalura.Literalura.repositorio;

import com.literalura.Literalura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findAutorByNombre(String nombre);

    @Query(value = "SELECT a FROM Autor a WHERE a.fechaDefuncion >:year AND a.fechaNacimiento <:year")
    List<Autor> findAutorByFecha(int year);
}