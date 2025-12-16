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
public class CommandRegistroAtaque extends Command{

    public CommandRegistroAtaque(String p, String objetivo, String arma) {
        super(CommandType.REGISTROATAQUE, new String[]{p,objetivo, arma});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String p = params[0];
        String objetivo = params[1];
        String arma = params[2];
        
        String msg = "Atacaste al jugador: " + objetivo + " usando a:" + p;
        String msg2 = "Weapon: " + arma;
        client.getRefFrame().writeLog2(msg, msg2);
        client.getRefFrame().gestor.incrementarAttacks(client.name, 1);
        client.getRefFrame().gestor.incrementarSuccess(client.name, 1);
        client.getRefFrame().actualizarHUD(client.name);
    }
    
    
}
