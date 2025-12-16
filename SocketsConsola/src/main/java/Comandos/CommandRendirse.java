/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandRendirse extends Command{
    private String senderName;
    public CommandRendirse(String[] args) {
        super(CommandType.RENDIRSE, args);
    }
    
    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String nombreRemitente = threadServidor.name;
        this.setSenderName(nombreRemitente);
        CommandEliminarJugador jugador = new CommandEliminarJugador(threadServidor.name, "R");
        jugador.processForServer(threadServidor);
    }

    @Override
    public void processInClient(Client client) {
        String nombreRemitente = ((CommandRendirse)this).getSenderName();
        
        if(client.name.equals(nombreRemitente)){
            client.getRefFrame().guardarLogsJugador("RENDIRSE", "", "Ok");
        }
        
        client.getRefFrame().gestor.incrementarGiveup(client.name, 1);
    }
}