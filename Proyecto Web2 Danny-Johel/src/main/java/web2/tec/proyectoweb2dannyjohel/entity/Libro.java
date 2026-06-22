package web2.tec.proyectoweb2dannyjohel.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(nullable = false, length = 150)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Column(nullable = false, length = 100)
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Column(nullable = false, unique = true,length = 13)
    private String isbn;

    @NotNull(message = "La categoria es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Categoria categoria;

    @Min(value = 1, message = "La cantidad total debe ser mayor o igual a 1")
    @Column(nullable = false)
    private int cantidadTotal;

    @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
    @Column(nullable = false)
    private int cantidadDisponible;

    @Column(nullable = false)
    private boolean activo = true;
}
