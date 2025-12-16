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
public class CommandMessageConsola extends Command{

    public CommandMessageConsola(String msg) {
        super(CommandType.MESSAGECONSOLA, new String[]{msg});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {

    }

    @Override
    public void processInClient(Client client) {
        String msg = this.getParameters()[0];
        client.getRefFrame().writeConsola(msg);
    }

}
