package Servidor;

import Models.Command;
import com.mycompany.oceanica.Personaje;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author diego
 */
public class ThreadServidor extends Thread {
    private Server server;
    private Socket socket;
    
    // Streams para leer y escribir objetos
    public ObjectInputStream objectListener;
    public ObjectOutputStream objectSender;
    public String name;
    public List<Personaje> heroes;

    public boolean isActive = true;
    public boolean isRunning = true;

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
        return server;  // ✅ corregido
    }

    public void showAllClients() {
        this.server.showAllNames();
    }
        
    public boolean esPersonajeValido(String nombrePersonaje) {
        for (Personaje heroe : heroes) {
            if (heroe.getNombre().equals(nombrePersonaje) || heroe.getNombreSecundario().equals(nombrePersonaje)) {
                return true; 
            }
        }
        return false;
    }
    
}
