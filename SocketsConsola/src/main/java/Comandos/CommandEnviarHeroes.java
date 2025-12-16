/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

import Cliente.Client;
import Servidor.ThreadServidor;
import com.mycompany.socketsconsola.Personajes;
import java.util.List;

/**
 *
 * @author lacay
 */
public class CommandEnviarHeroes extends Command{
    List<Personajes> heroes;
    public CommandEnviarHeroes(List<Personajes> heroes) {
        this.heroes = heroes;
        super(CommandType.ENVIARHEROES, new String[]{});
    }

    @Override
    public void processForServer(ThreadServidor threadServidor) {
        
    }

    @Override
    public void processInClient(Client client) {
         client.getRefFrame().verJugadores(heroes);
    }
    
    
    
}
