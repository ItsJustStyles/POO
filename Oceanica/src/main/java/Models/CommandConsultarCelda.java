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
public class CommandConsultarCelda extends Command{

    public CommandConsultarCelda(String[] args) {
        super(CommandType.CONSULTARCELDA, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String[] params = getParameters();
        String x = params[1];
        String y = params[2];
        
        CommandUnicast comandoARealizar = new CommandUnicast("CONSULTARCELDA", x, y, threadServidor.name);
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
