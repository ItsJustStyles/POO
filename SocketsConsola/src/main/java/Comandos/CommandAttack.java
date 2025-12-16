/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.Server;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandAttack extends Command{

    public CommandAttack(String[] args) {
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

                return; 
            }
        }
        
        ThreadServidor selfThread = threadServidor.getRefServer().getClientByName(threadServidor.name);
        String[] comodinInfo = this.getParameters();
        
        String comodinTipo = comodinInfo[comodinInfo.length - 4];
        String comodinDesbloqueado = comodinInfo[comodinInfo.length - 3];
        String comodinUsado = comodinInfo[comodinInfo.length - 2];
        String status = comodinInfo[comodinInfo.length - 1];
        
        boolean cTipo = Boolean.parseBoolean(comodinTipo);
        boolean cDesbloqueado = Boolean.parseBoolean(comodinDesbloqueado);
        boolean cUsado = Boolean.parseBoolean(comodinUsado);
        
        if(cDesbloqueado && cUsado){
            String[] params = this.getParameters();
            String p;
            String arma;
            String p2Oarma2;
            String arma2;
            String objetivo;
            
            try{
                p = params[1];
                arma = params[2];
                p2Oarma2 = params[3];
                
                boolean personajeValido = threadServidor.esPersonajeValido(p);
                boolean armaValida = threadServidor.esArmaValida(arma, p);
                
                if(!personajeValido){
                    String msg = "El personaje no es valido";
                    CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                    try {
                        selfThread.objectSender.writeObject(messageConsola);
                        selfThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    this.setIsBroadcast(false);
                    return;
                }
                
                if(!armaValida){
                    String msg = "El arma no es valida";
                    CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                    try {
                        selfThread.objectSender.writeObject(messageConsola);
                        selfThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    this.setIsBroadcast(false);
                    return;
                }
                
                boolean verficarP2 = threadServidor.esPersonajeValido(p2Oarma2);
                boolean verficarA2 = threadServidor.esArmaValida(p2Oarma2, p);
                
                if(verficarP2){
                    arma2 = params[4];
                    objetivo = params[5];
                    boolean verficarArma = threadServidor.esArmaValida(arma2, p2Oarma2);
                    if(!verficarArma){
                        String msg = "El arma no es valida";
                        CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                        try {
                            selfThread.objectSender.writeObject(messageConsola);
                            selfThread.objectSender.flush();
                        } catch (java.io.IOException ex) {
                            //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                        }
                        this.setIsBroadcast(false);
                        return;
                    }
                    
                    ThreadServidor targetThread = threadServidor.getRefServer().getClientByName(objetivo);
                    String damage = threadServidor.obtenerDano(arma, p) + "";
                    CommandHit hitCommand = new CommandHit(p, threadServidor.obtenerNombreArma(arma,p), threadServidor.name, damage, status);
                    try {
                        targetThread.objectSender.writeObject(hitCommand);
                        targetThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    
                    String damage2 = threadServidor.obtenerDano(arma, p) + "";
                    CommandHit hitCommand2 = new CommandHit(p2Oarma2, threadServidor.obtenerNombreArma(arma2, p2Oarma2), threadServidor.name, damage2, status);
                    try {
                        targetThread.objectSender.writeObject(hitCommand2);
                        targetThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    
                    CommandRegistroAtaque registro = new CommandRegistroAtaque(p2Oarma2, objetivo, threadServidor.obtenerNombreArma(arma2, p2Oarma2));
                    try {
                        selfThread.objectSender.writeObject(registro);
                        selfThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    
                }else if(verficarA2){
                    objetivo = params[4];
                    ThreadServidor targetThread = threadServidor.getRefServer().getClientByName(objetivo);
                    String damage = threadServidor.obtenerDano(arma, p) + "";
                    CommandHit hitCommand = new CommandHit(p, threadServidor.obtenerNombreArma(arma, p), threadServidor.name, damage, status);
                    try {
                        targetThread.objectSender.writeObject(hitCommand);
                        targetThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    
                    String damage2 = threadServidor.obtenerDano(arma, p) + "";
                    CommandHit hitCommand2 = new CommandHit(p, threadServidor.obtenerNombreArma(p2Oarma2, p), threadServidor.name, damage2, status);
                    try {
                        targetThread.objectSender.writeObject(hitCommand2);
                        targetThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    
                    CommandRegistroAtaque registro = new CommandRegistroAtaque(p, objetivo, threadServidor.obtenerNombreArma(p2Oarma2, p));
                    try {
                        selfThread.objectSender.writeObject(registro);
                        selfThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    
                    
                }else{
                    String msg = "Parametros para el ataque imcompletos";
                    CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                    try {
                        selfThread.objectSender.writeObject(messageConsola);
                        selfThread.objectSender.flush();
                    } catch (java.io.IOException ex) {
                        //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                    }
                    this.setIsBroadcast(false);
                    return;
                }
                
            }catch(Exception e){
                String msg = "Parametros para el ataque imcompletos";
                CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                try {
                    selfThread.objectSender.writeObject(messageConsola);
                    selfThread.objectSender.flush();
                } catch (java.io.IOException ex) {
                    //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                }
                this.setIsBroadcast(false);
                return;
            }
            
            CommandGastarComodin gastarComodin = new CommandGastarComodin();
            try {
                selfThread.objectSender.writeObject(gastarComodin);
                selfThread.objectSender.flush();
            } catch (java.io.IOException ex) {
                //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
            }
            
            server.avanzarTurno();
        }else{
        
            String[] params = this.getParameters();

            String p;
            String arma;
            String objetivo;

            try{
                p = params[1];
                arma = params[2];
                objetivo = params[3];

            }catch(ArrayIndexOutOfBoundsException e){
                String msg = "Parametros para el ataque imcompletos";
                CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                try {
                    selfThread.objectSender.writeObject(messageConsola);
                    selfThread.objectSender.flush();
                } catch (java.io.IOException ex) {
                    //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                }
            this.setIsBroadcast(false);
            return;
        }
        
        ThreadServidor targetThread = threadServidor.getRefServer().getClientByName(objetivo);
        
        if(targetThread != null){
            
            String attackerName = threadServidor.name;
            boolean personajeValido = threadServidor.esPersonajeValido(p);
            if(!personajeValido){
                String msg = "El personaje no es valido";
                CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                try {
                    selfThread.objectSender.writeObject(messageConsola);
                    selfThread.objectSender.flush();
                } catch (java.io.IOException ex) {
                    //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                }
                this.setIsBroadcast(false);
                return;
            }
            
            boolean armaValida = threadServidor.esArmaValida(arma, p);
            if(!armaValida){
                String msg = "El arma no es valida";
                CommandMessageConsola messageConsola = new CommandMessageConsola(msg);
                try {
                    selfThread.objectSender.writeObject(messageConsola);
                    selfThread.objectSender.flush();
                } catch (java.io.IOException ex) {
                    //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
                }
                this.setIsBroadcast(false);
                return;
            }
            
            String damage = threadServidor.obtenerDano(arma, p) + "";
            CommandHit hitCommand = new CommandHit(p, threadServidor.obtenerNombreArma(arma,p), attackerName, damage, status);
            try {
                targetThread.objectSender.writeObject(hitCommand);
                targetThread.objectSender.flush();
            } catch (java.io.IOException ex) {
                //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
            }
            
            CommandRegistroAtaque registro = new CommandRegistroAtaque(p, objetivo, threadServidor.obtenerNombreArma(arma,p));
            try {
                selfThread.objectSender.writeObject(registro);
                selfThread.objectSender.flush();
            } catch (java.io.IOException ex) {
                //threadServidor.getRefServer().getRefFrame().writeConsola("Error al enviar ataque a " + objetivo);
            }
            
            String[] broadcastParams = new String[]{threadServidor.name, objetivo, p, arma};
            CommandAttack broadcastCommand = new CommandAttack(broadcastParams);
            threadServidor.getRefServer().broadcast(broadcastCommand);
            
        }else{
            this.setIsBroadcast(false);
            return;
        }
        
        server.avanzarTurno();
        }
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String attackerName = params[0];
        String targetName = params[1];
        String personaje = params[2];
        String weapon = params[3];
        
        String bitacora = "El jugador " + attackerName + " atacó al jugador " + targetName + " usando el personaje: " + personaje + " y el arma: " + weapon;
        
        if(!client.name.equals(targetName)){
            client.getRefFrame().writeBitacora(bitacora);
        }
        
    }
      
}
