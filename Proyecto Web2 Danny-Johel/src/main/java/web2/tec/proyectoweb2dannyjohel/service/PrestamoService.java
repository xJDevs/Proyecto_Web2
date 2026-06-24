package web2.tec.proyectoweb2dannyjohel.service;

import org.springframework.stereotype.Service;
import web2.tec.proyectoweb2dannyjohel.entity.EstadoPrestamo;
import web2.tec.proyectoweb2dannyjohel.entity.Libro;
import web2.tec.proyectoweb2dannyjohel.entity.Prestamo;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.repository.PrestamoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroService libroService;

    // Se inyectan dos dependencias: el repositorio y LibroService
    public PrestamoService(PrestamoRepository prestamoRepository, LibroService libroService) {
        this.prestamoRepository = prestamoRepository;
        this.libroService = libroService;
    }

    // Registra un nuevo prestamo aplicando todas las reglas de negocio
    public Prestamo solicitarPrestamo(Usuario usuario, Long libroId, LocalDate fechaDevolucionEsperada) {

        // Regla 1: el libro debe existir
        Libro libro = libroService.buscarPorId(libroId)
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));

        // Regla 2: el libro debe tener unidades disponibles
        if (libro.getCantidadDisponible() <= 0) {
            throw new IllegalStateException("El libro no tiene unidades disponibles");
        }

        // Regla 3: el usuario no puede tener ya un prestamo activo del mismo libro
        boolean yaLoTiene = prestamoRepository.existsByUsuarioIdAndLibroIdAndEstado(
                usuario.getId(), libroId, EstadoPrestamo.ACTIVO);
        if (yaLoTiene) {
            throw new IllegalStateException("Ya tienes un prestamo activo de este libro");
        }

        // Regla 4: la fecha de devolucion debe ser posterior a hoy
        if (fechaDevolucionEsperada == null || !fechaDevolucionEsperada.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de devolucion debe ser posterior a hoy");
        }

        // Construir y guardar el prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionEsperada(fechaDevolucionEsperada);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        // Descontar disponibilidad del libro
        libroService.reducirDisponibilidad(libro);

        return prestamoRepository.save(prestamo);
    }

    // Registra la devolucion de un prestamo activo
    public Prestamo devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("Prestamo no encontrado"));

        // Regla: un prestamo devuelto no puede devolverse de nuevo
        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO) {
            throw new IllegalStateException("Este prestamo no esta activo");
        }

        prestamo.setFechaDevolucionReal(LocalDate.now());
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        // Devolver la unidad al inventario del libro
        libroService.aumentarDisponibilidad(prestamo.getLibro());

        return prestamoRepository.save(prestamo);
    }

    // Historial personal: el usuario solo ve sus propios prestamos
    public List<Prestamo> historialDeUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    // Solo el administrador ve todos los prestamos
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    public List<Prestamo> listarActivos() {
        return prestamoRepository.findByEstado(EstadoPrestamo.ACTIVO);
    }

    public Optional<Prestamo> buscarPorId(Long id) {
        return prestamoRepository.findById(id);
    }
}