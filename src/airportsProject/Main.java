package airportsProject;

public class Main {
    public static void main(String[] args) {

        System.out.println("AIRPORTS");
        System.out.println("-------------------");
        String path = ".//data//airports.txt";
        ImportFromFile airports = new ImportFromFile(path);
        airports.importAirports();

        System.out.println("PLANES");
        System.out.println("-------------------");
        path = ".//data//airplanes.txt";
        ImportFromFile airplanes = new ImportFromFile(path);
        airplanes.importPlanes();

        System.out.println("AIRLINES");
        System.out.println("-------------------");
        path = ".//data//airlines.txt";
        ImportFromFile airlines = new ImportFromFile(path);
        airlines.importAirlines();

    }
}
