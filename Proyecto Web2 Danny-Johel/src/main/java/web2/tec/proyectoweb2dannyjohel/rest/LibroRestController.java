package web2.tec.proyectoweb2dannyjohel.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web2.tec.proyectoweb2dannyjohel.entity.Libro;
import web2.tec.proyectoweb2dannyjohel.service.LibroService;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroRestController {

    private final LibroService libroService;

    public LibroRestController(LibroService libroService) {
        this.libroService = libroService;
    }

    // GET /api/libros - retorna todos los libros activos en formato JSON
    @GetMapping
    public List<Libro> listarTodos() {
        return libroService.listarCatalogo();
    }

    // GET /api/libros/disponibles - retorna solo libros con unidades disponibles
    @GetMapping("/disponibles")
    public List<Libro> listarDisponibles() {
        return libroService.listarDisponibles();
    }
}