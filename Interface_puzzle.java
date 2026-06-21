/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_progra3;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author MSI
 */

public class Interface_puzzle extends JFrame {
    private JButton[][] botones = new JButton[3][3];
    private int[][] tablero;
    private JPanel panelTablero;
    private Audio reproductor = new Audio();
    class NodoPuzzle implements Comparable<NodoPuzzle> {
        int[][] estado;
        NodoPuzzle padre;
        int g, h, f;
        int fichaMovida;
        

        NodoPuzzle(int[][] estado, NodoPuzzle padre, int g, int fichaMovida) {
            this.estado = estado;
            this.padre = padre;
            this.g = g;
            this.h = distanciaManhattan(estado); // ✅ ya compila
            this.f = g + h;
            this.fichaMovida = fichaMovida;
        }

        @Override
        public int compareTo(NodoPuzzle otro) {
            return Integer.compare(this.f, otro.f);
        }
    }


    public Interface_puzzle() {
        setTitle("8-Acertijo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

    
        panelTablero = new JPanel(new GridLayout(3, 3, 8, 8));
        panelTablero.setBackground(Color.DARK_GRAY);
        panelTablero.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelTablero.setPreferredSize(new Dimension(330, 330));

        JPanel panelLateral = new JPanel(null);
        panelLateral.setPreferredSize(new Dimension(420, 330));
        panelLateral.setBackground(new Color(200, 255, 200));

        JLabel lblUsuario = new JLabel("Usuario (rellene para jugar manual):");
        JTextField txtUsuario = new JTextField();

        JButton btnIniciar = new JButton("Iniciar");
        JButton btnTerminar = new JButton("Terminar");
        JButton btnInteligente = new JButton("Inteligente");
        JButton btnPuntuaciones = new JButton("Puntuaciones");
        JButton btnSugerencia = new JButton("Sugerir");

        lblUsuario.setBounds(20, 20, 350, 30);
        txtUsuario.setBounds(20, 55, 350, 40);
        
        
        btnIniciar.setBounds(20, 140, 120, 40);

        btnInteligente.setBounds(250, 140, 120, 40);

        btnSugerencia.setBounds(250, 190, 120, 40);

        btnPuntuaciones.setBounds(20, 200, 170, 40);

        btnTerminar.setBounds(20, 260, 170, 40);

        btnIniciar.addActionListener(new ActionListener() {
            private boolean iniciado = false;

            public void actionPerformed(ActionEvent e) {
                tablero = generarTableroAleatorio();

                if (!iniciado) {
                    inicializarTablero();
                    btnIniciar.setText("Nuevo Nivel");
                    iniciado = true;

                    reproductor.reproducir("musica.wav", true);
                } else {
                    actualizarTablero();
                }
            }
        });
        
        btnPuntuaciones.addActionListener(e -> {
            new VentanaRanking().setVisible(true);
        });
        
        btnTerminar.addActionListener(e -> {
            reproductor.detener();
            dispose();
        });

        btnSugerencia.addActionListener(e -> {
            sugerirMovimiento();
        });
        
        btnInteligente.addActionListener(e -> {
            modoInteligente();
        }); 
        
        
        panelLateral.add(lblUsuario);
        panelLateral.add(txtUsuario);
        panelLateral.add(btnIniciar);
        panelLateral.add(btnInteligente);
        panelLateral.add(btnPuntuaciones);
        panelLateral.add(btnTerminar);
        panelLateral.add(btnSugerencia);

        add(panelTablero, BorderLayout.WEST);
        add(panelLateral, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
}

    
    private void inicializarTablero() {
        panelTablero.removeAll();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j] = new botton(tablero[i][j] == 0 ? "" : String.valueOf(tablero[i][j]));
                botones[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                final int fila = i, col = j;
                botones[i][j].addActionListener(e -> moverFicha(fila, col));
                panelTablero.add(botones[i][j]);
            }
        }
        panelTablero.revalidate();
        panelTablero.repaint();
    }

    // Actualiza el tablero ya existente
    private void actualizarTablero() {
        if (botones[0][0] == null) {
            inicializarTablero();
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText(
                    tablero[i][j] == 0 ? "" : String.valueOf(tablero[i][j])
                );
            }
        }
    }
private int movimientos = 0;
    private void moverFicha(int fila, int col) {

    int filaVacia = -1, colVacia = -1;

    reproductor.reproducir("musicabotton.wav", false);

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            if (tablero[i][j] == 0) {
                filaVacia = i;
                colVacia = j;
            }
        }
    }

    if ((Math.abs(fila - filaVacia) == 1 && col == colVacia) ||
        (Math.abs(col - colVacia) == 1 && fila == filaVacia)) {

        tablero[filaVacia][colVacia] = tablero[fila][col];
        tablero[fila][col] = 0;

        movimientos++; 

        actualizarTablero();
    }
    if (esGanador()) {
    mostrarPantallaVictoria();
    
}}

    // Genera tablero aleatorio válido
    private int[][] generarTableroAleatorio() {
        java.util.List<Integer> numeros = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            numeros.add(i); // 0 representa el espacio vacío
        }

        java.util.Collections.shuffle(numeros);

        int[][] nuevoTablero = new int[3][3];
        int pos = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                nuevoTablero[i][j] = numeros.get(pos++);
            }
        }
        
        if (!esSoluble(nuevoTablero)) {
            return generarTableroAleatorio();
            }
        return nuevoTablero;
    }

    private boolean esSoluble(int[][] tablero) {
        int[] plano = new int[9];
        int index = 0;
        for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) plano[index++] = tablero[i][j];
        int inversiones = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (plano[i] != 0 && plano[j] != 0 && plano[i] > plano[j]) inversiones++;
            }
        }
        return inversiones % 2 == 0;
    }
    
    private int[][] copiarTablero(int[][] original) {
        int[][] copia = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                copia[i][j] = original[i][j];
            }
        }

        return copia;
    }
    
    private static int distanciaManhattan(int[][] estado) {
        int distancia = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int valor = estado[i][j];

                if (valor != 0) {

                    int filaObjetivo = (valor - 1) / 3;
                    int colObjetivo = (valor - 1) % 3;

                    distancia += Math.abs(i - filaObjetivo)
                           + Math.abs(j - colObjetivo);
                }
            }
        }

        return distancia;
    }
    
    private void sugerirMovimiento() {
        if (tablero == null) {
            JOptionPane.showMessageDialog(this, "Primero inicia una partida.");
            return;
        }

        NodoPuzzle inicial = new NodoPuzzle(copiarTablero(tablero), null, 0, -1);

        java.util.PriorityQueue<NodoPuzzle> abiertos = new java.util.PriorityQueue<>();
        java.util.HashSet<String> cerrados = new java.util.HashSet<>();

        abiertos.add(inicial);

        while (!abiertos.isEmpty()) {
            NodoPuzzle actual = abiertos.poll();

            if (distanciaManhattan(actual.estado) == 0) {
                while (actual.padre != null && actual.padre.padre != null) {
                    actual = actual.padre;
                }
                JOptionPane.showMessageDialog(this,
                    "Sugerencia (A*): mover la ficha " + actual.fichaMovida);
                return;
            }

            cerrados.add(java.util.Arrays.deepToString(actual.estado));

            int filaVacia = -1, colVacia = -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (actual.estado[i][j] == 0) {
                        filaVacia = i; colVacia = j;
                    }
                }
            }

            int[][] direcciones = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : direcciones) {
                int nf = filaVacia + d[0], nc = colVacia + d[1];
                if (nf >= 0 && nf < 3 && nc >= 0 && nc < 3) {
                    int[][] copia = copiarTablero(actual.estado);
                    copia[filaVacia][colVacia] = copia[nf][nc];
                    copia[nf][nc] = 0;

                    if (!cerrados.contains(java.util.Arrays.deepToString(copia))) {
                        abiertos.add(new NodoPuzzle(copia, actual, actual.g+1, actual.estado[nf][nc]));
                    }
                }
            }
        }

        JOptionPane.showMessageDialog(this, "No se encontró sugerencia.");
    }
    
    private java.util.List<Integer> resolverconInteligente(int[][] inicial) {
        NodoPuzzle start = new NodoPuzzle(copiarTablero(inicial), null, 0, -1);
        java.util.PriorityQueue<NodoPuzzle> abiertos = new java.util.PriorityQueue<>();
        java.util.HashSet<String> cerrados = new java.util.HashSet<>();
        abiertos.add(start);

        while (!abiertos.isEmpty()) {
            NodoPuzzle actual = abiertos.poll();

            if (distanciaManhattan(actual.estado) == 0) {
                java.util.List<Integer> camino = new java.util.ArrayList<>();
                while (actual.padre != null) {
                    camino.add(0, actual.fichaMovida);
                    actual = actual.padre;
                }
                return camino;
            }

            cerrados.add(java.util.Arrays.deepToString(actual.estado));

            int filaVacia = -1, colVacia = -1;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (actual.estado[i][j] == 0) {
                        filaVacia = i; colVacia = j;
                    }
                }
            }

            int[][] direcciones = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : direcciones) {
                int nf = filaVacia + d[0], nc = colVacia + d[1];
                if (nf >= 0 && nf < 3 && nc >= 0 && nc < 3) {
                    int[][] copia = copiarTablero(actual.estado);
                    copia[filaVacia][colVacia] = copia[nf][nc];
                    copia[nf][nc] = 0;

                    if (!cerrados.contains(java.util.Arrays.deepToString(copia))) {
                        abiertos.add(new NodoPuzzle(copia, actual, actual.g+1, actual.estado[nf][nc]));
                    }
                }
            }
        }
        
        return null;
    }
    
    private void moverFichaPorValor(int valor) {
        int filaFicha = -1, colFicha = -1;
        int filaVacia = -1, colVacia = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tablero[i][j] == valor) { filaFicha = i; colFicha = j; }
                if (tablero[i][j] == 0) { filaVacia = i; colVacia = j; }
            }
        }

        tablero[filaVacia][colVacia] = valor;
        tablero[filaFicha][colFicha] = 0;
        actualizarTablero();
    }
    
    private void modoInteligente() {
        java.util.List<Integer> solucion = resolverconInteligente(tablero);
        if (solucion == null) {
            JOptionPane.showMessageDialog(this, "No se encontró solución.");
            return;
        }

        new Thread(() -> {
            for (int ficha : solucion) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                    moverFichaPorValor(ficha);
            }
        }).start();
    }
    
    private boolean esGanador() {
            
    int valor = 1;

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {

            if (i == 2 && j == 2) {
                return tablero[i][j] == 0;
            }

            if (tablero[i][j] != valor++) {
                return false;
            }
        }
    }
    return true;
}
    
    private void mostrarPantallaVictoria() {

    JTextField txtAlias = new JTextField();

    Object[] mensaje = {
        "¡Felicidades, resolviste el puzzle!",
        "Ingresa tu alias:",
        txtAlias
    };

    int opcion = JOptionPane.showConfirmDialog(
        this,
        mensaje,
        "Victoria",
        JOptionPane.OK_CANCEL_OPTION
    );

    if (opcion == JOptionPane.OK_OPTION) {

        String alias = txtAlias.getText().trim();
        if (alias.isEmpty()) alias = "Anonimo";

        RankingManager.guardar(alias, movimientos); // 👈 AQUÍ está el cambio
    }
}
}



