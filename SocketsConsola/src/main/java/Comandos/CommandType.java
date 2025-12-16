/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author lacay
 */
public enum CommandType {
    UNICAST(2),
    ATAQUE(4),              // attack Andres 4 5
    HIT(0),
    MESSAGE(2),             // message hola a todos
    PRIVATE_MESSAGE(3),     // private Andres hola andres
    RENDIRSE(1),            // giveup
    ELIMINARJUGADOR(0),     // Elimina el jugador despues de que se rinde o pierde
    GANADOR(0),
    NAME(2),                // name Andres
    READY(1),               // nuevo comando: jugador listo
    START_GAME(0),          // nuevo comando: iniciar juego
    TEAM(0),
    LOBBY(0),
    SELECCIONARJUGADOR(2),
    ENVIARHEROES(0),
    PASARTURNO(0),
    USARCOMODIN(0),
    PARAMSCOMODIN(0),
    RESPUESTACMODIN(0),
    CHAT(2),
    CHAT_PRIVADO(3),
    MESSAGECONSOLA(0),
    LOG(0),
    REGISTROATAQUE(0),
    GASTARCOMODIN(0),
    NAME_RESPONSE(0);        
    
    private final int requiredParameters;

    private CommandType(int requiredParameters) {
        this.requiredParameters = requiredParameters;
    }

    public int getRequiredParameters() {
        return requiredParameters;
    }
}
