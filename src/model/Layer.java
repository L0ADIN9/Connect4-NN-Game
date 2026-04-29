package model;
import model.math.Init;
import model.math.Matrix;

public class Layer {

    // parameters
    private double[][] weights;
    private double[][] biases;

    // backward pass caches
    private double[][] lastInput;
    private double[][] lastZ;

    // for softmax
    private boolean isOutputLayer;

    public Layer(int inputSize, int outputSize, boolean isOutputLayer) {
        weights =  Init.random(outputSize, inputSize);
        biases = new double[outputSize][1];

        this.isOutputLayer = isOutputLayer;
    }

    public int paramCount(){
        return weights.length * weights[0].length + biases.length;
    }

    public double[][] forward(double[][] input) {
        lastInput = input;

        // z = Wx + b
        double[][] z = Matrix.add(Matrix.matmul(weights, input), biases);

        lastZ = z;
        
        // nonlinearity. makes anything negative zero.
        if (isOutputLayer){
            return Matrix.softmax(z);
        }
        return Matrix.relu(z);
    }

    public double[][] backward(double[][] dOutput, double learningRate) {

        // determine where gradient flows
        double[][] dZ = dOutput;
        if (!isOutputLayer){
            dZ = Matrix.elementwiseProduct(dOutput, Matrix.reluDerivative(lastZ));
        }

        // chain rule
        double[][] dW = Matrix.matmul(dZ, Matrix.transpose(lastInput));

        double[][] dInput = Matrix.matmul(Matrix.transpose(weights), dZ);

        // update weights & biases
        weights = Matrix.subtract(weights, Matrix.scale(dW, learningRate));
        biases = Matrix.subtract(biases, Matrix.scale(dZ, learningRate));

        return dInput;
    }

    public double[][] getWeights() { return weights; }
    public double[][] getBiases() { return biases; }
    public void setWeights(double[][] w) { weights = w; }
    public void setBiases(double[][] b) { biases = b; }
}