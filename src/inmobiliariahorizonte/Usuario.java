package inmobiliariahorizonte;

public abstract class Usuario {
    private final int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private boolean activo;

    public Usuario(int idUsuario, String nombreUsuario, String contrasena) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.activo = true;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public boolean isActivo() { return activo; }
    public void setNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.isBlank()) throw new IllegalArgumentException("Nombre de usuario obligatorio.");
        this.nombreUsuario = nombreUsuario;
    }
    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.isBlank()) throw new IllegalArgumentException("Contraseña obligatoria.");
        this.contrasena = contrasena;
    }
    public void setActivo(boolean activo) { this.activo = activo; }
    public abstract String getRol();
    public abstract String getNombreCompleto();

    public boolean autenticar(String usuario, String clave) {
        return activo && nombreUsuario.equals(usuario) && contrasena.equals(clave);
    }

    @Override
    public String toString() {
        return idUsuario + " - " + nombreUsuario + " (" + getRol() + ") - " + getNombreCompleto();
    }
}
