package com.mycompany.oceanica;

import java.awt.Desktop;
import java.net.URI;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class WebPopupExternal {

    private final String url;

    public WebPopupExternal(String url) {
        this.url = url;
        openBrowser();
    }

    private void openBrowser() {
        // Mostramos ventana Swing tipo “popup” mientras se abre el navegador

        // Abrimos la URL en el navegador predeterminado
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
