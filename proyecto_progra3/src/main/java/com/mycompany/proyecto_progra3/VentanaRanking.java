/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_progra3;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class VentanaRanking extends JFrame {

    public VentanaRanking() {
        setTitle("BEST 10");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("BEST PLAYERS");
        titulo.setForeground(Color.YELLOW);
        titulo.setFont(new Font("Consolas", Font.BOLD, 28));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);

        add(titulo, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        java.util.List<String[]> datos = leerDatos();

        datos.sort((a, b) ->
                Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1]))
        );

        panel.add(crearLinea("RANK   NAME     SCORE     DATE", Color.GREEN));
        panel.add(Box.createVerticalStrut(10));

        int rank = 1;

        for (String[] d : datos) {

            String alias = d[0];
            String score = d[1];
            String fecha = d[2];

            String linea = String.format(
                    "%-6d %-10s %-7s %s",
                    rank,
                    alias.toUpperCase(),
                    score,
                    fecha
            );

            Color color;

            if (rank == 1) color = new Color(255, 215, 0);
            else if (rank == 2) color = new Color(192, 192, 192);
            else if (rank == 3) color = new Color(205, 127, 50);
            else color = Color.GREEN;

            panel.add(crearLinea(linea, color));

            rank++;
            if (rank > 10) break;
        }

        add(panel, BorderLayout.CENTER);
    }

    private JLabel crearLinea(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setForeground(color);
        label.setFont(new Font("Consolas", Font.BOLD, 18));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private java.util.List<String[]> leerDatos() {
        java.util.List<String[]> lista = new ArrayList<>();

        File file = new File("puntuaciones.txt");
        if (!file.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");

               if (parts.length >= 3) {
                  lista.add(new String[]{
                     parts[0], // alias
                     parts[1], // puntos
                     parts[2].split(" ")[0]  // fecha sin hora
    });
}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
