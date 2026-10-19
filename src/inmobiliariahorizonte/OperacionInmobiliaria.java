package inmobiliariahorizonte;

import java.time.LocalDate;

// Registra una venta o alquiler concreto y mantiene sus relaciones con propiedad, cliente y vendedor. 
public class OperacionInmobiliaria {
    private final int idOperacion;
    private final String tipoOperacionInmobiliaria;
    private final LocalDate fecha;
    private final double precio;
    private String estadoOperacion;
    private final Propiedad propiedad;
    private final Cliente cliente;
    private final Vendedor vendedor;

    public OperacionInmobiliaria(int id, String tipo, LocalDate fecha, double precio,
                                 Propiedad propiedad, Cliente cliente, Vendedor vendedor) {
        if (!"Venta".equalsIgnoreCase(tipo) && !"Alquiler".equalsIgnoreCase(tipo))
            throw new IllegalArgumentException("El tipo de operación debe ser Venta o Alquiler.");
        if (fecha == null) throw new IllegalArgumentException("La fecha es obligatoria.");
        if (precio <= 0) throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        if (propiedad == null || cliente == null || vendedor == null) throw new IllegalArgumentException("Debe seleccionar propiedad, cliente y vendedor.");
        this.idOperacion = id;
        this.tipoOperacionInmobiliaria = tipo.equalsIgnoreCase("Venta") ? "Venta" : "Alquiler";
        this.fecha = fecha;
        this.precio = precio;
        this.propiedad = propiedad;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.estadoOperacion = "Activa";
    }
    public int getIdOperacion() { return idOperacion; }
    public String getTipoOperacionInmobiliaria() { return tipoOperacionInmobiliaria; }
    public LocalDate getFecha() { return fecha; }
    public double getPrecio() { return precio; }
    public String getEstadoOperacion() { return estadoOperacion; }
    public Propiedad getPropiedad() { return propiedad; }
    public Cliente getCliente() { return cliente; }
    public Vendedor getVendedor() { return vendedor; }
    public void anular() { estadoOperacion = "Anulada"; }
    @Override public String toString() {
        return idOperacion + " - " + tipoOperacionInmobiliaria + " | " + fecha + " | $" + String.format("%.2f", precio)
                + " | Propiedad " + propiedad.getIdPropiedad() + " | Cliente: " + cliente.getNombre() + " " + cliente.getApellido()
                + " | Vendedor: " + vendedor.getNombreCompleto() + " | " + estadoOperacion;
    }
}
