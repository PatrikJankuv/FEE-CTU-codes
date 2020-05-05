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
public class Knight implements figure{
    private final int x;
    private final int y;
    private Board board;
    
    public Knight(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public Knight(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    

    /**
     *will generate all possible movements
     * @param x is x position on board
     * @param y is y position on board
     * @param color of knight on the turn
     * @return will return array list of all moves
     */
    @Override
    public ArrayList moves(int x, int y, char color) {
        ArrayList<String> moves = new ArrayList<>();
        
          if(isValid(x+2, y+1, 1, color) == 1)
              moves.add((x+2) +" "+ (y+1));
          if(isValid(x+2, y-1, 1, color) == 1)
              moves.add((x+2) +" "+ (y-1));
          
          if(isValid(x-2, y+1, 1, color) == 1)
              moves.add((x-2) +" "+ (y+1));
          if(isValid(x-2, y-1, 1, color) == 1)
              moves.add((x-2) +" "+ (y-1));
          
          if(isValid(x+1, y-2, 1, color) == 1)
              moves.add((x+1) +" "+ (y-2));
          if(isValid(x+1, y+2, 1, color) == 1)
              moves.add((x+1) +" "+ (y+2));
          
          if(isValid(x-1, y+2, 1, color) == 1)
              moves.add((x-1) +" "+ (y+2));
          if(isValid(x-1, y-2, 1, color) == 1)
              moves.add((x-1) +" "+ (y-2));
         
          
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
                return 1;
        }
           
    }
    
}
