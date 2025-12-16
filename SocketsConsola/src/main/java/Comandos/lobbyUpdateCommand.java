/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.ThreadServidor;
import java.util.List;

/**
 *
 * @author lacay
 */
public class lobbyUpdateCommand extends Command{

    public lobbyUpdateCommand(List<String> playerNames) {
        super(CommandType.LOBBY, playerNames.toArray(new String[0]));
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        client.getRefFrame().clearLobby();
        client.getRefFrame().writeLobby("Jugadores:");
        String[] playersArray = this.getParameters();
        for(String p : playersArray){
            client.getRefFrame().writeLobby(p);
        }  
    }
    
    
    
}
