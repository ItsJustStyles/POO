/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.util.Random;

/**
 *
 * @author gabos
 */
public class PoseidonTrident extends Ataque{
    Random random = new Random();
    private String registro;

    public PoseidonTrident(int dano, Tablero tablero, String registro) {
        super(dano, tablero);
        this.registro = registro;
    }

    @Override
    public void aplicarDano(Casilla celda) {
    }
    
    public void ThreeLines(int x1,int y1, int x2 , int y2,int x3, int y3 ){
        Casilla c1 = tablero.CasillaPorCords(x1, y1);
        Casilla c2 = tablero.CasillaPorCords(x2, y2);
        Casilla c3 = tablero.CasillaPorCords(x3, y3);
        int dir = random.nextInt(4);
        int cant = random.nextInt(4)+1;
        int dir1 = random.nextInt(4);
        int cant1 = random.nextInt(4)+1;
        int dir2 = random.nextInt(4);
        int cant2 = random.nextInt(4)+1;
        atacarEnLinea(dir,cant,c1);
        atacarEnLinea(dir1,cant1,c2);
        atacarEnLinea(dir2,cant2,c3);
        
        
    }
    public void explotarXCasillas(int x){
    for(int i = 0; i < x; i++){
        int indexRandom = random.nextInt(tablero.casillas.size());
        Casilla c = tablero.casillas.get(indexRandom);
        tablero.recibirDanoLocacion(c.getX(), c.getY(), 100, registro);

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
    
}
