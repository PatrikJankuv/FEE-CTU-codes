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
import cz.fel.cvut.chess.UIPlayer;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author patrik
 */
public class UIPlayerButton extends Button{
    BoardControl control;
    Board board;
    GameManager gameManager;
    UIPlayer player;
    PGN pgn;
    
    public UIPlayerButton(String text, BoardControl control, Board board, GameManager gameManager, UIPlayer player, PGN pgn) {
        super(text);
        this.control = control;
        this.board = board;
        this.gameManager = gameManager;
        this.player = player;
        this.pgn = pgn;
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
                    //TO DOchange pawns back
                    board.generateNewGame();
                    control.reset();
                    pgn.reset();
                    gameManager.playerVsUI(board, control, player);
                } catch (Exception ex) {
                    System.err.println("UI player wasn't set"); 
                }
         }   
        }); 
    }
}
