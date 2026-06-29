package web2.tec.proyectoweb2dannyjohel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.tec.proyectoweb2dannyjohel.entity.Categoria;
import web2.tec.proyectoweb2dannyjohel.entity.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    List<Libro> findByTitulo(String titulo);

    List<Libro> findByAutor(String autor);

    Optional<Libro> findByIsbn(String isbn); // optional porque isbn es unico, o devuelve algo o nada
    boolean existsByIsbn(String isbn);

    List<Libro> findByCategoria(Categoria categoria);

    List<Libro> findByActivoTrue(); // libros visibles en catalogo

    List<Libro> findByActivoTrueAndCantidadDisponibleIsGreaterThan(int cantidad);
    // queremos solamente libros activos y disponibles en el sistema

}
