package com.mycompany.thewalkingtec;

public class JugadorConfig {
    private String nombre;
    private int nivel;

    public JugadorConfig() {}

    // Constructor (opcional)
    public JugadorConfig(String nombre, int nivel) {
        this.nombre = nombre;
        this.nivel = nivel;
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public void setNombre(String n) { this.nombre = n; }
    public void setNivel(int n) { this.nivel = n; }
}