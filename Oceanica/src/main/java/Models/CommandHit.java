/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Cliente.Client;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandHit extends Command{
    boolean exito;
    public CommandHit(String p, String ataque, String attackerName, String row, String col, String row2, String col2, String row3, String col3) {
        super(CommandType.HIT,new String[]{p,ataque, attackerName, row, col, row2, col2, row3, col3});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        
        String p = params[0];
        String ataque = params[1];
        String attacker = params[2];
        String row = params[3];
        String col = params[4];
        String row2 = params[5];
        String col2 = params[6];
        String row3 = params[7];
        String col3 = params[8];
        
        
        int fila = Integer.parseInt(row);
        int columna = Integer.parseInt(col);
        int fila2 = Integer.parseInt(row2);
        int columna2 = Integer.parseInt(col2);
        int fila3 = Integer.parseInt(row3);
        int columna3 = Integer.parseInt(col3);
        
        String registro = "Se recibio un ataque del jugador " + attacker + " usando " + p + " con el ataque: " + ataque;
        boolean exitoAttack = client.getRefFrame().recibirAtaqueCliente(p, ataque, fila, columna, fila2, columna2, fila3, columna3, registro, attacker);
        
        String ataqueAnterior = ataque;
        if(ataque.equals("Control")){
            p = client.getRefFrame().getPersonajeControlado();
            ataque = client.getRefFrame().getAtaqueControlado();
            
            int[] filas = client.getRefFrame().getFilasControladas();
            int[] columnas = client.getRefFrame().getColumnasControladas();
            
            fila = filas[0];
            fila2 = filas[1];
            fila3 = filas[2];
            
            columna = columnas[0];
            columna2 = columnas[1];
            columna3 = columnas[2];
            
        }
        
        
        String mensaje;
        
        if(ataqueAnterior.equals("Control")){
            if(ataque.equals("KrakenBreath")){
                mensaje = "El jugador: " + attacker + " controlo a tu personaje: " + p + " usando a Makima y uso el ataque " + ataque + " en fila: " + fila + " y columna: " + col; 
            }else if(ataque.equals("ThreeLines")){
                mensaje = "El jugador: " + attacker + " controlo a tu personaje: " + p + " usando a Makima y uso el ataque: " + ataque + " en los puntos: " +
                "(" + fila + ", " + columna + "), " + 
                "(" + fila2 + ", " + columna2 + "), " + 
                "(" + fila3 + ", " + columna3 + ")";;
            }else{
               mensaje = "El jugador: " + attacker + " controlo a tu personaje: " + p + " usando a Makima y uso el ataque: " + ataque; 
            }
        }else{
        
        if(ataque.equals("KrakenBreath")){
           mensaje = "El jugador: " + attacker + " te atacó con " + p + " usando: " + ataque + " en fila: " + fila + " y columna: " + col; 
        }else if(ataque.equals("ThreeLines")){
            mensaje = "El jugador: " + attacker + " te atacó con " + p + " usando: " + ataque + " en los puntos: " +
    "(" + fila + ", " + columna + "), " + 
    "(" + fila2 + ", " + columna2 + "), " + 
    "(" + fila3 + ", " + columna3 + ")";;
        }else{
           mensaje = "El jugador: " + attacker + " te atacó con " + p + " usando: " + ataque; 
        }
    }
        
        if(exitoAttack){
            client.getRefFrame().writeBitacora(mensaje);
            exito = true;
        }else{
            exito = false;
        }
        
        if(client.getRefFrame().haMuerto()){
            client.getRefFrame().writeBitacora("Has muerto");
            client.getRefFrame().mostrarDerrota();
            CommandEliminarJugador cmd = new CommandEliminarJugador(client.name, "D"); 
            try {
                client.objectSender.writeObject(cmd);
                client.objectSender.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else{
            
        }

    }

    public boolean isExito() {
        return exito;
    }

        
}
