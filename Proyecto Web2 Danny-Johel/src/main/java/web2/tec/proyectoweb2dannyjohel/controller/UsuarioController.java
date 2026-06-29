package web2.tec.proyectoweb2dannyjohel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Muestra el formulario de registro.html
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "usuario/registro.html";
    }

    // Procesa el formulario de registro.html
    @PostMapping("/registro")
    public String registrar(@RequestParam("nombre") String nombre,
                            @RequestParam("correo") String correo,
                            @RequestParam("password") String password,
                            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrar(nombre, correo, password);
            redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso, inicia sesion");
            return "redirect:/usuario/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuario/registro";
        }
    }

    // Muestra el formulario de login
    @GetMapping("/login")
    public String mostrarLogin() {
        return "usuario/login";
    }

    // Procesa el formulario de login
    @PostMapping("/login")
    public String login(@RequestParam("correo") String correo,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        return usuarioService.login(correo, password)
                .map(usuario -> {
                    session.setAttribute("usuarioActual", usuario);
                    session.setAttribute("rol", usuario.getRol().name());
                    return "redirect:/libro/catalogo";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Correo o contrasena incorrectos");
                    return "redirect:/usuario/login";
                });
    }

    // Cierra la sesion
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/usuario/login";
    }
}