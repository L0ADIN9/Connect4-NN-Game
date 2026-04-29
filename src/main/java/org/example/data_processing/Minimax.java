package org.example.data_processing;
import org.example.ConnectFour;

public class Minimax {

    private int scoreWindow(int[] window, int player) {
        int opponent = 3 - player;
        int playerCount = 0;
        int opponentCount = 0;
        int emptyCount = 0;

        for (int cell : window) {
            if (cell == player) playerCount++;
            else if (cell == opponent) opponentCount++;
            else emptyCount++;
        }

        // mixed window — blocked, worthless
        if (playerCount > 0 && opponentCount > 0) return 0;

        if (playerCount == 4) return 1000;
        if (playerCount == 3 && emptyCount == 1) return 50;
        if (playerCount == 2 && emptyCount == 2) return 10;
        if (opponentCount == 4) return -1000;
        if (opponentCount == 3 && emptyCount == 1) return -50;
        if (opponentCount == 2 && emptyCount == 2) return -10;

        return 0;
    }

    private int evaluate(ConnectFour board, int player) {
        int score = 0;

        // center column bonus
        for (int r = 0; r < 6; r++) {
            if (board.getCell(r, 3) == player) score += 3;
            else if (board.getCell(r, 3) == (3 - player)) score -= 3;
        }

        // horizontal windows
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                int[] window = {
                    board.getCell(r, c),
                    board.getCell(r, c+1),
                    board.getCell(r, c+2),
                    board.getCell(r, c+3)
                };
                score += scoreWindow(window, player);
            }
        }

        // vertical windows
        for (int c = 0; c < 7; c++) {
            for (int r = 0; r < 3; r++) {
                int[] window = {
                    board.getCell(r, c),
                    board.getCell(r+1, c),
                    board.getCell(r+2, c),
                    board.getCell(r+3, c)
                };
                score += scoreWindow(window, player);
            }
        }

        // diagonal (bottom-left to top-right)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                int[] window = {
                    board.getCell(r, c),
                    board.getCell(r+1, c+1),
                    board.getCell(r+2, c+2),
                    board.getCell(r+3, c+3)
                };
                score += scoreWindow(window, player);
            }
        }

        // diagonal (top-left to bottom-right)
        for (int r = 3; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                int[] window = {
                    board.getCell(r, c),
                    board.getCell(r-1, c+1),
                    board.getCell(r-2, c+2),
                    board.getCell(r-3, c+3)
                };
                score += scoreWindow(window, player);
            }
        }

        return score;
    }

    private int minimax(ConnectFour board, int depth, int lastCol, int currentPlayer, int maximizingPlayer, int alpha, int beta) {
        int opponent = 3 - currentPlayer;

        if (board.checkWin(lastCol, opponent)) {
            return opponent == maximizingPlayer ? 1000 : -1000;
        }

        if (board.isFull() || depth == 0) {
            return evaluate(board, maximizingPlayer); // ← the fix
        }

        int[] colOrder = {3, 2, 4, 1, 5, 0, 6};
        if (currentPlayer == maximizingPlayer) {
            int best = Integer.MIN_VALUE;
            for (int i : colOrder) {
                if (board.isValidMove(i)) {
                    board.makeMove(i, currentPlayer);
                    int score = minimax(board, depth - 1, i, 3 - currentPlayer, maximizingPlayer, alpha, beta);
                    board.undoMove(i);
                    best = Math.max(best, score);
                    alpha = Math.max(alpha, best);
                    if (alpha >= beta) break;
                }
            }
            return best;
        }

        int best = Integer.MAX_VALUE;
        for (int i : colOrder) {
            if (board.isValidMove(i)) {
                board.makeMove(i, currentPlayer);
                int score = minimax(board, depth - 1, i, 3 - currentPlayer, maximizingPlayer, alpha, beta);
                board.undoMove(i);
                best = Math.min(best, score);
                beta = Math.min(beta, best);
                if (alpha >= beta) break;
            }
        }
        return best;
    }

    public int bestMove(ConnectFour board, int player, int depth) {
        int bestCol = -1;
        int bestScore = Integer.MIN_VALUE;
        int[] colOrder = {3, 2, 4, 1, 5, 0, 6};

        for (int i : colOrder) {
            if (board.isValidMove(i)) {
                board.makeMove(i, player);
                int score = minimax(board, depth, i, 3 - player, player, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board.undoMove(i);
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = i;
                }
            }
        }
        return bestCol;
    }
}