import javax.swing.*;

public class Main {
    public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> {
                Clase_modelo ventana=new Clase_modelo();
                ventana.iniciar();
            });
        }
    }
