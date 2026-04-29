package model;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import model.math.Matrix;

public class Model {

    private ArrayList<Layer> nn = new ArrayList<>();

    public Model(int[] size) {
        for (int i = 1; i < size.length; i++) {
            if (i == size.length - 1) {
                nn.add(new Layer(size[i - 1], size[i], true));
            } else {
                nn.add(new Layer(size[i - 1], size[i], false));
            }
        }
    }

    public int countParams() {
        int total = 0;
        for (Layer l : nn) {
            total += l.paramCount();
        }

        return total;
    }

    public double[][] forward(double[][] input) {

        for (Layer l : nn) {
            input = l.forward(input);
        }
        return input;

    }

    public void backward(double[][] dOutput, double learningRate) {

        for (int i = nn.size() - 1; i >= 0; i--) {
            Layer currentLayer = nn.get(i);
            dOutput = currentLayer.backward(dOutput, learningRate);
        }

    }

    public double train(double[][] input, double[][] label, double learningRate) {

        double[][] out = forward(input);

        double loss = -Matrix.sum(Matrix.elementwiseProduct(label, Matrix.log(out)));
        double[][] dOutput = Matrix.subtract(out, label);

        backward(dOutput, learningRate);

        return loss;
    }

    public void save(String filename) throws Exception {
        FileWriter fw = new FileWriter(filename);
        for (Layer l : nn) {
            double[][] w = l.getWeights();
            double[][] b = l.getBiases();
            for (int i = 0; i < w.length; i++) {
                for (int j = 0; j < w[0].length; j++) {
                    fw.write(Double.toString(w[i][j]));
                    if (j < w[0].length - 1) {
                        fw.write(",");
                    }
                }
                fw.write("\n");
            }
            for (int i = 0; i < b.length; i++) {
                fw.write(Double.toString(b[i][0]) + "\n");
            }
        }
        fw.close();
        System.out.println("Model saved to " + filename);
    }

    public void load(String filename) throws Exception {
        Scanner sc = new Scanner(new File(filename));
        for (Layer l : nn) {
            double[][] w = l.getWeights();
            double[][] b = l.getBiases();
            for (int i = 0; i < w.length; i++) {
                String[] parts = sc.nextLine().split(",");
                for (int j = 0; j < w[0].length; j++) {
                    w[i][j] = Double.parseDouble(parts[j]);
                }
            }
            for (int i = 0; i < b.length; i++) {
                b[i][0] = Double.parseDouble(sc.nextLine());
            }
            l.setWeights(w);
            l.setBiases(b);
        }
        sc.close();
        System.out.println("Model loaded from " + filename);
    }

}
