package org.example.model;

import org.example.data_processing.DataLoader;
import org.example.model.math.Matrix;

public class Trainer {

    private Model model;
    private DataLoader dataLoader;

    private int epochs;
    private double lr;

    public Trainer(DataLoader dataLoader, int[] size, int epochs, double learningRate) {
        this.dataLoader = dataLoader;
        model = new Model(size);

        this.epochs = epochs;
        lr = learningRate;
    }

    private double trainEpoch() {
        double totalLoss = 0;

        for (int i = 0; i < dataLoader.size(); i++) {
            double[][] input = dataLoader.getInput(i);
            double[][] label = dataLoader.getLabel(i);

            double stepLoss = model.train(input, label, lr);
            totalLoss += stepLoss;
        }

        return totalLoss / dataLoader.size();
    }

    private double train() throws Exception {

        double finalLoss = 0;

        System.out.println("Parameters: " + model.countParams());

        for (int i = 0; i < epochs; i++) {
            dataLoader.shuffle();
            double avgLoss = trainEpoch();
            System.out.println("Epoch " + (i + 1) + " loss: " + avgLoss);

            double[] val = validate();
            System.out.println("Val loss: " + val[0] + ", val accuracy: " + val[1] + "%");

            finalLoss = avgLoss;
        }

        model.save("src/model/saved_models/model_mlp_v1.txt");

        return finalLoss;
    }

    private double[] validate() {
        double totalLoss = 0;
        int correct = 0;

        for (int i = 0; i < dataLoader.valSize(); i++) {
            double[][] valInput = dataLoader.getValInput(i);
            double[][] valLabel = dataLoader.getValLabel(i);

            double[][] output = model.forward(valInput);

            // loss
            totalLoss += -Matrix.sum(Matrix.elementwiseProduct(valLabel, Matrix.log(output)));

            // argmax of output — index of highest probability
            int predicted = 0;
            for (int j = 1; j < 7; j++) {
                if (output[j][0] > output[predicted][0]) {
                    predicted = j;
                }
            }

            // argmax of label — index of the 1.0
            int actual = 0;
            for (int j = 1; j < 7; j++) {
                if (valLabel[j][0] > valLabel[actual][0]) {
                    actual = j;
                }
            }

            if (predicted == actual) {
                correct++;
            }
        }

        double avgLoss = totalLoss / dataLoader.valSize();
        double accuracy = (double) correct / dataLoader.valSize();
        return new double[]{avgLoss, accuracy};
    }

    public static void main(String[] args) throws Exception {
        Trainer model = new Trainer(new DataLoader("/src/main/resources/training_data/connectFourApr24.txt"), new int[]{42, 256, 128, 64, 7}, 15, 0.005);

        double loss = model.train();
        System.out.println("Final loss: " + loss);
    }
}
