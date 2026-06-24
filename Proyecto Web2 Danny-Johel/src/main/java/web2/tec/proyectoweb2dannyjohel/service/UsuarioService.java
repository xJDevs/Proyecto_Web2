package web2.tec.proyectoweb2dannyjohel.service;

import org.springframework.stereotype.Service;
import web2.tec.proyectoweb2dannyjohel.entity.Rol;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Spring inyecta el repositorio automaticamente por constructor
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Registra un nuevo usuario con rol USUARIO por defecto
    public Usuario registrar(String nombre, String correo, String password) {
        if (usuarioRepository.existsByCorreo(correo)) {
            throw new IllegalArgumentException("El correo ya esta registrado: " + correo);
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPassword(password);
        usuario.setRol(Rol.USUARIO);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    // Valida credenciales para login
    public Optional<Usuario> login(String correo, String password) {
        return usuarioRepository.findByCorreo(correo)
                .filter(u -> u.isActivo())
                .filter(u -> u.getPassword().equals(password));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
