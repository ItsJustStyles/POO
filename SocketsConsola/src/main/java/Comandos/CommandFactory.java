/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Comandos;

/**
 *
 * @author lacay
 */
public class CommandFactory {
    
    
    public static Command getCommand(String[] args){
        String type = args[0].toUpperCase();
        
        switch (type) {
            case "ATAQUE":
                return new CommandAttack(args);
            case "NAME":
                return new CommandName(args);
            case "RENDIRSE":
                return new CommandRendirse(args);
            case "SELECCIONARJUGADOR":
                return new CommandSeleccionarJugador(args);
            case "PASARTURNO":
                return new CommandPasarTurno(args);
            case "USARCOMODIN":
                return new CommandUsarComodin(args);
            case "CHAT":
                return new CommandChat(args);
            case "CHATPRIVADO":
                return new CommandChatPrivado(args);
            case "LOG":
                return new CommandLog(args);
            default:
                return null;
        }

    }
    
}
