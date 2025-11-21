package Distribuido.Proyecto.Controller;

import Distribuido.Proyecto.Model.Cliente;
import Distribuido.Proyecto.Service.ClienteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) { this.service = service; }

    @PostMapping("/{banco}/crear")
    public String crear(@PathVariable String banco, @RequestBody Cliente c) {
        return service.crearCliente(banco, c);
    }

    @GetMapping("/{banco}")
    public List<Cliente> listar(@PathVariable String banco) {
        return service.listar(banco);
    }
}
