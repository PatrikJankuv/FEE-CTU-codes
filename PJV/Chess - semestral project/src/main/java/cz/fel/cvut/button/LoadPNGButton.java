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
import java.io.File;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author patrik
 */
public class LoadPNGButton extends Button {
    private final BoardControl control;
    private final Board board;
    private final PGN pgn;
    private final StackPane stackPane;
    private final GameManager gameManager;
    private final Stage stage;
    private final PGNDecoder decoder;

    /**
     *
     * @param text is show in TextArea
     * @param pgn PGN.class
     * @param stackPane stackPane
     * @param gameManager gameManager.class
     * @param control BoardControl.class
     * @param board board.class
     * @param stage main stage
     */
    public LoadPNGButton(String text, PGN pgn, StackPane stackPane, GameManager gameManager, BoardControl control, Board board, Stage stage, PGNDecoder decoder) {
        super(text);
        this.pgn = pgn;
        this.stackPane = stackPane;
        this.gameManager = gameManager;
        this.control = control;
        this.board = board;
        this.stage = stage;
        this.decoder = decoder;
        mouseEvent();
    }

    /**
     * add event to restart game
     */
    public void mouseEvent() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    pgn.reset();

                    //add new buttons for controling pgn moves
                    Button button8 = new PrevPNGMoveButton("Prev", pgn, gameManager, control, board, decoder);
                    button8.setTranslateY(300);
                    button8.setTranslateX(450 - 75);

                    //add new buttons for controling pgn moves
                    Button button9 = new NextPGNMoveButton("Next", pgn, gameManager, decoder);
                    button9.setTranslateY(300);
                    button9.setTranslateX(450 + 75);

                    //add buttons on the screen
                    stackPane.getChildren().addAll(button8, button9);

                    //load game to pgn
                    //pgn.loadPGNGame();
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Load pgn file");
                    File f = new File("src/main/resources/pgn/");
                    fileChooser.setInitialDirectory(f);

                    File file = fileChooser.showOpenDialog(stage);

                    String src = file.getAbsolutePath();
                    String fileExtension = src.substring(src.length() - 3);
                    
                    //file verification
                    if(fileExtension.equals("pgn")){
                        pgn.setPgnFile(file);
                        pgn.loadPGNGame();
                    }
                    else
                        control.setInfo("Wrong file format");

                    //set board for editing
                    gameManager.playerVsPlayer(board, control);
                    
       
                } catch (Exception ex) {
                    System.err.println("No game load " + ex);
                }

            }

        });
    }
    
}
