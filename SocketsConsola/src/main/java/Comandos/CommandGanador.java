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
public class CommandGanador extends Command{

    public CommandGanador(String nombre) {
        super(CommandType.GANADOR, new String[]{nombre});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
         String ganador = getParameters()[0];
         if(client.name.equals(ganador)){
             client.getRefFrame().mostrarVictoria(ganador);
             client.getRefFrame().gestor.incrementarWins(client.name, 1);
         }
         
         
    }
    
    
    
}
