/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.ThreadServidor;
import java.io.Serializable;

/**
 *
 * @author lacay
 */
public class CommandReady extends Command implements Serializable {
    private String playerName;

    public CommandReady(String playerName) {
        super(CommandType.READY, new String[]{playerName}); // 
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
        threadServidor.getRefServer().markPlayerReady(playerName);
    }

    @Override
    public void processInClient(Client client) {
        client.getRefFrame().writeLobby("Jugador listo: " + playerName);
    }

    @Override
    public String toString() {
        return "Jugador listo: " + playerName;
    }
}
