/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

/**
 *
 * @author lacay
 */
public class EstoyCodificando extends Ataque{
    Juego refFrame;
    private String registro;
    ReproductorSonido sonido;
    
    public EstoyCodificando(int dano, Tablero tablero, Juego refFrame, String registro) {
        this.refFrame = refFrame;
        this.registro = registro;
        super(dano, tablero);
    }

    @Override
    public void aplicarDano(Casilla celda) {
        
    }
    
    public void Microprocesador_x264(){
        sonido = new ReproductorSonido("/sonidos/Microprocesador.wav", refFrame);
        sonido.play();
        final int MAX_FILAS = 20;
        final int MAX_COLUMNAS = 30;

        int centroX = 10;   
        int centroY = 15;   
        int maxR = 10;      

        for (int x = 1; x <= MAX_FILAS; x++) {
            for (int y = 1; y <= MAX_COLUMNAS; y++) {
                int d = Math.abs(x - centroX) + Math.abs(y - centroY);         
                if (d <= maxR && d % 2 == 0) {
                    tablero.recibirDanoLocacion(x, y, 100,registro);
                }
            }
        }
        //sonido.close();
    }
    
    public void Slower(){
        sonido = new ReproductorSonido("/sonidos/Slower.wav", refFrame);
        sonido.play();
        final int MAX_FILAS = 20;
        final int MAX_COLUMNAS = 30;

        for (int x = 1; x <= MAX_FILAS; x++) {
            for (int y = 1; y <= MAX_COLUMNAS; y++) {

                boolean bordeArriba = (x == 1);
                boolean bordeAbajo = (x == MAX_FILAS);
                boolean bordeIzq = (y == 1);
                boolean bordeDer = (y == MAX_COLUMNAS);

                if (bordeArriba || bordeAbajo || bordeIzq || bordeDer) {
                    tablero.recibirDanoLocacion(x, y, 100,registro);
                }
            }
        }
        //sonido.close();
    }
    
    public void NoSePuedeMas(){
        sonido = new ReproductorSonido("/sonidos/NoSePuedeMas.wav", refFrame);
        sonido.play();
        final int MAX_FILAS = 20;
        final int MAX_COLUMNAS = 30;

        int centroX = 10;
        int centroY = 15;

        for (int x = 1; x <= MAX_FILAS; x++) {
            for (int y = 1; y <= MAX_COLUMNAS; y++) {
                if (x == centroX || y == centroY) {
                    tablero.recibirDanoLocacion(x, y, 100,registro);
                }
            }
        }
        //sonido.close();
    }
    
    public void EstoNoEsUnJuego(){
        for(Casilla c : tablero.casillas){
            tablero.recibirDanoLocacion(c.getX(), c.getY(), 10, registro);
        }
        ReproductorSonido sonido = new ReproductorSonido("/sonidos/EstoNoEsUnJuego.wav", refFrame);
        sonido.play();
        //sonido.close();
    }
    
}
