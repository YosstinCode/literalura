package com.literalura.Literalura.principal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.literalura.Literalura.models.Autor;
import com.literalura.Literalura.models.DatosAutor;
import com.literalura.Literalura.models.DatosLibro;
import com.literalura.Literalura.models.Libro;
import com.literalura.Literalura.models.Idioma;
import com.literalura.Literalura.repositorio.AutorRepository;
import com.literalura.Literalura.repositorio.LibroRepository;
import com.literalura.Literalura.service.ConsumoApi;
import com.literalura.Literalura.service.ConvierteDatos;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MenuPrincipal {
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final Scanner teclado = new Scanner(System.in);
    private final ConvierteDatos convierteDatos = new ConvierteDatos();
    private DatosLibro datosLibro;
    private DatosAutor datosAutor;

    private Autor autorDelLibro;
    private int opciones = -1;

    private List<Autor> autores;
    private List<Libro> libros;

    public MenuPrincipal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraElMenu() throws Exception {
        final String textoMenu = """
                \n===========================
                =   M E N U  P R I N C I P A L   =
                =    L I T E R  A L U R A     =
                ===========================

                [ 1 ] Buscar libro por título
                [ 2 ] Listar libros registrados
                [ 3 ] Listar autores registrados
                [ 4 ] Listar autores vivos en un determinado año
                [ 5 ] Listar libros por idioma
                [ 6 ] Top 5 libros más descargados
                [ 7 ] Listado de libros por autor
                
                [ 0 ] Salir

                ===========================
                Elija una opción a través del número correspondiente:
                ===========================
                """;

        do {
            System.out.print(textoMenu);
            opciones = teclado.nextInt();
            teclado.nextLine();

            switch (opciones) {
                case 1 -> obtenerLibroPorTitulo();
                case 2 -> listarLibrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosPorAno();
                case 5 -> listarLibrosPorIdioma();
                case 6 -> top10LibrosDescargados();
                case 7 -> listadoDeLibrosPorAutor();
                case 0 -> System.out.println("\n¡Hasta luego!");
                default -> System.out.println("\nOpción inválida, por favor intente nuevamente.");
            }
        } while (opciones != 0);
    }

    private void obtenerLibroPorTitulo() throws Exception {
        busquedaDeLibros();
    }

    private void listarLibrosRegistrados() {
        System.out.println("\nListado de libros registrados:");
        libros = libroRepository.findAll();
        libros.forEach(System.out::println);
        hacerPause();
    }

    private void listarAutoresRegistrados() {
        System.out.println("\nListado de autores registrados:");
        autores = autorRepository.findAll();
        autores.forEach(System.out::println);
        hacerPause();
    }

    private void listarAutoresVivosPorAno() {
        System.out.print("\nEscriba el año que desea buscar: ");
        var ano = teclado.nextInt();

        autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores vivos en ese año.");
        } else {
            System.out.println("\nListado de autores vivos en el año " + ano + ":");
            autores = autorRepository.findAutorByFecha(ano);
            autores.forEach(System.out::println);
        }
        hacerPause();
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\nListado de idiomas registrados:");
        var listaDeIdiomas = libroRepository.findDistinctIdiomas();
        for (int i = 0; i < listaDeIdiomas.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + Idioma.from(listaDeIdiomas.get(i)));
        }
        System.out.print("\nSeleccione el número del idioma que desea buscar: ");
        var eleccion = teclado.nextInt();
        if (eleccion < 1 || eleccion > listaDeIdiomas.size()) {
            System.out.println("\nOpción inválida.");
            return;
        }
        String idiomaSeleccionado = Idioma.from(listaDeIdiomas.get(eleccion - 1)).toString();
        System.out.println("\nListado de libros en [ " + idiomaSeleccionado.toUpperCase() + " ]:");
        libros = libroRepository.findByIdioma(listaDeIdiomas.get(eleccion - 1));
        libros.forEach(System.out::println);
        hacerPause();
    }

    private void top10LibrosDescargados() {
        System.out.println("\nTop 5 libros más descargados:\n");
        libros = libroRepository.findTop10Descargados();
        libros.forEach(System.out::println);
        hacerPause();
    }

    private void listadoDeLibrosPorAutor() {
        autores = autorRepository.findAll();
        System.out.println("\nListado de autores registrados:");
        for (int i = 0; i < autores.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + autores.get(i).getNombre());
        }

        System.out.print("\nEscriba el número del autor que desea buscar: ");
        var eleccion = teclado.nextInt();
        if (eleccion < 1 || eleccion > autores.size()) {
            System.out.println("\nOpción inválida.");
            return;
        }
        System.out.println("\nListado de libros de: " + autores.get(eleccion - 1).getNombre());
        libros = libroRepository.findLibrosByAutor(autores.get(eleccion - 1));
        libros.forEach(System.out::println);
        hacerPause();
    }

    public void hacerPause() {
        System.out.println();
        Terminal terminal;
        try {
            terminal = TerminalBuilder.builder().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).build();

        System.out.println("Presione cualquier tecla para continuar...");
        lineReader.readLine();
    }

    private void busquedaDeLibros() throws Exception {
        System.out.print("Escriba el título del libro que desea buscar: ");
        var tituloLibro = teclado.nextLine();

        var resultadoBusqueda = new ConsumoApi().buscarLibro(tituloLibro);
        JSONObject jsonObject = new JSONObject(resultadoBusqueda);
        JSONArray resultsArray = jsonObject.getJSONArray("results");

        if (resultsArray.isEmpty()) {
            System.out.println("\nLibro no encontrado con el título: " + tituloLibro);
            return;
        }

        System.out.println("\nSe encontraron " + resultsArray.length() + " libros:");
        for (int i = 0; i < resultsArray.length(); i++) {
            System.out.println("[" + (i + 1) + "] " + resultsArray.getJSONObject(i).getString("title"));
        }

        System.out.print("\nSeleccione el libro deseado indicando su número, o presione 0 para cancelar: ");
        var numeroLibro = teclado.nextInt();
        if (numeroLibro == 0) {
            return;
        }
        numeroLibro = numeroLibro - 1;

        jsonObject = new JSONObject(resultsArray.getJSONObject(numeroLibro).toString());
        datosLibro = convierteDatos.obtenerDatos(jsonObject.toString(), DatosLibro.class);

        // Verificar si el libro ya está registrado
        Optional<Libro> libro = libroRepository.findById(datosLibro.idLibro());
        if (libro.isPresent()) {
            System.out.println("\nEl libro ya está registrado:");
            System.out.println(libro.get());
            hacerPause();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> jsonMap = mapper.readValue(jsonObject.toString(), Map.class);
        @SuppressWarnings("unchecked")
        String authorsJson = mapper.writeValueAsString((List<Map<String, Object>>) jsonMap.get("authors"));
        String resultado = authorsJson.substring(1, authorsJson.length() - 1);
        datosAutor = convierteDatos.obtenerDatos(resultado, DatosAutor.class);

        String idioma = new ConsumoApi().getIdioma(jsonMap);

        autores = autorRepository.findAll();
        Optional<Autor> autor = autores.stream()
                .filter(a -> a.getNombre().equals(datosAutor.nombre()))
                .findFirst();
        if (autor.isPresent()) {
            autorDelLibro = autor.get();
        } else {
            autorDelLibro = new Autor(datosAutor);
            autorRepository.save(autorDelLibro);
        }
        Libro libroNuevo = new Libro(datosLibro, autorDelLibro, idioma);
        libroRepository.save(libroNuevo);
        System.out.println("\nLibro registrado exitosamente:");
        System.out.println(libroNuevo);
        hacerPause();
    }
}
