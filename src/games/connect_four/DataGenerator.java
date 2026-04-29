package games.connect_four;

import java.io.FileWriter;
import java.io.IOException;

public class DataGenerator {

    private Minimax minimax = new Minimax();
    private static final double EPSILON = 1.0;

    private int selectMove(ConnectFour board, int player) {
        if (Math.random() < EPSILON) {
            int col;
            do { col = (int)(Math.random() * 7); } while (!board.isValidMove(col));
            return col;
        }
        return minimax.bestMove(board, player, 7);
    }

    private String encodeBoard(int[][] board, int currentPlayer) {
        StringBuilder sb = new StringBuilder();
        int opponent = 3 - currentPlayer;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j] == currentPlayer) sb.append("1");
                else if (board[i][j] == opponent) sb.append("-1");
                else sb.append("0");
                if (i != 5 || j != 6) sb.append(",");
            }
        }
        return sb.toString();
    }

    private void generateGame(FileWriter fw) throws IOException {
        ConnectFour board = new ConnectFour();
        int currentPlayer = 1;
        int lastCol = -1;

        while (true) {
            if (lastCol != -1 && board.checkWin(lastCol, 3 - currentPlayer)) break;
            if (board.isFull()) break;

            // label is always minimax's best, regardless of what move we actually play
            int label = minimax.bestMove(board, currentPlayer, 7);
            String encoded = encodeBoard(board.getBoard(), currentPlayer);
            fw.write(encoded + "," + label + "\n");

            // but we might make a random move to diversify positions
            int played = selectMove(board, currentPlayer);
            board.makeMove(played, currentPlayer);
            lastCol = played;
            currentPlayer = 3 - currentPlayer;
        }
    }

    public void generate(int numGames, String filename) throws IOException {
        FileWriter fw = new FileWriter(filename);
        for (int i = 0; i < numGames; i++) {
            generateGame(fw);
            if (i % 100 == 0) System.out.println("Generated " + i + " games...");
        }
        fw.close();
        System.out.println("Done. Saved to " + filename);
    }

    public static void main(String[] args) throws IOException {
        new DataGenerator().generate(3000, "connectFour.txt");
    }
}