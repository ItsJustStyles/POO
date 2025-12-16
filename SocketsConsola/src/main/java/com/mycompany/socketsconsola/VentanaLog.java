/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 *
 * @author gabos
 */


public class VentanaLog extends JFrame {
    private JTextArea textArea;
    private String nombreJugador;
    private String contenido;
    
    public VentanaLog(String nombreJugador, String contenido, String titulo) {
        this.nombreJugador = nombreJugador;
        this.contenido = contenido;
        initComponents(titulo);
    }
    
    private void initComponents(String titulo) {
        setTitle(titulo + " - " + nombreJugador);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblJugador = new JLabel("Jugador: " + nombreJugador);
        lblJugador.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(lblJugador);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setText(contenido);
        textArea.setCaretPosition(0); // Scroll al inicio
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
       
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> actualizarContenido());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    
    
    private void actualizarContenido() {
        String nuevoContenido = LogPartida.obtenerLogMasReciente(nombreJugador);
        this.contenido = nuevoContenido;
        textArea.setText(nuevoContenido);
        textArea.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, 
            "Contenido actualizado", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
