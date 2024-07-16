package com.literalura.Literalura.models;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") int fechaNacimiento,
        @JsonAlias("death_year") int fechaDefuncion
) {
}
