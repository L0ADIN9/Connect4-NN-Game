package model.math;

public class Matrix {

    public static boolean verifyDim(double[][] m1, double[][] m2, String type) {

        if (type.equals("elementwise")) {
            if (m1.length != m2.length || m1[0].length != m2[0].length) {
                throw new IllegalArgumentException("Matrices are not the same dimension.");
            }
        } else if (type.equals("inner")) {
            if (m1[0].length != m2.length) {
                throw new IllegalArgumentException("Inner dimensions don't match.");
            }
        }

        return true;
    }

    public static double[][] add(double[][] m1, double[][] m2) {

        // check dimensions
        verifyDim(m1, m2, "elementwise");

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[i][j] = m1[i][j] + m2[i][j];
            }
        }

        return result;
    }

    public static double[][] subtract(double[][] m1, double[][] m2) {

        // check dimensions
        verifyDim(m1, m2, "elementwise");

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[i][j] = m1[i][j] - m2[i][j];
            }
        }

        return result;
    }

    public static double[][] elementwiseProduct(double[][] m1, double[][] m2) {

        // check dimensions
        verifyDim(m1, m2, "elementwise");

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[i][j] = m1[i][j] * m2[i][j];
            }
        }

        return result;
    }

    public static double[][] log(double[][] m1) {

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[i][j] = Math.log(Math.max(m1[i][j], 1e-6));
            }
        }

        return result;
    }

    public static double[][] exp(double[][] m1) {

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[i][j] = Math.exp(m1[i][j]);
            }
        }

        return result;
    }

    public static double[][] scale(double[][] m1, double constant) {

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[i][j] = m1[i][j] * constant;
            }
        }

        return result;
    }

    public static double sum(double[][] m1) {

        double result = 0;

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result += m1[i][j];
            }
        }

        return result;
    }

    public static double[][] transpose(double[][] m1) {

        double[][] result = new double[m1[0].length][m1.length];

        // swap rows and columns
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                result[j][i] = m1[i][j];
            }
        }

        return result;
    }

    public static double[][] relu(double[][] m1) {

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                if (m1[i][j] > 0) {
                    result[i][j] = m1[i][j];
                } else {
                    result[i][j] = 0;
                }
            }
        }

        return result;
    }

    public static double[][] reluDerivative(double[][] m1) {

        double[][] result = new double[m1.length][m1[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                if (m1[i][j] > 0) {
                    result[i][j] = 1;
                } else {
                    result[i][j] = 0;
                }
            }
        }

        return result;
    }

    public static double[][] softmax(double[][] m1) {

        double[][] exponentiated = exp(m1);

        double total = sum(exponentiated);

        return scale(exponentiated, 1/total);
    }

    public static double[][] matmul(double[][] m1, double[][] m2) {

        // check dimensions
        verifyDim(m1, m2, "inner");

        double[][] result = new double[m1.length][m2[0].length];

        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m1[0].length; k++) {
                    result[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return result;
    }

}
