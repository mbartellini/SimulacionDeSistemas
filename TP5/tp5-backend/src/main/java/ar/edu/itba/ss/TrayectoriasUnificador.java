package ar.edu.itba.ss;

import java.io.*;
import java.util.*;

public class TrayectoriasUnificador {

    public static void main(String[] args) {
        String archivo1 = "../tp5-data/Trayectorias_0_To_13_frames_1_1000_m.txt";
        String archivo2 = "../tp5-data/Trayectorias_14_To_25_frames_1_1000_m.txt";
        String archivoSalida = "../tp5-data/Trayectorias_Unificadas.txt";
        
        List<String> lineasArchivo1 = leerArchivo(archivo1);
        List<String> lineasArchivo2 = leerArchivo(archivo2);
        
        lineasArchivo1.sort(new FrameComparator());
        lineasArchivo2.sort(new FrameComparator());
        
        List<String> lineasUnificadas = new ArrayList<>();
        lineasUnificadas.addAll(procesarLineas(lineasArchivo1, 0));
        lineasUnificadas.addAll(procesarLineas(lineasArchivo2, 14));
        
        lineasUnificadas.sort(new FrameComparator());
        
        escribirArchivo(archivoSalida, lineasUnificadas);
    }
    
    public static List<String> leerArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lineas;
    }

    public static List<String> procesarLineas(List<String> lineas, int offsetId) {
        List<String> lineasProcesadas = new ArrayList<>();
        for (String linea : lineas) {
            linea = linea.trim();  // Elimina los espacios en blanco al principio y al final de la línea
            String[] partes = linea.split("\\s+");  // Divide la línea en partes basadas en uno o más espacios en blanco
            try {
                double frameDouble = Double.parseDouble(partes[0]);
                int frame = (int) frameDouble;
                double y = Double.parseDouble(partes[1]);
                double x = Double.parseDouble(partes[2]);
                double idDouble = Double.parseDouble(partes[3]) + offsetId;  // Parsea el id como un double
                int id = (int) idDouble;  // Convierte el double a un int
                double seg = frame * 4.0 / 30.0;
                lineasProcesadas.add(String.format(Locale.US, "%g\t%s\t%s\t%d", seg, partes[1], partes[2], id));  // TODO: Preguntar presizao
            } catch (NumberFormatException nfe) {
                System.err.println("Error al procesar la línea: " + linea);
            }
        }
        return lineasProcesadas;
    }

    public static void escribirArchivo(String ruta, List<String> lineas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (String linea : lineas) {
                bw.write(linea);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static class FrameComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            o1 = o1.trim(); o2 = o2.trim();
            double frame1 = Double.parseDouble(o1.split("\\s+")[0]);
            double frame2 = Double.parseDouble(o2.split("\\s+")[0]);
            return Double.compare(frame1, frame2);
        }
    }
}
