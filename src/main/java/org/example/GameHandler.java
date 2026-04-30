package org.example;

public class GameHandler {
    private static boolean playerTurn = true;
    private static int [][] board = new int[6][7];
    private static int turnCnt = 0;
    /*
    0 = empty slot
    1 = human chip
    2 = bot chip
     */
    public void startGame(boolean humanFirst) throws  Exception{
        clearBoard();
        playerTurn = humanFirst;
        turnCnt = 0;
        while (turnCnt < 42){
            int mv=0;
            if (playerTurn) {
                while (true){
                    mv= Main.getHumanInputPos();
                    if(isValidMove(mv)){
                        move(mv, 1);
                        break;
                    }
                }
            }
            if (!playerTurn) {
                Thread.sleep(500);
                mv = Main.getBotInputPos();
                move(mv, 2);
            }

            playerTurn  = !playerTurn;
            turnCnt++;
            GameWindow.boardP.repaint();

        }
        if(turnCnt>=42) draw();


    }
    public static void clearBoard(){
        board = new int[6][7];
    }
    public static int whoTurn(){
        if(playerTurn) return 1;
        else return 2;
    }
    public static int[][] getBoard(){
        return board;
    }
    public static void move(int pos, int who){
        // pos is the x position of the dropped chip this move
        // who is which player's turn it is repersented numerically
        // 1 = human
        // 2 = bot
        int top = 5;
        while(board[top][pos] != 0){
            top--;
            if(top <0){
                System.out.println("ILLEGAL MOVE");
                return;
            }
        }
        board[top][pos] = who;
    }
    public static boolean isValidMove(int pos){
        if (0>pos || pos>7) return false;
        int top = 5;
        while(board[top][pos] != 0){
            top--;
            if(top <0){
                System.out.println("ILLEGAL MOVE");
                return false;
            }
        }
        return true;

    }
    public static boolean checkWin(int x, int y){
        int who = board[y][x];
        for(int xv= -1; xv<2; xv++){
            for(int yv = -1; yv<2; yv++){
                for(int i = 0; i< 4; i++){
                    //checks to see if the vectors are both zerod
                    if(xv==0 &&yv==0)break;
                    //checks if the x val is out of bounds
                    if(x+xv*i<0 ||x+xv*i>= 7)break;
                    //checks if the y val is out of bounds
                    if(y+yv*i<0 ||y+yv*i>= 6)break;
                    //checks if the val doesn't match the pattern
                    if(board[y+yv*i][x+xv*i]!= who)break;
                    else if (i==3) return true;
                    // if these match for the 4th time in a row, then winner!
                }
            }
        }
        return false;

    }
    public static void win(int who){
        System.out.println(who+ "won");
    }
    public static void draw(){System.out.println("draw");}
}
