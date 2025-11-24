/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author gabos
 */
public class WavesControl extends Ataque {
    
     private final Random random = new Random();
     private String registro;
     
     private final List<Casilla> casillasRadioactivas = new ArrayList<>();
     Juego refFrame;

    

    public WavesControl(int dano, Tablero tablero, String registro) {
        super(dano, tablero);
        this.registro = registro;
    }

    @Override
    public void aplicarDano(Casilla celda) {
    }
    
    public void SwirlRaising(){
        Casilla casilla = casillaRandom();
        int radio = random.nextInt(9) + 2;
        Remolino r = new Remolino(casilla.getX(),casilla.getY(),radio,true);
        tablero.remolinos.add(r);
        danoArea(casilla,radio);
                
    }
    public void sendHumanGarbage() {
        if (tablero.remolinos.isEmpty()) return;

        Remolino r = tablero.remolinos.get(random.nextInt(tablero.remolinos.size()));
        int toneladas = 10 * r.getRadio();

        for (int i = 0; i < toneladas; i++) {
            Casilla destino = tablero.casillas.get(random.nextInt(tablero.casillas.size()));

            tablero.recibirDanoLocacion(destino.getX(), destino.getY(), 25, registro);

            if (random.nextBoolean()) {
                synchronized (casillasRadioactivas) {
                    casillasRadioactivas.add(destino); //50 porciento de probabilidad de ser radioactiva 
                }
            }
        }
    }
    public void radioactiveRush() {
        if (casillasRadioactivas.isEmpty()) return;

        int segundos = random.nextInt(10) + 1; 
        
        for (Casilla c : casillasRadioactivas) {
            tablero.recibirDanoLocacion(c.getX(), c.getY(), 10*segundos, registro);
            
        }
    }

    
    public Casilla casillaRandom(){
        int x = random.nextInt(20);
        int y = random.nextInt(30);
        return tablero.CasillaPorCords(x, y);
    }
    
    
    public void danoArea(Casilla celda,int rango){
        int filaInicial = celda.getX();
        int columnaInicial = celda.getY();
        final int MAX_FILAS = 20;
        final int MAX_COLUMNAS = 30;
        
        
        for(int fila = filaInicial - rango; fila <= filaInicial + rango; fila++){
            for(int columna = columnaInicial - rango; columna <= columnaInicial + rango; columna++){
                if(fila == filaInicial && columna == columnaInicial){
                    continue;
                }
                
                boolean filaValida = (fila >= 0 && fila <= MAX_FILAS);
                boolean columnaValida = (columna >= 0 && columna <= MAX_COLUMNAS);
                
                if(filaValida && columnaValida){
                    tablero.recibirDanoLocacion(fila, columna, dano, registro);
                }
            }
        }
    }
    
}
