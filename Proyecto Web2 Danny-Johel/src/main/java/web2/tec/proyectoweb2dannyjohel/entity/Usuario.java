package web2.tec.proyectoweb2dannyjohel.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // permite definir el tipo de dato del enum
import jakarta.persistence.Enumerated; // para guardar enums
import jakarta.persistence.GeneratedValue; // ID generado auto
import jakarta.persistence.GenerationType; // para definir id autoincremental
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank; // string no sea null, ni vacío, ni solo espacios
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato valido")
    @Column(nullable = false, unique = true, length = 150)
    private String correo;

    @NotBlank(message = "La contrasena es obligatoria")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(nullable = false)
    private boolean activo = true;
}


