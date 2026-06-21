/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_progra3;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class RankingManager {

    private static final String ARCHIVO = "puntuaciones.txt";

    public static void guardar(String alias, int movimientos) {
        Map<String, Integer> ranking = leerTodo();

        int puntos = Math.max(0, 1000 - movimientos);

        ranking.put(alias, ranking.getOrDefault(alias, 0) + puntos);

        escribirTodo(ranking);
    }

    private static Map<String, Integer> leerTodo() {
        Map<String, Integer> data = new HashMap<>();

        File file = new File(ARCHIVO);
        if (!file.exists()) return data;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String alias = parts[0];
                    int puntos = Integer.parseInt(parts[1]);

                    data.put(alias, data.getOrDefault(alias, 0) + puntos);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static void escribirTodo(Map<String, Integer> ranking) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {

            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new Date());

            for (Map.Entry<String, Integer> entry : ranking.entrySet()) {

                bw.write(entry.getKey() + "|" + entry.getValue() + "|" + fecha);
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}