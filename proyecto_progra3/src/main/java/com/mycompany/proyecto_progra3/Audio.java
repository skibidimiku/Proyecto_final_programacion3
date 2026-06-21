/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_progra3;
import javax.sound.sampled.*;
import java.io.IOException;

/**
 *
 * @author MSI
 */
public class Audio {
       private Clip musica;

    public void reproducir(String nombreArchivo, boolean repetir) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/" + nombreArchivo) // busca en src/main/resources
            );
            musica= AudioSystem.getClip();
            musica.open(audioStream);

            if (repetir) {
                musica.loop(Clip.LOOP_CONTINUOUSLY); // 🔁 bucle infinito
            } else {
                musica.start(); // solo una vez
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void detener() {
        if (musica != null && musica.isRunning()) {
            musica.stop();
        }
    }
}
