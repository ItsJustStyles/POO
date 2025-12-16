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
public class CommandTurn extends Command {

    public CommandTurn(String currentPlayerName) {
        super(CommandType.MESSAGE, new String[]{"TURN", currentPlayerName});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // No hace nada en el servidor
    }

    @Override
    public void processInClient(Client client) {
        String turnoJugador = this.getParameters()[1];

        if (client.name.equals(turnoJugador)) {
            client.getRefFrame().writeBitacora("➡️ ES TU TURNO");
        } else {
            client.getRefFrame().writeBitacora("Turno de: " + turnoJugador);
        }
    }
}
