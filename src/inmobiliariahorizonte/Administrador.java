package inmobiliariahorizonte;

public class Administrador extends Usuario {
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;

    public Administrador(int id, String usuario, String clave, String nombre, String apellido, String dni, String telefono) {
        super(id, usuario, clave);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public void setNombre(String v) { nombre = v; }
    public void setApellido(String v) { apellido = v; }
    public void setDni(String v) { dni = v; }
    public void setTelefono(String v) { telefono = v; }
    @Override public String getRol() { return "Administrador"; }
    @Override public String getNombreCompleto() { return nombre + " " + apellido; }
}
