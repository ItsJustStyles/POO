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
public class ReleaseTheKraken extends Ataque{
    private Random random = new Random();
    private String registro;
    
    
    public ReleaseTheKraken(int dano, Tablero tablero, String registro) {
        super(dano, tablero);
        this.registro = registro;
    }

    
    public void tentaculos(){
        for(int i = 0; i < 3; i++){
            int indexRandom = random.nextInt(tablero.casillas.size());
            Casilla casillaRandom = tablero.casillas.get(indexRandom);
            danoArea(casillaRandom,1);
        }
    }
  
    public void kraken_breath(int x , int y){
        Casilla c = tablero.CasillaPorCords(x, y);
        int indexRandom = random.nextInt(4);
        int numCasillas = random.nextInt(8)+1;
        atacarEnLinea(indexRandom,numCasillas,c);

        
        
        
    }
    
    public void release_the_kraken(){
    int indexRandom = random.nextInt(tablero.casillas.size());
    int rangoRandom = random.nextInt(9)+1;
    Casilla casillaRandom = tablero.casillas.get(indexRandom);
    danoArea(casillaRandom,rangoRandom);
    System.out.println(rangoRandom);
    

        
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
    public void atacarEnLinea(int dir,int cant,Casilla celda){
        switch (dir) {
            case 0:
                for (int fila = celda.getX(); fila >= celda.getX()-cant; fila--) {
                    boolean filaValida = fila >= 0;
                    if (filaValida){
                        tablero.recibirDanoLocacion(fila,celda.getY(),dano, registro);
                    }
                }
                break;
            case 1:
                for (int fila = celda.getX(); fila <= celda.getX()+cant; fila++) {
                    boolean filaValida = fila <= 20;
                    if (filaValida){
                        tablero.recibirDanoLocacion(fila,celda.getY(),dano, registro);
                    }
                }
                break;
            case 2:
                for (int columna = celda.getY(); columna <= celda.getY() + cant; columna++) {
                    boolean columnaValida = columna <= 30;
                    if (columnaValida){
                        tablero.recibirDanoLocacion(celda.getX(),columna,dano, registro);
                    }
                }
                break;
            default:
                for (int columna = celda.getY(); columna >= celda.getY() - cant; columna--) {
                    boolean columnaValida = columna >= 0;
                    if (columnaValida){
                        tablero.recibirDanoLocacion(celda.getX(),columna,dano, registro);
                    }
                }
                break;
            
        }
    }

    @Override
    public void aplicarDano(Casilla celda) {
        
    }
    
    
    
    
    
}
