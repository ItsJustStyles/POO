/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.awt.Color;
import java.awt.GridLayout;
import static java.lang.Math.random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author lacay
 */
public class Tablero{
    
    private JFrame refFrame;
    private static final int FILAS = 20;
    List<Casilla> casillas = new ArrayList<>();
    List<Casilla> casillasP1 = new ArrayList<>();
    List<Casilla> casillasP2 = new ArrayList<>();
    List<Casilla> casillasP3 = new ArrayList<>();
    
    public List<Volcan> volcanes = new ArrayList<>();
    public List<Remolino> remolinos = new ArrayList<>();
    
    private static final int COLUMNAS = 30;
    Random random = new Random();
    
    public JPanel crearTablero(Personaje p1, Personaje p2, Personaje p3, JFrame refFrame){
        this.refFrame = refFrame;
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(FILAS + 1, COLUMNAS + 1, 1, 1));        
        panelTablero.add(crearEtiquetaVacia());

        int total = FILAS * COLUMNAS; // 600
        int max1 = (int) ((p1.getPorcentaje() * total) / 100.0);
        int max2 = (int) ((p2.getPorcentaje() * total) / 100.0);
        int max3 = total - max1 - max2; // asegura que sumen exacto

        // Encabezado columnas
        for (int c = 1; c <= COLUMNAS; c++) {
            panelTablero.add(crearEtiquetaNumeracion(String.valueOf(c)));
        }

        // Arma la bolsa y baraja
        List<Integer> bolsa = new ArrayList<>(total);
        for (int i = 0; i < max1; i++) bolsa.add(0);
        for (int i = 0; i < max2; i++) bolsa.add(1);
        for (int i = 0; i < max3; i++) bolsa.add(2);
        Collections.shuffle(bolsa, random);

        int idx = 0;
        for (int f = 1; f <= FILAS; f++) {
            panelTablero.add(crearEtiquetaNumeracion(String.valueOf(f)));
            for (int c = 1; c <= COLUMNAS; c++) {
                int seleccion = bolsa.get(idx++);
                JPanel celda;
                switch (seleccion) {
                    case 0:
                        celda = crearCeldaTablero(f, c, p1, new Color(5,36,135), casillasP1);
                        break;
                    case 1:
                        celda = crearCeldaTablero(f, c, p2, new Color(145,17,6),casillasP2);
                        break;
                    default:
                        celda = crearCeldaTablero(f, c, p3,new Color(18,112,3), casillasP3); // ojo: p3
                        break;
                }
                panelTablero.add(celda);
            }
        }
        return panelTablero;
    }
    
    private JPanel crearCeldaTablero(int fila, int columna,Personaje p, Color color, List<Casilla> personaje) {
        JPanel celda = new JPanel();
        Casilla casilla = new Casilla(100,p,fila,columna,celda, refFrame);
        casillas.add(casilla);
        personaje.add(casilla);
        celda.setBackground(color); 
        celda.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); 
        return celda;
    }
    
    private JLabel crearEtiquetaNumeracion(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setHorizontalAlignment(SwingConstants.CENTER);
        etiqueta.setBackground(new Color(230, 230, 230));
        etiqueta.setOpaque(true);
        etiqueta.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        return etiqueta;
    }

    private JLabel crearEtiquetaVacia() {
        JLabel vacia = new JLabel("");
        vacia.setBackground(new Color(200, 200, 200));
        vacia.setOpaque(true);
        vacia.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        return vacia;
    }
    
    public int casillasVivas(){
        int contadorCasillasVivas = 0;
        for(Casilla c : casillas){
            if(c.esta_vivo()){
                contadorCasillasVivas++;
            }
        }
        int porcentajeVivo = (int) ((contadorCasillasVivas * 100.0)/600.0);
        return porcentajeVivo;
    }
    
    public int casillasDestruidas(){
        int contadorCasillasDestruidas = 0;
        for(Casilla c : casillas){
            if(!c.esta_vivo()){
                contadorCasillasDestruidas++;
            }
        }
        return contadorCasillasDestruidas;
    }
    
    public int casillasDestruidasP1(){
        int contadorCasillasDestruidas = 0;
        for(Casilla c : casillasP1){
            if(!c.esta_vivo()){
                contadorCasillasDestruidas++;
            }
        }
        return contadorCasillasDestruidas;
    }
    
        public int casillasDestruidasP2(){
        int contadorCasillasDestruidas = 0;
        for(Casilla c : casillasP2){
            if(!c.esta_vivo()){
                contadorCasillasDestruidas++;
            }
        }
        return contadorCasillasDestruidas;
    }
      
    public int casillasDestruidasP3(){
        int contadorCasillasDestruidas = 0;
        for(Casilla c : casillasP3){
            if(!c.esta_vivo()){
                contadorCasillasDestruidas++;
            }
        }
        return contadorCasillasDestruidas;
    }
    
    public int porcentajeP1(){
        int contadorCasillasMuertas = 0;
        for(Casilla c : casillasP1){
            if(!c.esta_vivo()){
                contadorCasillasMuertas++;
            }
        }
        int porcentajeMuerto = (int) ((contadorCasillasMuertas * 100.0)/casillasP1.size());
        return porcentajeMuerto;
    }
    
        public int porcentajeP2(){
        int contadorCasillasMuertas = 0;
        for(Casilla c : casillasP2){
            if(!c.esta_vivo()){
                contadorCasillasMuertas++;
            }
        }
        int porcentajeMuerto = (int) ((contadorCasillasMuertas * 100.0)/casillasP2.size());
        return porcentajeMuerto;
    }
        
    public int porcentajeP3(){
        int contadorCasillasMuertas = 0;
        for(Casilla c : casillasP3){
            if(!c.esta_vivo()){
                contadorCasillasMuertas++;
            }
        }
        int porcentajeMuerto = (int) ((contadorCasillasMuertas * 100.0)/casillasP3.size());
        return porcentajeMuerto;
    }
    
    public void recibirDanoLocacion(int fila, int columna, int dano, String registro){
        for(Casilla c : casillas){
            if(c.getX() == fila && c.getY() == columna){
                if (c.esta_vivo()){
                    c.recibirAtaque(dano);
                    c.registrar(registro);
                }
                if(!c.esta_vivo()){
                    c.ponerX();
                }
            }
        }
    }
    
    public Casilla CasillaPorCords(int fila,int columna){
        for(Casilla c : casillas){
            if(c.getX() == fila && c.getY() == columna){
                return c;

            }
        }
        return null;
      
    }
    
    public void vidaCasillas(){
        for(Casilla c : casillas){
            c.ponerVida();
        }
    }
    
    public void pintarVivasCasillas(){
        for(Casilla c : casillas){
            c.pintarVivas();
        }
    }
    
    public void MostrarCasillasOcupadas(){
        if(!remolinos.isEmpty()){
            for(Remolino r : remolinos){
                Casilla c = CasillaPorCords(r.getX(), r.getY());
                c.setTieneRemolino(true);
            }
        }
        
        if(!volcanes.isEmpty()){
            for(Volcan v : volcanes){
                Casilla c = CasillaPorCords(v.getX(), v.getY());
                c.setTieneVolcan(true);
            }
        }
        
        for(Casilla c : casillas){
            c.MostrarCeldasOcupadas();
        }
        
    }
    
    public void infoCasillas(int x, int y){
        Casilla c = CasillaPorCords(x, y);
        c.consultarCelda();
    }
    
}
