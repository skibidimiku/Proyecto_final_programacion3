/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_progra3;

/**
 *
 * @author seraf
 */
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class PanelTablero extends JPanel {

    private JButton[][] botones;

    public PanelTablero() {

        setLayout(new GridLayout(3, 3));

        botones = new JButton[3][3];

        int numero = 1;

        for (int fila = 0; fila < 3; fila++) {

            for (int columna = 0; columna < 3; columna++) {

                if (fila == 2 && columna == 2) {
                    botones[fila][columna] = new JButton("");
                } else {
                    botones[fila][columna] = new JButton(String.valueOf(numero));
                    numero++;
                }

                add(botones[fila][columna]);
            }
        }
    }
}