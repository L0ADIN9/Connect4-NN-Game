package org.example;
import javax.swing.SwingUtilities;
import org.example.model.Model;



public class Main {


    private static Model model;
    public static GameWindow gWindow;


    public static void main(String[] args) throws Exception {
        model = new Model(new int[]{42, 256, 128, 64, 7});
        model.load("src/main/resources/saved_models/model.txt");



        SwingUtilities.invokeLater(() -> {
            gWindow = new GameWindow();
        });
        GameHandler game = new GameHandler();
        game.startGame(false);


    }

    private static String encodeBoard(int currentPlayer) {
        for (int[] row : GameHandler.getBoard()) {
            for (int element : row) {
                System.out.print(element + "\t"); // Using \t for better alignment
            }
            System.out.println();
        }
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
    /**
     * Find the row where a piece would land if dropped in the given column.
     * Returns -1 if the column is full.
     */
    private static int findLandingRow(int[][] board, int col) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == 0) return row;
        }
        return -1;
    }

    /**
     * Check if a player can win by playing in the given column.
     * Temporarily places the piece, checks, then undoes.
     */
    private static boolean canWinAt(int[][] board, int col, int player) {
        int row = findLandingRow(board, col);
        if (row < 0) return false;
        board[row][col] = player;
        boolean wins = GameHandler.checkWin(col, row);
        board[row][col] = 0;
        return wins;
    }

    public static int getBotInputPos(){


        int[][] b = GameHandler.getBoard();

        // Take AI Winning Move

        for (int col = 0; col < 7; col++) {
            if (GameHandler.isValidMove(col) && canWinAt(b, col, -1)) {
                System.out.println("Heuristic: WINNING move at column " + col);
                return col;
            }
        }

        // Block Human Winning Move

        int blockCol = -1;
        int threatCount = 0;
        for (int col = 0; col < 7; col++) {
            if (GameHandler.isValidMove(col) && canWinAt(b, col, 1)) {
                blockCol = col;
                threatCount++;
            }
        }
        if (threatCount >= 1) {
            System.out.println("Heuristic: BLOCKING opponent win at column " + blockCol
                    + " (threats: " + threatCount + ")");
            return blockCol;
        }


        // Block Gives Human Winning Move

        boolean[] avoid = new boolean[7];

        for (int col = 0; col < 7; col++) {
            if (!GameHandler.isValidMove(col)) continue;
            int row = findLandingRow(b, col);

            if (row - 1 >= 0) {
                b[row][col] = -1;
                b[row - 1][col] = 1;
                if (GameHandler.checkWin(col, row - 1)) {
                    avoid[col] = true;
                }
                b[row - 1][col] = 0;
                b[row][col] = 0;
            }
        }



        // NN predictions

        double[][] input = new double[42][1];
        String[] parts = encodeBoard(-1).split(",");
        for (int i = 0; i < 42; i++) {
            input[i][0] = Double.parseDouble(parts[i]);
        }

        double[][] output = model.forward(input);

        // Pick the best column
        int best = -1;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 7; i++) {
            System.out.println(output[i][0]);

            if (GameHandler.isValidMove(i) && !avoid[i] && output[i][0] > bestScore) {
                bestScore = output[i][0];
                best = i;
            }
        }

        // If all valid columns are avoided
        if (best == -1) {
            System.out.println("Warning: all columns flagged, falling back to best valid");
            for (int i = 0; i < 7; i++) {
                if (GameHandler.isValidMove(i) && output[i][0] > bestScore) {
                    bestScore = output[i][0];
                    best = i;
                }
            }
        }
        

        System.out.println("Bot Move:" + best);
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