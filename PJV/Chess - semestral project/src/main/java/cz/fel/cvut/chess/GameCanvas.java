/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.VPos;
import javafx.scene.text.TextAlignment;

import static cz.fel.cvut.chess.Util.*;
import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author jankupat
 */
public class GameCanvas extends Canvas {
    private final Map<String, Image> figureImages;
    private final BoardControl control;
    private final Board board;
    private final GameManager gameManager;


    public GameCanvas(BoardControl control, Board board, GameManager gameManager) {

        this.control = control;
        this.board = board;
        this.gameManager = gameManager;
        this.figureImages = new HashMap<>();
        
        initFigureImage();

        // Redraw canvas when size changes
        widthProperty().addListener(evt -> redraw());
        heightProperty().addListener(evt -> redraw());
        mouseEvent();

    }

    public void fixAspectRatio(double aspectRatio) {
        Parent parent = getParent();
        if (parent instanceof Pane) {
            // Bind canvas size to parent Pane width...
            widthProperty().bind(((Pane) getParent()).widthProperty());
            // ...and keep its aspect ratio while resizing
            heightProperty().bind(((Pane) getParent()).widthProperty().multiply(aspectRatio));
        }
    }

    /**
     * use for redraw
     */
    public void redraw() {
        GraphicsContext gc = getGraphicsContext2D();
        renderBackground(gc);
        renderOnTurn(gc);
        renderTime(gc);
        renderFigures(gc);

        //rendering possible moves
        if (control.getAllMovements() != null) {
            renderMoves(gc);
        }


//    renderScore(gc);
    }

    /**
     *
     * @param gc Graphic Context use for rendering board
     *
     */
    private void renderBackground(GraphicsContext gc) {
        double width = getWidth();
        double height = getHeight();

        gc.setFill(Color.GREY);
        gc.fillRect(0, 0, width, height);
        Image back = new Image(GFX_DIR+"back3.jpg");
        gc.drawImage(back,0, 0);

        //draw board
        gc.setFill(DARK);
        gc.fillRect(SHIFT - 30, SHIFT - 30, 702, 702);

        gc.setFill(Color.BLACK);
        gc.fillRect(SHIFT - 3, SHIFT - 3, 646, 646);
       
        gc.fillText("8", SHIFT-15, SHIFT+40);
        gc.fillText("7", SHIFT-15, SHIFT+40+80);
        gc.fillText("6", SHIFT-15, SHIFT+40+80*2);
        gc.fillText("5", SHIFT-15, SHIFT+40+80*3);
        gc.fillText("4", SHIFT-15, SHIFT+40+80*4);
        gc.fillText("3", SHIFT-15, SHIFT+40+80*5);
        gc.fillText("2", SHIFT-15, SHIFT+40+80*6);
        gc.fillText("1", SHIFT-15, SHIFT+40+80*7);
        
        gc.fillText("a", SHIFT+40, SHIFT+655);
        gc.fillText("b", SHIFT+40+80, SHIFT+655);
        gc.fillText("c", SHIFT+40+80*2, SHIFT+655);
        gc.fillText("d", SHIFT+40+80*3, SHIFT+655);
        gc.fillText("e", SHIFT+40+80*4, SHIFT+655);
        gc.fillText("f", SHIFT+40+80*5, SHIFT+655);
        gc.fillText("g", SHIFT+40+80*6, SHIFT+655);
        gc.fillText("h", SHIFT+40+80*7, SHIFT+655);

        //render board square
        for (int j = 0; j <= 3; j++) {
            int b = j * 160 + SHIFT;

            for (int i = 0; i <= 3; i++) {
                int a = i * 160 + SHIFT;
                gc.setFill(LIGHT);
                gc.fillRect(0 + a, 0 + b, 80, 80);
                gc.setFill(DARK);
                gc.fillRect(80 + a, 0 + b, 80, 80);
            }

            for (int i = 0; i <= 3; i++) {
                int a = i * 160 + SHIFT;
                gc.setFill(DARK);
                gc.fillRect(0 + a, 80 + b, 80, 80);
                gc.setFill(LIGHT);
                gc.fillRect(80 + a, 80 + b, 80, 80);
            }

        }
    }

    public void mouseEvent() {
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {

                int fieldX = (int)(((e.getY() - SHIFT)/80));
                int fieldY = (int)((e.getX() - SHIFT)/80);
                
                if(fieldX < 8 && fieldY < 8) 
                    gameManager.manageGame(fieldX, fieldY);
  
         }   
        }); 
    }

    /**
     *
     * @param gc Graphic Context use for rendering information about, who is on
 changeOnTurn and state of game (check or check mate)
     *
     */
    private void renderOnTurn(GraphicsContext gc) {
        String infoText = control.getOnTurn() == 'w'
                ? "On turn: white" : "On turn: black";

        gc.setFill(Color.WHITE);
        gc.setFont(new Font(Font.getDefault().getName(), 25));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(infoText, 825, TEXT_Y_POSITION, INIT_APP_WIDTH);

        gc.fillText(control.getInfo(), 825, TEXT_Y_POSITION + 100, INIT_APP_WIDTH);

    }

    /**
     *
     * @param gc Graphic Context use for rendering blue dots, which shows player
     * possible moves
     */
    private void renderMoves(GraphicsContext gc) {
        for (String element : control.getAllMovements()) {
            char x = element.charAt(0);
            char y = element.charAt(2);

            int posX = x - 48;
            int posY = y - 48;
            gc.setFill(Color.BLUE);
            gc.setFill(Color.TRANSPARENT);

            gc.setStroke(Color.BLUE);
            gc.setLineWidth(5);
            gc.strokeRect(posY * 80 + SHIFT, posX * 80 + SHIFT, 80, 80);
    
            //draw dots
            //gc.fillOval(posY * 80 + SHIFT + 25, posX * 80 + SHIFT + 25, 30, 30);

        }
    }

    /**
     *  use for rendering time on the canvas
     * @param gc GraphicContext
     */
    private void renderTime(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(Font.getDefault().getName(), 25));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(control.getBlackClock().getTime(), 825, TEXT_Y_POSITION + 200, INIT_APP_WIDTH);
        gc.fillText(control.getWhiteClock().getTime(), 825, TEXT_Y_POSITION + 250, INIT_APP_WIDTH);

    }
    
    /**
     *  from model of board will render figures on screen 
     * @param gc
     */
    private void renderFigures(GraphicsContext gc){
        //final Image image = new Image(GFX_DIR+"BlackBishop.png");
              
       for (int k = 0; k < 8; k++){
            for (int l = 0; l < 8; l++){
                 switch(board.getField(k, l)){
                    case "bk":
                        gc.drawImage(figureImages.get("BK"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "wk":
                        gc.drawImage(figureImages.get("WK"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "bq":
                        gc.drawImage(figureImages.get("BQ"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "wq":
                        gc.drawImage(figureImages.get("WQ"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "wb":
                        gc.drawImage(figureImages.get("WB"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "bb":
                        gc.drawImage(figureImages.get("BB"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "wr":
                        gc.drawImage(figureImages.get("WR"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "br":
                        gc.drawImage(figureImages.get("BR"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "wn":
                        gc.drawImage(figureImages.get("WN"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "bn":
                        gc.drawImage(figureImages.get("BN"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "wp":
                        gc.drawImage(figureImages.get("WP"),SHIFT+l*80, SHIFT+k*80);
                    break;
                    case "bp":
                        gc.drawImage(figureImages.get("BP"),SHIFT+l*80, SHIFT+k*80);
                    break;
                 }
            }
       }
        
    }
    
    /**
     *  full map figureMap which contain images of figures
     */
    private void initFigureImage(){
        Image image1 = new Image(GFX_DIR+"BlackBishop.png");
        figureImages.put("BB", image1);
        Image image2 = new Image(GFX_DIR+"WhiteBishop.png");
        figureImages.put("WB", image2);
        Image image3 = new Image(GFX_DIR+"BlackRook.png");
        figureImages.put("BR", image3);
        Image image4 = new Image(GFX_DIR+"WhiteRook.png");
        
        figureImages.put("WR", image4);
        Image image5 = new Image(GFX_DIR+"BlackKnight.png");
        figureImages.put("BN", image5);
        Image image6 = new Image(GFX_DIR+"BlackQueen.png");
        figureImages.put("BQ", image6);
        Image image7 = new Image(GFX_DIR+"BlackKing.png");
        figureImages.put("BK", image7);
        Image image8 = new Image(GFX_DIR+"BlackPawn.png");
        figureImages.put("BP", image8);
        
        Image image15 = new Image(GFX_DIR+"WhiteKnight.png");
        figureImages.put("WN", image15);
        Image image16 = new Image(GFX_DIR+"WhiteQueen.png");
        figureImages.put("WQ", image16);
        Image image17 = new Image(GFX_DIR+"WhiteKing.png");
        figureImages.put("WK", image17);
        Image image18 = new Image(GFX_DIR+"WhitePawn.png");
        figureImages.put("WP", image18);
    }
    

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
