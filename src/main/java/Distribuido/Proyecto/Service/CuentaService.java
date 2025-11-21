package Distribuido.Proyecto.Service;

import Distribuido.Proyecto.Model.Cuenta;
import Distribuido.Proyecto.Storage.BancoStorage;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CuentaService {

    private final BancoStorage storage;

    public CuentaService(BancoStorage storage) {
        this.storage = storage;
    }

    public String crearCuenta(String banco, Cuenta c) {
        if (c == null || c.getNumeroCuenta() == null || c.getNumeroCuenta().trim().isEmpty()) {
            return "ERROR: número de cuenta inválido";
        }
        String suf = banco.toUpperCase();
        String dni = c.getCedula() == null ? "" : c.getCedula().trim();
        if (dni.isEmpty()) return "ERROR: la cuenta necesita un DNI de cliente";
        var cliente = storage.findClienteReferencia(suf, dni);
        if (cliente == null) return "ERROR: Cliente no existe: " + dni;
        var existeCuenta = storage.findCuentaReferencia(suf, c.getNumeroCuenta().trim());
        if (existeCuenta != null) return "ERROR: Cuenta ya existe";
        storage.addCuenta(suf, new Cuenta(c.getNumeroCuenta().trim(), dni, c.getSaldo()));
        return "Cuenta creada en banco " + suf;
    }

    public List<Cuenta> listar(String banco) {
        return storage.getCuentas(banco);
    }
}
