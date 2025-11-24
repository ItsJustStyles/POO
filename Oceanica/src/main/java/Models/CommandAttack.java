/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Cliente.Client;
import Models.CommandType;
import Servidor.Server;
import Servidor.ThreadServidor;
import com.mycompany.oceanica.Personaje;
import java.util.List;

/**
 *
 * @author diego
 */
public class CommandAttack extends Command{
    CommandHit hitCommand;
    
    String row;
    String columna;
    
    //2 par de putos más pára el ataque de Three lines xd
    String row2;
    String columna2;
    
    String row3;
    String columna3;
    
    public CommandAttack(String[] args) { //ATTACK Andres 5 7
        super(CommandType.ATAQUE, args);
    }
    
    @Override
    public void processForServer(ThreadServidor threadServidor) {
        // =============================================================
        // VALIDAR TURNO DEL JUGADOR
        // =============================================================
        Server server = threadServidor.getRefServer();

        if (server.turnSystemEnabled) {
            ThreadServidor jugadorActual = server.getCurrentTurnPlayer();
            
            if (jugadorActual != threadServidor) {
                // NO ES SU TURNO → rechazo del ataque
                System.out.println("NO ES TU TURNO");
                try {
                    threadServidor.objectSender.writeObject(
                        new CommandMessage(new String[]{
                            "SERVER", "NO ES TU TURNO"
                        })
                    );
                } catch (Exception e) {}

                return; // NO permitir que haga el ataque
            }
        }
        
        String[] params = this.getParameters();
        
        String p;
        String ataque;
        String objetivo;
        boolean exitoAttack;
        
        row2 = "0";
        columna2 = "0";

        row3 = "0";
        columna3 = "0";
        try{
            p = params[1];
            ataque = params[2];
            objetivo = params[3];

            if(ataque.equals("KrakenBreath")){
                row = params[4];
                columna = params[5];
            }else if(ataque.equals("ThreeLines")){
                row = params[4];
                columna = params[5];

                row2 = params[6];
                columna2 = params[7];

                row3 = params[8];
                columna3 = params[9];
            }else{
                row = "0";
                columna = "0";
            }
        }catch(ArrayIndexOutOfBoundsException e){
            String msg = "Parametros para el ataque imcompletos";
            server.getRefFrame().writeConsola(msg);
            this.setIsBroadcast(false);
            return;
        }
        
        
        //System.out.println(objetivo);
        
        ThreadServidor targetThread = threadServidor.getRefServer().getClientByName(objetivo);
        ThreadServidor selfThread = threadServidor.getRefServer().getClientByName(threadServidor.name);
        
        if(targetThread != null){
            
            String attackerName = threadServidor.name;
            //Verificar si el personaje con el que se ataca es valido para los que se seleccionaron por el jugador
            
            boolean personajeValido = threadServidor.esPersonajeValido(p);
            if(!personajeValido){
                System.out.println("El personaje no es valido");
                this.setIsBroadcast(false);
                return;
            }
            
            hitCommand = new CommandHit(p, ataque, attackerName, row, columna, row2, columna2, row3, columna3);
            CommandRegistrar registro = new CommandRegistrar(threadServidor.name);
            // 3. UNICAST: Enviar el comando SÓLO al cliente objetivo
            try {
                targetThread.objectSender.writeObject(hitCommand);
                targetThread.objectSender.flush();
            } catch (java.io.IOException ex) {
                // Manejar la desconexión del objetivo
                threadServidor.getRefServer().getRefFrame().writeMessage("Error al enviar ataque a " + objetivo);
            }
            // Unicast para registrar en el client del atacante
            try {
                selfThread.objectSender.writeObject(registro);
                selfThread.objectSender.flush();
            } catch (java.io.IOException ex) {
                // Manejar la desconexión del objetivo
                selfThread.getRefServer().getRefFrame().writeMessage("Error al enviar ataque a " + objetivo);
            }
            
            exitoAttack = hitCommand.isExito();
            String exito = String.valueOf(!exitoAttack);
        
            String[] broadcastParams = new String[]{threadServidor.name, objetivo, ataque ,row, columna, row2, columna2, row3, columna3, exito};
            CommandAttack broadcastCommand = new CommandAttack(broadcastParams);
            
            // Reenviar a todos (BROADCAST)
            threadServidor.getRefServer().broadcast(broadcastCommand);
            //this.setIsBroadcast(true);
        }else{
            this.setIsBroadcast(false);
            return;
        }
        
        // =============================================================
        // AVANZAR TURNO DESPUÉS REALIZAR EL ATAQUE
        // =============================================================
        
        server.avanzarTurno();
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String attackerName = params[0];
        String targetName = params[1];
        String ataque = params[2];
        String row = params[3]; 
        String col = params[4];
        
        String row2 = params[5]; 
        String col2 = params[6];
        String row3 = params[7]; 
        String col3 = params[8];
        
        String exitoAtaque = params[9];                           
        
        
        
        //Escribir el mensaje dependiendo del ataque xd
        String bitacora;
        if(ataque.equals("KrakenBreath")){
            bitacora = "El jugador " + attackerName + " atacó al jugador " + targetName + " en fila: " + row + " y columna: " + col;
        }else if(ataque.equals("ThreeLines")){
            bitacora = "El jugador " + attackerName + " atacó al jugador " + " en los puntos: " +
    "(" + row + ", " + col + "), " + 
    "(" + row2 + ", " + col2 + "), " + 
    "(" + row3 + ", " + col3 + ")";
        }else{
            bitacora = "El jugador " + attackerName + " atacó al jugador " + targetName;
        }

        if(!client.name.equals(targetName) && exitoAtaque.equals("true")){
            client.getRefFrame().writeBitacora(bitacora);
        }
        
    }
    
    
    
}
