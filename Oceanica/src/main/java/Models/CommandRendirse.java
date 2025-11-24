/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandRendirse extends Command{

    public CommandRendirse(String[] args) {
        super(CommandType.RENDIRSE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        CommandEliminarJugador cmd = new CommandEliminarJugador(threadServidor.name, "R");
        cmd.processForServer(threadServidor);
               
    }
    
}
