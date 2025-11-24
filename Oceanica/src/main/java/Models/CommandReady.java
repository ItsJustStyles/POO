package Models;

import java.io.Serializable;
import Cliente.Client;
import Servidor.ThreadServidor;

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
        client.getRefFrame().writeMessage("Jugador listo: " + playerName);
    }

    @Override
    public String toString() {
        return "Jugador listo: " + playerName;
    }
}



