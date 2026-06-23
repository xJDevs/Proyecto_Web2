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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // autogenerado por la db gracias a IDENTITY
    private Long id;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne // relacion muchos a uno: muchos prestamos pueden estar asociados a un mismo usuario
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotEmpty(message = "Debe existir al menos una linea de prestamo") // validamos que la lista no este vacia, ya que Prestamo contiene 1 o varias LP
    @OneToMany(mappedBy = "prestamo") // mapped indica que la relacion la maneja el campo "prestamo" dentro LP
    private List<LineaPrestamo> lineasPrestamo = new ArrayList<>();

    @NotNull(message = "La fecha de prestamo es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    @NotNull(message = "La fecha de devolucion esperada es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaDevolucionEsperada;

    private LocalDate fechaDevolucionReal;

    @NotNull(message = "El estado del prestamo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;
}
