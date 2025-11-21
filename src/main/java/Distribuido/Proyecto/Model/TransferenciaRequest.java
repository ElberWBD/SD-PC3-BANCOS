package Distribuido.Proyecto.Model;

public class TransferenciaRequest {

    private String bancoOrigen;
    private String cuentaOrigen;
    private String bancoDestino;
    private String cuentaDestino;
    private double monto;

    public TransferenciaRequest() {}

    public String getBancoOrigen() { return bancoOrigen; }
    public void setBancoOrigen(String bancoOrigen) { this.bancoOrigen = bancoOrigen; }

    public String getCuentaOrigen() { return cuentaOrigen; }
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    public String getBancoDestino() { return bancoDestino; }
    public void setBancoDestino(String bancoDestino) { this.bancoDestino = bancoDestino; }

    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
