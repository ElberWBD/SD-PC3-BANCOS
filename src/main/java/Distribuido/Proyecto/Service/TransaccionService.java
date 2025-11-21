package Distribuido.Proyecto.Service;

import Distribuido.Proyecto.Model.Cuenta;
import Distribuido.Proyecto.Model.TransferenciaRequest;
import Distribuido.Proyecto.Storage.BancoStorage;
import org.springframework.stereotype.Service;

@Service
public class TransaccionService {

    private final BancoStorage storage;

    public TransaccionService(BancoStorage storage) {
        this.storage = storage;
    }

    public synchronized String depositar(String bancoId, String cuenta, double monto) {
        Cuenta c = storage.findCuentaReferencia(bancoId, cuenta);
        if (c == null) return "Cuenta no encontrada";

        c.setSaldo(c.getSaldo() + monto);

        storage.guardarCuentas(bancoId); // ← MUY IMPORTANTE

        return "Depósito exitoso. Nuevo saldo: " + c.getSaldo();
    }

    public synchronized String retirar(String bancoId, String cuenta, double monto) {
        Cuenta c = storage.findCuentaReferencia(bancoId, cuenta);
        if (c == null) return "Cuenta no encontrada";

        if (c.getSaldo() < monto) return "Saldo insuficiente";

        c.setSaldo(c.getSaldo() - monto);

        storage.guardarCuentas(bancoId); // ← MUY IMPORTANTE

        return "Retiro exitoso. Nuevo saldo: " + c.getSaldo();
    }

    public synchronized String transferir(TransferenciaRequest req) {
        Cuenta origen = storage.findCuentaReferencia(req.getBancoOrigen(), req.getCuentaOrigen());
        Cuenta destino = storage.findCuentaReferencia(req.getBancoDestino(), req.getCuentaDestino());

        if (origen == null) return "Cuenta origen no existe";
        if (destino == null) return "Cuenta destino no existe";
        if (origen.getSaldo() < req.getMonto()) return "Saldo insuficiente";

        origen.setSaldo(origen.getSaldo() - req.getMonto());
        destino.setSaldo(destino.getSaldo() + req.getMonto());

        storage.guardarCuentas(req.getBancoOrigen());
        storage.guardarCuentas(req.getBancoDestino());

        return "Transferencia exitosa.";
    }

    public double consultarSaldo(String bancoId, String cuenta) {
        Cuenta c = storage.findCuentaReferencia(bancoId, cuenta);
        return c == null ? 0 : c.getSaldo();
    }
}
