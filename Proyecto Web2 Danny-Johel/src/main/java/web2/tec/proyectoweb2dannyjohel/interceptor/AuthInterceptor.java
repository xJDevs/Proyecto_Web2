package web2.tec.proyectoweb2dannyjohel.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // Se usa getSession(false) para no crear sesiones vacias en visitas anonimas
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuarioActual") == null) {
            response.sendRedirect("/usuario/login");
            return false;
        }

        String path = request.getRequestURI();
        String rol = (String) session.getAttribute("rol");

        // Estas rutas cambian datos administrativos; solo el rol administrador puede usarlas
        if (esRutaAdministrativa(path) && !"ADMINISTRADOR".equals(rol)) {
            response.sendRedirect("/libro/catalogo");
            return false;
        }

        return true;
    }

    private boolean esRutaAdministrativa(String path) {
        // Agrupa las rutas que no debe ejecutar un usuario regular
        return path.startsWith("/libro/nuevo")
                || path.startsWith("/libro/editar")
                || path.startsWith("/libro/desactivar")
                || path.startsWith("/libro/activar")
                || path.startsWith("/prestamo/activos")
                || path.startsWith("/prestamo/historial");
    }
}
