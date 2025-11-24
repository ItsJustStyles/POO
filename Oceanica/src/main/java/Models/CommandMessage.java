/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Cliente.Client;
import Models.CommandType;
import Models.CommandType;
import Models.CommandType;
import Servidor.ThreadServidor;

/**
 *
 * @author diego
 */
public class CommandMessage extends Command{

    public CommandMessage(String[] args) {
        super(CommandType.MESSAGE, args);
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(true);
        
    }
    
    @Override
    public void processInClient(Client client) {
        //Message "string"
        client.getRefFrame().writeBitacora("" + this.getParameters()[0]);
    }
    
}
