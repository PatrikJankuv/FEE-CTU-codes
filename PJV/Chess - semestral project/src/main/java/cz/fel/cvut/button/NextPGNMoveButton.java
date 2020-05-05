/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.button;

import cz.fel.cvut.chess.GameManager;
import cz.fel.cvut.chess.PGN;
import cz.fel.cvut.chess.PGNDecoder;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author patrik
 */
public class NextPGNMoveButton extends Button {
     private final PGN pgn;
     private final GameManager gameManager;
     private final PGNDecoder decoder;
    
    public NextPGNMoveButton(String text, PGN pgn, GameManager gameManager, PGNDecoder decoder) {
        super(text);
        this.pgn = pgn;
        this.gameManager = gameManager;
        this.decoder = decoder;
        mouseEvent();
    }
   
    /**
     *  add event to  restart game
     */
    public void mouseEvent() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
           @Override
            public void handle(MouseEvent event) {
                try {
                   pgn.nextMove();
                   decoder.decodePGN();
                } catch (Exception ex) {
                   System.err.println("noNextMove"); 
                }
                
            }
            
        }); 
    }
    
}
