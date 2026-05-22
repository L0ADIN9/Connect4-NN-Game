package org.example.data_processing;

import java.io.FileWriter;
import java.io.IOException;
import org.example.ConnectFour;

public class DataGenerator {

    private SolverC4 solver = new SolverC4();

    private int selectRandomValidMove(ConnectFour board) {
        int col;
        do { col = (int)(Math.random() * 7); } while (!board.isValidMove(col));
        return col;
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

    private void generateGame(FileWriter fw, int randomPrefixLength) throws IOException {
        ConnectFour board = new ConnectFour();
        int currentPlayer = 1;
        int lastCol = -1;
        String moveSequence = "";
        
        for (int ply = 0; ply < randomPrefixLength; ply++) {
            if (board.isFull() || (lastCol != -1 && board.checkWin(lastCol, 3 - currentPlayer))) {
                return;
            }
            int played = selectRandomValidMove(board);
            board.makeMove(played, currentPlayer);
            moveSequence += (played + 1);
            lastCol = played;
            currentPlayer = 3 - currentPlayer;
        }

        while (true) {
            if (lastCol != -1 && board.checkWin(lastCol, 3 - currentPlayer)) break;
            if (board.isFull()) break;

            // Generate label for the current position
            int bestMove = -1;
            int bestOpponentScore = Integer.MAX_VALUE;

            for (int i = 0; i < 7; i++) {
                if (board.isValidMove(i)) {
                    board.makeMove(i, currentPlayer);
                    if (board.checkWin(i, currentPlayer)) {
                        bestMove = i;
                        board.undoMove(i);
                        break;
                    }
                    board.undoMove(i);

                    int opponentScore = solver.getScore(moveSequence + (i + 1));
                    if (opponentScore < bestOpponentScore) {
                        bestOpponentScore = opponentScore;
                        bestMove = i;
                    }
                }
            }

            if (bestMove == -1) {
                bestMove = selectRandomValidMove(board);
            }

            String encoded = encodeBoard(board.getBoard(), currentPlayer);
            fw.write(encoded + "," + bestMove + "\n");

            // Play the optimal move 80% of the time, random 20% to diversify positions
            int played;
            if (Math.random() < 0.2) {
                played = selectRandomValidMove(board);
            } else {
                played = bestMove;
            }

            board.makeMove(played, currentPlayer);
            moveSequence += (played + 1);
            lastCol = played;
            currentPlayer = 3 - currentPlayer;
        }
    }

    public void generate(int numGames, String filename, boolean append) throws IOException {
        FileWriter fw = new FileWriter(filename, append);
        for (int i = 1; i <= numGames; i++) {
            // vary the random prefix: early games (0-3) and mid games (4-8)
            int prefix = (i % 2 == 0) ? (int)(Math.random() * 4) : 4 + (int)(Math.random() * 5);
            generateGame(fw, prefix);
            if (i % 100 == 0) System.out.println("Generated " + i + " games...");
        }
        fw.close();
        solver.close();
        System.out.println("Done. Saved to " + filename);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting optimal data generation (10000 games, early+mid game focus)...");
        new DataGenerator().generate(10000, "optimalConnectFourData.txt", true);
    }
}