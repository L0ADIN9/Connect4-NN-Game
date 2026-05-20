package org.example;
import javax.swing.SwingUtilities;
import org.example.model.Model;



public class Main {


    private static Model model; 


    public static void main(String[] args) throws Exception {
        model = new Model(new int[]{42, 256, 128, 64, 7});
        model.load("src/main/resources/saved_models/model.txt");



        SwingUtilities.invokeLater(() -> {
            new GameWindow();
        });
        GameHandler game = new GameHandler();
        game.startGame(false);


    }

    private static String encodeBoard(int currentPlayer) {
        int[][] b = GameHandler.getBoard();
        int opponent = 3 - currentPlayer;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (b[i][j] == currentPlayer) sb.append("1");
                else if (b[i][j] == opponent) sb.append("-1");
                else sb.append("0");
                if (i != 5 || j != 6) sb.append(",");
            }
        }
        return sb.toString();
    }
    public static int getBotInputPos(){

        // encode board from model's perspective (player 2)
        double[][] input = new double[42][1];
        String[] parts = encodeBoard(2).split(",");
        for (int i = 0; i < 42; i++) {
            input[i][0] = Double.parseDouble(parts[i]);
        }

        double[][] output = model.forward(input);

        // pick highest probability valid column
        int best = -1;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 7; i++) {
            System.out.println(output[i][0]);
            if (GameHandler.isValidMove(i) && output[i][0] > bestScore) {
                bestScore = output[i][0];
                best = i;
            }
        }
        System.out.println("Move");


        return best;


    }


    public synchronized static int getHumanInputPos(){
        synchronized (GameWindow.class) {

            while (BoardPanel.mouseLocation == -1 ) {
                try {
                    GameWindow.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            int ret = BoardPanel.mouseLocation;
            BoardPanel.mouseLocation = -1;
            return ret;
        }

    }


}