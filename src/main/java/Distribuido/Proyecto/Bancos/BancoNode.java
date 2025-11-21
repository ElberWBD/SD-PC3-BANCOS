package Distribuido.Proyecto.Bancos;

import Distribuido.Proyecto.Model.Cuenta;
import Distribuido.Proyecto.Distributed.ClusterHttpClient;
import Distribuido.Proyecto.Distributed.RequestMessage;
import Distribuido.Proyecto.Distributed.RicartAgrawala;
import Distribuido.Proyecto.Model.Cliente;
import Distribuido.Proyecto.Storage.BancoStorage;

import java.util.List;

public class BancoNode {

    private final String id;
    private final BancoStorage storage;
    private final RicartAgrawala ra;
    private final ClusterHttpClient client;
    private final String myUrl;

    public BancoNode(String id, BancoStorage storage, ClusterHttpClient client) {
        this.id = id;
        this.storage = storage;
        this.client = client;
        String envUrl = System.getenv().getOrDefault("BASE_URL", "");
        this.myUrl = envUrl;
        this.ra = new RicartAgrawala(id, myUrl, client);
    }

    public String getId() { return id; }
    public RicartAgrawala getRa() { return ra; }

    public List<Cliente> getClientes(String bancoId) { return storage.getClientes(bancoId); }
    public List<Cuenta> getCuentas(String bancoId) { return storage.getCuentas(bancoId); }

    public void guardarClientes(String bancoId) { storage.guardarClientes(bancoId); }
    public void guardarCuentas(String bancoId) { storage.guardarCuentas(bancoId); }

    // Ahora obtiene la referencia real desde el storage para que las modificaciones persistan
    public Cuenta findCuenta(String bancoId, String numero) {
        return storage.findCuentaReferencia(bancoId, numero);
    }

    public void handleRequest(RequestMessage msg) { ra.onReceiveRequest(msg); }
    public void handleOk(RequestMessage msg) { ra.onReceiveOk(msg); }
    public void handleRelease(RequestMessage msg) { ra.onReceiveRelease(msg); }
}
