/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Comandos.Command;
import com.mycompany.socketsconsola.Armas;
import com.mycompany.socketsconsola.Personajes;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Random;

/**
 *
 * @author lacay
 */
public class ThreadServidor extends Thread {
    private Server server;
    public Socket socket;
    
    // Streams para leer y escribir objetos
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    public String name;

    public boolean isActive = true;
    public boolean isRunning = true;
    public List<Personajes> heroes;
    
    private Random random = new Random();

    public ThreadServidor(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            objectSender = new ObjectOutputStream(socket.getOutputStream());
            objectSender.flush();
            objectListener = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        Command comando;
        while (isRunning) {
            try {
                comando = (Command) objectListener.readObject();
                //No hace falta imprimir lo que se recibe del comando xd
                //server.getRefFrame().writeMessage("ThreadServer recibió: " + comando);
                comando.processForServer(this);

                if (isActive)
                    server.executeCommand(comando);

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public Server getRefServer() {
        return server; 
    }

    public void showAllClients() {
        this.server.showAllNames();
    }
    
    public boolean esPersonajeValido(String nombrePersonaje) {
        for (Personajes heroe : heroes) {
            if (heroe.getNombre().equals(nombrePersonaje)) {
                return true; 
            }
        }
        return false;
    }
    
    public boolean esArmaValida(String arma, String personaje){
        Personajes p = null;
        for(Personajes heroe: heroes){
            if (heroe.getNombre().equals(personaje)) {
                p = heroe;
                break;
            }
        }
        
        if (p == null) {
            return false;
        }
        
        List<Armas> armasHeroe = p.getArmas();
        for(Armas a : armasHeroe){
            if(a.getNombre().equals(arma.toLowerCase()) || a.getNombreSecundario().equals(arma.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    
    public String obtenerNombreArma(String nombreArma, String personaje){
        Personajes p = null;
        for(Personajes heroe: heroes){
            if (heroe.getNombre().equals(personaje)) {
                p = heroe;
                break;
            }
        }
      
        List<Armas> armasHeroe = p.getArmas();
        for(Armas a : armasHeroe){
            if(a.getNombre().equals(nombreArma.toLowerCase()) || a.getNombreSecundario().equals(nombreArma.toLowerCase())){
                return a.getNombre();
            }
        }
        return "";
    }
    
    public int obtenerDano(String arma, String personaje){
        List<List<Integer>> danos = null;
        int indexDamage = 0;
        
        int i = 0;
        for(Personajes heroe: heroes){
            if (heroe.getNombre().equals(personaje)) {
                danos = heroe.getDamages();
                indexDamage = i;
                break;
            }
            i++;
        }
        
        List<Integer> danoArmaSeleccionada = danos.get(indexDamage);
        return danoArmaSeleccionada.get(random.nextInt(10));
    }
    
}
