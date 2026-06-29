package web2.tec.proyectoweb2dannyjohel.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(HttpSession session) {
        // Punto de entrada del sistema: sin sesion va al login, con sesion va al catalogo.
        if (session.getAttribute("usuarioActual") == null) {
            return "redirect:/usuario/login";
        }

        return "redirect:/libro/catalogo";
    }
}
