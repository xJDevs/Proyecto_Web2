package web2.tec.proyectoweb2dannyjohel.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import web2.tec.proyectoweb2dannyjohel.entity.Rol;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;

    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {

            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setCorreo("admin@biblioteca.com");
            admin.setPassword("admin123");
            admin.setRol(Rol.ADMINISTRADOR);
            admin.setActivo(true);
            usuarioRepository.save(admin);

            Usuario usuario = new Usuario();
            usuario.setNombre("Usuario Prueba");
            usuario.setCorreo("usuario@biblioteca.com");
            usuario.setPassword("usuario123");
            usuario.setRol(Rol.USUARIO);
            usuario.setActivo(true);
            usuarioRepository.save(usuario);

            System.out.println("Usuarios iniciales creados");
        }
    }
}