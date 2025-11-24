/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;
import Models.CommandStartGame;
import Models.Command;
import Models.CommandGanador;
import Models.CommandMessage;
import Models.CommandTurn;
import Models.lobbyUpdateCommand;
import com.mycompany.oceanica.Juego;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;


/**
 *
 * @author diego
 */
public class Server {
    // ---- SISTEMA DE TURNOS ----
    public List<ThreadServidor> turnOrder = new ArrayList<>();
    private int currentTurnIndex = 0;
    private int attacksThisRound = 0;
    public boolean turnSystemEnabled = false;
    
    private final int PORT = 35500;
    private final int maxConections = 4;
    private ServerSocket serverSocket;
    private ArrayList<ThreadServidor> connectedClients; // arreglo de hilos por cada cliente conectado
    //referencia a la pantalla
    Juego refFrame;
    private ThreadConnections connectionsThread;
    private final Set<String> readyPlayers = new HashSet<>();


    public Server(Juego refFrame) {
        connectedClients = new ArrayList<ThreadServidor>();
        this.refFrame = refFrame;
        this.init();
        connectionsThread = new ThreadConnections(this);
        connectionsThread.start();
    }
    
    //método que inicializa el server
    private void init(){
        try {
            serverSocket = new ServerSocket(PORT);
            //refFrame.writeMessage("Server running!!!");
        } catch (IOException ex) {
            refFrame.writeMessage("Error: " + ex.getMessage());
        }
    }
    public synchronized void markPlayerReady(String playerName) {
        readyPlayers.add(playerName);
        System.out.println("Jugador listo: " + playerName + " (" + readyPlayers.size() + "/" + connectedClients.size() + ")");

        if (readyPlayers.size() >= 1) { // mínimo 2 jugadores listos
            System.out.println("Mínimo de jugadores listos alcanzado. Iniciando partida...");
            broadcast(new CommandStartGame());
            iniciarTurnos();
            //VerificarGanador(); // Como prueba a ver si sirve xd
        }
}
    
    void executeCommand(Command comando) {
        if (comando.isIsBroadcast())
            this.broadcast(comando);
        else
            this.sendPrivate(comando);

    }
    
    public void broadcast(Command comando){
        for (ThreadServidor client : connectedClients) {
            try {
                client.objectSender.writeObject(comando);
            } catch (IOException ex) {
                
            }
        }

    }
    
    public void sendPrivate(Command comando){
        //asumo que el nombre del cliente viene en la posición 1 .  private_message Andres "Hola"
        if (comando.getParameters().length <= 1)
            return;
        
        String searchName =  comando.getParameters()[1];
        
        for (ThreadServidor client : connectedClients) {
            if (client.name.equals(searchName)){
                try {
                //simulo enviar solo al primero, pero debe buscarse por nombre
                    client.objectSender.writeObject(comando);
                    break;
                } catch (IOException ex) {
                
                }
            }
        }
    }
    
    
    public void showAllNames(){
        List<String> players = new ArrayList<>();
        //this.refFrame.clearMessages();
        //this.refFrame.writeMessage("Usuarios conectados:");
        for (ThreadServidor client : connectedClients) {
            players.add(client.name);
        }
        lobbyUpdateCommand sync = new lobbyUpdateCommand(players);
        broadcast(sync);
    }

    public int getMaxConections() {
        return maxConections;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ArrayList<ThreadServidor> getConnectedClients() {
        return connectedClients;
    }

    public Juego getRefFrame() {
        return refFrame;
    }
    
    public ThreadServidor getClientByName(String targetName) {
        
        for (ThreadServidor clientThread : connectedClients) {
            if (clientThread.name != null && clientThread.name.equalsIgnoreCase(targetName)) {
                
                return clientThread; // ¡Encontrado!
            }
        }
        return null; 
    }
    
    // ======================= SISTEMA DE TURNOS ==========================

    public void iniciarTurnos() {
        turnOrder = new ArrayList<>(connectedClients);

        Collections.shuffle(turnOrder); // Orden aleatorio de ronda

        currentTurnIndex = 0;
        attacksThisRound = 0;
        turnSystemEnabled = true;

        anunciarTurnoActual();
    }

    private void anunciarTurnoActual() {
        ThreadServidor jugador = turnOrder.get(currentTurnIndex);

        // Enviar un comando TURN a todos los clientes
        CommandTurn turnoCommand = new CommandTurn(jugador.name);

        broadcast(turnoCommand);
}

    public ThreadServidor turnoActual(){
        ThreadServidor jugador = turnOrder.get(currentTurnIndex);
        return jugador;
    }

    public ThreadServidor getCurrentTurnPlayer() {
        return turnOrder.get(currentTurnIndex);
    }

    public void avanzarTurno() {
        attacksThisRound++;

        // ¿Se acabó la ronda completa?
        if (attacksThisRound >= turnOrder.size()) {

            //Collections.shuffle(turnOrder); // Nuevo orden aleatorio
            attacksThisRound = 0;
            currentTurnIndex = 0;

            broadcast(new CommandMessage(
                    new String[]{"Nueva ronda iniciada"}
            ));

        } else {
            currentTurnIndex++;
        }

        anunciarTurnoActual();
}

// ===================================================================
    
public synchronized void eliminarjugador(ThreadServidor target) {

    // 1. Encontrar su posición en la lista de turnos
    int index = turnOrder.indexOf(target);

    // 2. Removerlo del orden de turnos
    if (index != -1) {
        turnOrder.remove(index);
    }

    // 3. Ajustar el turno actual si es necesario
    if (index < currentTurnIndex) {
        currentTurnIndex--;
    }

    // 4. Si el jugador eliminado era el jugador actual
    if (currentTurnIndex == index) {
        avanzarTurno();  // Pasar al siguiente jugador válido
    }
    
    System.out.println("Jugador eliminado de turnos y clientes: " + target.name);
    VerificarGanador();
}
        
 
    public void VerificarGanador(){
        if (turnOrder.size() == 1) {
        ThreadServidor ganador = turnOrder.get(0);
        broadcast(new CommandGanador(ganador.name));
        }
    }


}
