package games.connect_four;

public class ConnectFour {

    private int[][] board = new int[6][7];
    private int[] heights;

    public ConnectFour() {
        heights = new int[7];
    }

    public int getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isValidMove(int col) {
        return heights[col] < 6;
    }

    public void makeMove(int col, int player) {
        if (isValidMove(col)) {
            board[heights[col]][col] = player;
            heights[col]++;
        }
    }

    public void undoMove(int col) {
        if (heights[col] > 0) {
            heights[col]--;
            board[heights[col]][col] = 0;
        }
    }

    public boolean checkWin(int col, int player) {
        int row = heights[col] - 1;
        int[][] directions = {{0,1},{1,0},{1,1},{1,-1}};

        for (int[] d : directions) {
            int count = 1;
            for (int sign : new int[]{1, -1}) {
                int r = row + sign * d[0];
                int c = col + sign * d[1];
                while (r >= 0 && r < 6 && c >= 0 && c < 7 && board[r][c] == player) {
                    count++;
                    r += sign * d[0];
                    c += sign * d[1];
                }
            }
            if (count >= 4) return true;
        }
        return false;
    }

    public boolean isFull() {
        for (int h : heights) {
            if (h < 6) {
                return false;
            }
        }  
        return true;
    }

    public int[][] getBoard() {
        return board;
    }
}
