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
public class CommandStartGame extends Command {

    public CommandStartGame() {
        super(CommandType.START_GAME, new String[]{});
        this.setIsBroadcast(true);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // Este comando no se procesa en el servidor, solo se envía a los clientes
    }

    @Override
    public void processInClient(Client client) {
        //client.getRefFrame().writeMessage("¡El juego ha comenzado!");
        client.getRefFrame().startGame(); // CORREGIDO: debe invocar al método con paréntesis
    }
}