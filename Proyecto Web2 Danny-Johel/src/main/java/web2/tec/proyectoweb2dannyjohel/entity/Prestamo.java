package web2.tec.proyectoweb2dannyjohel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "El libro es obligatorio")
    @ManyToOne
    @JoinColumn(name = "libro_id", nullable = false)
    private Libro libro;

    @NotNull(message = "La fecha de prestamo es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    @NotNull(message = "La fecha de devolucion esperada es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaDevolucionEsperada;

    // null hasta que el usuario devuelve el libro
    private LocalDate fechaDevolucionReal;

    @NotNull(message = "El estado del prestamo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;
}