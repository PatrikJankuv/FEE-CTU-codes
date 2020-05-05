/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.button;

import cz.fel.cvut.chess.Board;
import cz.fel.cvut.chess.BoardControl;
import cz.fel.cvut.chess.GameManager;
import cz.fel.cvut.chess.PGN;
import cz.fel.cvut.chess.PGNDecoder;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author patrik
 */
public class PrevPNGMoveButton extends Button {
    private final PGN pgn;
    private final GameManager gameManager;
    private final BoardControl control;
    private final Board board;
    private final PGNDecoder decoder;
    
    public PrevPNGMoveButton(String text, PGN pgn, GameManager gameManager, BoardControl control, Board board, PGNDecoder decoder) {
        super(text);
        this.pgn = pgn;
        this.gameManager = gameManager;
        this.control = control;
        this.board = board;
        this.decoder = decoder;
        this.mouseEvent();
    }
   
    /**
     *  set mouse event
     */
    public final void mouseEvent() {
        this.setOnMouseClicked((MouseEvent event) -> {
            try {

                //get previous turn number
                int pom = pgn.getI();
                pom--;
                
                //reset pgn
                pgn.reset();
                pgn.loadPGNGame();
                
                //call next move from start until previous turn
                for(int i = 0; i < pom; i++){
                    pgn.nextMove();
                    decoder.decodePGN();
                }
                
            } catch (IOException ex) {
                System.err.println("noPrevMove");
            }
        }); 
    }
    
}
