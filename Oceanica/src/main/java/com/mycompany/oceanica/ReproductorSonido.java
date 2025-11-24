package com.mycompany.oceanica;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author lacay
 */
public class ReproductorSonido {
    private static Clip clip;
    Juego refFrame;

    public ReproductorSonido(String rutaInterna, Juego refFrame){
        this.refFrame = refFrame;
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
            
            if (refFrame.menuPersonajes != null) {
                refFrame.menuPersonajes.stop();
            }
            
            clip.stop();
            clip.setFramePosition(0);
            
            final LineListener[] tempHolder = new LineListener[1];

            LineListener listener = new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.removeLineListener(tempHolder[0]);
                        if (refFrame != null && refFrame.menuPersonajes != null) {
                            refFrame.menuPersonajes.loop();
                        }
                    }
                }
            };

            tempHolder[0] = listener;
            clip.addLineListener(listener);
            
            clip.start();
        }
    }
    
    public void playSeguido(){
            if (clip != null) {
            
            if (refFrame.menuPersonajes != null) {
                refFrame.menuPersonajes.stop();
            }
            
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
    }}
    
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
        refFrame.menuPersonajes.loop();
    }
   
}


