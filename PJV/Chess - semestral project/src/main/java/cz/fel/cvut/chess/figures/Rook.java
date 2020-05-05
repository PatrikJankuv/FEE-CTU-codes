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
public class Rook implements figure{
    private final int x;
    private final int y;
    private final Board board;
    
    /**
     * constructor
     * @param x position
     * @param y position
     * @param board Board
     */
    public Rook(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }


    /**
     *
     * @param x position x on board
     * @param y position y on board
     * @param color of figure
     * @return array list of all possible moves 
     */
    @Override
    public ArrayList moves(int x, int y, char color) {
         ArrayList<String> moves = new ArrayList<>();
         int[] direct = {1,1,1,1};
         
         for(int i = 1; i <=7; i++){
             direct[0] = isValid(x+i, y, direct[0], color);
             direct[1] = isValid(x, y-i, direct[1], color);
             direct[2] = isValid(x-i, y, direct[2], color);
             direct[3] = isValid(x, y+i, direct[3], color);
             
             if((direct[0] == 1)||(direct[0] == 2))
                 moves.add((x+i)+ " " + (y));
             if((direct[1] == 1)||(direct[1] == 2))
                 moves.add((x)+ " " + (y-i));
             if((direct[2] == 1)||(direct[2] == 2))
                 moves.add((x-i)+ " " + (y));
             if((direct[3] == 1)||(direct[3] == 2))
                 moves.add((x)+ " " + (y+i));
         }
             
                         
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
