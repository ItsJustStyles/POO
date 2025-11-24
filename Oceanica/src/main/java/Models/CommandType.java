package Models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



/**
 * Tipos de comandos disponibles en el sistema de red.
 * Cada comando define cuántos parámetros requiere.
 * 
 * @author diego
 */
public enum CommandType {
    ATAQUE(5),              // attack Andres 4 5
    MESSAGE(2),             // message hola a todos
    PRIVATE_MESSAGE(3),     // private Andres hola andres
    GIVEUP(1),              // giveup
    NAME(2),                // name Andres
    READY(1),               // nuevo comando: jugador listo
    START_GAME(0),          // nuevo comando: iniciar juego
    LOBBY(0),
    HIT(0),
    REGISTRAR(0),
    TEAM(2),
    PORCENTAJECELDAS(1),
    PINTARVIVAS(1),
    MOSTRARCELDASOCUPADAS(1),
    CONSULTARCELDA(3),
    SALTARTURNO(1),
    LOGRESUMEN(1),
    RENDIRSE(1),
    UNICAST(1),
    ELIMINARJUGADOR(0),
    GANADOR(0);

    private final int requiredParameters;

    private CommandType(int requiredParameters) {
        this.requiredParameters = requiredParameters;
    }

    public int getRequiredParameters() {
        return requiredParameters;
    }
}
