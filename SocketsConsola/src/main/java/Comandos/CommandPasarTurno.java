/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.Server;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandPasarTurno extends Command{

    public CommandPasarTurno(String[] args) {
        super(CommandType.PASARTURNO, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        CommandUnicast comandoARealizar = new CommandUnicast("PASARTURNO", threadServidor.name);
        
        Server server = threadServidor.getRefServer();
        ThreadServidor jugadorActual = server.getCurrentTurnPlayer();
        
        if(jugadorActual == threadServidor){
            server.avanzarTurno();
        }
        try {
            jugadorActual.objectSender.writeObject(comandoARealizar);
            jugadorActual.objectSender.flush();
        } catch (java.io.IOException ex) {
            //threadServidor.getRefServer().getRefFrame().writeMessage("Error al enviar ataque a " + threadServidor.name);
        }
        
        String[] broadcastParams = new String[]{threadServidor.name};
        CommandPasarTurno broadcastCommand = new CommandPasarTurno(broadcastParams);
        threadServidor.getRefServer().broadcast(broadcastCommand);
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String nombre = params[0];
        
        if(!client.name.equals(nombre)){
            client.getRefFrame().writeBitacora("El jugador " + nombre + " salto su turno");
        }
    }

}
