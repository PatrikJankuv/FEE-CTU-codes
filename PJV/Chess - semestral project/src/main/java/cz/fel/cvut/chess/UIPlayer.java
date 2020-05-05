/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import java.util.Random;

/**
 *
 * @author patrik
 */
public class UIPlayer {
    private Board board;
    private BoardControl control;
    private char color;
    private PGN pgn;
    private GameManager gameManager;
    private final Random rand = new Random();
    
    /**
     *
     */
    public UIPlayer() {
    }

    /**
     *
     * @param color of PC player
     * @param control
     * @param board
     * @param pgn
     */
    public UIPlayer(char color, BoardControl control, Board board, PGN pgn, GameManager gameManager) {
        this.color = color;
        this.board = board;
        this.control = control;
        this.pgn = pgn;
        this.gameManager = gameManager;
    }

    public char getColor() {
        return color;
    }
    
    
    public void move() {
        int j = rand.nextInt(7);
        int i = rand.nextInt(7);


        
           boolean condition = true;
           //looking for figure with at the least one valid move 
           while (condition) {
                    
                    i = rand.nextInt(7);
                    j = rand.nextInt(7);
             
                    if(!board.getField(i, j).equals("  ")){
                        if(color == board.getColor(i, j)){
                            control.characterMoves(i, j, board);
                            control.movementCheckControl(i, j);
                            
                            if(control.getAllMovements().size() > 0){
                                condition = false;
                            }
                        }
                    }
                }
           
         //get index of move from arraylist size  
         int indexOfTurn = rand.nextInt(control.getAllMovements().size());
         
         gameManager.manageGame(i, j);
         
        char x = control.getAllMovements().get(indexOfTurn).charAt(0);
        char y = control.getAllMovements().get(indexOfTurn).charAt(2);

        int posX = x - 48;
        int posY = y - 48;
        
        gameManager.manageGame(posX, posY);
         
//         if (control.getAllMovements().contains(control.getAllMovements().get(indexOfTurn))) {
//             char x = control.getAllMovements().get(indexOfTurn).charAt(0);
//             char y = control.getAllMovements().get(indexOfTurn).charAt(2);
//                
//             int posX = x-48;
//             int posY = y-48;
//             
//             if(control.movement(board, i, j, board.getField(i, j), posX, posY)){
//                control.setOnTurn(control.opColor(control.getOnTurn()));
//                //pgn.moveWriteToPGN(board.getField(i, j), board.getField(posX, posY), i, j, posY, posX);
//                }
//             else{   
//                move();
//                }
//            
//         }
       
      
        }
         
    }

