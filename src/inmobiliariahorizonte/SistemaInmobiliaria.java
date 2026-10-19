package inmobiliariahorizonte;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Administra los datos del prototipo en listas en memoria y valida las reglas principales del negocio. */
public class SistemaInmobiliaria {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Propiedad> propiedades = new ArrayList<>();
    private final List<OperacionInmobiliaria> operaciones = new ArrayList<>();
    private int siguienteUsuario = 4, siguientePropietario = 4, siguienteCliente = 4;
    private int siguientePropiedad = 5, siguienteFotografia = 1, siguienteOperacion = 1;

    public SistemaInmobiliaria() {
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        Administrador admin = new Administrador(1, "admin", "admin123", "Carlos", "Gomez", "30123456", "3435000001");
        Vendedor juan = new Vendedor(2, "juan", "juan123", "Juan", "Perez", "32123456", "3435000002");
        Vendedor maria = new Vendedor(3, "maria", "maria123", "Maria", "Lopez", "33123456", "3435000003");
        usuarios.add(admin); usuarios.add(juan); usuarios.add(maria);

        Propietario p1 = new Propietario(1, "Roberto", "Martinez", "27123456", "3435000010", "Belgrano 125", "Crespo");
        Propietario p2 = new Propietario(2, "Laura", "Fernandez", "28123456", "3435000011", "San Martin 845", "Crespo");
        Propietario p3 = new Propietario(3, "Daniel", "Gonzalez", "29123456", "3435000012", "Entre Rios 430", "Crespo");
        propietarios.add(p1); propietarios.add(p2); propietarios.add(p3);

        Cliente c1 = new Cliente(1, "Pedro", "Rodriguez", "34123456", "3435000020", "Casa", "Compra", "Crespo", 85000000, juan);
        Cliente c2 = new Cliente(2, "Ana", "Sanchez", "35123456", "3435000021", "Departamento", "Alquiler", "Crespo", 55000000, maria);
        Cliente c3 = new Cliente(3, "Lucia", "Ramirez", "36123456", "3435000022", "Casa", "Compra", "Crespo", 70000000, juan);
        clientes.add(c1); clientes.add(c2); clientes.add(c3);

        Propiedad pr1 = new Propiedad(1, "Av. Belgrano 1250", "Crespo", "Centro", "Casa", "Venta", 80000000, 3, 2, 180, true, true, p1, juan);
        Propiedad pr2 = new Propiedad(2, "San Martin 845", "Crespo", "Centro", "Departamento", "Alquiler", 520000, 2, 1, 95, false, true, p2, maria);
        Propiedad pr3 = new Propiedad(3, "Entre Rios 430", "Crespo", "Sur", "Casa", "Venta", 65000000, 3, 2, 140, true, true, p3, juan);
        Propiedad pr4 = new Propiedad(4, "Sarmiento 1120", "Crespo", "Norte", "Casa", "Alquiler", 450000, 2, 1, 80, true, false, p1, null);
        propiedades.add(pr1); propiedades.add(pr2); propiedades.add(pr3); propiedades.add(pr4);
        pr1.agregarFotografia(new Fotografia(siguienteFotografia++, "fotos/propiedad1_frente.jpg", "Vista frontal"));
        pr1.agregarFotografia(new Fotografia(siguienteFotografia++, "fotos/propiedad1_living.jpg", "Living"));
        pr2.agregarFotografia(new Fotografia(siguienteFotografia++, "fotos/propiedad2_frente.jpg", "Vista frontal"));
        siguienteOperacion = 1;
    }

    public Usuario iniciarSesion(String nombre, String clave) {
        for (Usuario u : usuarios) if (u.autenticar(nombre, clave)) return u;
        throw new IllegalArgumentException("Credenciales incorrectas o usuario inactivo.");
    }
    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Propietario> getPropietarios() { return propietarios; }
    public List<Cliente> getClientes() { return clientes; }
    public List<Propiedad> getPropiedades() { return propiedades; }
    public List<OperacionInmobiliaria> getOperaciones() { return operaciones; }

    public List<Vendedor> vendedoresActivos() {
        List<Vendedor> r = new ArrayList<>();
        for (Usuario u : usuarios) if (u instanceof Vendedor && u.isActivo()) r.add((Vendedor) u);
        return r;
    }
    public List<Propietario> propietariosActivos() {
        List<Propietario> r = new ArrayList<>();
        for (Propietario p : propietarios) if (p.isActivo()) r.add(p);
        return r;
    }
    public List<Cliente> clientesActivos() {
        List<Cliente> r = new ArrayList<>();
        for (Cliente c : clientes) if (c.isActivo()) r.add(c);
        return r;
    }
    public List<Propiedad> propiedadesActivas() {
        List<Propiedad> r = new ArrayList<>();
        for (Propiedad p : propiedades) if (p.isActiva()) r.add(p);
        return r;
    }

    public Propietario buscarPropietario(int id) {
        return propietarios.stream().filter(p -> p.getIdPropietario() == id && p.isActivo()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un propietario activo con ese ID."));
    }
    public Cliente buscarCliente(int id) {
        return clientes.stream().filter(c -> c.getIdCliente() == id && c.isActivo()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un cliente activo con ese ID."));
    }
    public Propiedad buscarPropiedad(int id) {
        return propiedades.stream().filter(p -> p.getIdPropiedad() == id && p.isActiva()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró una propiedad activa con ese ID."));
    }
    public Vendedor buscarVendedor(int id) {
        return usuarios.stream().filter(u -> u.getIdUsuario() == id && u instanceof Vendedor && u.isActivo())
                .map(u -> (Vendedor) u).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un vendedor activo con ese ID."));
    }

    public Propietario registrarPropietario(String n, String a, String dni, String tel, String dom, String ciudad) {
        validarDniUnico(dni, null);
        Propietario p = new Propietario(siguientePropietario++, n, a, dni, tel, dom, ciudad);
        propietarios.add(p); return p;
    }
    public Cliente registrarCliente(String n, String a, String dni, String tel, String tipoProp, String tipoOp,
                                   String ubicacion, double presupuesto, Vendedor vendedor) {
        validarDniUnico(dni, null);
        Cliente c = new Cliente(siguienteCliente++, n, a, dni, tel, tipoProp, tipoOp, ubicacion, presupuesto, vendedor);
        clientes.add(c); return c;
    }
    public Vendedor registrarVendedor(String usuario, String clave, String n, String a, String dni, String tel) {
        for (Usuario u : usuarios) if (u.getNombreUsuario().equalsIgnoreCase(usuario)) throw new IllegalArgumentException("El nombre de usuario ya existe.");
        validarDniUnico(dni, null);
        Vendedor v = new Vendedor(siguienteUsuario++, usuario, clave, n, a, dni, tel);
        usuarios.add(v); return v;
    }
    public Propiedad registrarPropiedad(String direccion, String ciudad, String zona, String tipo, String tipoOperacion, double precio,
                                        int habitaciones, int banios, double superficie, boolean patio, boolean cochera,
                                        int idPropietario) {
        Propietario propietario = buscarPropietario(idPropietario);
        Propiedad p = new Propiedad(siguientePropiedad++, direccion, ciudad, zona, tipo, tipoOperacion, precio, habitaciones,
                banios, superficie, patio, cochera, propietario, null);
        propiedades.add(p); return p;
    }
    public void asignarPropiedad(int idPropiedad, int idVendedor) {
        Propiedad p = buscarPropiedad(idPropiedad);
        if (p.getVendedor() != null) throw new IllegalStateException("La propiedad ya tiene un vendedor asignado.");
        p.asignarVendedor(buscarVendedor(idVendedor));
    }
    public void asignarCliente(int idCliente, int idVendedor) {
        Cliente c = buscarCliente(idCliente);
        c.setVendedor(buscarVendedor(idVendedor));
    }
    public Fotografia agregarFotografia(int idPropiedad, String ruta, String descripcion) {
        Propiedad p = buscarPropiedad(idPropiedad);
        Fotografia f = new Fotografia(siguienteFotografia++, ruta, descripcion);
        p.agregarFotografia(f); return f;
    }
    public OperacionInmobiliaria registrarOperacion(String tipo, LocalDate fecha, double precio,
                                                    int idPropiedad, int idCliente, int idVendedor) {
        Propiedad p = buscarPropiedad(idPropiedad);
        Cliente c = buscarCliente(idCliente);
        Vendedor v = buscarVendedor(idVendedor);
        if (!"Disponible".equals(p.getEstadoPropiedad())) throw new IllegalStateException("La propiedad no está disponible.");
        if (!"Venta o alquiler".equals(p.getTipoOperacionOfrecida())
                && !p.getTipoOperacionOfrecida().equalsIgnoreCase(tipo))
            throw new IllegalStateException("La propiedad no está ofrecida para este tipo de operación.");
        if (p.getVendedor() != null && p.getVendedor().getIdUsuario() != v.getIdUsuario())
            throw new IllegalStateException("El vendedor seleccionado no es el responsable de esta propiedad.");
        if (c.getVendedor() != null && c.getVendedor().getIdUsuario() != v.getIdUsuario())
            throw new IllegalStateException("El vendedor seleccionado no es el responsable de este cliente.");
        OperacionInmobiliaria op = new OperacionInmobiliaria(siguienteOperacion++, tipo, fecha, precio, p, c, v);
        operaciones.add(op);
        p.actualizarEstado("Venta".equalsIgnoreCase(tipo) ? "Vendida" : "Alquilada");
        return op;
    }
    public void anularOperacion(int id) {
        OperacionInmobiliaria op = operaciones.stream().filter(o -> o.getIdOperacion() == id && "Activa".equals(o.getEstadoOperacion()))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No se encontró una operación activa con ese ID."));
        op.anular();
        // Regla simplificada para el prototipo: al anular, la propiedad vuelve a estar Disponible.
        op.getPropiedad().actualizarEstado("Disponible");
    }
    public void darDeBajaPropiedad(int id) {
        Propiedad p = buscarPropiedad(id);
        if ("Vendida".equals(p.getEstadoPropiedad()) || "Alquilada".equals(p.getEstadoPropiedad()))
            throw new IllegalStateException("No se puede dar de baja una propiedad vendida o alquilada.");
        p.setActivo(false);
    }
    public void darDeBajaCliente(int id) { buscarCliente(id).setActivo(false); }
    public void darDeBajaPropietario(int id) {
        Propietario p = buscarPropietario(id);
        boolean tiene = propiedades.stream().anyMatch(pr -> pr.isActiva() && pr.getPropietario().getIdPropietario() == id);
        if (tiene) throw new IllegalStateException("No se puede dar de baja: el propietario tiene propiedades activas.");
        p.setActivo(false);
    }
    public void darDeBajaVendedor(int id) {
        Vendedor v = buscarVendedor(id);
        boolean asignaciones = propiedades.stream().anyMatch(p -> p.isActiva() && p.getVendedor() != null && p.getVendedor().getIdUsuario() == id)
                || clientes.stream().anyMatch(c -> c.isActivo() && c.getVendedor() != null && c.getVendedor().getIdUsuario() == id);
        if (asignaciones) throw new IllegalStateException("Reasigne sus propiedades y clientes antes de darlo de baja.");
        v.setActivo(false);
    }
    // Aplica filtros opcionales: un campo vacío no restringe los resultados. Todos los filtros informados se combinan con AND.
    public List<Propiedad> filtrarPropiedades(String tipoOperacion, String tipo, String ciudad, String zona,
                                               String estado, String comparadorPrecio, Double precio,
                                               String habitacionesMinimas, String patio, String cochera) {
        return propiedades.stream().filter(Propiedad::isActiva)
                .filter(p -> vacio(tipoOperacion) || "Todos".equals(tipoOperacion)
                        || p.getTipoOperacionOfrecida().equals(tipoOperacion)
                        || ("Alquiler".equals(tipoOperacion) && "Venta o alquiler".equals(p.getTipoOperacionOfrecida()))
                        || ("Venta".equals(tipoOperacion) && "Venta o alquiler".equals(p.getTipoOperacionOfrecida())))
                .filter(p -> vacio(tipo) || "Todos".equals(tipo) || p.getTipoPropiedad().equalsIgnoreCase(tipo))
                .filter(p -> vacio(ciudad) || p.getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .filter(p -> vacio(zona) || p.getZona().toLowerCase().contains(zona.toLowerCase()))
                .filter(p -> vacio(estado) || "Todos".equals(estado) || p.getEstadoPropiedad().equalsIgnoreCase(estado))
                .filter(p -> precio == null || comparar(p.getPrecio(), comparadorPrecio, precio))
                .filter(p -> vacio(habitacionesMinimas) || p.getHabitaciones() >= Integer.parseInt(habitacionesMinimas))
                .filter(p -> vacio(patio) || "Indiferente".equals(patio) || p.isPatio() == "Sí".equals(patio))
                .filter(p -> vacio(cochera) || "Indiferente".equals(cochera) || p.isCochera() == "Sí".equals(cochera))
                .sorted(Comparator.comparingDouble(Propiedad::getPrecio)).collect(java.util.stream.Collectors.toList());
    }

    public List<Cliente> filtrarClientes(String nombre, String tipoPropiedad, String tipoOperacion,
                                         String ubicacion, String comparadorPresupuesto, Double presupuesto) {
        return clientes.stream().filter(Cliente::isActivo)
                .filter(c -> vacio(nombre) || (c.getNombre() + " " + c.getApellido()).toLowerCase().contains(nombre.toLowerCase()))
                .filter(c -> vacio(tipoPropiedad) || "Todos".equals(tipoPropiedad) || c.getTipoPropiedadBuscada().equalsIgnoreCase(tipoPropiedad))
                .filter(c -> vacio(tipoOperacion) || "Todos".equals(tipoOperacion) || c.getTipoOperacion().equalsIgnoreCase(tipoOperacion))
                .filter(c -> vacio(ubicacion) || c.getUbicacionBuscada().toLowerCase().contains(ubicacion.toLowerCase()))
                .filter(c -> presupuesto == null || comparar(c.getPresupuesto(), comparadorPresupuesto, presupuesto))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Propietario> filtrarPropietarios(String nombre, String ciudad, String dni) {
        return propietarios.stream().filter(Propietario::isActivo)
                .filter(p -> vacio(nombre) || (p.getNombre() + " " + p.getApellido()).toLowerCase().contains(nombre.toLowerCase()))
                .filter(p -> vacio(ciudad) || (p.getCiudad() != null && p.getCiudad().toLowerCase().contains(ciudad.toLowerCase())))
                .filter(p -> vacio(dni) || p.getDni().contains(dni))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Vendedor> filtrarVendedores(String nombre, String usuario) {
        return vendedoresActivos().stream()
                .filter(v -> vacio(nombre) || v.getNombreCompleto().toLowerCase().contains(nombre.toLowerCase()))
                .filter(v -> vacio(usuario) || v.getNombreUsuario().toLowerCase().contains(usuario.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }

    public List<OperacionInmobiliaria> filtrarOperaciones(String tipo, String estado, String ciudad,
                                                            String tipoPropiedad, String comparadorPrecio, Double precio) {
        return operaciones.stream()
                .filter(o -> vacio(tipo) || "Todos".equals(tipo) || o.getTipoOperacionInmobiliaria().equalsIgnoreCase(tipo))
                .filter(o -> vacio(estado) || "Todos".equals(estado) || o.getEstadoOperacion().equalsIgnoreCase(estado))
                .filter(o -> vacio(ciudad) || o.getPropiedad().getCiudad().toLowerCase().contains(ciudad.toLowerCase()))
                .filter(o -> vacio(tipoPropiedad) || "Todos".equals(tipoPropiedad) || o.getPropiedad().getTipoPropiedad().equalsIgnoreCase(tipoPropiedad))
                .filter(o -> precio == null || comparar(o.getPrecio(), comparadorPrecio, precio))
                .collect(java.util.stream.Collectors.toList());
    }

    private boolean vacio(String texto) { return texto == null || texto.isBlank(); }
    private boolean comparar(double valor, String operador, double limite) {
        if (operador == null) return true;
        switch (operador) {
            case "Menor que": return valor < limite;
            case "Menor o igual que": return valor <= limite;
            case "Mayor que": return valor > limite;
            case "Mayor o igual que": return valor >= limite;
            case "Igual a": return valor == limite;
            default: return true;
        }
    }

    public List<Propiedad> buscarPropiedades(String criterio) {
        String q = criterio == null ? "" : criterio.toLowerCase();
        List<Propiedad> r = new ArrayList<>();
        for (Propiedad p : propiedades) {
            if (p.isActiva() && (p.toString().toLowerCase().contains(q)
                    || String.valueOf(p.getPrecio()).contains(q))) r.add(p);
        }
        r.sort(Comparator.comparingDouble(Propiedad::getPrecio));
        return r;
    }
    public List<Cliente> clientesDelVendedor(Vendedor v) {
        List<Cliente> r = new ArrayList<>();
        for (Cliente c : clientes) if (c.isActivo() && c.getVendedor() != null && c.getVendedor().getIdUsuario() == v.getIdUsuario()) r.add(c);
        return r;
    }
    public List<Propiedad> propiedadesDelVendedor(Vendedor v) {
        List<Propiedad> r = new ArrayList<>();
        for (Propiedad p : propiedades) if (p.isActiva() && p.getVendedor() != null && p.getVendedor().getIdUsuario() == v.getIdUsuario()) r.add(p);
        return r;
    }
    private void validarDniUnico(String dni, String excluir) {
        if (dni == null || dni.isBlank()) throw new IllegalArgumentException("El DNI es obligatorio.");
        for (Usuario u : usuarios) {
            String d = u instanceof Administrador ? ((Administrador)u).getDni()
                    : u instanceof Vendedor ? ((Vendedor)u).getDni() : null;
            if (d != null && d.equals(dni)) throw new IllegalArgumentException("El DNI ya está registrado.");
        }
        for (Propietario p : propietarios) if (p.getDni().equals(dni) && p.isActivo()) throw new IllegalArgumentException("El DNI ya está registrado.");
        for (Cliente c : clientes) if (c.getDni().equals(dni) && c.isActivo()) throw new IllegalArgumentException("El DNI ya está registrado.");
    }
}
