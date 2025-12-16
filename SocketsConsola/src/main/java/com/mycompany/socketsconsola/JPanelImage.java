/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lacay
 */
public class JPanelImage extends JLabel {
    private int x;
    private int y;
    private String path;

    public JPanelImage(JPanel panel, String path) {
        this.x = panel.getWidth();
        this.y = panel.getHeight();
        this.path = path;
        this.setSize(x, y);
    }
    
    @Override
    public void paint(Graphics g){
        ImageIcon img= new ImageIcon(getClass().getResource(path));
        g.drawImage(img.getImage(), 0, 0, x, y, this);
    }

    public void setPath(String path) {
        this.path = path;
        this.repaint();
    }

}
