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
public class CommandUnicast extends Command{

    public CommandUnicast(String comando, String x, String y, String nombre) {
        super(CommandType.UNICAST, new String[]{comando, x, y, nombre});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String comandoARealizar = params[0];
        String nombre = params[3];
        
        int x;
        int y;
        if(comandoARealizar.equals("CONSULTARCELDA")){
            x = Integer.valueOf(params[1]);
            y = Integer.valueOf(params[2]);
        }else{
            x = 0;
            y = 0;
        }
        
        if(comandoARealizar.equals("SALTARTURNO")){
            client.getRefFrame().writeBitacora("Has saltado tu turno");
        }
        
        client.getRefFrame().comandos(comandoARealizar, x, y);
    }
     
}
