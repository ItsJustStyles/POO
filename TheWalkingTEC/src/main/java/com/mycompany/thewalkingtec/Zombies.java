package com.mycompany.thewalkingtec;

import java.awt.Image;
import java.awt.Point;
import java.net.URL;
import javax.swing.ImageIcon;

public abstract class Zombies {
    
    // --- Atributos de la clase base Zombies ---
    protected String nombrePersonaje; // ⭐️ NUEVO: Nombre único (Ej: "Zombi Suicida")
    protected int vidaActual;
    protected int danoAtaque;
    protected int velocidad; // Celdas por turno o intervalo de movimiento
    protected int contadorTicks = 0;
    protected int rangoEspecial; // ⭐️ NUEVO: Rango de explosión/ataque especial
    protected Point posicion;
    protected ImageIcon assetImagen;
    protected Tablero tableroAsociado;

    // -----------------------------------------------------------------------------------
    // ⭐️ CONSTRUCTOR ADAPTADO AL JSON (9 argumentos) ⭐️
    // -----------------------------------------------------------------------------------
    public Zombies(String nombrePersonaje, int vidaMaxima, int dano, int velocidad, 
                   String rutaImagen, int rangoEspecial, int filaInicial, 
                   int columnaInicial, Tablero tablero) {
        
        // Inicialización de atributos del JSON
        this.nombrePersonaje = nombrePersonaje; // Usado para lógica de comportamiento
        this.danoAtaque = dano;
        this.velocidad = velocidad;
        this.rangoEspecial = rangoEspecial; // Rango para el comportamiento especial

        // Inicialización de atributos base
        this.vidaActual = vidaMaxima;
        this.posicion = new Point(filaInicial, columnaInicial);
        this.tableroAsociado = tablero;
        
        // Carga del asset
        cargarAsset(rutaImagen, 25, 25); 
    }
    
    // -----------------------------------------------------------------------------------
    // --- MÉTODOS Y LÓGICA ---
    // -----------------------------------------------------------------------------------
    
    public ImageIcon getAssetImagen() {
        return assetImagen;
    }
    
    protected void cargarAsset(String rutaImagen, int ancho, int alto) {
        try {
            URL urlRecurso = getClass().getResource(rutaImagen);

            if (urlRecurso != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
                Image imagenOriginal = iconoOriginal.getImage();
                Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                this.assetImagen = new ImageIcon(imagenEscalada);
            } else {
                System.err.println("❌ ERROR (ZOMBIE): Asset de imagen no encontrado en: " + rutaImagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el asset del zombie: " + e.getMessage());
        }
    }

    // El método abstracto actuar() es donde defines el comportamiento único
    public void actuar(Tablero tablero){
        
        this.contadorTicks++;
        if (this.contadorTicks * 100 < this.velocidad) {
            return; 
        }
        this.contadorTicks = 0;
        
        int filaActual = this.posicion.x;
        int columnaActual = this.posicion.y;
        Defensas defensaObjetivo = tablero.obtenerDefensaEn(filaActual, columnaActual);
        
        if(defensaObjetivo != null){
            defensaObjetivo.recibirDano(this.danoAtaque);
        }else{
            moverHaciaObjetivo(tablero);
        }
    }

    public void recibirDano(int dano) {
        this.vidaActual -= dano;
        if (this.vidaActual <= 0) {
            tableroAsociado.eliminarZombie(this);
        }
    }
    
    public void moverHaciaObjetivo(Tablero tablero) {
        EstructuraCentral base = tablero.getBaseCentral();
        if (base == null) return;

        Point posBase = base.getPosicion();
        int deltaFila = posBase.x - this.posicion.x;
        int deltaColumna = posBase.y - this.posicion.y;
        
        // Moverse en Columna (horizontal) tiene prioridad, ya que es la dirección principal
        if (deltaColumna != 0) {
            this.posicion.y += (deltaColumna > 0) ? 1 : -1;
            
        } 
        // Si no hay movimiento horizontal pendiente, moverse en Fila (vertical)
        else if (deltaFila != 0) {
            this.posicion.x += (deltaFila > 0) ? 1 : -1;
        }
    }
    
    // Getters
    public Point getPosicion() { return posicion; }
    public String getNombrePersonaje() { return nombrePersonaje; } // ⭐️ Nuevo Getter
}