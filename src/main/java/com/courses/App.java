package com.courses;

import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        int opcionMenu = -1;
        String[] botones = {
                "1. Ver gatos.",
                "2. Salir."
        };

        do {
            // Menu principal
            String opcion = (String) JOptionPane.showInputDialog(null, "Gatitos java", "Menu principal",
                    JOptionPane.INFORMATION_MESSAGE, null, botones, botones[0]);

            // Validamos que opcion selecciona el usuario
            for (int i = 0; i < botones.length; i++) {
                if (opcion.equals(botones[i])) {
                    opcionMenu = i;
                }
            }

            // Validar opcion seleccionada
            switch (opcionMenu) {
                case 0:
                    GatosService.verGatos();
                    break;
                default:
                    break;
            }
        } while (opcionMenu != 1);
    }
}
