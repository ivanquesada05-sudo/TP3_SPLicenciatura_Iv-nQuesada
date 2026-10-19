package inmobiliariahorizonte;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) { /* Se conserva el aspecto predeterminado. */ }
            SistemaInmobiliaria sistema = new SistemaInmobiliaria();
            Menu menu = new Menu(sistema);
            menu.iniciar();
        });
    }
}
