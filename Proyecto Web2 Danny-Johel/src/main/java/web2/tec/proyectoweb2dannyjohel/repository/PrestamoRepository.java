package web2.tec.proyectoweb2dannyjohel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.tec.proyectoweb2dannyjohel.entity.EstadoPrestamo;
import web2.tec.proyectoweb2dannyjohel.entity.Prestamo;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    boolean existsByUsuarioIdAndLibroIdAndEstado(Long usuarioId, Long libroId, EstadoPrestamo estado);

}
