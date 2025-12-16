/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author lacay
 */
public class Personajes implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String nombre;
    private String tipo;
    private String imagen;
    
    private int vida = 100;
    
    //Armas:
    private transient Random random = new Random();;
    private List<Armas> armas;
    private List<List<Integer>> damages = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setArmas(List<Armas> armas) {
        this.armas = armas;
    }
    
    public void generarDamage(){
        for(int i = 0; i < 5; i++){
            List<Integer> damagePorArma = new ArrayList<>();
            for(int j = 0; j < 10; j++){
                int damage = random.nextInt(81) + 20;
                damagePorArma.add(damage);
            }
            damages.add(damagePorArma);
        }
    }

    public List<Armas> getArmas() {
        return armas;
    }

    public List<List<Integer>> getDamages() {
        return damages;
    }
    
    public void recibirDano(int dano){
        vida -= dano;
    }

    public int getVida() {
        return vida;
    }
    
    
    
}
