/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess.figures;

import cz.fel.cvut.chess.Board;
import java.util.ArrayList;

/**
 *
 * @author patrik
 */
public class Pawn implements figure{
    private int x;
    private int y;
    private final Board board;
    private final String passant;
    
    /**
     * constructor
     * @param x position
     * @param y position
     * @param board position
     * @param pawn pawn with color
     */
    public Pawn(int x, int y, Board board, String pawn) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.passant= pawn; 
    }

    
 
    /**
     *will generate all possible movements
     * @param x is x position on board
     * @param y is y position on board
     * @param color
     * @return will return array list of all moves
     */
    @Override
    public ArrayList<String> moves(int x, int y, char color) {
        int direction = 1;
        this.y = y;
        this.x = x;
        ArrayList<String> moves = new ArrayList<>();
        
        if(color == 'w' && x == 0){
            return moves;
    }
         if(color == 'b' && x == 7){
            return moves;
    }
        
        if(0 == Character.valueOf(color).compareTo('w'))
            direction = -1;
        
        //first move
        if(x == 6){
            this.x = x -2; 
                if(board.isEmpty(this.x, y) && board.isEmpty(++this.x, y)){
                    moves.add(--this.x+ " " + y);}
               }
        //first move
        if((x == 1)&&(0 == Character.valueOf(color).compareTo('b'))){
            this.x = x+2;
            if(board.isEmpty(this.x, y) && board.isEmpty(--this.x, y)){
                moves.add(++this.x+ " " + y);}
        }
       
        
        this.x= x + direction;
        if(board.isEmpty(this.x, y)){
            moves.add(this.x+ " " + y);
        }
        
        //diagonal move
        this.y = y +1;
        if(this.y < 8){
            if((!board.isEmpty(this.x, this.y))&&(board.getColor(this.x, this.y) != color)){
                moves.add(this.x+ " " + (y+1));
                }
        }
        
        //diagonal move
        this.y = y -1;
        if (this.y >= 0){
            if((!board.isEmpty(this.x, this.y))&&(board.getColor(this.x, this.y) != color)){
                moves.add(this.x+ " " + (y-1));
            }
        }
        
        
        //en passant//////////////////////////////////////////////////////////////
        if(passant.length() == 3){
            
            this.y = y;
            

            if(this.y < 8){
                if((y+1) == ((int)passant.charAt(2)-48) && (passant.charAt(0) != color)){
                    if((color == 'w' && x == 3)||(color == 'b' && x == 4)){
                        moves.add((x+direction)+ "p" + (y+1));
                    }
            }

            //diagonal move
            this.y = y;
            if (this.y > 0){
                if((y-1) == ((int)passant.charAt(2)-48) && (passant.charAt(0) != color)){
                    if((color == 'w' && x == 3)||(color == 'b' && x == 4)){
                    moves.add((x+direction)+ "p" + (y-1));}
                    }
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////////      
       
        
        return moves;
    }

    /**
     * from
     * @param x position
     * @param y will get position on board and
     * @param history remember if field in this direction was free
     * @param color is color of figure
     * @return if this field is available for figurine 
     */
    @Override
    public int isValid(int x, int y, int history, char color) {
        if(history == 2)
            return 0;
        if(history == 0)
            return 0;
        
        if ((7 < y)||(x > 7))
            return 0;
        if ((0 > y)||(x < 0))
            return 0;
        if (board.isEmpty(x, y))
            return 1;
        else{
            if(board.getColor(x, y) == color)
                return 0;
            else
                return 2;
        }
           
    }
         
}

    

