package com.mycompany.thewalkingtec;

import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.Timer;
// Se asume que importas Timer si EstructuraCentral usa su propio Timer o si Defensas lo hace.


public class EstructuraCentral extends Defensas {
    private int vidaMaxima = 200;
    
    public EstructuraCentral(int fila, int columna, Tablero tablero) {
        super(    // nombrePersonaje: El nombre es fijo
            200,                    // vidaMaxima: Vida fija a 200 (como lo tenías)
            0,                      // dano: Fijo a 0 (la base no ataca)
            0,                      // velocidad: Fijo a 0
            "/imagenes/SantoGrial.png", // rutaImagen: La ruta es fija
            0,                      // rango: Fijo a 0
            fila,                   // fila: Posición variable
            columna,                // columna: Posición variable
            tablero                 // tablero: Referencia al tablero
        );
    }

    @Override
    public void destruir() {
        super.destruir(); // Llama a la lógica de destrucción (eliminar de la lista)
        System.out.println("🚨🚨 ¡FIN DEL JUEGO! La base central ha sido destruida. 🚨🚨");
        tableroAsociado.finalizarJuego(false);
       
    }
    
    public void restaurarVidaCompleta() {
        this.vidaActual = this.vidaMaxima;
        
        System.out.println("Base Central restaurada. Vida actual: " + this.vidaActual);
        
        if (tableroAsociado != null) {
            tableroAsociado.repaint();
        }
    }
}