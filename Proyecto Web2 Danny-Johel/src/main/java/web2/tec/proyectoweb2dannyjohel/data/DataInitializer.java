package web2.tec.proyectoweb2dannyjohel.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import web2.tec.proyectoweb2dannyjohel.entity.Categoria;
import web2.tec.proyectoweb2dannyjohel.entity.Libro;
import web2.tec.proyectoweb2dannyjohel.entity.Rol;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.repository.LibroRepository;
import web2.tec.proyectoweb2dannyjohel.repository.UsuarioRepository;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public DataInitializer(UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
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

        if (libroRepository.count() == 0) {

            List<Libro> libros = List.of(

                    // Sus libros
                    crearLibro("Cien anos de soledad", "Gabriel Garcia Marquez", "9780060883287", Categoria.NOVELA, 3),
                    crearLibro("El principito", "Antoine de Saint-Exupery", "9780156012195", Categoria.INFANTIL, 4),
                    crearLibro("Don Quijote de la Mancha", "Miguel de Cervantes", "9788420412146", Categoria.NOVELA, 2),
                    crearLibro("Veinte poemas de amor y una cancion desesperada", "Pablo Neruda", "9788420632765", Categoria.POESIA, 3),
                    crearLibro("Leaves of Grass", "Walt Whitman", "9780140421353", Categoria.POESIA, 2),
                    crearLibro("Matilda", "Roald Dahl", "9780142410370", Categoria.INFANTIL, 3),

                    // Libros populares adicionales
                    crearLibro("1984", "George Orwell", "9780451524935", Categoria.CIENCIA_FICCION, 4),
                    crearLibro("Brave New World", "Aldous Huxley", "9780060850524", Categoria.CIENCIA_FICCION, 3),
                    crearLibro("Fahrenheit 451", "Ray Bradbury", "9781451673319", Categoria.CIENCIA_FICCION, 3),
                    crearLibro("El senor de los anillos", "J.R.R. Tolkien", "9780261102354", Categoria.FANTASIA, 2),
                    crearLibro("Harry Potter y la piedra filosofal", "J.K. Rowling", "9788478884452", Categoria.FANTASIA, 5),
                    crearLibro("Crimen y castigo", "Fiodor Dostoievski", "9788491051459", Categoria.NOVELA, 2),
                    crearLibro("Anna Karenina", "Leon Tolstoi", "9788491051510", Categoria.NOVELA, 2),
                    crearLibro("Sapiens", "Yuval Noah Harari", "9780062316097", Categoria.HISTORIA, 4),
                    crearLibro("El arte de la guerra", "Sun Tzu", "9788497595841", Categoria.HISTORIA, 3),
                    crearLibro("Una breve historia del tiempo", "Stephen Hawking", "9780553380163", Categoria.EDUCACION, 3),
                    crearLibro("El codigo Da Vinci", "Dan Brown", "9780307474278", Categoria.NOVELA, 3),
                    crearLibro("It", "Stephen King", "9781501156700", Categoria.TERROR, 2),
                    crearLibro("El resplandor", "Stephen King", "9780307743657", Categoria.TERROR, 2),
                    crearLibro("Fundacion", "Isaac Asimov", "9780553293357", Categoria.CIENCIA_FICCION, 3)
            );

            libroRepository.saveAll(libros);
            System.out.println("20 libros iniciales creados");
        }
    }

    private Libro crearLibro(String titulo, String autor, String isbn, Categoria categoria, int cantidad) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setIsbn(isbn);
        libro.setCategoria(categoria);
        libro.setCantidadTotal(cantidad);
        libro.setCantidadDisponible(cantidad);
        libro.setActivo(true);
        return libro;
    }
}