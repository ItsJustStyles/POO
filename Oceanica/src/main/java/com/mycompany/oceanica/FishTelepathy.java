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
public class FishTelepathy extends Ataque{
    Random random = new Random();
    private String registro;
    
    public FishTelepathy(int dano, Tablero tablero, String registro) {
        super(dano, tablero);
        this.registro = registro;
    }

    @Override
    public void aplicarDano(Casilla celda) {
        
    }
    
    public void Cardumen(){
        int peces = random.nextInt(201) + 100;
        for(int i = 0; i < peces; i++){
            int indexRandom = random.nextInt(tablero.casillas.size());
            Casilla c = tablero.casillas.get(indexRandom);
            tablero.recibirDanoLocacion(c.getX(), c.getY(), 33, registro);
        }
    }
    
    public void Shark_attack(){
        int rango = random.nextInt(10) + 1;
        Casilla c1 = tablero.CasillaPorCords(1, 1);
        Casilla c2 = tablero.CasillaPorCords(20, 1);
        Casilla c3 = tablero.CasillaPorCords(1, 30);
        Casilla c4 = tablero.CasillaPorCords(20, 30);
        
        danoArea(c1, rango);
        danoArea(c2, rango);
        danoArea(c3, rango);
        danoArea(c4, rango);
    }
    
    public void Pulp(){
        int pulpos = (random.nextInt(21) + 30) * 8;
        for(int i = 0; i < pulpos; i++){
            int indexRandom = random.nextInt(tablero.casillas.size());
            Casilla c = tablero.casillas.get(indexRandom);
            tablero.recibirDanoLocacion(c.getX(), c.getY(), 25, registro);
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
