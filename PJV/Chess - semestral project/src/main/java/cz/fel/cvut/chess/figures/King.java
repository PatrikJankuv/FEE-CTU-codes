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
public class King implements figure{
    private int x;
    private int y;
    private Board board;
    private ArrayList<String> moves = new ArrayList<>();
    
    public King(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public King(int x, int y) {
        this.x = x;
        this.y = y;
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
         
         int[] direct = {1,1,1,1,1,1,1,1};
         
         for(int i = 1; i <=1; i++){
             direct[0] = isValid(x+i, y+i, direct[0], color);
             direct[1] = isValid(x+i, y-i, direct[1], color);
             direct[2] = isValid(x-i, y+i, direct[2], color);
             direct[3] = isValid(x-i, y-i, direct[3], color);
             direct[4] = isValid(x+i, y, direct[4], color);
             direct[5] = isValid(x, y-i, direct[5], color);
             direct[6] = isValid(x-i, y, direct[6], color);
             direct[7] = isValid(x, y+i, direct[7], color);
             
             if((direct[0] == 1)||(direct[0] == 2))
                 if(!board.isEmpty(x+i, y+i))
                    moves.add((x+i)+ "x" + (y+i));
                 else
                    moves.add((x+i)+ " " + (y+i));
             if((direct[1] == 1)||(direct[1] == 2))
                 moves.add((x+i)+ " " + (y-i));
             if((direct[2] == 1)||(direct[2] == 2))
                 moves.add((x-i)+ " " + (y+i));
             if((direct[3] == 1)||(direct[3] == 2))
                 moves.add((x-i)+ " " + (y-i));
             if((direct[4] == 1)||(direct[4] == 2))
                 moves.add((x+i)+ " " + (y));
             if((direct[5] == 1)||(direct[5] == 2))
                 moves.add((x)+ " " + (y-i));
             if((direct[6] == 1)||(direct[6] == 2))
                 moves.add((x-i)+ " " + (y));
             if((direct[7] == 1)||(direct[7] == 2))
                 moves.add((x)+ " " + (y+i));
         }
         
         //check if is possible casting
         casting(color);
         
         
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
    
    /**
     *method which from
     * @param color check if is possible make casting
     * if is possible ad move into moves, 
     */    
    public void casting(char color){
        //for white king
        if (color == 'w'){
            if(board.isEmpty(7, 2) && board.isEmpty(7, 1) && board.isEmpty(7, 3)){
                if("wr".equals(board.getField(7, 0)))
                    moves.add("7c2");}
            if(board.isEmpty(7, 6) && board.isEmpty(7, 5)){
                if("wr".equals(board.getField(7, 7)))
                    moves.add("7c6");}
        }
        
        //for black king
        if (color == 'b'){
            if(board.isEmpty(0, 2) && board.isEmpty(0, 1) && board.isEmpty(0, 3)){
                if("br".equals(board.getField(0, 0)))
                    moves.add("0c2");}
            if(board.isEmpty(0, 6) && board.isEmpty(0, 5)){
                if("br".equals(board.getField(0, 7)))
                    moves.add("0c6");}
        }
        
       
    }
    
}
