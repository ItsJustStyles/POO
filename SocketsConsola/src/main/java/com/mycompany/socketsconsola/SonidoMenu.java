/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author lacay
 */
public class SonidoMenu {
    
     private Clip clip;

    public SonidoMenu(String rutaInterna) throws IOException, LineUnavailableException{

        try {
            
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }
            
            InputStream file = getClass().getResourceAsStream(rutaInterna);
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(file));

            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (UnsupportedAudioFileException e) {
            System.out.println("⚠ Formato de audio no soportado: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("⚠ Error leyendo el archivo: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("⚠ Línea de audio no disponible: " + e.getMessage());
        }
    }

    // Reproducir desde el inicio
    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Reproducir en loop infinito
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Pausar la reproducción
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    // Cierra recursos
    public void close() {
        if (clip != null) {
            clip.close();
        }
    }
    
    public void waitUntilFinished() {
        if (clip != null) {
            while (clip.isRunning()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) { }
            }
        }
    }
}