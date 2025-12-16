/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servidor;

import Comandos.Command;
import Comandos.CommandGanador;
import Comandos.CommandMessage;
import Comandos.CommandStartGame;
import Comandos.CommandTurn;
import Comandos.lobbyUpdateCommand;
import com.mycompany.socketsconsola.Juego;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author lacay
 */
public class Server {
    private final Map<String, Boolean> comodinResponses = new ConcurrentHashMap<>();
    private final Map<String, Object> syncLocks = new ConcurrentHashMap<>();
    
    public List<ThreadServidor> turnOrder = new ArrayList<>();
    private int currentTurn = 0;
    private int attacksThisRound = 0;
    public boolean turnSystemEnabled = false;
    private final int maxConections = 4;
    private final int PORT = 35500;
    private ServerSocket serverSocket;
    private ArrayList<ThreadServidor> connectedClients;
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
    
    private void init(){
        try {
            serverSocket = new ServerSocket(PORT);
            //refFrame.writeMessage("Server running!!!");
        } catch (IOException ex) {
            //refFrame.writeMessage("Error: " + ex.getMessage());
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
                
                return clientThread; 
            }
        }
        return null; 
    }
     
    public void iniciarTurnos() {
        turnOrder = new ArrayList<>(connectedClients);

        Collections.shuffle(turnOrder); // Orden aleatorio de ronda

        currentTurn = 0;
        attacksThisRound = 0;
        turnSystemEnabled = true;

        anunciarTurnoActual();
    }

    private void anunciarTurnoActual() {
        ThreadServidor jugador = turnOrder.get(currentTurn);

        // Enviar un comando TURN a todos los clientes
        CommandTurn turnoCommand = new CommandTurn(jugador.name);

        broadcast(turnoCommand);
}

    public ThreadServidor turnoActual(){
        ThreadServidor jugador = turnOrder.get(currentTurn);
        return jugador;
    }

    public ThreadServidor getCurrentTurnPlayer() {
        return turnOrder.get(currentTurn);
    }

    public void avanzarTurno() {
        attacksThisRound++;

        // ¿Se acabó la ronda completa?
        if (attacksThisRound >= turnOrder.size()) {

            //Collections.shuffle(turnOrder); // Nuevo orden aleatorio
            attacksThisRound = 0;
            currentTurn = 0;

            broadcast(new CommandMessage(
                    new String[]{"Nueva ronda iniciada"}
            ));

        } else {
            currentTurn++;
        }

        anunciarTurnoActual();
    }
    
    public synchronized void eliminarJugador(ThreadServidor target){
        int index = turnOrder.indexOf(target);
        
        if(index != -1){
            turnOrder.remove(index);
        }
        
        if(index < currentTurn){
            currentTurn--;
        }
        
        if(currentTurn == index){
            avanzarTurno();
        }
        
        VerificarGanador();
    }
    
    public void VerificarGanador(){
        if(turnOrder.size() == 1){
            ThreadServidor ganador = turnOrder.get(0);
            broadcast(new CommandGanador(ganador.name));
        }
    }
   
    public synchronized void addThreadServidor(ThreadServidor thread) {
        if (thread != null && thread.name != null) {
            connectedClients.add(thread);
            showAllNames();
        }
    }
    
    public synchronized void removeThreadServidor(ThreadServidor thread) {
        if (thread != null) {
            String removedName = thread.name != null ? thread.name : "Cliente Desconocido";

            // 1. Eliminar de la lista principal
            connectedClients.remove(thread);

            // 2. Eliminar del sistema de turnos (llama a tu método existente)
            if (turnSystemEnabled) {
                eliminarJugador(thread); 
            }

            // 3. Eliminar de la lista de listos (si estaba en ella)
            readyPlayers.remove(removedName);

            // 5. Sincronizar la lista de jugadores en todos los clientes (Lobby)
            showAllNames();

            // 6. Notificar a todos los jugadores sobre la desconexión
            broadcast(new CommandMessage(
                new String[]{removedName + " se ha desconectado."}
            ));
        }
    }
    
    
}
