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
public class CommandChat extends Command{
    private String senderName;
    public CommandChat(String[] args) {
        super(CommandType.CHAT, args);
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String nombreRemitente = threadServidor.name;
        this.setSenderName(nombreRemitente);
        this.setIsBroadcast(true);
    }

    @Override
    public void processInClient(Client client) {
        String nombreRemitente = ((CommandChat)this).getSenderName();
        
        String[] text = getParameters();
        String textoEnviar = "";
        for(int i = 1; i < text.length - 4; i++){
            textoEnviar += text[i] + " ";
        }
        client.getRefFrame().writeBitacora(nombreRemitente + ": " + textoEnviar);
        
        if(client.name.equals(nombreRemitente)){
            client.getRefFrame().guardarLogsJugador("CHAT", textoEnviar, "Ok");
        }
    }
 
}