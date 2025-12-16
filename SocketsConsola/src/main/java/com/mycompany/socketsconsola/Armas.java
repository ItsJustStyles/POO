/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

import java.io.Serializable;

/**
 *
 * @author lacay
 */
public class Armas implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String tipo;
    private String nombre;
    private String tipo_arma;
    private String imagen;
    private String nombreSecundario;
    
    public Armas() {
    }
    
    public String getNombre() {
        return nombre;
    }

    public String getTipoArma() {
        return tipo_arma;
    }

    public String getImagen() {
        return imagen;
    }

    public String getNombreSecundario() {
        return nombreSecundario;
    }
}
