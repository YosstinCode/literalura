package com.literalura.Literalura.models;

import jakarta.persistence.*;
import jdk.jfr.Name;

@Entity
@Table(name = "libro")
public class Libro {
    @Id
    private long idLibro;
    @Column(unique = true)
    @Name(value = "titulo")
    private String titulo;
    private  String idioma;
    @ManyToOne
    @JoinColumn(name = "idAutor")
    private Autor autor;
    private int totalDeDescargas;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro, Autor autor, String idioma) {
        this.idLibro = datosLibro.idLibro();
        this.titulo = datosLibro.titulo();
        this.totalDeDescargas = datosLibro.totalDedescargas();
        this.autor = autor;
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getTotalDeDescargas() {
        return totalDeDescargas;
    }

    public void setTotalDeDescargas(int totalDeDescargas) {
        this.totalDeDescargas = totalDeDescargas;
    }

     @Override
    public String toString() {
        String newLibro= "------- LIBRO -------\n" +
                         "Título: " + titulo + "\n" +
                         "Autor: " + autor.getNombre() + "\n" +
                         "Idioma: " + Idioma.from(idioma)+ "\n" +
                         "Numero de descargas: " + totalDeDescargas + "\n";

        return newLibro;
    }
}
