/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author lacay
 */
public class Personaje implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String icon;
    private String ataque;
    private int porcentaje;
    private int poder;
    private int resistencia;
    private int sanidad;
    private String nombreSecundario;

    public String getNombre() {
        return nombre;
    }

    public String getRutaIcon() {
        return icon;
    }

    public String getAtaque() {
        return ataque;
    }

    public int getPorcentaje() {
        return porcentaje;
    }

    public int getPoder() {
        return poder;
    }

    public int getResistencia() {
        return resistencia;
    }

    public int getSanidad() {
        return sanidad;
    }

    public String getNombreSecundario(){
        return nombreSecundario;
    }
 
}
