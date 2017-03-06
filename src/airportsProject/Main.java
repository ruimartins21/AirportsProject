package airportsProject;

public class Main {
    public static void main(String[] args) {

        System.out.println("AIRPORTS");
        String path = ".//data//airports.txt";
        ImportFromFile airports = new ImportFromFile(path);
        airports.importAirports();

        System.out.println("PLANES");
        path = ".//data//airplanes.txt";
        ImportFromFile airplanes = new ImportFromFile(path);
        airplanes.importPlanes();

    }
}
