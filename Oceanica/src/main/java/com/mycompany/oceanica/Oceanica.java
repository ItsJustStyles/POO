package com.mycompany.oceanica;

import com.formdev.flatlaf.FlatIntelliJLaf;
import static com.mycompany.oceanica.BoreTunnel.stopBoreTunnel;
import java.io.IOException;
import javax.swing.SwingUtilities;

public class Oceanica {
    public static void main(String[] args) {

        FlatIntelliJLaf.setup();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Cerrando aplicación. Deteniendo Bore Tunnel...");
            stopBoreTunnel(); // Llama al método estático para detener Bore
            System.out.println("Bore Tunnel detenido.");
        }));
        
        SwingUtilities.invokeLater(() -> {
            try {
                Juego miJuego = new Juego();
                miJuego.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
                miJuego.pack();
                miJuego.setLocationRelativeTo(null);
                miJuego.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        
    }
}