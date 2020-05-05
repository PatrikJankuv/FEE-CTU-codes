/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import cz.fel.cvut.button.EditBoardButton;
import cz.fel.cvut.button.SavePNGButton;
import cz.fel.cvut.button.ExitButton;
import cz.fel.cvut.button.LoadButton;
import cz.fel.cvut.button.LoadPNGButton;
import cz.fel.cvut.button.RestartButton;
import cz.fel.cvut.button.SaveButton;
import cz.fel.cvut.button.UIPlayerButton;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.TimelineBuilder;
import javafx.scene.control.Button;
import javafx.util.Duration;

import static cz.fel.cvut.chess.Util.*;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;

/**
 *
 * @author jankupat
 */
public class Main extends Application{
    final double ASPECT_RATIO = INIT_APP_HEIGHT / INIT_APP_WIDTH;
    private GameLoopHandler gameLoopHandler;
    private GameCanvas gameCanvas;
    private Board board = new Board();
    private BoardControl control;
    private UIPlayer player;
    private GameManager gameManager;
    private PGN pgn;
    private TextArea PGNtext;
    private PGNDecoder decoder;
    
     
     
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void init() throws Exception {

    board.generateNewGame();
    control = new BoardControl(board);
    
    PGNtext = new TextArea();
    pgn = new PGN(board, control, PGNtext); 
    
    
           
    gameManager = new GameManager(board, control, pgn);
    decoder = new PGNDecoder(gameManager, pgn, board, control);
    player = new UIPlayer('b', control, board, pgn, gameManager);

    gameCanvas = new GameCanvas(control, board, gameManager);
    gameLoopHandler = new GameLoopHandler(gameCanvas);
    

 }
    

    @Override
    public void start(Stage stage) throws Exception {
         // 1. build the scene
        StackPane stackPane = new StackPane();
        
        stackPane.getChildren().add(gameCanvas);
        gameCanvas.fixAspectRatio(ASPECT_RATIO);
        
        Button button1 = new RestartButton("New Game Player vs Player", control, board, gameManager, pgn);
        button1.setTranslateY(-300);
        button1.setTranslateX(BUTTONS_POS);   
        
        Button button2 = new ExitButton("Exit game");
        button2.setTranslateY(-50);
        button2.setTranslateX(BUTTONS_POS); 
        
        
        GameSaver save = new GameSaver(board, control);
        
        Button button3 = new SaveButton("Save", save);
        button3.setTranslateY(-200);
        button3.setTranslateX(BUTTONS_POS);  
        
        Button button4 = new LoadButton("Load", save);
        button4.setTranslateY(-150);
        button4.setTranslateX(BUTTONS_POS);  
        
        Button button5 = new UIPlayerButton("New Game Player vs UI player", control, board, gameManager,  player, pgn);
        button5.setTranslateY(-250);
        button5.setTranslateX(BUTTONS_POS);
        
        Button button6 = new SavePNGButton("Save PNG", pgn);
        button6.setTranslateY(250);
        button6.setTranslateX(BUTTONS_POS);
        
        Button button7 = new LoadPNGButton("Load PNG", pgn, stackPane, gameManager, control, board, stage, decoder);
        button7.setTranslateY(300);
        button7.setTranslateX(BUTTONS_POS);

        PGNtext.setTranslateY(100);
        PGNtext.setTranslateX(BUTTONS_POS);
        PGNtext.setMaxSize(200, 200);
        PGNtext.setWrapText(true);
        
        Button button8 = new EditBoardButton("Load edited board", save, stage);
        button8.setTranslateY(-100);
        button8.setTranslateX(BUTTONS_POS);  
        
        //disable maximalize
        stage.setResizable(false);

        
        //scene set
        Scene scene = new Scene(stackPane, INIT_APP_WIDTH, INIT_APP_HEIGHT);
        
        stackPane.getChildren().addAll(button1, button2, button3, button4, button5, button6, button7, button8, PGNtext);
        stage.setScene(scene);
        stage.setTitle("Chess");
        
        //kill clock after close window
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });

                
    // 3. create the game loop
    final KeyFrame oneFrame = new KeyFrame(
      Duration.millis(1000 / 10), // [10 fps]
      gameLoopHandler
    );
    TimelineBuilder.create()
      .cycleCount(Animation.INDEFINITE)
      .keyFrames(oneFrame)
      .build()
      .play();
        
        
        //stage show
        stage.show();
    }
    
}
