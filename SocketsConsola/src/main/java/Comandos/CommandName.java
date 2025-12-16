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
public class CommandName extends Command{
    
    public CommandName(String[] args) { 
        super(CommandType.NAME, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String nombrePropuesto = getParameters()[1];
        Server server = threadServidor.getRefServer();
        ThreadServidor existingThread = server.getClientByName(nombrePropuesto);

        try {
            if (existingThread != null) {
                // A. NOMBRE RECHAZADO (Lógica correcta, sin broadcast)
                Command nameDenied = new CommandNameResponse(new String[]{"false", "El nombre '" + nombrePropuesto + "' ya está en uso."});
                threadServidor.objectSender.writeObject(nameDenied);
                threadServidor.getRefServer().getRefFrame().writeConsola("Nombre rechazado: " + nombrePropuesto);
                threadServidor.isRunning = false;
                threadServidor.socket.close();

                // Asegurarse de que no se haga broadcast al final
                this.setIsBroadcast(false); 

            } else {
                // B. NOMBRE ACEPTADO
                threadServidor.name = nombrePropuesto;
                server.addThreadServidor(threadServidor);

                // Enviar respuesta de aceptación al cliente (privado)
                Command nameAccepted = new CommandNameResponse(new String[]{"true", "Nombre aceptado. Conectando..."});
                threadServidor.objectSender.writeObject(nameAccepted);

                // Notificar a todos sobre el nuevo jugador (ya manejado por showAllClients())
                threadServidor.showAllClients(); 

                // Asegurarse de que no se haga broadcast al final (ya se hizo)
                this.setIsBroadcast(false); 
            }
        } catch (java.io.IOException e) {
            // Manejar error de comunicación
            server.getRefFrame().writeConsola("Error al responder al cliente " + nombrePropuesto + ": " + e.getMessage());
        }
    }
    
    @Override
    public void processInClient(Client client) {
        //client.getRefFrame().writeMessage("Conectado el cliente: " + this.getParameters()[1]);
    }

}
