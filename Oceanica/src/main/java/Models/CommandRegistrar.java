/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Cliente.Client;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandRegistrar extends Command{

    public CommandRegistrar(String nombre) {
        super(CommandType.REGISTRAR, new String[]{nombre});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        client.getRefFrame().aumentarAtaquesRealizados();
    }
    
    
    
}
