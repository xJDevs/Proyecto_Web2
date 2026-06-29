package web2.tec.proyectoweb2dannyjohel.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web2.tec.proyectoweb2dannyjohel.entity.Categoria;
import web2.tec.proyectoweb2dannyjohel.entity.Libro;
import web2.tec.proyectoweb2dannyjohel.entity.Rol;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.service.LibroService;

@Controller
@RequestMapping("/libro")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // Catalogo visible para todos los usuarios autenticados
    @GetMapping("/catalogo")
    public String catalogo(Model model, HttpSession session) {
        if (session.getAttribute("usuarioActual") == null) {
            return "redirect:/usuario/login";
        }
        // Admin ve activos e inactivos; usuario normal solo ve libros activos.
        if (esAdmin(session)) {
            model.addAttribute("libros", libroService.listarTodos());
        } else {
            model.addAttribute("libros", libroService.listarCatalogo());
        }
        model.addAttribute("categorias", Categoria.values());
        return "libro/catalogo";
    }

    // Formulario para crear libro - solo admin
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }
        model.addAttribute("libro", new Libro());
        model.addAttribute("categorias", Categoria.values());
        return "libro/formulario";
    }

    // Procesa la creacion de un libro - solo admin
    @PostMapping("/nuevo")
    public String crear(@Valid @ModelAttribute Libro libro,
                        BindingResult bindingResult,
                        Model model,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }

        // Si fallan las validaciones de Libro, se vuelve al formulario sin guardar
        if (bindingResult.hasErrors()) {
            model.addAttribute("categorias", Categoria.values());
            return "libro/formulario";
        }

        try {
            libroService.crear(libro);
            redirectAttributes.addFlashAttribute("mensaje", "Libro creado exitosamente");
            return "redirect:/libro/catalogo";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/libro/nuevo";
        }
    }

    // Formulario para editar libro - solo admin
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id,
                                          Model model,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }
        return libroService.buscarPorId(id)
                .map(libro -> {
                    model.addAttribute("libro", libro);
                    model.addAttribute("categorias", Categoria.values());
                    return "libro/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Libro no encontrado");
                    return "redirect:/libro/catalogo";
                });
    }

    // Procesa la edicion de un libro - solo admin
    @PostMapping("/editar/{id}")
    public String editar(@PathVariable("id") Long id,
                         @Valid @ModelAttribute Libro libro,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }

        // Conserva el id de la URL para que el formulario siga en modo edicion
        if (bindingResult.hasErrors()) {
            libro.setId(id);
            model.addAttribute("categorias", Categoria.values());
            return "libro/formulario";
        }

        try {
            libroService.editar(id, libro);
            redirectAttributes.addFlashAttribute("mensaje", "Libro actualizado exitosamente");
            return "redirect:/libro/catalogo";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/libro/catalogo";
        }
    }

    // Desactiva un libro - solo admin
    @PostMapping("/desactivar/{id}")
    public String desactivar(@PathVariable("id") Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }
        try {
            libroService.desactivar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Libro desactivado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/libro/catalogo";
    }

    // Reactiva un libro - solo admin
    @PostMapping("/activar/{id}")
    public String activar(@PathVariable("id") Long id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }
        try {
            libroService.activar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Libro activado exitosamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/libro/catalogo";
    }

    // Metodo auxiliar para verificar si el usuario en sesion es administrador
    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        return usuario != null && usuario.getRol() == Rol.ADMINISTRADOR;
    }
}
