package com.mycompany.thewalkingtec;

// Clase raíz del JSON
public class ConfiguracionJuegoCompleta {
    // Gson mapeará automáticamente a las clases definidas
    private JugadorConfig[] jugadores;
    private PersonajeConfig[] personajes;
    
    public ConfiguracionJuegoCompleta() {}
    
    // Getters y Setters
    public JugadorConfig[] getJugadores() { return jugadores; }
    public PersonajeConfig[] getPersonajes() { return personajes; }
    public void setJugadores(JugadorConfig[] j) { this.jugadores = j; }
    public void setPersonajes(PersonajeConfig[] p) { this.personajes = p; }
}