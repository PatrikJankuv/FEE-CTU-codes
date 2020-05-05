/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.button;


import cz.fel.cvut.chess.GameSaver;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author patrik
 */
public class SaveButton extends Button{

    GameSaver save;
    
    public SaveButton(String text, GameSaver save) {
        super(text);
        this.save = save;
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
                   save.saveGame();
                } catch (Exception ex) {
                   System.err.println("Game wasn't save"); 
                }
                
            }
            
        }); 
    }
    
}
