package Distribuido.Proyecto.Model;

public class Cuenta {

    private String numeroCuenta;
    private String cedula;
    private double saldo;

    public Cuenta() {}

    public Cuenta(String numeroCuenta, String cedula, double saldo) {
        this.numeroCuenta = numeroCuenta;
        this.cedula = cedula;
        this.saldo = saldo;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getDniCliente() { return cedula; }
    public void setDniCliente(String dni) { this.cedula = dni; }
}
