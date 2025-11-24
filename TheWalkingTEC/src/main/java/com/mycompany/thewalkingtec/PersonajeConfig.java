/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.thewalkingtec;

/**
 *
 * @author araya
 */

// PersonajeConfig.java

public class PersonajeConfig {
    private String nombre;
    private int vida;
    private int dano;
    private int velocidad;
    private String ruta;
    private String tipo;
    private int rango;

    public PersonajeConfig() {}

    public PersonajeConfig(String nombre, int vida, int dano, int velocidad, String ruta, String tipo, int rango) {
        this.nombre = nombre;
        this.vida = vida;
        this.dano = dano;
        this.velocidad = velocidad;
        this.ruta = ruta;
        this.tipo = tipo;
        this.rango = rango;
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getVida() { return vida; }
    public int getDano() { return dano; }
    public int getVelocidad() { return velocidad; }
    public String getRuta() { return ruta; }
    public String getTipo() { return tipo; }
    public int getRango() { return rango; }

    // Setters (opcionales pero recomendados)
    public void setNombre(String n) { this.nombre = n; }
    public void setVida(int v) { this.vida = v; }
    public void setDano(int d) { this.dano = d; }
    public void setVelocidad(int vel) { this.velocidad = vel; }
    public void setRuta(String r) { this.ruta = r; }
    public void setTipo(String t) { this.tipo = t; }
    public void setRango(int r) { this.rango = r; }
}