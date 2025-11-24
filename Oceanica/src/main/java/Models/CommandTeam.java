/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Servidor.ThreadServidor;
import com.mycompany.oceanica.Personaje;
import java.util.List;

/**
 *
 * @author lacay
 */
public class CommandTeam extends Command{
    private List<Personaje> personajesSeleccionados;
            
    public CommandTeam(List<Personaje> personajesSeleccionados) {
        super(CommandType.TEAM, new String[]{});
        this.personajesSeleccionados = personajesSeleccionados;
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        this.setIsBroadcast(false);
        threadServidor.heroes = this.personajesSeleccionados;
    }
}
