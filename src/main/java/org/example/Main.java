package org.example;
import javax.swing.SwingUtilities;
import org.example.data_processing.SolverC4;
import org.example.model.Model;



public class Main {

    private static Model model;
    private static SolverC4 solver;
    private static String currentDiff = "MEDIUM";
    private static final String ZACK_MODE = "ZACK";
    public static boolean isZachMode = false;
    public static GameWindow gWindow;

    public static GameHandler game;

    public static void main(String[] args)  throws Exception{

        SwingUtilities.invokeLater(() -> {
            gWindow = new GameWindow();
        });

    }

    public static void RESET(){
        closeSolver();
        gWindow.homeUI();

    }

    public static void START(String diff, boolean hfst)throws Exception{
        new Thread(() -> {
            try {
                currentDiff = diff;
                closeSolver();
                model = new Model(new int[]{42, 256, 128, 64, 7});
                System.out.println(diff);
                if(diff.equals("HARD") || diff.equals(ZACK_MODE)){
                    model.load("src/main/resources/saved_models/model_mlp_v1.txt");

                } else if (diff.equals("EASY")) {
                    model.load("src/main/resources/saved_models/model_mlp_v2.txt");
                }else {
                    model.load("src/main/resources/saved_models/model_mlp_v3.txt");
                }
                if(diff.equals(ZACK_MODE)){
                    solver = new SolverC4();
                    isZachMode = true;


                }
                game = new GameHandler();
                game.startGame(hfst);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(400);
        gWindow.startUI();
    }

    public static void END(int w){
        closeSolver();
        gWindow.endUI(w);

    }

    private static void closeSolver(){
        if(solver != null){
            solver.close();
            solver = null;
        }
    }




    /**
     * Converts the board into the comma-separated vector expected by the model.
     * The values are read relative to the player being scored.
     */
    private static String encodeBoard(int currentPlayer) {
        int[][] b = GameHandler.getBoard();
        int opponent = -currentPlayer;
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
     * Finds the row where a piece would land if dropped in the given column.
     * Returns -1 if the column is full.
     */
    private static int findLandingRow(int[][] board, int col) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == 0) return row;
        }
        return -1;
    }

    /**
     * Checks whether the given player can win by playing this column.
     */
    private static boolean canWinAt(int[][] board, int col, int player) {
        int row = findLandingRow(board, col);
        if (row < 0) return false;
        board[row][col] = player;
        boolean wins = GameHandler.checkWin(col, row);
        board[row][col] = 0;
        return wins;
    }

    /**
     * Chooses the bot's column. Normal modes use tactical checks before the model.
     * Zack mode uses the external solver.
     */
    public static int getBotInputPos(){


        int[][] b = GameHandler.getBoard();
        if( isZachMode && solver != null){
            return getPerfectMove(b);
        }

        // Win immediately if possible.
        for (int col = 0; col < 7; col++) {
            if (GameHandler.isValidMove(col) && canWinAt(b, col, -1)) {
                System.out.println("Heuristic: WINNING move at column " + col);
                return col;
            }
        }

        // Block an immediate human win.
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


        // Avoid moves that give the human an immediate reply.
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



        // Let the model rank the remaining columns.
        double[][] input = new double[42][1];
        String[] parts = encodeBoard(-1).split(",");
        for (int i = 0; i < 42; i++) {
            input[i][0] = Double.parseDouble(parts[i]);
        }

        double[][] output = model.forward(input);

        // Pick the highest-ranked legal column.
        int best = -1;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 7; i++) {
            System.out.println(output[i][0]);

            if (GameHandler.isValidMove(i) && !avoid[i] && output[i][0] > bestScore) {
                bestScore = output[i][0];
                best = i;
            }
        }

        // If every option looked risky, still make a legal move.
        if (best == -1) {
            System.out.println("No safe column found; using best valid move.");
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

    private static int getPerfectMove(int[][] b){
        for (int col = 0; col < 7; col++) {
            if (GameHandler.isValidMove(col) && canWinAt(b, col, -1)) {
                System.out.println("Zack move: " + col);
                return col;
            }
        }

        int best = -1;
        int bestOpponentScore = Integer.MAX_VALUE;
        String sequence = GameHandler.getMoveSequence();

        for (int col = 0; col < 7; col++) {
            if (GameHandler.isValidMove(col)) {
                int opponentScore = solver.getScore(sequence + (col + 1));
                if (opponentScore < bestOpponentScore) {
                    bestOpponentScore = opponentScore;
                    best = col;
                }
            }
        }

        if (best == -1) {
            for (int col = 0; col < 7; col++) {
                if (GameHandler.isValidMove(col)) {
                    best = col;
                    break;
                }
            }
        }

        System.out.println("Zack move: " + best);
        return best;
    }

    /**
     * Waits until the player clicks a column and returns that column.
     */
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
