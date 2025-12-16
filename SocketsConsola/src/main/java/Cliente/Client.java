/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import Comandos.Command;
import Comandos.CommandFactory;
import Comandos.CommandTeam;
import com.mycompany.socketsconsola.Juego;
import com.mycompany.socketsconsola.Personajes;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author lacay
 */
public class Client {
    private int PORT;
    private final String IP_ADDRESS;
    private Socket socket;
    private Juego refFrame;
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    private ThreadClient threadClient;
    
    public String name;
    List<Personajes> heroes;
    
    

    public Client(Juego refFrame, String name, List<Personajes> heroes, int PORT, String IP_ADDRESS) {
        this.PORT = PORT;
        this.IP_ADDRESS = IP_ADDRESS;
        this.refFrame = refFrame;
        this.name = name;
        this.heroes = heroes;
        this.connect();
        
    }
    
    private void connect (){
        try {
            socket = new Socket(IP_ADDRESS , PORT);
            objectSender =  new ObjectOutputStream (socket.getOutputStream());
            objectSender.flush();
            objectListener =  new ObjectInputStream (socket.getInputStream());
            
            threadClient =  new ThreadClient(this);
            threadClient.start();
            
            
            //envía el nombre
            String args[] = {"NAME", this.name};
            objectSender.writeObject(CommandFactory.getCommand(args));
            
            //enviar los personajes seleccionados xd
            CommandTeam comandoEquipo = new CommandTeam(this.heroes);
            objectSender.writeObject(comandoEquipo);
            objectSender.flush();
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public ObjectInputStream getObjectListener() {
        return objectListener; 
    }

    public ObjectOutputStream getObjectSender() {
        return objectSender;
    }
    
    public Juego getRefFrame() {
        return refFrame;
    }

    public void disconnect() {
        try {
            if (threadClient != null && threadClient.isAlive()) {
                threadClient.interrupt(); 
            }

            if (objectSender != null) {
                objectSender.close();
            }
            if (objectListener != null) {
                objectListener.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            System.err.println("Error al desconectar el cliente: " + e.getMessage());
        }
    }

     
}