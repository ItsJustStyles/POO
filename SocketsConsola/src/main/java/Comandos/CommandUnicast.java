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
public class CommandUnicast extends Command{

    public CommandUnicast(String comando, String nombre) {
        super(CommandType.UNICAST, new String[]{comando, nombre});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String comandoARealizar = params[0];
        String nombre = params[1];
        
        if(comandoARealizar.equals("PASARTURNO")){
            client.getRefFrame().writeBitacora("Has saltado tu turno");
        }else if(comandoARealizar.equals("USARCOMODIN")){
            if(client.getRefFrame().getComodinDesbloqueado()){
                client.getRefFrame().actualizarComodin();
                if(client.getRefFrame().getComodinTipo() == false){
                    client.getRefFrame().iniciarComodin();
                }
            }else{
                client.getRefFrame().writeConsola("El comodin aun no se ha desbloqueado");
            }
        }else if(comandoARealizar.equals("LOG")){
            client.getRefFrame().VerLog(nombre);
        }
        
        client.getRefFrame().guardarLogsJugador(comandoARealizar, "", "Ok");
        
    }
     
}