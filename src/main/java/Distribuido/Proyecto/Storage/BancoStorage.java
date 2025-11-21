package Distribuido.Proyecto.Storage;

import Distribuido.Proyecto.Model.Cliente;
import Distribuido.Proyecto.Model.Cuenta;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class BancoStorage {

    private final TXTLoader loader;
    private final Map<String, List<Cliente>> clientes = new HashMap<>();
    private final Map<String, List<Cuenta>> cuentas = new HashMap<>();
    private final String basePath;

    public BancoStorage(TXTLoader loader) {
        this.loader = loader;
        String env = System.getenv().getOrDefault("DATA_DIR", "src/main/resources/data");
        this.basePath = env.endsWith("/") ? env : env + "/";
         // Inicializar todos los bancos
        for (String bancoId : new String[]{"A", "B", "C"}) {
        initBanco(bancoId);
    }
    }

    private void initBanco(String bancoId) {
        String suf = bancoId.toUpperCase();
        String clientesPath = basePath + "banco" + suf + "_clientes.txt";
        String cuentasPath = basePath + "banco" + suf + "_cuentas.txt";
        loader.ensureFile(clientesPath, "cedula|nombre");
        loader.ensureFile(cuentasPath, "numeroCuenta|cedula|saldo");
        clientes.put(suf, loadClientes(clientesPath));
        cuentas.put(suf, loadCuentas(cuentasPath));
        System.out.println("BancoStorage.initBanco -> inicializado " + suf + " (clientes=" +
                clientes.get(suf).size() + ", cuentas=" + cuentas.get(suf).size() + ")");
    }

    private List<Cliente> loadClientes(String path) {
        List<Cliente> list = new ArrayList<>();
        for (String[] row : loader.read(path)) {
            if (row.length >= 2) list.add(new Cliente(row[0].trim(), row[1].trim()));
        }
        return list;
    }

    private List<Cuenta> loadCuentas(String path) {
        List<Cuenta> list = new ArrayList<>();
        for (String[] row : loader.read(path)) {
            if (row.length >= 3) {
                double saldo = 0;
                try { saldo = Double.parseDouble(row[2].trim()); } catch (Exception ignored) {}
                list.add(new Cuenta(row[0].trim(), row[1].trim(), saldo));
            }
        }
        return list;
    }

    public synchronized void guardarClientes(String bancoId) {
        String suf = bancoId.toUpperCase();
        String path = basePath + "banco" + suf + "_clientes.txt";
        List<String[]> rows = new ArrayList<>();
        var list = clientes.get(suf);
        if (list != null) {
            for (Cliente c : list) rows.add(new String[]{c.getCedula(), c.getNombre()});
        }
        loader.write(path, rows, "cedula|nombre");
    }

    public synchronized void guardarCuentas(String bancoId) {
        String suf = bancoId.toUpperCase();
        String path = basePath + "banco" + suf + "_cuentas.txt";
        List<String[]> rows = new ArrayList<>();
        var list = cuentas.get(suf);
        if (list != null) {
            for (Cuenta c : list) rows.add(new String[]{c.getNumeroCuenta(), c.getCedula(), String.valueOf(c.getSaldo())});
        }
        loader.write(path, rows, "numeroCuenta|cedula|saldo");
    }

    public synchronized void addCliente(String bancoId, Cliente c) {
        String suf = bancoId.toUpperCase();
        clientes.computeIfAbsent(suf, k -> new ArrayList<>()).add(c);
        guardarClientes(suf);
    }

    public synchronized void addCuenta(String bancoId, Cuenta c) {
        String suf = bancoId.toUpperCase();
        var list = cuentas.computeIfAbsent(suf, k -> new ArrayList<>());
        for (Cuenta existente : list) {
            if (existente.getNumeroCuenta() != null && existente.getNumeroCuenta().equals(c.getNumeroCuenta())) {
                return;
            }
        }
        list.add(c);
        guardarCuentas(suf);
    }

    public List<Cliente> getClientes(String bancoId) {
        String suf = bancoId.toUpperCase();
        synchronized (clientes) {
            var l = clientes.get(suf);
            return l == null ? new ArrayList<>() : new ArrayList<>(l);
        }
    }

    public List<Cuenta> getCuentas(String bancoId) {
        String suf = bancoId.toUpperCase();
        synchronized (cuentas) {
            var l = cuentas.get(suf);
            return l == null ? new ArrayList<>() : new ArrayList<>(l);
        }
    }

    public synchronized Cuenta findCuentaReferencia(String bancoId, String numeroCuenta) {
        String suf = bancoId.toUpperCase();
        var list = cuentas.get(suf);
        if (list == null) return null;
        for (Cuenta c : list) {
            if (c.getNumeroCuenta() != null && c.getNumeroCuenta().equals(numeroCuenta)) return c;
        }
        return null;
    }

    public synchronized Cliente findClienteReferencia(String bancoId, String cedula) {
        String suf = bancoId.toUpperCase();
        var list = clientes.get(suf);
        if (list == null) return null;
        for (Cliente cl : list) {
            if (cl.getCedula() != null && cl.getCedula().equals(cedula)) return cl;
        }
        return null;
    }
}
