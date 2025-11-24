/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

/**
 *
 * @author gabos
 */
public class Volcan {
    private int x;
    private int y;
    private int radio;
    private boolean vivo;
    
    public Volcan(int x, int y, int radio, boolean vivo) {
        this.x = x;
        this.y = y;
        this.radio = radio;
        this.vivo = vivo;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadio() {
        return radio;
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    @Override
    public String toString() {
        return "Volcan{" +
               "x=" + x +
               ", y=" + y +
               ", radio=" + radio +
               ", vivo=" + vivo +
               '}';
    }
    
}
