/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.button;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author patrik
 */
public class ExitButton extends Button{

    public ExitButton(String text) {
        super(text);
        mouseEvent();
    }
   
    public void mouseEvent() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                 try {
                   System.exit(0);
                } catch (Exception ex) {
                   System.err.println("Program exit error: " + ex); 
                }
         }   
        }); 
    }
}
