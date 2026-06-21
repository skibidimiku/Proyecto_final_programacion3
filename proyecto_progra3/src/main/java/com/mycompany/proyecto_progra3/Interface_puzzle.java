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
    private int[][] tablero, tableroMeta;
    private JPanel panelTablero;
    private Audio reproductor = new Audio();
    private int movimientos = 0;
    private JPanel panelMeta;
    private JLabel[][] labelsMeta = new JLabel[3][3];
            
    
    class NodoPuzzle implements Comparable<NodoPuzzle> {
        int[][] estado;
        NodoPuzzle padre;
        int g, h, f;
        int fichaMovida;

        NodoPuzzle(int[][] estado, NodoPuzzle padre, int g, int fichaMovida) {
            this.estado = estado;
            this.padre = padre;
            this.g = g;
            this.h = distanciaManhattan(estado); 
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
        panelMeta = new JPanel(new GridLayout(3, 3, 8, 8));
        panelMeta.setBorder(BorderFactory.createTitledBorder("Tablero Meta"));
        panelMeta.setBackground(Color.LIGHT_GRAY);
        panelMeta.setBounds(20, 20, 350, 100); 
        

        JButton btnIniciar = new JButton("Iniciar");
        JButton btnTerminar = new JButton("Terminar");
        JButton btnInteligente = new JButton("Inteligente");
        JButton btnPuntuaciones = new JButton("Puntuaciones");
        JButton btnSugerencia = new JButton("Sugerir");


        
        
        btnIniciar.setBounds(20, 140, 120, 40);

        btnInteligente.setBounds(250, 140, 120, 40);

        btnSugerencia.setBounds(250, 190, 120, 40);

        btnPuntuaciones.setBounds(20, 200, 170, 40);

        btnTerminar.setBounds(20, 260, 170, 40);

        btnIniciar.addActionListener(new ActionListener() {
            private boolean iniciado = false;

            public void actionPerformed(ActionEvent e) {
                tablero = generarTableroAleatorio();
                tableroMeta = generarTableroMeta();

                if (!iniciado) {
                    inicializarTablero();
                    inicializarPanelMeta(); // mostrar meta
                    btnIniciar.setText("Nuevo Nivel");
                    iniciado = true;
                    reproductor.reproducir("musica.wav", true);
                } else {
                    actualizarTablero();
                    inicializarPanelMeta(); 
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
            mostrarTableroEditable();
        }); 
        
        panelLateral.add(panelMeta);
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
            
            movimientos ++;
            
            actualizarTablero();
        }
        
        if (esGanador()){ 
            mostrarPantallaVictoria();
            movimientos = 0;
        }
    }
        
    private int[][] generarTableroAleatorio() {
        java.util.List<Integer> numeros = new java.util.ArrayList<>();
        for (int i = 0; i < 9; i++) {
            numeros.add(i); 
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
    
    private int[][] generarTableroMeta() {
        return generarTableroAleatorio();
    }
    
    private void inicializarPanelMeta() {
        panelMeta.removeAll();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                labelsMeta[i][j] = new JLabel(tableroMeta[i][j] == 0 ? "" : String.valueOf(tableroMeta[i][j]));
                labelsMeta[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                labelsMeta[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                labelsMeta[i][j].setOpaque(true);
                labelsMeta[i][j].setBackground(new Color(220, 220, 220));
                panelMeta.add(labelsMeta[i][j]);
            }
        }
        panelMeta.revalidate();
        panelMeta.repaint();
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
        if (tablero == null || tableroMeta == null) {
            JOptionPane.showMessageDialog(this, "Primero inicia una partida.");
            return;
        }

        NodoPuzzle inicial = new NodoPuzzle(copiarTablero(tablero), null, 0, -1);

        java.util.PriorityQueue<NodoPuzzle> abiertos = new java.util.PriorityQueue<>();
        java.util.HashSet<String> cerrados = new java.util.HashSet<>();

        abiertos.add(inicial);

        while (!abiertos.isEmpty()) {
            NodoPuzzle actual = abiertos.poll();

            if (java.util.Arrays.deepEquals(actual.estado, tableroMeta)) {
                while (actual.padre != null && actual.padre.padre != null) {
                    actual = actual.padre;
                }
                
                JOptionPane.showMessageDialog(this,
                "Sugerencia (A*): mover la ficha " + actual.fichaMovida);
                movimientos += 20;
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
    
    private java.util.List<Integer> resolverconInteligente(int[][] inicial, int[][] meta) {
        NodoPuzzle start = new NodoPuzzle(copiarTablero(inicial), null, 0, -1);
        java.util.PriorityQueue<NodoPuzzle> abiertos = new java.util.PriorityQueue<>();
        java.util.HashSet<String> cerrados = new java.util.HashSet<>();
        abiertos.add(start);

        while (!abiertos.isEmpty()) {
            NodoPuzzle actual = abiertos.poll();

            if (java.util.Arrays.deepEquals(actual.estado, meta)) {
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
    
    private void modoInteligenteConMeta(int[][] inicial, int[][] meta) {
        java.util.List<Integer> solucion = resolverconInteligente(inicial, meta);
        if (solucion == null) {
            JOptionPane.showMessageDialog(this, "No se encontró solución.");
            return;
        }

        new Thread(() -> {
            for (int ficha : solucion) {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                moverFichaPorValor(ficha);
            }
            
            JOptionPane.showMessageDialog(this, "Modo inteligente completado, pasando a manual");
            
            tablero = generarTableroAleatorio();
            tableroMeta = generarTableroMeta();
            actualizarTablero();
            inicializarPanelMeta();
            movimientos = 0;
        }).start();
    }
    
    private boolean esGanador() {        
        return java.util.Arrays.deepEquals(tablero, tableroMeta);
    }
    
    private void mostrarPantallaVictoria() {
        JTextField txtAlias = new JTextField();

        Object[] mensaje = {
            "¡Felicidades, resolviste el puzzle!",
            "Con un puntaje: " + (1000 - movimientos) ,
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

            RankingManager.guardar(alias, movimientos);
        }
        
        tablero = generarTableroAleatorio();
        tableroMeta = generarTableroMeta();
        actualizarTablero();
        inicializarPanelMeta(); 
    }
    
    private void mostrarTableroEditable() {
        JDialog dialogo = new JDialog(this, "Configurar tablero inicial y meta", true);
        dialogo.setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel panelInicial = new JPanel(new GridLayout(3, 3, 5, 5));
        JTextField[][] camposInicial = new JTextField[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                camposInicial[i][j] = new JTextField();
                camposInicial[i][j].setHorizontalAlignment(JTextField.CENTER);
                camposInicial[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                panelInicial.add(camposInicial[i][j]);
            }
        }

        JPanel panelMeta = new JPanel(new GridLayout(3, 3, 5, 5));
        JTextField[][] camposMeta = new JTextField[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                camposMeta[i][j] = new JTextField();
                camposMeta[i][j].setHorizontalAlignment(JTextField.CENTER);
                camposMeta[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                panelMeta.add(camposMeta[i][j]);
            }
        }

        panelPrincipal.add(new JLabel("Tablero inicial", SwingConstants.CENTER));
        panelPrincipal.add(panelInicial);
        panelPrincipal.add(new JLabel("Tablero meta", SwingConstants.CENTER));
        panelPrincipal.add(panelMeta);

        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.addActionListener(e -> {
            int[][] tableroInicial = new int[3][3];
            int[][] tableroMeta = new int[3][3];
            java.util.Set<Integer> numerosInicial = new java.util.HashSet<>();
            java.util.Set<Integer> numerosMeta = new java.util.HashSet<>();

            try {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int valor = Integer.parseInt(camposInicial[i][j].getText().trim());
                        tableroInicial[i][j] = valor;
                        numerosInicial.add(valor);
                    }
                }

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int valor = Integer.parseInt(camposMeta[i][j].getText().trim());
                        tableroMeta[i][j] = valor;
                        numerosMeta.add(valor);
                    }
                }

                if (numerosInicial.size() != 9 || !numerosInicial.containsAll(java.util.List.of(0,1,2,3,4,5,6,7,8)) ||
                    numerosMeta.size() != 9 || !numerosMeta.containsAll(java.util.List.of(0,1,2,3,4,5,6,7,8))) {
                    JOptionPane.showMessageDialog(dialogo, "Ambos tableros deben contener los números del 0 al 8 sin repetir.");
                    return;
                }

                tablero = tableroInicial;
                inicializarTablero();
                dialogo.dispose();
            
                modoInteligenteConMeta(tableroInicial, tableroMeta);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Solo se permiten números enteros.");
            }
        });

        dialogo.add(panelPrincipal, BorderLayout.CENTER);
        dialogo.add(btnAceptar, BorderLayout.SOUTH);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }
}



