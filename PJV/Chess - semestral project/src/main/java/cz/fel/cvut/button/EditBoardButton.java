/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.button;

import cz.fel.cvut.chess.GameSaver;
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author patrik
 */
public class EditBoardButton extends Button{
    private final Stage stage;
    private final GameSaver save;
    
    public EditBoardButton(String text, GameSaver save, Stage stage) {
        super(text);
        this.save = save;
        this.stage = stage;
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
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Edit Board Load");
                File f = new File("src/main/resources/editedBoard/");
                fileChooser.setInitialDirectory(f);

                File file = fileChooser.showOpenDialog(stage);
               
                save.loadBoard(file);
                
                } catch (Exception ex) {
                   System.err.println("Game load error " + ex); 
                }
                
            }
            
        }); 
    }
    
}