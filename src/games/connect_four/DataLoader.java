package games.connect_four;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class DataLoader {

    private ArrayList<double[][]> inputs = new ArrayList<>();
    private ArrayList<double[][]> labels = new ArrayList<>();
    private int splitIndex;

    public DataLoader(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(",");

            double[][] input = new double[42][1];
            for (int i = 0; i < 42; i++) {
                input[i][0] = Double.parseDouble(parts[i]);
            }

            int col = Integer.parseInt(parts[42]);
            double[][] label = new double[7][1];
            label[col][0] = 1.0;

            inputs.add(input);
            labels.add(label);
        }
        sc.close();

        // shuffle before splitting so val set is random
        shuffle();
        splitIndex = (int)(inputs.size() * 0.8);

        System.out.println("Loaded " + inputs.size() + " examples.");
        System.out.println("Train: " + splitIndex + " | Val: " + (inputs.size() - splitIndex));
    }

    public void shuffle() {
        for (int i = inputs.size() - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));

            double[][] tempInput = inputs.get(i);
            inputs.set(i, inputs.get(j));
            inputs.set(j, tempInput);

            double[][] tempLabel = labels.get(i);
            labels.set(i, labels.get(j));
            labels.set(j, tempLabel);
        }
    }

    // train set
    public double[][] getInput(int i) { return inputs.get(i); }
    public double[][] getLabel(int i) { return labels.get(i); }
    public int size() { return splitIndex; }

    // val set
    public double[][] getValInput(int i) { return inputs.get(splitIndex + i); }
    public double[][] getValLabel(int i) { return labels.get(splitIndex + i); }
    public int valSize() { return inputs.size() - splitIndex; }
}