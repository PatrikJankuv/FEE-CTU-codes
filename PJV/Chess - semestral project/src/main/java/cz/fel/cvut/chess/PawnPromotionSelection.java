/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author patrik
 */
public class PawnPromotionSelection {
    public static void display(SelectionFigure data) {
        Stage popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Pawn selection/");

        Label label1 = new Label("Select figure");

        Button button1 = new Button("Queen");
        Button button2 = new Button("Knight");
        Button button3 = new Button("Rook");
        Button button4 = new Button("Bishop");

        button1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popupwindow.close();
                data.setData("q");

            }

        });

        button2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popupwindow.close();
                data.setData("n");

            }

        });

        button3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popupwindow.close();
                data.setData("r");

            }

        });

        button4.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                popupwindow.close();
                data.setData("b");

            }

        });
//button1.setOnAction(e -> );
//     

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label1, button1, button2, button3, button4);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 250);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

    }
    
}
