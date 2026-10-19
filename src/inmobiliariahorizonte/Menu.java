package inmobiliariahorizonte;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Presenta los menús y formularios Swing para que administrador y vendedores usen el sistema. */
public class Menu {
    private final SistemaInmobiliaria sistema;

    public Menu(SistemaInmobiliaria sistema) {
        this.sistema = sistema;
    }

    public void iniciar() {
        while (true) {
            try {
                String usuario = pedirUsuario();
                if (usuario == null) return;
                String clave = pedirContrasena(usuario);
                if (clave == null) return;

                Usuario sesion = sistema.iniciarSesion(usuario.trim(), clave);
                JOptionPane.showMessageDialog(null,
                        "Bienvenido/a " + sesion.getNombreCompleto() + "\nRol: " + sesion.getRol());

                if (sesion instanceof Administrador) {
                    menuAdministrador((Administrador) sesion);
                } else {
                    menuVendedor((Vendedor) sesion);
                }
            } catch (IllegalArgumentException ex) {
                error(ex.getMessage());
            }
        }
    }

    private String pedirUsuario() {
        StringBuilder lista = new StringBuilder("Usuarios disponibles:\n\n");
        for (Usuario u : sistema.getUsuarios()) {
            if (u.isActivo()) {
                lista.append("- ").append(u.getNombreUsuario())
                        .append(" ( ").append(u.getRol()).append(" )\n");
            }
        }
        lista.append("\nIngrese el nombre de usuario (Cancelar para salir):");
        return JOptionPane.showInputDialog(null, lista.toString(), "Inicio de sesión",
                JOptionPane.QUESTION_MESSAGE);
    }

    private String pedirContrasena(String nombreUsuario) {
        StringBuilder lista = new StringBuilder("Contraseñas disponibles para este prototipo:\n\n");
        for (Usuario u : sistema.getUsuarios()) {
            if (u.isActivo()) {
                lista.append(u.getNombreUsuario()).append(" → ")
                        .append(u.getContrasena()).append("\n");
            }
        }
        lista.append("\nIngrese la contraseña para: ").append(nombreUsuario);
        return JOptionPane.showInputDialog(null, lista.toString(), "Inicio de sesión",
                JOptionPane.QUESTION_MESSAGE);
    }

    private void menuAdministrador(Administrador admin) {
        boolean seguir = true;
        while (seguir) {
            String opciones = "Sesión: " + admin.getNombreCompleto() + "\n\n"
                    + "1 - Registrar propiedad\n"
                    + "2 - Registrar propietario\n"
                    + "3 - Registrar cliente\n"
                    + "4 - Registrar vendedor\n"
                    + "5 - Asignar propiedad\n"
                    + "6 - Asignar cliente\n"
                    + "7 - Registrar operación\n"
                    + "8 - Anular operación\n"
                    + "9 - Agregar fotografía\n"
                    + "10 - Consultar/buscar información\n"
                    + "11 - Dar de baja propiedad\n"
                    + "12 - Dar de baja cliente\n"
                    + "13 - Dar de baja propietario\n"
                    + "14 - Dar de baja vendedor\n"
                    + "0 - Cerrar sesión\n\nIngrese el número de la opción:";
            Integer op = pedirOpcionNumerica(opciones);
            if (op == null) {
                seguir = false;
                continue;
            }
            try {
                switch (op) {
                    case 1: registrarPropiedad(); break;
                    case 2: registrarPropietario(); break;
                    case 3: registrarCliente(); break;
                    case 4: registrarVendedor(); break;
                    case 5: asignarPropiedad(); break;
                    case 6: asignarCliente(); break;
                    case 7: registrarOperacion(); break;
                    case 8: anularOperacion(); break;
                    case 9: agregarFotografia(); break;
                    case 10: consultarGeneral(); break;
                    case 11: bajaPropiedad(); break;
                    case 12: bajaCliente(); break;
                    case 13: bajaPropietario(); break;
                    case 14: bajaVendedor(); break;
                    case 0: seguir = false; break;
                    default: error("Seleccione un número entre 0 y 14.");
                }
            } catch (RuntimeException ex) {
                error(ex.getMessage());
            }
        }
    }

    private void menuVendedor(Vendedor vendedor) {
        boolean seguir = true;
        while (seguir) {
            String opciones = "Sesión: " + vendedor.getNombreCompleto() + "\n\n"
                    + "1 - Consultar mis propiedades\n"
                    + "2 - Consultar mis clientes\n"
                    + "3 - Buscar propiedades\n"
                    + "0 - Cerrar sesión\n\nIngrese el número de la opción:";
            Integer op = pedirOpcionNumerica(opciones);
            if (op == null) {
                seguir = false;
                continue;
            }
            switch (op) {
                case 1: mostrarLista("Mis propiedades", sistema.propiedadesDelVendedor(vendedor)); break;
                case 2: mostrarLista("Mis clientes", sistema.clientesDelVendedor(vendedor)); break;
                case 3: buscarPropiedades(); break;
                case 0: seguir = false; break;
                default: error("Seleccione un número entre 0 y 3.");
            }
        }
    }

    private Integer pedirOpcionNumerica(String mensaje) {
        String valor = JOptionPane.showInputDialog(null, mensaje, "Menú de opciones",
                JOptionPane.QUESTION_MESSAGE);
        if (valor == null) return null;
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            error("Ingrese un número entero válido.");
            return null;
        }
    }

    // Registra primero los datos de la propiedad y después permite crear o seleccionar su propietario.
    private void registrarPropiedad() {
        String direccion = pedirObligatorio("Dirección:");
        String ciudad = pedirObligatorio("Ciudad:");
        String zona = pedirObligatorio("Zona:");
        String tipo = elegir("Tipo de propiedad", new String[]{"Casa", "Departamento", "Terreno", "Local", "Otro"});
        double precio = pedirDouble("Precio:");
        int habitaciones = pedirInt("Cantidad de habitaciones:");
        int banios = pedirInt("Cantidad de baños:");
        double superficie = pedirDouble("Superficie en m²:");
        boolean patio = confirmar("¿Tiene patio?");
        boolean cochera = confirmar("¿Tiene cochera?");
        String tipoOperacion = elegir("¿Para qué operación se ofrece?",
                new String[]{"Venta", "Alquiler", "Venta o alquiler"});

        // Primero se define cómo vincular al propietario; si se crea uno nuevo, queda asociado a esta propiedad.
        String[] opcionesPropietario = {"Agregar y asignar propietario", "Asignar propietario existente"};
        String opcion = elegir("Propietario de la propiedad", opcionesPropietario);
        int idProp;
        if ("Agregar y asignar propietario".equals(opcion)) {
            Propietario nuevo = registrarPropietarioConFormulario();
            idProp = nuevo.getIdPropietario();
        } else {
            idProp = pedirId("Seleccione el propietario ingresando su ID:", sistema.propietariosActivos(),
                    Propietario::getIdPropietario, Propietario::toString, false);
        }
        Propiedad p = sistema.registrarPropiedad(direccion, ciudad, zona, tipo, tipoOperacion, precio, habitaciones,
                banios, superficie, patio, cochera, idProp);
        JOptionPane.showMessageDialog(null, "Propiedad registrada:\n" + p);
    }

    private void registrarPropietario() {
        Propietario p = registrarPropietarioConFormulario();
        JOptionPane.showMessageDialog(null, "Propietario registrado:\n" + p);
    }

    private Propietario registrarPropietarioConFormulario() {
        return sistema.registrarPropietario(pedirObligatorio("Nombre:"), pedirObligatorio("Apellido:"),
                pedirObligatorio("DNI:"), pedirTexto("Teléfono:"), pedirTexto("Domicilio:"), pedirTexto("Ciudad:"));
    }

    private void registrarCliente() {
        String n = pedirObligatorio("Nombre:");
        String a = pedirObligatorio("Apellido:");
        String dni = pedirObligatorio("DNI:");
        String tel = pedirTexto("Teléfono:");
        String tipoProp = elegir("Tipo de propiedad buscada", new String[]{"Casa", "Departamento", "Terreno", "Local", "Indiferente"});
        String tipoOp = elegir("Tipo de operación buscada", new String[]{"Compra", "Alquiler"});
        String ubicacion = pedirObligatorio("Ubicación buscada:");
        double presupuesto = pedirDouble("Presupuesto:");
        Integer idVendedor = pedirId("Seleccione el vendedor responsable (ingrese 0 para dejar sin asignar):",
                sistema.vendedoresActivos(), Usuario::getIdUsuario, Object::toString, true);
        Vendedor v = (idVendedor == 0) ? null : sistema.buscarVendedor(idVendedor);
        Cliente c = sistema.registrarCliente(n, a, dni, tel, tipoProp, tipoOp, ubicacion, presupuesto, v);
        JOptionPane.showMessageDialog(null, "Cliente registrado:\n" + c);
    }

    private void registrarVendedor() {
        Vendedor v = sistema.registrarVendedor(pedirObligatorio("Nombre de usuario:"), pedirObligatorio("Contraseña:"),
                pedirObligatorio("Nombre:"), pedirObligatorio("Apellido:"), pedirObligatorio("DNI:"), pedirTexto("Teléfono:"));
        JOptionPane.showMessageDialog(null, "Vendedor registrado:\n" + v);
    }

    private void asignarPropiedad() {
        int idp = pedirId("Seleccione la propiedad que desea asignar:", sistema.propiedadesActivas(),
                Propiedad::getIdPropiedad, Propiedad::toString, false);
        int idv = pedirId("Seleccione el vendedor responsable:", sistema.vendedoresActivos(),
                Usuario::getIdUsuario, Object::toString, false);
        sistema.asignarPropiedad(idp, idv);
        JOptionPane.showMessageDialog(null, "Propiedad asignada correctamente.");
    }

    private void asignarCliente() {
        int idc = pedirId("Seleccione el cliente:", sistema.clientesActivos(),
                Cliente::getIdCliente, Cliente::toString, false);
        int idv = pedirId("Seleccione el vendedor responsable:", sistema.vendedoresActivos(),
                Usuario::getIdUsuario, Object::toString, false);
        sistema.asignarCliente(idc, idv);
        JOptionPane.showMessageDialog(null, "Cliente asignado correctamente.");
    }

    private void registrarOperacion() {
        int idp = pedirId("Seleccione la propiedad disponible:", sistema.propiedadesActivas().stream()
                        .filter(p -> "Disponible".equals(p.getEstadoPropiedad())).collect(Collectors.toList()),
                Propiedad::getIdPropiedad, Propiedad::toString, false);
        int idc = pedirId("Seleccione el cliente:", sistema.clientesActivos(),
                Cliente::getIdCliente, Cliente::toString, false);
        int idv = pedirId("Seleccione el vendedor responsable:", sistema.vendedoresActivos(),
                Usuario::getIdUsuario, Object::toString, false);
        String tipo = elegir("Tipo de operación", new String[]{"Venta", "Alquiler"});
        double precio = pedirDouble("Precio de la operación:");
        OperacionInmobiliaria op = sistema.registrarOperacion(tipo, LocalDate.now(), precio, idp, idc, idv);
        JOptionPane.showMessageDialog(null, "Operación registrada:\n" + op
                + "\nNuevo estado de la propiedad: " + op.getPropiedad().getEstadoPropiedad());
    }

    private void anularOperacion() {
        List<OperacionInmobiliaria> activas = sistema.getOperaciones().stream()
                .filter(o -> "Activa".equals(o.getEstadoOperacion())).collect(Collectors.toList());
        int id = pedirId("Seleccione la operación que desea anular:", activas,
                OperacionInmobiliaria::getIdOperacion, OperacionInmobiliaria::toString, false);
        sistema.anularOperacion(id);
        JOptionPane.showMessageDialog(null, "Operación anulada. En este prototipo, la propiedad vuelve a Disponible.");
    }

    private void agregarFotografia() {
        int id = pedirId("Seleccione la propiedad:", sistema.propiedadesActivas(),
                Propiedad::getIdPropiedad, Propiedad::toString, false);
        Fotografia f = sistema.agregarFotografia(id, pedirObligatorio("Ruta o nombre de archivo:"), pedirTexto("Descripción:"));
        JOptionPane.showMessageDialog(null, "Fotografía asociada:\n" + f);
    }

    // La búsqueda cambia los filtros disponibles según la categoría seleccionada.
    private void consultarGeneral() {
        String[] categorias = {"Propiedades", "Propietarios", "Clientes", "Vendedores", "Operaciones"};
        String categoria = elegir("¿Qué información desea buscar?", categorias);
        switch (categoria) {
            case "Propiedades": buscarPropiedades(); break;
            case "Propietarios": buscarPropietarios(); break;
            case "Clientes": buscarClientes(); break;
            case "Vendedores": buscarVendedores(); break;
            case "Operaciones": buscarOperaciones(); break;
            default: break;
        }
    }

    private void buscarPropiedades() {
        String operacion = elegir("Tipo de operación ofrecida", new String[]{"Todos", "Venta", "Alquiler"});
        String tipo = elegir("Tipo de propiedad", new String[]{"Todos", "Casa", "Departamento", "Terreno", "Local", "Otro"});
        String ciudad = pedirOpcional("Ciudad (dejar vacío para ignorar):");
        String zona = pedirOpcional("Zona o barrio (dejar vacío para ignorar):");
        String estado = elegir("Estado", new String[]{"Todos", "Disponible", "Vendida", "Alquilada"});
        String comparador = elegir("Filtro de precio", new String[]{"Sin filtro de precio", "Menor que", "Menor o igual que", "Mayor que", "Mayor o igual que", "Igual a"});
        Double precio = "Sin filtro de precio".equals(comparador) ? null : pedirDouble("Ingrese el precio de referencia:");
        if (precio != null && "Sin filtro de precio".equals(comparador)) comparador = null;
        String hab = pedirOpcional("Cantidad mínima de habitaciones (vacío para ignorar):");
        if (!hab.isBlank()) {
            try { if (Integer.parseInt(hab) < 0) throw new NumberFormatException(); }
            catch (NumberFormatException ex) { throw new IllegalArgumentException("Las habitaciones deben ser un entero igual o mayor a cero."); }
        }
        String patio = elegir("¿Patio?", new String[]{"Indiferente", "Sí", "No"});
        String cochera = elegir("¿Cochera?", new String[]{"Indiferente", "Sí", "No"});
        mostrarLista("Resultados de búsqueda", sistema.filtrarPropiedades(operacion, tipo, ciudad, zona, estado,
                comparador, precio, hab, patio, cochera));
    }

    private void buscarPropietarios() {
        String nombre = pedirOpcional("Nombre o apellido (opcional):");
        String ciudad = pedirOpcional("Ciudad (opcional):");
        String dni = pedirOpcional("DNI (opcional):");
        mostrarLista("Resultados de búsqueda", sistema.filtrarPropietarios(nombre, ciudad, dni));
    }

    private void buscarClientes() {
        String nombre = pedirOpcional("Nombre o apellido (opcional):");
        String tipoProp = elegir("Tipo de propiedad buscada", new String[]{"Todos", "Casa", "Departamento", "Terreno", "Local", "Indiferente"});
        String tipoOp = elegir("Operación buscada", new String[]{"Todos", "Compra", "Alquiler"});
        String ubicacion = pedirOpcional("Ubicación buscada (opcional):");
        String comp = elegir("Filtro de presupuesto", new String[]{"Sin filtro", "Menor que", "Menor o igual que", "Mayor que", "Mayor o igual que", "Igual a"});
        Double presupuesto = "Sin filtro".equals(comp) ? null : pedirDouble("Ingrese el presupuesto de referencia:");
        if (presupuesto == null) comp = null;
        mostrarLista("Resultados de búsqueda", sistema.filtrarClientes(nombre, tipoProp, tipoOp, ubicacion, comp, presupuesto));
    }

    private void buscarVendedores() {
        String nombre = pedirOpcional("Nombre o apellido (opcional):");
        String usuario = pedirOpcional("Nombre de usuario (opcional):");
        mostrarLista("Resultados de búsqueda", sistema.filtrarVendedores(nombre, usuario));
    }

    private void buscarOperaciones() {
        String tipo = elegir("Tipo de operación", new String[]{"Todos", "Venta", "Alquiler"});
        String estado = elegir("Estado de la operación", new String[]{"Todos", "Activa", "Anulada"});
        String ciudad = pedirOpcional("Ciudad de la propiedad (opcional):");
        String tipoProp = elegir("Tipo de propiedad", new String[]{"Todos", "Casa", "Departamento", "Terreno", "Local", "Otro"});
        String comp = elegir("Filtro de precio de operación", new String[]{"Sin filtro", "Menor que", "Menor o igual que", "Mayor que", "Mayor o igual que", "Igual a"});
        Double precio = "Sin filtro".equals(comp) ? null : pedirDouble("Ingrese el precio de referencia:");
        if (precio == null) comp = null;
        mostrarLista("Resultados de búsqueda", sistema.filtrarOperaciones(tipo, estado, ciudad, tipoProp, comp, precio));
    }

    private String pedirOpcional(String mensaje) {
        String valor = JOptionPane.showInputDialog(null, mensaje, "Filtro opcional", JOptionPane.QUESTION_MESSAGE);
        return valor == null ? "" : valor.trim();
    }

    private void bajaPropiedad() {
        int id = pedirId("Seleccione la propiedad que desea dar de baja:", sistema.propiedadesActivas(),
                Propiedad::getIdPropiedad, Propiedad::toString, false);
        sistema.darDeBajaPropiedad(id);
        JOptionPane.showMessageDialog(null, "Propiedad dada de baja.");
    }

    private void bajaCliente() {
        int id = pedirId("Seleccione el cliente que desea dar de baja:", sistema.clientesActivos(),
                Cliente::getIdCliente, Cliente::toString, false);
        sistema.darDeBajaCliente(id);
        JOptionPane.showMessageDialog(null, "Cliente dado de baja.");
    }

    private void bajaPropietario() {
        int id = pedirId("Seleccione el propietario que desea dar de baja:", sistema.propietariosActivos(),
                Propietario::getIdPropietario, Propietario::toString, false);
        sistema.darDeBajaPropietario(id);
        JOptionPane.showMessageDialog(null, "Propietario dado de baja.");
    }

    private void bajaVendedor() {
        int id = pedirId("Seleccione el vendedor que desea dar de baja:", sistema.vendedoresActivos(),
                Usuario::getIdUsuario, Object::toString, false);
        sistema.darDeBajaVendedor(id);
        JOptionPane.showMessageDialog(null, "Vendedor dado de baja.");
    }

    private <T> int pedirId(String mensaje, List<T> elementos, Function<T, Integer> obtenerId,
                            Function<T, String> descripcion, boolean permitirCero) {
        if ((elementos == null || elementos.isEmpty()) && !permitirCero) {
            throw new IllegalArgumentException("No hay registros disponibles para seleccionar.");
        }
        StringBuilder texto = new StringBuilder(mensaje).append("\n\n");
        if (elementos == null || elementos.isEmpty()) {
            texto.append("No hay registros disponibles.\n");
        }
        for (T elemento : elementos) {
            texto.append("ID: ").append(obtenerId.apply(elemento)).append(" - ")
                    .append(descripcion.apply(elemento)).append("\n");
        }
        if (permitirCero) texto.append("\n0 - Dejar sin asignar\n");
        texto.append("\nIngrese el ID:");
        while (true) {
            int id = pedirInt(texto.toString());
            if (permitirCero && id == 0) return 0;
            for (T elemento : elementos) {
                if (obtenerId.apply(elemento) == id) return id;
            }
            error("El ID ingresado no aparece en la lista. Seleccione uno de los ID mostrados.");
        }
    }

    private String pedirTexto(String mensaje) {
        return JOptionPane.showInputDialog(null, mensaje);
    }

    private String pedirObligatorio(String mensaje) {
        String v = pedirTexto(mensaje);
        if (v == null) throw new IllegalArgumentException("Operación cancelada.");
        if (v.isBlank()) throw new IllegalArgumentException("El campo no puede quedar vacío.");
        return v.trim();
    }

    private int pedirInt(String mensaje) {
        String v = pedirObligatorio(mensaje);
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingrese un número entero válido.");
        }
    }

    private double pedirDouble(String mensaje) {
        String v = pedirObligatorio(mensaje).replace(",", ".");
        try {
            double n = Double.parseDouble(v);
            if (!Double.isFinite(n)) throw new NumberFormatException();
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ingrese un número válido.");
        }
    }

    private boolean confirmar(String mensaje) {
        return JOptionPane.showConfirmDialog(null, mensaje, "Confirmación",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private String elegir(String mensaje, String[] opciones) {
        String v = (String) JOptionPane.showInputDialog(null, mensaje, "Seleccione",
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (v == null) throw new IllegalArgumentException("Operación cancelada.");
        return v;
    }

    private void mostrarLista(String titulo, List<?> lista) {
        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay registros para mostrar.", titulo,
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : lista) sb.append(o).append("\n");
        JOptionPane.showMessageDialog(null, sb.toString(), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje == null ? "Se produjo un error." : mensaje,
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }
}
