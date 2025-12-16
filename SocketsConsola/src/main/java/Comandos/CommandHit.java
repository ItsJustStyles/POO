/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.ThreadServidor;

/**
 *
 * @author lacay
 */
public class CommandHit extends Command{

    public CommandHit(String p, String arma, String atacante, String dano, String status) {
        super(CommandType.HIT, new String[]{p, arma, atacante, dano, status});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
        String[] params = this.getParameters();
        String p = params[0];
        String arma = params[1];
        String attacker = params[2];
        String status = params[4];

        String dano = params[3];
        int damage = Integer.parseInt(dano);
        
        boolean exitoAtaque = client.getRefFrame().recibirAtaque(damage);
        String mensaje  = "El jugador: " + attacker + " te atacó con " + p + " usando el arma: " + arma;
        String log =  "El jugador: " + attacker + " te atacó con " + p;
        String log_1 = "Weapon: " + arma;
        client.getRefFrame().writeBitacora(mensaje);
        client.getRefFrame().writeLog(log, log_1);
        client.getRefFrame().actualizarHUDEnemigo(status);
        
        if(client.getRefFrame().haMuerto()){
            client.getRefFrame().writeBitacora("Has muerto");
            client.getRefFrame().mostrarDerrota();
            client.getRefFrame().gestor.incrementarLoses(client.name, 1);
            CommandEliminarJugador cmd = new CommandEliminarJugador(client.name, "D"); 
            try {
                client.objectSender.writeObject(cmd);
                client.objectSender.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
