/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Servidor.Server;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
class CommandEliminarJugador extends Command{

    public CommandEliminarJugador(String objetivo, String s){
        super(CommandType.ELIMINARJUGADOR, new String[]{objetivo, s});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String objetivo = getParameters()[0];
        String state = getParameters()[1];
        Server server = threadServidor.getRefServer();
        
        ThreadServidor target = server.getClientByName(objetivo);
        
        server.eliminarJugador(target);
        String estadoDerrota;
        if(state.equals("R")){
            estadoDerrota = " se ha rendido";
        }else{
            estadoDerrota = " ha sido derrotado";
        }
        server.broadcast(new CommandMessage(new String[]{"El jugador " + objetivo + estadoDerrota}));
        
        this.setIsBroadcast(false);
    }
    
}
