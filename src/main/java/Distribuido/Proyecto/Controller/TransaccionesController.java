package Distribuido.Proyecto.Controller;

import Distribuido.Proyecto.Model.TransferenciaRequest;
import Distribuido.Proyecto.Service.TransaccionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacciones")
public class TransaccionesController {

    private final TransaccionService service;

    public TransaccionesController(TransaccionService service) {
        this.service = service;
    }

    @PostMapping("/{bancoId}/depositar")
    public ResponseEntity<String> depositar(
            @PathVariable String bancoId,
            @RequestParam String cuenta,
            @RequestParam double monto) {

        return ResponseEntity.ok(service.depositar(bancoId, cuenta, monto));
    }

    @PostMapping("/{bancoId}/retirar")
    public ResponseEntity<String> retirar(
            @PathVariable String bancoId,
            @RequestParam String cuenta,
            @RequestParam double monto) {

        return ResponseEntity.ok(service.retirar(bancoId, cuenta, monto));
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaRequest req) {
        return ResponseEntity.ok(service.transferir(req));
    }
}
