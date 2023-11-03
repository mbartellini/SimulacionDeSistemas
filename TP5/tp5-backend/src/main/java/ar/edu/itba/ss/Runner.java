package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {

    public static final String INPUT_FILE = "../tp5-data/Trayectorias_Unificadas.txt";
    public static final double DT = 4. /30/100, DT2 = 4./30;

    public static void main(String[] args) {
        List<String> lineasArchivo = TrayectoriasUnificador.leerArchivo(INPUT_FILE);

        lineasArchivo.sort(new SecondComparator());

        List<InputData> inputDataList = lineasArchivo.stream().map(s -> {
            s = s.trim();
            String[] partes = s.split("\\s+");
            try {
                double second = Double.parseDouble(partes[0]);
                double y = Double.parseDouble(partes[1]);
                double x = Double.parseDouble(partes[2]);
                int id = (int) Double.parseDouble(partes[3]);
                return new InputData(second, x, y, id);
            } catch (NumberFormatException nfe) {
                System.err.println("Error al procesar la línea: " + s);
            }
            return null;
        }).toList();
        Map<Integer, List<InputData>> inputDataByID = inputDataList.stream().collect(Collectors.groupingBy(InputData::getId));
        Simulation s = new Simulation(
                new Particle(25, 9.75, -6.5, 0.3, 70, 0, 0),
                inputDataByID,
                DT,
                33.5,
                100);
        s.run();
    }

    static class SecondComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            o1 = o1.trim(); o2 = o2.trim();
            double sec1 = Double.parseDouble(o1.split("\\s+")[0]);
            double sec2 = Double.parseDouble(o2.split("\\s+")[0]);
            return Double.compare(sec1, sec2);
        }
    }

    public static class InputData {
        private double second;
        private double x, y;
        private int id;

        public InputData(double second, double x, double y, int id) {
            this.second = second;
            this.x = x;
            this.y = y;
            this.id = id;
        }

        public double getSecond() {
            return second;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getId() {
            return id;
        }
    }

}
