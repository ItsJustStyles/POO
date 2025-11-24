/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import Models.CommandFactory;
import Models.CommandTeam;
import com.mycompany.oceanica.Juego;
import com.mycompany.oceanica.Personaje;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
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
    List<Personaje> heroes;

    public Client(Juego refFrame, String name, List<Personaje> heroes, int PORT, String IP_ADDRESS) {
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
     
}
