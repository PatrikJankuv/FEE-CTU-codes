/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import java.util.ArrayList;


/**
 *
 * @author patrik
 */
public class Board {
    private String[][] board;
     

    /**
     *
     * @param board is two dimensional representation of the board in a array 
     */
    public Board(String[][] board) {
        this.board = board;
    }

    /**
     *  constructor for Board class
     */
    public Board() {
        board = new String[8][8];
    }
    
    /**
     *
     * @param board is array which represents board
     */
    public void setBoard(String[][] board) {
        this.board = board;
    }
    
    /**
     *  use for printing board into the console
     */
    public  void printBoard(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                System.out.print("["+board[i][j]+"]");
            }
            System.out.println("");
         } 
    }
    

    /**
     *  method to generate new board, with figures at the original positions
     */
    public void generateNewGame(){
        board[0][0] = "br";
        board[0][7] = "br";
        board[0][1] = "bn";
        board[0][6] = "bn";
        board[0][2] = "bb";
        board[0][5] = "bb";
        board[0][4] = "bk";
        board[0][3] = "bq";
        
        
        board[7][0] = "wr";
        board[7][7] = "wr";


        board[7][1] = "wn";
        board[7][6] = "wn";
        board[7][2] = "wb";
        board[7][5] = "wb";
        board[7][3] = "wq";
        board[7][4] = "wk";
        
       
        int k = 0;
        
        //generate black pawns
        for (int i = 0; i < 8; i++){
            k = i + 1;
            board[1][i] = "bp";
        }
        
        //generate white pawns
        for (int i = 0; i < 8; i++){
            k = i + 1;
            board[6][i] = "wp";
        }

        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 8; j++){
                board[i+2][j]="  ";
            }          
        }
        
    }

    /**
     *
     * @param x is row in the board
     * @param y is column in the board
     * @return according x and y return value from the square
     */
    public String getField(int x, int y){
        return board[x][y];
    }
    
    /**
     * set figure on square with
     * @param x position
     * @param y position
     * @param value will be replace original value of field
     */
    public void setField(int x, int y, String value){
        board[x][y] = value;
    }
    
    /**
     *check if square on is empty
     * @param x is row in the board
     * @param y is column in the board
     * @return true if is empty, false if not
     */
    public boolean isEmpty(int x, int y){
        return "  ".equals(board[x][y]);
    }
    
    /**
     *
     * @param x is row in the board
     * @param y is column in the board
     * @return return color of figure which is on the field
     */
    public char getColor(int x, int y){
        return board[x][y].charAt(0);
    }
    
    /**
     * according
     * @param color will 
     * @return position of king on board
     */
    public String getKing(char color){
        String king = color + "k";
        for (int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){
                if (king.equals(board[i][j])){
                  return i+" "+j;  
                }
            }
        }
        return null;
    }
    
    //return array of board
    public String[][] getBoard(){
        return board;
    }  
    
    /**
     * for 
     * @param figure represents figure in String
     * @return first position of figure, if not there return null
     */
    public String getPosition(String figure){
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){
                if(figure.equals(getField(i, j)))
                    return i +" "+j;
            }
        }
        return null;
    }
    
    /**
     * for 
     * @param figure represents figure in String
     * @return ArrayList all positions of figure on the board
     */
    public ArrayList<String> getPositionsOfFigure(String figure){
        ArrayList <String> positionsOfFigure= new ArrayList<>();
        
        for(int i = 0; i <= 7; i++){
            for(int j = 0; j <= 7; j++){
                if(figure.equals(getField(i, j)))
                    positionsOfFigure.add(i + " " + j);
            }
        }
        return positionsOfFigure;
    }
    
    
  
}
