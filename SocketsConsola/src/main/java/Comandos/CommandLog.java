/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Servidor.Server;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandLog extends Command{

    public CommandLog(String[] args) {
        super(CommandType.LOG, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        CommandUnicast comandoARealizar = new CommandUnicast("LOG", threadServidor.name);
        ThreadServidor jugadorActual = threadServidor.getRefServer().getClientByName(threadServidor.name);
        
        try {
            jugadorActual.objectSender.writeObject(comandoARealizar);
            jugadorActual.objectSender.flush();
        } catch (java.io.IOException ex) {
            //threadServidor.getRefServer().getRefFrame().writeMessage("Error al enviar ataque a " + threadServidor.name);
        }
    }
    
}
