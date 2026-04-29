package org.example.data_processing;

import org.example.model.Model;
import java.util.Scanner;
import org.example.ConnectFour;



public class Game {

    private ConnectFour board;
    private Model model;
    private Scanner sc = new Scanner(System.in);

    public Game(Model model) {
        this.board = new ConnectFour();
        this.model = model;
    }

    private void printBoard() {
        int[][] b = board.getBoard();
        System.out.println("\n 0 1 2 3 4 5 6");
        System.out.println("---------------");
        for (int r = 5; r >= 0; r--) {
            System.out.print("|");
            for (int c = 0; c < 7; c++) {
                if (b[r][c] == 1) System.out.print("X ");
                else if (b[r][c] == 2) System.out.print("O ");
                else System.out.print(". ");
            }
            System.out.println("|");
        }
        System.out.println("---------------");
    }

    private String encodeBoard(int currentPlayer) {
        int[][] b = board.getBoard();
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

    private int modelMove() {
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
            if (board.isValidMove(i) && output[i][0] > bestScore) {
                bestScore = output[i][0];
                best = i;
            }
        }
        return best;
    }

    private int humanMove() {
        while (true) {
            System.out.print("Your move (0-6): ");
            int col = sc.nextInt();
            if (col >= 0 && col < 7 && board.isValidMove(col)) return col;
            System.out.println("Invalid move, try again.");
        }
    }

    public void play() {
        System.out.println("You are X (player 1). AI is O (player 2).");
        int currentPlayer = 1;
        int lastCol = -1;

        while (true) {
            printBoard();

            if (lastCol != -1 && board.checkWin(lastCol, 3 - currentPlayer)) {
                if (3 - currentPlayer == 1) System.out.println("You win!");
                else System.out.println("AI wins!");
                break;
            }

            if (board.isFull()) {
                System.out.println("Draw!");
                break;
            }

            int col;
            if (currentPlayer == 1) {
                col = humanMove();
            } else {
                col = modelMove();
                System.out.println("AI plays column " + col);
            }

            board.makeMove(col, currentPlayer);
            lastCol = col;
            currentPlayer = 3 - currentPlayer;
        }

        printBoard();
    }

    public static void main(String[] args) throws Exception {
        Model model = new Model(new int[]{42, 256, 128, 64, 7});
        model.load("src/model/saved_models/model.txt");
        new Game(model).play();
    }
}