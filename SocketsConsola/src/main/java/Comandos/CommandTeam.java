/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Servidor.ThreadServidor;
import com.mycompany.socketsconsola.Personajes;
import java.util.List;

/**
 *
 * @author lacay
 */
public class CommandTeam extends Command{
    private List<Personajes> personajesSeleccionados;
            
    public CommandTeam(List<Personajes> personajesSeleccionados) {
        super(CommandType.TEAM, new String[]{});
        this.personajesSeleccionados = personajesSeleccionados;
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
        threadServidor.heroes = this.personajesSeleccionados;
    }
}
