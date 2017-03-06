package airportsProject;

import edu.princeton.cs.algs4.In;

import java.io.File;
import java.util.Arrays;

public class ImportFromFile {
    private String path;

    public ImportFromFile(String path) {
        setPath(path);
    }

    private void setPath(String path) {
        if (new File(path).exists()) {
            this.path = path;
        }else{
            throw new IllegalArgumentException("Path \"" + path + "\" doesn't exist");
        }
    }

    public void importAirports() {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            System.out.println(Arrays.toString(fileContent));
            // criar ST nao ordenada
        }
    }

    public void importPlanes() {
        In in = new In(path);
        while (!in.isEmpty()) {
            String[] fileContent = in.readLine().split(";");
            System.out.println(Arrays.toString(fileContent));
            // pegar na companhia aerea e noutra funcao inserir a companhia na respetiva table, verificar se ja existe
            // ST ordenada por nome
        }
    }
}
