package com.mycompany.oceanica;

import java.util.Random;

/**
 *
 * @author lacay
 */
public class HoraDeBalatrear extends Ataque {
    Juego refFrame;
    private String registro;
    ReproductorSonido sonido;

    public HoraDeBalatrear(int dano, Tablero tablero, Juego refFrame, String registro) {
        this.refFrame = refFrame;
        this.registro = registro;
        super(dano, tablero);
    }

    @Override
    public void aplicarDano(Casilla celda) { }

    public void BluePrint() {
        sonido = new ReproductorSonido("/sonidos/explosion1.wav", refFrame);
        sonido.play();
        int x = 1;
        int y = 1;

        while (x <= 20 && y <= 30) {
            tablero.recibirDanoLocacion(x, y, 100,registro);
            x++;
            y++;
        }
        //sonido.close();
    }

    public void Balatrito() {
        sonido = new ReproductorSonido("/sonidos/win.wav", refFrame);
        sonido.play();
        Random random = new Random();
        for (int i = 0; i < 40; i++) {
            Casilla c = tablero.casillas.get(random.nextInt(tablero.casillas.size()));
            tablero.recibirDanoLocacion(c.getX(), c.getY(), 100,registro);
        }
        //sonido.close();
    }

    public void Cavendish() {
        sonido = new ReproductorSonido("/sonidos/multhit2.wav", refFrame); //hay q cambiarlo o hacerle un loop abajo
        //Cambie esto para que ahora no sean fijas int[] filas = {5, 10, 15};
        Random random = new Random();
        int f1 = random.nextInt(20) + 1;
        int f2 = random.nextInt(20) + 1;
        int f3 = random.nextInt(20) + 1;
        
        int[] filas = {f1, f2, f3};

        for (int f : filas) {
            for (int y = 1; y <= 30; y++) {
                tablero.recibirDanoLocacion(f, y, 100,registro);
                sonido.playSeguido();
            try {
                Thread.sleep(70); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                }
            }
        }
        sonido.close();
    }
}
