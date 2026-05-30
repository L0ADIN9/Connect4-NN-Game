package org.example.model.math;

public class Init {

    public static double[][] zeros(int rows, int cols) {

        // returns zero matrix
        return new double[rows][cols];
    }

    public static double[][] random(int rows, int cols) {

        double[][] result = new double[rows][cols];
        double s = Math.sqrt(2.0 / (rows + cols));

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++) {
                result[i][j] = (Math.random() * 2 - 1) * s;
            }    
        }

        return result;
    }
}
