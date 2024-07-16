package com.literalura.Literalura.service;

import com.literalura.Literalura.models.Autor;
import com.literalura.Literalura.repositorio.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorServices {

    @Autowired
    private AutorRepository autorRepository;



    public List<Autor> getAutoresVivosPorAno(int year) {
        return autorRepository.findAutorByFecha(year);
    }


}
