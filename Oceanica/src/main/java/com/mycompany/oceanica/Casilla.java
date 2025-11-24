/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

/**
 *
 * @author gabos
 */
public class Casilla {
    private int vida;
    private boolean muerta;
    private Personaje p;
    private int x;
    private int y;
    private JPanel panel;
    private Timer temporizador;
    
    private boolean tieneVolcan;
    private boolean tieneRemolino;
    
    private JLabel pX;
    private JLabel vidaC;
    private JFrame refFrame;
    private List<String> registros = new ArrayList<>();

    public Casilla(int vida, Personaje p, int x, int y, JPanel panel, JFrame refFrame) {
        this.vida = vida;
        this.p = p;
        this.x = x;
        this.y = y;
        this.panel = panel;
        this.refFrame = refFrame;
    }

    
    public boolean esta_vivo(){
        return vida > 0;
    }
    
    public void recibirAtaque(int fuerza){
        this.vida -= fuerza;
    }
    
    public void curar(int sanidad){
        this.vida += sanidad;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setMuerta(boolean muerta) {
        this.muerta = muerta;
    }
    
    public void registrar(String r){
        registros.add(r);
    }

    public void ponerX(){
        setMuerta(true);
        
        pX = new JLabel("X");
        panel.setLayout(new BorderLayout());
        pX.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        pX.setForeground(java.awt.Color.WHITE);
        
        pX.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pX.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        
        panel.add(pX, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint(); 
    }
    
    private void quitarX(){
        if(pX != null){
            panel.remove(pX);
            pX = null;
            panel.revalidate();
            panel.repaint();
        }
    }
    
    private void quitarVida(){
        if (vidaC != null) {
            panel.remove(vidaC); 
            vidaC = null;        
            panel.revalidate();      
            panel.repaint();        
        }
    }
    
    public void ponerVida(){
        quitarX();
        
        if(vidaC != null){
            panel.remove(vidaC);
            vidaC = null;
        }
        if (temporizador != null && temporizador.isRunning()) {
            temporizador.stop();
        }
        
        vidaC = new JLabel(vida + "");
        vidaC.setFont(new java.awt.Font("Arial", Font.BOLD, 8));
        vidaC.setForeground(java.awt.Color.WHITE);
        
        vidaC.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        vidaC.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        //vidaC.setPreferredSize(new Dimension(15, 15));
        
        panel.add(vidaC, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint(); 
        
        int tiempoAnimacion = 2500;
        
        temporizador = new Timer(tiempoAnimacion, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                quitarVida();
                if(muerta){
                    ponerX();
                }
                temporizador.stop();
            }
            
        });
        temporizador.setRepeats(false);
        temporizador.start();
    }
    
    public void pintarVivas(){
        Color colorAntiguo = panel.getBackground();
        
        quitarX();
        
        if(esta_vivo()){
            panel.setBackground(Color.GREEN);
        }else{
            panel.setBackground(Color.BLACK);
        }
        
        int tiempoAnimacion = 2500;
        temporizador = new Timer(tiempoAnimacion, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(colorAntiguo);
                if(muerta){
                    ponerX();
                }
                temporizador.stop();
            }
            
        });
        temporizador.setRepeats(false);
        temporizador.start();
        
    }
    
    public void MostrarCeldasOcupadas(){
        Color colorAntiguo = panel.getBackground();
        quitarX();
        
        if(tieneVolcan && tieneRemolino){
            panel.setBackground(new Color(112, 63, 28));
        }else if(tieneVolcan){
            panel.setBackground(Color.RED);
        }else if(tieneRemolino){
            panel.setBackground(Color.GRAY);
        }
        
        int tiempoAnimacion = 2500;
        temporizador = new Timer(tiempoAnimacion, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.setBackground(colorAntiguo);
                if(muerta){
                    ponerX();
                }
                temporizador.stop();
            }
            
        });
        temporizador.setRepeats(false);
        temporizador.start();
        
    }

    public void setTieneVolcan(boolean tieneVolcan) {
        this.tieneVolcan = tieneVolcan;
    }

    public void setTieneRemolino(boolean tieneRemolino) {
        this.tieneRemolino = tieneRemolino;
    }
    
    public void consultarCelda(){
        JDialog infoCasilla = new JDialog(refFrame, "Información de la Casilla (" + x + "," + y + ")", Dialog.ModalityType.APPLICATION_MODAL);
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelContenido.setBackground(new Color(30, 30, 30));
        
        JLabel lblTitulo = new JLabel("Información de la casilla (" + x + "," + y + ")");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(255, 200, 0));
        lblTitulo.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        JLabel lblVida = new JLabel("Vida: " + vida);
        lblVida.setForeground(Color.WHITE);
        lblVida.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        String isVivo;
        if(esta_vivo()){
            isVivo = "Viva";
        }else{
            isVivo = "Muerta";
        }
        
        JLabel lblEstado = new JLabel("Estado: " + isVivo) ;
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        
        // --- Sección de Registros ---
        JLabel lblTituloRegistro = new JLabel("--- Registros de Eventos ---");
        lblTituloRegistro.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloRegistro.setForeground(new Color(0, 200, 255));
        lblTituloRegistro.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        JPanel panelRegistros = new JPanel();
        panelRegistros.setLayout(new BoxLayout(panelRegistros, BoxLayout.Y_AXIS));
        panelRegistros.setBackground(new Color(40, 40, 40));
        panelRegistros.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        if(!registros.isEmpty()){
            for(String registroUnico : registros){ 
                JLabel lblEntrada = new JLabel(registroUnico); 
                lblEntrada.setForeground(new Color(180, 180, 180));
                lblEntrada.setFont(new Font("Monospaced", Font.PLAIN, 12));
                lblEntrada.setAlignmentX(JComponent.LEFT_ALIGNMENT);
                panelRegistros.add(lblEntrada);
                panelRegistros.add(Box.createVerticalStrut(2)); 
            }
        } else {
            JLabel lblNoRegistros = new JLabel("No hay registros de eventos para esta casilla.");
            lblNoRegistros.setForeground(new Color(150, 150, 150));
            lblNoRegistros.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            panelRegistros.add(lblNoRegistros);
        }

        JScrollPane scrollRegistros = new JScrollPane(panelRegistros);
        scrollRegistros.setPreferredSize(new Dimension(300, 150));
        scrollRegistros.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollRegistros.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollRegistros.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));
        // --- Fin Sección de Registros ---
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(e -> infoCasilla.dispose());
        btnCerrar.setBackground(new Color(70, 70, 70));
        btnCerrar.setForeground(Color.WHITE);
        
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(lblVida);
        panelContenido.add(lblEstado);
        panelContenido.add(lblTituloRegistro);
        panelContenido.add(Box.createVerticalStrut(5));
        panelContenido.add(scrollRegistros);
        
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(btnCerrar, BorderLayout.SOUTH);

        infoCasilla.setContentPane(panelContenido);
        infoCasilla.pack();
        infoCasilla.setLocationRelativeTo(refFrame); 
        infoCasilla.setVisible(true);
        
    }
}
