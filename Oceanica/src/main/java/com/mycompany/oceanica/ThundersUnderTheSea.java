/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.util.Random;

/**
 *
 * @author lacay
 */
public class ThundersUnderTheSea extends Ataque{
    Random random = new Random();
    private String registro;
    public ThundersUnderTheSea(int dano, Tablero tablero, String registro) {
        super(dano, tablero);
        this.registro = registro;
    }

    @Override
    public void aplicarDano(Casilla celda) {
        
    }
    
    public void Thunder_rain(){
        for(int i = 0; i < 100; i++){
            int damageThunder = random.nextInt(11) + 10;
            int indexRandom = random.nextInt(tablero.casillas.size());
            Casilla c = tablero.casillas.get(indexRandom);
            tablero.recibirDanoLocacion(c.getX(), c.getY(), damageThunder, registro);
        }
    }
    
    public void Poseidon_thunders(){
        int rayos = random.nextInt(6) + 5;
        for(int i = 0; i < rayos; i++){
            int rango = random.nextInt(9) + 2;
            int indexRandom = random.nextInt(tablero.casillas.size());
            Casilla c = tablero.casillas.get(indexRandom);
            danoArea(c, rango);
        }
    }
    
    public void Eel_attack(){
        int anguilas = random.nextInt(76) + 25;
        for(int i = 0; i < anguilas; i++){
            int descargas = random.nextInt(10) + 1;
            int indexRandom = random.nextInt(tablero.casillas.size());
            Casilla c = tablero.casillas.get(indexRandom);
            tablero.recibirDanoLocacion(c.getX(), c.getY(), 10*descargas, registro);
        }
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
