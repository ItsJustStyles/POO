/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL; 
import javax.swing.ImageIcon;
import javax.swing.JPanel; 

public class JPanelImage2 extends JPanel {
    
    private Image imagenDeFondo; 
    private String path;

    public JPanelImage2(String path) {
        this.path = path;
        cargarImagen(); 
    }
    
    private void cargarImagen() {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                this.imagenDeFondo = new ImageIcon(url).getImage();
            } else {
                System.err.println("Error de carga: No se encontró el recurso en la ruta: " + path);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 

        if (imagenDeFondo != null) {
            g.drawImage(imagenDeFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (imagenDeFondo != null) {
            return new Dimension(imagenDeFondo.getWidth(this), imagenDeFondo.getHeight(this)); 
        }
        return new Dimension(150, 180); 
    }
    
    public void setPath(String path) {
        this.path = path;
        cargarImagen();
        this.revalidate(); 
        this.repaint(); 
    }
}