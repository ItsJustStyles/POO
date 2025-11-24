/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

/**
 *
 * @author lacay
 */
public class infoAtaques {
    private String ataque;

    public infoAtaques(String ataque) {
        this.ataque = ataque;
    }
    
    public String[] buscar(){
        String[] args;
        switch(ataque){
            case "Fish Telepathy":
                args = new String[]{"Cardumen", "Shark attack", "Pulp"};
                return args;
            case "Release the Kraken":
                args = new String[]{"Tentaculos", "Kraken breath", "Release the kraken"};
                return args;
            case "Thunders under the sea":
                args = new String[]{"Thunder rain", "Poseidon thunders", "Eel attack"};
                return args;
            case "Waves control":
                args = new String[]{"Swirl raising", "Send human garbage", "Radioactive rush"};
                return args;
            case "Estoy codificando":
                args = new String[]{"Esto no es un Juego", "No se puede mas", "Microprocesador x264", "Slower"};
                return args;
            case "Undersea Fire":
                args = new String[]{"Volcano raising", "Volcano explosion", "Termal rush"};
                return args;
            case "Control":
                args = new String[]{"Control", "Bang", "Makima is listening"};
                return args;
            case "Poseidon Trident":
                args = new String[]{"Three lines", "Three numbers", "Control the Kraken"};
                return args;
            case "Hora de balatrear":
                args = new String[]{"BluePrint", "Balatrito", "Cavendish"};
                return args;
            default:
                args = null;
                return args;
        }
    }
    
    public String[] buscarControl(){
        String[] args;
        switch(ataque){
            case "Fish Telepathy":
                args = new String[]{"Cardumen", "SharkAttack", "Pulp"};
                return args;
            case "Release the Kraken":
                args = new String[]{"Tentaculos", "KrakenBreath", "ReleaseTheKraken"};
                return args;
            case "Thunders under the sea":
                args = new String[]{"ThunderRain", "PoseidonThunders", "EelAttack"};
                return args;
            case "Waves control":
                args = new String[]{"SwirlRaising", "SendHumanGarbage", "RadioactiveRush"};
                return args;
            case "Estoy codificando":
                args = new String[]{"EstoNoEsUnJuego", "NoSePuedeMas", "MicroprocesadorX264", "Slower"};
                return args;
            case "Undersea Fire":
                args = new String[]{"VolcanoRaising", "VolcanoExplosion", "TermalRush"};
                return args;
            case "Control":
                args = new String[]{"Bang"};
                return args;
            case "Poseidon Trident":
                args = new String[]{"ThreeLines", "ThreeNumbers", "ControlTheKraken"};
                return args;
            case "Hora de balatrear":
                args = new String[]{"BluePrint", "Balatrito", "Cavendish"};
                return args;
            default:
                args = null;
                return args;
        }
    }
    
}
