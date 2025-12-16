package Servidor;

import java.io.IOException;
import java.net.Socket;

/**
 * Hilo que escucha continuamente nuevas conexiones de clientes.
 */
public class ThreadConnections extends Thread{
    private Server server;

    public ThreadConnections(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        Socket newSocket = null;
        
        // El bucle ahora solo comprueba el máximo de conexiones. La eliminación se hace
        // en Server.removeThreadServidor.
        while (server.getConnectedClients().size() < server.getMaxConections() ) {
            
            try {
                System.out.println("Esperando conexión...");
                
                // Bloquea y espera una nueva conexión de socket
                newSocket = server.getServerSocket().accept();
                
                // 1. Crear e iniciar el ThreadServidor (Hilo de escucha del cliente)
                ThreadServidor newServerThread = new ThreadServidor(server, newSocket);
                
                // 2. ¡CRUCIAL! Ya NO se añade el hilo a la lista de conectados aquí.
                // El registro (add) se hará SÓLO si el nombre es validado como único 
                // dentro de CommandName.processForServer.
                
                newServerThread.start();
                
                // Notificar que un socket está siendo procesado
                System.out.println("Cliente conectado y esperando validación de nombre...");

            } catch (IOException ex) {
                // Si el ServerSocket se cierra o hay un error de I/O
                // Generalmente ignoramos si el error es 'Socket closed' al apagar el servidor.
                if (server.getServerSocket() != null && !server.getServerSocket().isClosed()) {
                     System.err.println("Error al aceptar conexión: " + ex.getMessage());
                } else {
                     System.out.println("Hilo de conexión terminado (Socket del servidor cerrado).");
                     break; // Salir del bucle si el servidor se cerró
                }
            }
        }
        
    }
}