/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.button;

import cz.fel.cvut.chess.PGN;
import cz.fel.cvut.chess.SelectionFigure;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author patrik
 */
public class PGNPopUp {
    public static void display(PGN pgn) {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("PGN save");

        Label label1 = new Label("File name");
        Label label2 = new Label("Event");
        Label label3 = new Label("Site");
        Label label4 = new Label("White");
        Label label5 = new Label("Black");

        Button button1 = new Button("Save in PGN");
        TextField fileNameT = new TextField();
        TextField eventPlay = new TextField();
        TextField site = new TextField();
        TextField white = new TextField();
        TextField black = new TextField();



        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    String fileName = fileNameT.getText();
                    String eventString = eventPlay.getText();
                    String siteString = site.getText();
                    String whiteString = white.getText();
                    String blackString = black.getText();

                    popupwindow.close();
                    pgn.savePGNGame(eventString, siteString, whiteString, blackString, fileName);
                } catch (IOException ex) {
                    Logger.getLogger(PGNPopUp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });


        //set components
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, fileNameT, label2, eventPlay, label3, site, label4, white, label5, black, button1);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 250, 350);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }
    
}
