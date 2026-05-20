package org.example;
import javax.swing.SwingUtilities;
import org.example.model.Model;



public class Main {


    private static Model model;
    public static GameWindow gWindow;


    public static void main(String[] args) throws Exception {
        model = new Model(new int[]{42, 256, 128, 64, 7});
        model.load("src/main/resources/saved_models/model_v3.txt");



        SwingUtilities.invokeLater(() -> {
            gWindow = new GameWindow();
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

/*
        int[][] b = GameHandler.getBoard();
        // Take AI Winning Move


        for (int col = 0; col < 7; col++) {
            if (GameHandler.isValidMove(col) && canWinAt(b, col, 2)) {
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

        // Block Future Human Fork

        for (int col = 0; col < 7; col++) {
            if (!GameHandler.isValidMove(col)) continue;
            int row = findLandingRow(b, col);
            if (row < 0) continue;

            // Simulate opponent playing here
            b[row][col] = 1;
            int futureThreats = 0;
            for (int c2 = 0; c2 < 7; c2++) {
                if (c2 == col && row - 1 < 0) continue;
                int r2 = findLandingRow(b, c2);
                if (r2 < 0) continue;
                b[r2][c2] = 1;
                if (GameHandler.checkWin(c2, r2)) futureThreats++;
                b[r2][c2] = 0;
            }
            b[row][col] = 0;

            if (futureThreats >= 2) {
                // Also make sure our block doesn't let opponent win directly above
                boolean safeBlock = true;
                if (row - 1 >= 0) {
                    b[row][col] = 2;
                    b[row - 1][col] = 1;
                    if (GameHandler.checkWin(col, row - 1)) safeBlock = false;
                    b[row - 1][col] = 0;
                    b[row][col] = 0;
                }
                if (safeBlock) {
                    System.out.println("Heuristic: BLOCKING opponent fork at column " + col
                            + " (would create " + futureThreats + " threats)");
                    return col;
                }
            }
        }

        // Block Gives Human Winning Move

        boolean[] avoid = new boolean[7];
        int validCount = 0;
        int avoidCount = 0;

        for (int col = 0; col < 7; col++) {
            if (!GameHandler.isValidMove(col)) continue;
            validCount++;
            int row = findLandingRow(b, col);
            if (row < 0) continue;

            // Check: does our move give opponent a win directly above?
            if (row - 1 >= 0) {
                b[row][col] = 2;
                b[row - 1][col] = 1;
                if (GameHandler.checkWin(col, row - 1)) {
                    avoid[col] = true;
                    avoidCount++;
                }
                b[row - 1][col] = 0;
                b[row][col] = 0;
            }

            // Check: after our move, can opponent win immediately anywhere?
            if (!avoid[col]) {
                b[row][col] = 2;
                for (int oCol = 0; oCol < 7; oCol++) {
                    int oRow = findLandingRow(b, oCol);
                    if (oRow < 0) continue;
                    b[oRow][oCol] = 1;
                    if (GameHandler.checkWin(oCol, oRow)) {
                        // Opponent wins after our move — but only avoid if we
                        // can't block that threat on our next turn (i.e., there
                        // are multiple threats or we won't get to respond).
                        // Simple check: would this create a NEW threat that
                        // didn't exist before our move?
                        b[row][col] = 0; // undo our move temporarily
                        boolean alreadyThreat = canWinAt(b, oCol, 1);
                        b[row][col] = 2; // re-apply
                        if (!alreadyThreat) {
                            avoid[col] = true;
                            avoidCount++;
                        }
                    }
                    b[oRow][oCol] = 0;
                    if (avoid[col]) break;
                }
                b[row][col] = 0;
            }
        }
        */


        // NN predictions

        double[][] input = new double[42][1];
        String[] parts = encodeBoard(2).split(",");
        for (int i = 0; i < 42; i++) {
            input[i][0] = Double.parseDouble(parts[i]);
        }

        double[][] output = model.forward(input);

        // Pick the best column
        int best = -1;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 7; i++) {
            System.out.println(output[i][0]);

            if (GameHandler.isValidMove(i) /*&& !avoid[i]*/ && output[i][0] > bestScore) {
                bestScore = output[i][0];
                best = i;
            }
        }
        /*
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
        */

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