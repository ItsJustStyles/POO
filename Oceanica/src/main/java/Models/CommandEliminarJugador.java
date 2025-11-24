/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Servidor.Server;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandEliminarJugador extends Command{

    public CommandEliminarJugador(String objetivo, String s) {
        super(CommandType.ELIMINARJUGADOR, new String[]{objetivo, s});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String objetivo = getParameters()[0];
        String state = getParameters()[1];
        Server server = threadServidor.getRefServer();
        
        ThreadServidor target = server.getClientByName(objetivo);
        
        server.eliminarjugador(target);
        
        for(ThreadServidor p : server.turnOrder){
            System.out.println(p.name);
        }
        String estadoDerrota;
        if(state.equals("R")){
            estadoDerrota = " se ha rendido";
        }else{
            estadoDerrota = " ha sido eliminado";
        }
        server.broadcast(new CommandMessage(new String[]{"El jugador " + objetivo + estadoDerrota}));
        
        
        this.setIsBroadcast(false);
    
    }
    
    
}
