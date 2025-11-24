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
public class CommandPintarVivas extends Command{

    public CommandPintarVivas(String[] args) {
        super(CommandType.PINTARVIVAS, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        CommandUnicast comandoARealizar = new CommandUnicast("PINTARVIVAS", "x", "y", threadServidor.name);
        ThreadServidor targetThread = threadServidor.getRefServer().getClientByName(threadServidor.name);
        try {
            targetThread.objectSender.writeObject(comandoARealizar);
            targetThread.objectSender.flush();
        } catch (java.io.IOException ex) {
            // Manejar la desconexión del objetivo
            threadServidor.getRefServer().getRefFrame().writeMessage("Error al enviar ataque a " + threadServidor.name);
        }
    }
    
}
