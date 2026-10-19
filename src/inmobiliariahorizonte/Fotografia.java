package inmobiliariahorizonte;

public class Fotografia {
    private final int idFotografia;
    private String ruta;
    private String descripcion;

    public Fotografia(int id, String ruta, String descripcion) {
        if (ruta == null || ruta.isBlank()) throw new IllegalArgumentException("La ruta de la fotografía es obligatoria.");
        this.idFotografia = id;
        this.ruta = ruta.trim();
        this.descripcion = descripcion;
    }
    public int getIdFotografia() { return idFotografia; }
    public String getRuta() { return ruta; }
    public String getDescripcion() { return descripcion; }
    public void setRuta(String v) { if (v == null || v.isBlank()) throw new IllegalArgumentException("Ruta obligatoria."); ruta = v.trim(); }
    public void setDescripcion(String v) { descripcion = v; }
    @Override public String toString() { return idFotografia + " - " + ruta + (descripcion == null ? "" : " (" + descripcion + ")"); }
}
