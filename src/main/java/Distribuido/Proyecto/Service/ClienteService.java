package Distribuido.Proyecto.Service;

import Distribuido.Proyecto.Model.Cliente;
import Distribuido.Proyecto.Storage.BancoStorage;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClienteService {

    private final BancoStorage storage;

    public ClienteService(BancoStorage storage) {
        this.storage = storage;
    }

    public String crearCliente(String banco, Cliente c) {
        if (c == null || c.getCedula() == null || c.getCedula().trim().isEmpty()) {
            return "ERROR: DNI inválido";
        }
        String suf = banco.toUpperCase();
        var existing = storage.findClienteReferencia(suf, c.getCedula().trim());
        if (existing != null) return "ERROR: Cliente ya existe";
        storage.addCliente(suf, new Cliente(c.getCedula().trim(), c.getNombre() == null ? "" : c.getNombre().trim()));
        return "Cliente creado en banco " + suf;
    }

    public List<Cliente> listar(String banco) {
        return storage.getClientes(banco);
    }
}
