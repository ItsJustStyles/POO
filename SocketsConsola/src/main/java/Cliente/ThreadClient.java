/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import Comandos.Command;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lacay
 */
public class ThreadClient extends Thread{
    private Client client;
    
    private boolean isRunning = true;

    public ThreadClient(Client client) {
        this.client = client;

    }
    
    public void run (){
        
        Command comandoRecibido;
        while (isRunning){
            try {
                comandoRecibido = (Command) client.objectListener.readObject();
                //receivedMessage = client.getListener().readUTF(); //espera hasta recibir un String desde el cliente que tiene su socket
                comandoRecibido.processInClient(client);
            } catch (IOException ex) {
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ThreadClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            
        }
        
    }
 
}
