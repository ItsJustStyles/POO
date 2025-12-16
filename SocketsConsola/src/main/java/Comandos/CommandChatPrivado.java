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
public class CommandChatPrivado extends Command{
    String senderName;
    public CommandChatPrivado(String[] args) {
        super(CommandType.CHAT_PRIVADO, args);
    }

    public String getRemitente() {
        return senderName;
    }

    public void setRemitente(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        String nombreRemitente = threadServidor.name;
        this.setRemitente(nombreRemitente);
        this.setIsBroadcast(false);
    }

    @Override
    public void processInClient(Client client) {
        String nombreRemitente = ((CommandChatPrivado)this).getRemitente();
        
        String[] text = getParameters();
        String textoEnviar = "";
        for(int i = 2; i < text.length - 4; i++){
            textoEnviar += text[i] + " ";
        }
        client.getRefFrame().writeBitacora(nombreRemitente + ": " + textoEnviar);
        if(client.name.equals(nombreRemitente)){
            client.getRefFrame().guardarLogsJugador("CHATPRIVADO", textoEnviar, "Ok");
        }
    }
    
}
