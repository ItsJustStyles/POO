/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.util.List;

/**
 *
 * @author lacay
 */
public class realizarAtaquePorGrupo {
    
    Tablero tablero;
    Juego refFrame;
    List<Personaje> todosLosPersonajes;
    
    public realizarAtaquePorGrupo(Tablero tablero, List<Personaje> todosLosPersonajes, Juego refFrame) {
        this.tablero = tablero;
        this.refFrame = refFrame;
        this.todosLosPersonajes = todosLosPersonajes;
    }
    
    public boolean atacar(String p, String ataque, int fila, int columna, int fila2, int columna2, int fila3, int columna3, String registro){
        String GrupoAtaque = buscarPersonaje(p);
        if(GrupoAtaque == null){
            System.out.println("Personaje no valido");
            return false;
        }
        boolean exito = buscarAtaque(GrupoAtaque, ataque, fila, columna, fila2, columna2, fila3, columna3, registro);
        if(exito){
            return true;
        }
        return false;
    }
    
    public boolean buscarAtaque(String ataque, String ataqueSolicitado, int fila, int columna, int fila2, int columna2, int fila3, int columna3, String registro){
        switch(ataque){
            case "Release the Kraken":
                ReleaseTheKraken attack = new ReleaseTheKraken(100, tablero, registro);
                if(ataqueSolicitado.equals("Tentaculos")){
                    attack.tentaculos();
                    return true;
                }else if(ataqueSolicitado.equals("KrakenBreath")){
                    attack.kraken_breath(fila, columna);
                    return true;
                }else if(ataqueSolicitado.equals("ReleaseTheKraken")){
                    attack.release_the_kraken();
                    return true;
                }else{
                    return false;
                    //System.out.println("No hay ataque con ese nombre");
                }
            case "Poseidon Trident":
                PoseidonTrident attack2 = new PoseidonTrident(100, tablero, registro);
                if(ataqueSolicitado.equals("ThreeLines")){
                    attack2.ThreeLines(fila, columna, fila2, columna2, fila3, columna3);
                    return true;
                }else{
                    return false;
                }
            case "Fish Telepathy":
                FishTelepathy attack3 = new FishTelepathy(50, tablero, registro);
                if(ataqueSolicitado.equals("Cardumen")){
                    attack3.Cardumen();
                    return true;
                }else if(ataqueSolicitado.equals("SharkAttack")){
                    attack3.Shark_attack();
                    return true;
                }else if(ataqueSolicitado.equals("Pulp")){
                    attack3.Pulp();
                    return true;
                }else{
                    return false;
                }
            case "Thunders under the sea":
                ThundersUnderTheSea attack4 = new ThundersUnderTheSea(100, tablero, registro);
                if(ataqueSolicitado.equals("ThunderRain")){
                    attack4.Thunder_rain();
                    return true;
                }else if(ataqueSolicitado.equals("PoseidonThunders")){
                    attack4.Poseidon_thunders();
                    return true;
                }else if(ataqueSolicitado.equals("EelAttack")){
                    attack4.Eel_attack();
                    return true;
                }else{
                    return false;
                }
            case "Waves control":
                WavesControl attack5 = new WavesControl(100, tablero, registro);
                if(ataqueSolicitado.equals("SwirlRaising")){
                    attack5.SwirlRaising();
                    return true;
                }else if(ataqueSolicitado.equals("SendHumanGarbage")){
                    attack5.sendHumanGarbage();
                    return true;
                }else if(ataqueSolicitado.equals("RadioactiveRush")){
                    attack5.radioactiveRush();
                    return true;
                }else{
                    return false;
                }
            case "Estoy codificando":
                EstoyCodificando attack6 = new EstoyCodificando(100, tablero, refFrame, registro);
                if(ataqueSolicitado.equals("EstoNoEsUnJuego")){
                    attack6.EstoNoEsUnJuego();
                    return true;
                }else if(ataqueSolicitado.equals("NoSePuedeMas")){
                    attack6.NoSePuedeMas();
                    return true;
                }else if(ataqueSolicitado.equals("MicroprocesadorX264")){
                    attack6.Microprocesador_x264();
                    return true;
                }else if(ataqueSolicitado.equals("Slower")){
                    attack6.Slower();
                    return true;
                }else{
                    return false;
                }
            case "Undersea Fire":
                UnderseaFire attack7 = new UnderseaFire(100, tablero, registro);
                if(ataqueSolicitado.equals("VolcanoRaising")){
                    attack7.volcanoRaising();
                    return true;
                }else if(ataqueSolicitado.equals("VolcanoExplosion")){
                    attack7.volcanoExplosion();
                    return true;
                }else if(ataqueSolicitado.equals("TermalRush")){
                    attack7.termalRush();
                    return true;
                }else{
                    return false;
                }
            case "Hora de balatrear":
                HoraDeBalatrear attack8 = new HoraDeBalatrear(100, tablero, refFrame ,registro);
                if(ataqueSolicitado.equals("BluePrint")){
                    attack8.BluePrint();
                    return true;
                }else if(ataqueSolicitado.equals("Balatrito")){
                    attack8.Balatrito();
                    return true;
                }else if(ataqueSolicitado.equals("Cavendish")){
                    attack8.Cavendish();
                    return true;
                }else{
                    return false;
                }
            default:
                return false;
        }           
    }

    private String buscarPersonaje(String nombrePersonaje) {
        for(Personaje p : todosLosPersonajes){
            if(p.getNombre().equals(nombrePersonaje) || p.getNombreSecundario().equals(nombrePersonaje)){
                return p.getAtaque();
            }
        }
        return null;
    }
    
}
