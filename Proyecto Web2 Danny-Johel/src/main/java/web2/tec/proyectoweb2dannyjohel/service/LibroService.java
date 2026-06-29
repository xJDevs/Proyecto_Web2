package web2.tec.proyectoweb2dannyjohel.service;

import org.springframework.stereotype.Service;
import web2.tec.proyectoweb2dannyjohel.entity.Categoria;
import web2.tec.proyectoweb2dannyjohel.entity.Libro;
import web2.tec.proyectoweb2dannyjohel.repository.LibroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    // Catalogo completo de libros activos
    public List<Libro> listarCatalogo() {
        return libroRepository.findByActivoTrue();
    }

    // Vista administrativa: incluye libros activos e inactivos para poder reactivarlos.
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    // Solo libros con unidades disponibles
    public List<Libro> listarDisponibles() {
        return libroRepository.findByActivoTrueAndCantidadDisponibleIsGreaterThan(0);
    }

    public List<Libro> listarPorCategoria(Categoria categoria) {
        return libroRepository.findByCategoria(categoria);
    }

    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }

    // Solo el administrador crea libros (el controller valida el rol)
    public Libro crear(Libro libro) {
        if (libroRepository.existsByIsbn(libro.getIsbn())) {
            throw new IllegalArgumentException("Ya existe un libro con ese ISBN");
        }
        libro.setCantidadDisponible(libro.getCantidadTotal());
        libro.setActivo(true);
        return libroRepository.save(libro);
    }

    // Solo el administrador edita libros
    public Libro editar(Long id, Libro datosNuevos) {
        Libro existente = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + id));
        existente.setTitulo(datosNuevos.getTitulo());
        existente.setAutor(datosNuevos.getAutor());
        existente.setIsbn(datosNuevos.getIsbn());
        existente.setCategoria(datosNuevos.getCategoria());

        if (datosNuevos.getCantidadTotal() < existente.getCantidadDisponible()) {
            throw new IllegalArgumentException("La cantidad total no puede ser menor que la cantidad disponible actual");
        }

        existente.setCantidadTotal(datosNuevos.getCantidadTotal());
        return libroRepository.save(existente);
    }

    // Desactiva un libro sin borrarlo de la base de datos
    public void desactivar(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + id));
        libro.setActivo(false);
        libroRepository.save(libro);
    }

    // Reactiva un libro desactivado desde la vista de administracion.
    public void activar(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado: " + id));
        libro.setActivo(true);
        libroRepository.save(libro);
    }

    // Usado internamente por PrestamoService
    public void reducirDisponibilidad(Libro libro) {
        if (libro.getCantidadDisponible() <= 0) {
            throw new IllegalStateException("El libro no tiene unidades disponibles");
        }
        libro.setCantidadDisponible(libro.getCantidadDisponible() - 1);
        libroRepository.save(libro);
    }

    // Usado internamente por PrestamoService al devolver
    public void aumentarDisponibilidad(Libro libro) {
        libro.setCantidadDisponible(libro.getCantidadDisponible() + 1);
        libroRepository.save(libro);
    }
}
