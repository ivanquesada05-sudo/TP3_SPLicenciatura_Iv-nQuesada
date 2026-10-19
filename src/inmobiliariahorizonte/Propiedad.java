package inmobiliariahorizonte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Representa una propiedad, su oferta comercial, estado y vínculos con propietario y vendedor. */
public class Propiedad {
    private final int idPropiedad;
    private String direccion, ciudad, zona, tipoPropiedad, tipoOperacionOfrecida, estadoPropiedad;
    private double precio, superficie;
    private int habitaciones, banios;
    private boolean patio, cochera, activa = true;
    private Propietario propietario;
    private Vendedor vendedor;
    private final List<Fotografia> fotografias = new ArrayList<>();

    public Propiedad(int id, String direccion, String ciudad, String zona, String tipoPropiedad, String tipoOperacionOfrecida,
                     double precio, int habitaciones, int banios, double superficie,
                     boolean patio, boolean cochera, Propietario propietario, Vendedor vendedor) {
        this.idPropiedad = id;
        this.direccion = requerido(direccion, "Dirección");
        this.ciudad = requerido(ciudad, "Ciudad");
        this.zona = requerido(zona, "Zona");
        this.tipoPropiedad = requerido(tipoPropiedad, "Tipo de propiedad");
        if (!"Venta".equals(tipoOperacionOfrecida) && !"Alquiler".equals(tipoOperacionOfrecida) && !"Venta o alquiler".equals(tipoOperacionOfrecida))
            throw new IllegalArgumentException("La oferta debe ser Venta, Alquiler o Venta o alquiler.");
        this.tipoOperacionOfrecida = tipoOperacionOfrecida;
        if (precio <= 0) throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        if (habitaciones < 0 || banios < 0 || superficie < 0) throw new IllegalArgumentException("Las características numéricas no pueden ser negativas.");
        if (propietario == null || !propietario.isActivo()) throw new IllegalArgumentException("Debe seleccionar un propietario activo.");
        this.precio = precio; this.habitaciones = habitaciones; this.banios = banios;
        this.superficie = superficie; this.patio = patio; this.cochera = cochera;
        this.propietario = propietario; this.vendedor = vendedor;
        this.estadoPropiedad = "Disponible";
    }
    private String requerido(String v, String campo) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(campo + " obligatorio.");
        return v.trim();
    }
    public int getIdPropiedad() { return idPropiedad; }
    public String getDireccion() { return direccion; }
    public String getCiudad() { return ciudad; }
    public String getZona() { return zona; }
    public String getTipoPropiedad() { return tipoPropiedad; }
    public String getTipoOperacionOfrecida() { return tipoOperacionOfrecida; }
    public double getPrecio() { return precio; }
    public String getEstadoPropiedad() { return estadoPropiedad; }
    public int getHabitaciones() { return habitaciones; }
    public int getBanios() { return banios; }
    public double getSuperficie() { return superficie; }
    public boolean isPatio() { return patio; }
    public boolean isCochera() { return cochera; }
    public Propietario getPropietario() { return propietario; }
    public Vendedor getVendedor() { return vendedor; }
    public boolean isActiva() { return activa; }
    public List<Fotografia> getFotografias() { return Collections.unmodifiableList(fotografias); }
    public void setDireccion(String v) { direccion = requerido(v, "Dirección"); }
    public void setCiudad(String v) { ciudad = requerido(v, "Ciudad"); }
    public void setZona(String v) { zona = requerido(v, "Zona"); }
    public void setTipoPropiedad(String v) { tipoPropiedad = requerido(v, "Tipo de propiedad"); }
    public void setTipoOperacionOfrecida(String v) {
        if (!"Venta".equals(v) && !"Alquiler".equals(v) && !"Venta o alquiler".equals(v)) throw new IllegalArgumentException("Tipo de oferta inválido.");
        tipoOperacionOfrecida = v;
    }
    public void setPrecio(double v) { if (v <= 0) throw new IllegalArgumentException("Precio inválido."); precio = v; }
    public void setHabitaciones(int v) { if (v < 0) throw new IllegalArgumentException("Cantidad inválida."); habitaciones = v; }
    public void setBanios(int v) { if (v < 0) throw new IllegalArgumentException("Cantidad inválida."); banios = v; }
    public void setSuperficie(double v) { if (v < 0) throw new IllegalArgumentException("Superficie inválida."); superficie = v; }
    public void setPatio(boolean v) { patio = v; }
    public void setCochera(boolean v) { cochera = v; }
    public void setActivo(boolean v) { activa = v; }
    public void asignarVendedor(Vendedor v) {
        if (!"Disponible".equals(estadoPropiedad)) throw new IllegalStateException("Solo se pueden asignar propiedades disponibles.");
        if (v == null || !v.isActivo()) throw new IllegalArgumentException("Debe seleccionar un vendedor activo.");
        vendedor = v;
    }
    public void agregarFotografia(Fotografia f) { if (f == null) throw new IllegalArgumentException("Fotografía inválida."); fotografias.add(f); }
    public void actualizarEstado(String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.isBlank()) throw new IllegalArgumentException("Estado obligatorio.");
        estadoPropiedad = nuevoEstado;
    }
    @Override public String toString() {
        return idPropiedad + " - " + tipoPropiedad + " | Oferta: " + tipoOperacionOfrecida + " | " + direccion + ", " + ciudad + " (" + zona + ")"
                + " | $" + String.format("%.2f", precio) + " | " + estadoPropiedad
                + " | Propietario: " + propietario.getNombre() + " " + propietario.getApellido()
                + " | Vendedor: " + (vendedor == null ? "Sin asignar" : vendedor.getNombreCompleto())
                + (activa ? "" : " [INACTIVA]");
    }
}
