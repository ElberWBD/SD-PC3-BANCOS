package Distribuido.Proyecto.Model;

public class Cliente {

    private String cedula;
    private String nombre;

    public Cliente() {}

    public Cliente(String cedula, String nombre) {
        this.cedula = cedula;
        this.nombre = nombre;
    }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDni() { return cedula; }
    public void setDni(String dni) { this.cedula = dni; }
}
