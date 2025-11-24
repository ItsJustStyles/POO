/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

/**
 *
 * @author lacay
 */
public abstract class Ataque {
    protected int dano;
    protected Tablero tablero;
    
    public Ataque(int dano, Tablero tablero){
        this.dano = dano;
        this.tablero = tablero;
    }
    
    public abstract void aplicarDano(Casilla celda);
}
