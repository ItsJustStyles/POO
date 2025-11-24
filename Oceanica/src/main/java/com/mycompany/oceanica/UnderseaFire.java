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
public class UnderseaFire extends Ataque {
     private final Random random = new Random();
     private String registro;

    public UnderseaFire(int dano, Tablero tablero, String registro) {
        super(dano, tablero);
        this.registro = registro;
    }
    
    public void volcanoRaising(){
        Casilla casilla = casillaRandom();
        int radio = random.nextInt(10) + 1;
        Volcan v = new Volcan(casilla.getX(),casilla.getY(),radio,true);
        tablero.volcanes.add(v);
        danoArea(casilla,radio);
    }
    public void volcanoExplosion(){
        if (tablero.volcanes.isEmpty()) return;

        Volcan v = tablero.volcanes.get(random.nextInt(tablero.volcanes.size()));
        int piedras = 10 * v.getRadio();

        for (int i = 0; i < piedras; i++) {
            Casilla destino = tablero.casillas.get(random.nextInt(tablero.casillas.size()));
            tablero.recibirDanoLocacion(destino.getX(), destino.getY(), 20, registro);
        }
  
    
    }
    public void termalRush(){
        if (tablero.volcanes.isEmpty()) return;
        Volcan v = tablero.volcanes.get(random.nextInt(tablero.volcanes.size()));
        int segundos = random.nextInt(2) + 5; // 5 o 6
        int dañoPorSegundo = v.getRadio();
        int dañoTotal = dañoPorSegundo * segundos;
        Casilla centro = tablero.CasillaPorCords(v.getX(), v.getY());
        danoAreaTermal(centro, 5, dañoTotal);
        
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
    
    private void danoAreaTermal(Casilla centro, int rango, int dañoTotal) {
    int x0 = centro.getX();
    int y0 = centro.getY();

    final int MAX_FILAS = 20;
    final int MAX_COLUMNAS = 30;

    for (int x = x0 - rango; x <= x0 + rango; x++) {
        for (int y = y0 - rango; y <= y0 + rango; y++) {

            if (x == x0 && y == y0) continue; //para que no dañe al volcan 
            boolean valido = (x >= 1 && x <= MAX_FILAS &&
                              y >= 1 && y <= MAX_COLUMNAS);
            if (valido) {
                tablero.recibirDanoLocacion(x, y, dañoTotal, registro);
            }
        }
    }
    }
    
    public Casilla casillaRandom(){
        int x = random.nextInt(20);
        int y = random.nextInt(30);
        return tablero.CasillaPorCords(x, y);
    }


    @Override
    public void aplicarDano(Casilla celda) {
    }
    
}
