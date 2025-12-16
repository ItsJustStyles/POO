/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

/**
 *
 * @author lacay
 */
public class Jugador {
    private String nombre;
    private int wins;
    private int loses;
    private int attacks;
    private int success;
    private int failed;
    private int giveup;

    public String getNombre() {
        return nombre;
    }

    public int getWins() {
        return wins;
    }

    public int getLoses() {
        return loses;
    }

    public int getAttacks() {
        return attacks;
    }

    public int getSuccess() {
        return success;
    }

    public int getFailed() {
        return failed;
    }

    public int getGiveup() {
        return giveup;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setAttacks(int attacks) {
        this.attacks = attacks;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public void setGiveup(int giveup) {
        this.giveup = giveup;
    }
    
    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", wins=" + wins +
                ", loses=" + loses +
                ", attacks=" + attacks +
                ", success=" + success +
                ", failed=" + failed +
                ", giveup=" + giveup +
                '}';
    }
    
}
