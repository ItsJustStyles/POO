/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

import Models.CommandAttack;
import Models.CommandGiveup;
import Models.CommandMessage;
import Models.CommandName;
import Models.CommandPrivateMessage;

/**
 *
 * @author diego
 */
public class CommandFactory {
    
    
    public static Command getCommand(String[] args){
        String type = args[0].toUpperCase();
        
        switch (type) {
            case "ATAQUE":
                return new CommandAttack(args);
            case "MESSAGE":
                return new CommandMessage(args);
            case "PRIVATE_MESSAGE":
                return new CommandPrivateMessage(args);
            case "GIVEUP":
                return new CommandGiveup(args);
            case "NAME":
                return new CommandName(args);
            case "MOSTRARPORCENTAJECELDAS":
                return new CommandPorcentajeCeldas(args);
            case "PINTARVIVAS":
                return new CommandPintarVivas(args);
            case "MOSTRARCELDASOCUPADAS":
                return new CommandMostrarCeldasOcupadas(args);
            case "CONSULTARCELDA":
                return new CommandConsultarCelda(args);
            case "SALTARTURNO":
                return new CommandSaltarTurno(args);
            case "LOGRESUMEN":
                return new CommandLogResumen(args);
            case "RENDIRSE":
                return new CommandRendirse(args);
            default:
                return null;
        }
        
        
    }
    
}
