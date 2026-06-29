package web2.tec.proyectoweb2dannyjohel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web2.tec.proyectoweb2dannyjohel.entity.Rol;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.service.LibroService;
import web2.tec.proyectoweb2dannyjohel.service.PrestamoService;
import web2.tec.proyectoweb2dannyjohel.entity.Prestamo;

import java.time.LocalDate;

@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;

    public PrestamoController(PrestamoService prestamoService, LibroService libroService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
    }

    // Lista todos los prestamos activos - solo admin
    @GetMapping("/activos")
    public String listarActivos(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }
        model.addAttribute("prestamos", prestamoService.listarActivos());
        return "prestamo/activos";
    }

    // Historial completo - solo admin
    @GetMapping("/historial")
    public String historialCompleto(Model model, HttpSession session) {
        if (!esAdmin(session)) {
            return "redirect:/libro/catalogo";
        }
        model.addAttribute("prestamos", prestamoService.listarTodos());
        return "prestamo/historial";
    }

    // Historial personal del usuario autenticado
    @GetMapping("/mi-historial")
    public String miHistorial(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("prestamos", prestamoService.historialDeUsuario(usuario.getId()));
        return "prestamo/mi-historial";
    }

    // Formulario para solicitar prestamo
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioActual") == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("libros", libroService.listarDisponibles());
        return "prestamo/formulario";
    }

    // Procesa la solicitud de prestamo
    @PostMapping("/nuevo")
    public String solicitar(@RequestParam("libroId") Long libroId,
                            @RequestParam("fechaDevolucionEsperada") LocalDate fechaDevolucionEsperada,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }
        try {
            prestamoService.solicitarPrestamo(usuario, libroId, fechaDevolucionEsperada);
            redirectAttributes.addFlashAttribute("mensaje", "Prestamo registrado exitosamente");
            return "redirect:/prestamo/mi-historial";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/prestamo/nuevo";
        }
    }

    // Procesa la devolucion de un prestamo
    @PostMapping("/devolver/{id}")
    public String devolver(@PathVariable("id") Long id,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        if (usuario == null) {
            return "redirect:/usuario/login";
        }

        try {
            // Se carga el prestamo antes de devolverlo para validar quien lo esta solicitando
            Prestamo prestamo = prestamoService.buscarPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Prestamo no encontrado"));

            // Un usuario normal solo puede devolver prestamos propios; el admin puede devolver cualquiera
            if (!esAdmin(session) && !prestamo.getUsuario().getId().equals(usuario.getId())) {
                throw new IllegalStateException("No se puede devolver el prestamo de otro usuario");
            }

            prestamoService.devolverPrestamo(id);
            redirectAttributes.addFlashAttribute("mensaje", "Devolucion registrada exitosamente");

        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        // Admin vuelve a activos, usuario vuelve a su historial
        if (esAdmin(session)) {
            return "redirect:/prestamo/activos";
        }
        return "redirect:/prestamo/mi-historial";
    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioActual");
        return usuario != null && usuario.getRol() == Rol.ADMINISTRADOR;
    }
}