package com.literalura.Literalura.dto;

import com.literalura.Literalura.models.Autor;

public record LibroDTO(int idLibro,
                       String titulo,
                       Autor autor,
                       String idioma,
                       int numeroDeDescargas
) {
}

