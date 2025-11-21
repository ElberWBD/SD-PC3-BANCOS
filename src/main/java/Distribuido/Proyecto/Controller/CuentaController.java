package Distribuido.Proyecto.Controller;

import Distribuido.Proyecto.Model.Cuenta;
import Distribuido.Proyecto.Service.CuentaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService service;

    public CuentaController(CuentaService service) { this.service = service; }

    @PostMapping("/{banco}/crear")
    public String crear(@PathVariable String banco, @RequestBody Cuenta c) {
        return service.crearCuenta(banco, c);
    }

    @GetMapping("/{banco}")
    public List<Cuenta> listar(@PathVariable String banco) {
        return service.listar(banco);
    }
}
