package com.mycompany.thewalkingtec;

import java.awt.Image;
import java.awt.Point;
import java.util.List;
import javax.swing.ImageIcon;

public abstract class Defensas {

    // Atributos base
    protected String nombrePersonaje; 
    protected int vida;
    protected int vidaActual;
    protected Point posicion;
    
    protected int dano;             // Daño base
    protected int velocidad;        // Cadencia de disparo (ms)
    protected int contadorTicks = 0;
    protected int rango;            // Radio de ataque o efecto (AoE)

    protected ImageIcon assetEstructura;
    Tablero tableroAsociado;
    
    public Defensas(int vidaMaxima, int dano, int velocidad, 
                    String rutaImagen, int rango, int fila, int columna, Tablero tablero) {
        
        // 1. Inicialización de atributos del JSON
        this.nombrePersonaje = nombrePersonaje;
        this.dano = dano;
        this.velocidad = velocidad;
        this.rango = rango;

        // 2. Inicialización de atributos base
        this.vida = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.posicion = new Point(fila, columna);
        this.tableroAsociado = tablero;
        
        // 3. Carga del asset (la ruta viene del JSON)
        cargarAsset(rutaImagen, 25, 25);
    }
    
    
    public void disparar(List<Zombies> hordaZombies){
        
        this.contadorTicks++;
        if (this.contadorTicks * 100 < this.velocidad) {
            return;
        }
        this.contadorTicks = 0;
        
        Zombies objetivo = buscarObjetivoMasCercano(hordaZombies);
        if (objetivo != null) {              
            objetivo.recibirDano(this.dano);
        }
    }
    
    private Zombies buscarObjetivoMasCercano(List<Zombies> hordaZombies) {
        Zombies objetivoCercano = null;
        int distanciaMinima = Integer.MAX_VALUE;

        for (Zombies zombie : hordaZombies) {
            Point posZombie = zombie.getPosicion();
            
            // Distancia de Manhattan
            int distancia = Math.abs(this.posicion.x - posZombie.x) + 
                            Math.abs(this.posicion.y - posZombie.y);

            // Verificar si el zombi está dentro del rango Y si es el más cercano
            if (distancia <= this.rango && distancia < distanciaMinima) {
                distanciaMinima = distancia;
                objetivoCercano = zombie;
            }
        }
        return objetivoCercano;
    }

    public void recibirDano(int dano) {
        this.vidaActual -= dano;
        if (this.vidaActual <= 0) {
            destruir();
            if (tableroAsociado != null) {
                tableroAsociado.eliminarDefensa(this);
            }
        }
    }
    
    public void destruir() {
        System.out.println("La defensa '" + this.nombrePersonaje + 
                           "' en (" + posicion.x + ", " + posicion.y + ") ha sido destruida.");
    }
    
    protected void cargarAsset(String rutaImagen, int ancho, int alto) {
        try {
            java.net.URL urlRecurso = getClass().getResource(rutaImagen);

            if (urlRecurso != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
                Image imagenOriginal = iconoOriginal.getImage();
                
                Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                
                this.assetEstructura = new ImageIcon(imagenEscalada);
            } else {
                System.err.println("❌ ERROR: Asset de imagen no encontrado en: " + rutaImagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el asset: " + e.getMessage());
        }
    }
    
    // -----------------------------------------------------------------------------------
    // --- GETTERS ---
    // -----------------------------------------------------------------------------------
    
    public ImageIcon getAssetImagen() { return assetEstructura; }
    public Point getPosicion() { return posicion; }
    public int getVidaActual() { return vidaActual; }
    protected Tablero getTableroAsociado() { return this.tableroAsociado; }
    
    // Nuevos Getters para atributos del JSON
    public String getNombrePersonaje() { return nombrePersonaje; }
    public int getDano() { return dano; }
    public int getVelocidad() { return velocidad; }
    public int getRango() { return rango; }
}