/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.thewalkingtec;
/**
 *
 * @author maicol
 */
public class Partida {
    private String nombre;
    private int nivel;

    public Partida(String nombre) {
        this.nombre = nombre;
        this.nivel = 1;
    }

    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; } // ⭐️ AÑADIR ESTE MÉTODO
}

