package web2.tec.proyectoweb2dannyjohel.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web2.tec.proyectoweb2dannyjohel.entity.Prestamo;
import web2.tec.proyectoweb2dannyjohel.entity.Usuario;
import web2.tec.proyectoweb2dannyjohel.service.PrestamoService;
import web2.tec.proyectoweb2dannyjohel.service.UsuarioService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {

    private final PrestamoService prestamoService;
    private final UsuarioService usuarioService;

    public PrestamoRestController(PrestamoService prestamoService, UsuarioService usuarioService) {
        this.prestamoService = prestamoService;
        this.usuarioService = usuarioService;
    }

    // POST /api/prestamos - crea un nuevo prestamo
    @PostMapping
    public ResponseEntity<?> solicitar(@RequestBody Map<String, Object> body) {
        try {
            Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
            Long libroId = Long.valueOf(body.get("libroId").toString());
            LocalDate fechaDevolucion = LocalDate.parse(body.get("fechaDevolucionEsperada").toString());

            Usuario usuario = usuarioService.buscarPorId(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            Prestamo prestamo = prestamoService.solicitarPrestamo(usuario, libroId, fechaDevolucion);
            return ResponseEntity.ok(prestamo);

        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PATCH /api/prestamos/{id}/devolver - registra la devolucion
    @PatchMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        try {
            Prestamo prestamo = prestamoService.devolverPrestamo(id);
            return ResponseEntity.ok(prestamo);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}