package inmobiliariahorizonte;

public class Cliente {
    private final int idCliente;
    private String nombre, apellido, dni, telefono, tipoPropiedadBuscada, tipoOperacion, ubicacionBuscada;
    private double presupuesto;
    private Vendedor vendedor;
    private boolean activo = true;

    public Cliente(int id, String nombre, String apellido, String dni, String telefono,
                   String tipoPropiedadBuscada, String tipoOperacion, String ubicacionBuscada,
                   double presupuesto, Vendedor vendedor) {
        this.idCliente = id;
        this.nombre = requerido(nombre, "Nombre");
        this.apellido = requerido(apellido, "Apellido");
        this.dni = requerido(dni, "DNI");
        this.telefono = telefono;
        this.tipoPropiedadBuscada = tipoPropiedadBuscada;
        this.tipoOperacion = tipoOperacion;
        this.ubicacionBuscada = ubicacionBuscada;
        if (presupuesto < 0) throw new IllegalArgumentException("El presupuesto no puede ser negativo.");
        this.presupuesto = presupuesto;
        this.vendedor = vendedor;
    }
    private String requerido(String v, String campo) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(campo + " obligatorio.");
        return v.trim();
    }
    public int getIdCliente() { return idCliente; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getTipoPropiedadBuscada() { return tipoPropiedadBuscada; }
    public String getTipoOperacion() { return tipoOperacion; }
    public String getUbicacionBuscada() { return ubicacionBuscada; }
    public double getPresupuesto() { return presupuesto; }
    public Vendedor getVendedor() { return vendedor; }
    public boolean isActivo() { return activo; }
    public void setNombre(String v) { nombre = requerido(v, "Nombre"); }
    public void setApellido(String v) { apellido = requerido(v, "Apellido"); }
    public void setDni(String v) { dni = requerido(v, "DNI"); }
    public void setTelefono(String v) { telefono = v; }
    public void setTipoPropiedadBuscada(String v) { tipoPropiedadBuscada = v; }
    public void setTipoOperacion(String v) { tipoOperacion = v; }
    public void setUbicacionBuscada(String v) { ubicacionBuscada = v; }
    public void setPresupuesto(double v) { if (v < 0) throw new IllegalArgumentException("Presupuesto inválido."); presupuesto = v; }
    public void setVendedor(Vendedor v) { vendedor = v; }
    public void setActivo(boolean v) { activo = v; }
    @Override public String toString() {
        return idCliente + " - " + nombre + " " + apellido + " | DNI: " + dni
                + " | Busca: " + tipoPropiedadBuscada + " / " + tipoOperacion
                + " | Vendedor: " + (vendedor == null ? "Sin asignar" : vendedor.getNombreCompleto())
                + (activo ? "" : " [INACTIVO]");
    }
}
