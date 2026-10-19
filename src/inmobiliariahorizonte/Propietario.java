package inmobiliariahorizonte;

public class Propietario {
    private final int idPropietario;
    private String nombre, apellido, dni, telefono, domicilio, ciudad;
    private boolean activo = true;

    public Propietario(int id, String nombre, String apellido, String dni, String telefono, String domicilio, String ciudad) {
        this.idPropietario = id;
        this.nombre = requerido(nombre, "Nombre");
        this.apellido = requerido(apellido, "Apellido");
        this.dni = requerido(dni, "DNI");
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.ciudad = ciudad;
    }
    private String requerido(String v, String campo) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(campo + " obligatorio.");
        return v.trim();
    }
    public int getIdPropietario() { return idPropietario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getDomicilio() { return domicilio; }
    public String getCiudad() { return ciudad; }
    public boolean isActivo() { return activo; }
    public void setNombre(String v) { nombre = requerido(v, "Nombre"); }
    public void setApellido(String v) { apellido = requerido(v, "Apellido"); }
    public void setDni(String v) { dni = requerido(v, "DNI"); }
    public void setTelefono(String v) { telefono = v; }
    public void setDomicilio(String v) { domicilio = v; }
    public void setCiudad(String v) { ciudad = v; }
    public void setActivo(boolean v) { activo = v; }
    @Override public String toString() { return idPropietario + " - " + nombre + " " + apellido + " | DNI: " + dni + (activo ? "" : " [INACTIVO]"); }
}
