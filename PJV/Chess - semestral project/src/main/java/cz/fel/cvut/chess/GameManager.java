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
public class GameManager {
    private UIPlayer player;
    private boolean UI;
    private Board board;
    private BoardControl control;
    private boolean selectedFigure;
    private String figure;
    private int orgX;
    private int orgY;
    private PGN pgn;

    /**
     *
     * @param board
     * @param control
     * @param pgn
     */
    public GameManager(Board board, BoardControl control, PGN pgn) {
        this.board = board;
        this.control = control;
        this.selectedFigure = false;
        this.figure = "";
        this.UI = false;
        this.pgn = pgn;

        control.startTime();
    }

    //constructor for test
    public GameManager() {
    }
    
    

    /**
     *
     * @param board board
     * @param control control
     * @param player PC player 
     */
    public GameManager(Board board, BoardControl control, UIPlayer player) {
        this.board = board;
        this.control = control;
        this.selectedFigure = false;
        this.figure = "";
        this.player = player;
        UI = true;
        control.startTime();
    }

    /**
     * for player vs pc
     * @param board
     * @param control
     * @param player
     */
    public void playerVsUI(Board board, BoardControl control, UIPlayer player) {
        this.board = board;
        this.control = control;
        this.selectedFigure = false;
        this.figure = "";
        this.player = player;
        UI = true;
        control.startTime();
    }

    /**
     * for two players on one pc
     * @param board
     * @param control
     */
    public void playerVsPlayer(Board board, BoardControl control) {
        this.board = board;
        this.control = control;
        this.selectedFigure = false;
        this.figure = "";
        this.UI = false;
        UI = false;
        control.startTime();
    }

    /**
     * use for managing game
     *
     * @param fieldX is X position on board
     * @param fieldY is Y position on board
     */
    public void manageGame(int fieldX, int fieldY) {
        
        //time control
        if (control.getBlackClock().getTime().equals("No time") || control.getBlackClock().getTime().equals("No time")) {
            control.setMate(true);
        }
        
        figure = board.getField(orgX, orgY);
        
        //check mate control
        if (!control.isMate()) {

            if (selectedFigure) {

                //deselect figure
                selectedFigure = false;
                
                //check if selected move is in availible moves
                if (control.getAllMovements().contains(fieldX + " " + fieldY) || control.getAllMovements().contains(fieldX + "c" + fieldY) || (control.getAllMovements().contains(fieldX + "p" + fieldY))) {
                    
                    //castling
                    if (control.getAllMovements().contains(fieldX + "c" + fieldY)) {
                        //castling for black 
                        if (fieldX == 0) {
                            control.casting(fieldX, fieldY, 0, 4);
                            pgn.castlingWrite(fieldY, control.opColor(control.getOnTurn()));

                        }
                        //castling for white
                        if (fieldX == 7) {
                            control.casting(fieldX, fieldY, 7, 4);
                            pgn.castlingWrite(fieldY, control.opColor(control.getOnTurn()));

                        }
                    } //enpassant    
                    else if (control.getAllMovements().contains(fieldX + "p" + fieldY)) {
                        if (control.movement(board, orgX, orgY, figure, fieldX, fieldY)) {
                            control.removeEnpassantFigure();
                            pgn.enpassantWrite(orgY, fieldX, fieldY);
                        }
                    } //normal moveWriteToPGN
                    else {
                        String squareForPNG = board.getField(fieldX, fieldY);

                        if (control.movement(board, orgX, orgY, figure, fieldX, fieldY)) {
                            if (!control.isIsPromotion()) {
                                pgn.moveWriteToPGN(figure, squareForPNG, fieldX, fieldY, orgY, orgX);
                            } else {
                                pgn.promotionWrite(figure, squareForPNG, fieldX, fieldY, orgY, orgX);
                            }

                        }
                    }
                    
                    control.setOnTurn(control.opColor(control.getOnTurn()));
                    control.setAllMovements(null);

                    //UI player changeOnTurn
                    if (UI) {
                        if (control.getOnTurn() == player.getColor() && !control.isMate()) {
                            player.move();

                        }
                    }

                    control.clockChange();
                }

                //if selected invalid position will deselect figure
                control.setAllMovements(null);
                
            } //if not selected figure
            else {
                //set figere
                figure = board.getField(fieldX, fieldY);
      
                //check if selected figure is right color
                if (control.getOnTurn() == figure.charAt(0)) {

                    orgX = fieldX;
                    orgY = fieldY;

                    control.characterMoves(fieldX, fieldY, board);
                    control.movementCheckControl(fieldX, fieldY);
                    selectedFigure = true;

                    //if figure doesn't have any moveWriteToPGN, will prevent to block game
                    if (control.getAllMovements().isEmpty()) {
                        selectedFigure = false;
                    }
                }
         
            }
        }
    }

    public String getFigure() {
        return figure;
    }
    
    
}
