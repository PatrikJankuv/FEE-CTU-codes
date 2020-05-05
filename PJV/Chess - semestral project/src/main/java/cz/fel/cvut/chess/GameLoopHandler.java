/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;


import javafx.event.Event;
import javafx.event.EventHandler;


/**
 *
 * @author patrik
 */
public class GameLoopHandler implements EventHandler {
    private final GameCanvas gameCanvas;


    public GameLoopHandler(GameCanvas gameCanvas) {
        this.gameCanvas = gameCanvas;
        initSprites();
    }

    private void initSprites() {
        gameCanvas.redraw();
    }

    private void updateSprites() {
        gameCanvas.redraw();
    }

    
    @Override
    public void handle(Event event) {
        updateSprites();
    }

}
