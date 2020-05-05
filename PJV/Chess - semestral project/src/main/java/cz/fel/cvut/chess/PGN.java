/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.TextArea;

/**
 *
 * @author patrik
 */
public class PGN {
    private final ArrayList<String> moves = new ArrayList();
    private int i = 0;
    private String pgnText;
    private final Board board;
    private final BoardControl control;
    private final char[] yCoord;
    private final char[] xCoord;
    private int round;
    private final TextArea pgnArea;
    private String pgnMove;
    private File pgnFile;



    public PGN(Board board, BoardControl control, TextArea textArea) {
        this.board = board;
        this.control = control;
        this.round = 1;
        this.pgnArea = textArea;
        this.pgnText = "";

        //convert to pgn parameters
        this.yCoord = new char[8];
        this.yCoord[0] = 'a';
        this.yCoord[1] = 'b';
        this.yCoord[2] = 'c';
        this.yCoord[3] = 'd';
        this.yCoord[4] = 'e';
        this.yCoord[5] = 'f';
        this.yCoord[6] = 'g';
        this.yCoord[7] = 'h';

        //convert to pgn parameters
        this.xCoord = new char[8];
        this.xCoord[0] = '8';
        this.xCoord[1] = '7';
        this.xCoord[2] = '6';
        this.xCoord[3] = '5';
        this.xCoord[4] = '4';
        this.xCoord[5] = '3';
        this.xCoord[6] = '2';
        this.xCoord[7] = '1';

    }

    /**
     *  use for reset all variables which could influence new game, when starting new game read or write
     */
    public void reset() {
        pgnText = "";
        round = 1;
        
        //important for reading pgn
        board.generateNewGame();
        control.reset();
        i = 0;
        moves.clear();
        pgnArea.setText(pgnText);
    }

    /**
     *
     * @param fieldY use for write castlingWrite in
     * @param color of figure on turn
     */
    public void castlingWrite(int fieldY, char color) {
        round();
        if (fieldY == 6) {
            this.pgnText = this.pgnText + "0-0";
        } else {
            this.pgnText = this.pgnText + "0-0-0";
        }

        lastChar(color + "r");
    }

    /**
     * use for enpassantWrite
     *
     * @param orgY is position before moveWriteToPGN in array in board, which represents
 board.
     * @param fieldX position in array represents board
     * @param fieldY position in array represents board
     */
    public void enpassantWrite(int orgY, int fieldX, int fieldY) {
        round();
        this.pgnText = this.pgnText + yCoord[orgY] + "x" + yCoord[fieldY] + xCoord[fieldX];
        String figure = board.getField(fieldX, fieldY);
        lastChar(figure);
    }

    /**
     *
     * @param figure is figure, with we moveWriteToPGN
     * @param square is content of the square, where we go
     * @param fieldX x position where figure goes
     * @param fieldY y position where figure goes
     * @param orgY Y position where figure stands
     * @param orgX X position where figure stands
     */
    public void moveWriteToPGN(String figure, String square, int fieldX, int fieldY, int orgY, int orgX) {
        String sameFigurePosition;
        //write "1." round number
        round();

        if (figure.charAt(1) == 'p') {
            if (square.equals("  ")) {
                this.pgnText = this.pgnText + yCoord[fieldY] + "" + xCoord[fieldX];
            } else {
                this.pgnText = this.pgnText + yCoord[orgY] + "x" + yCoord[fieldY] + "" + xCoord[fieldX];
            }
        } else {

            sameFigurePosition = findAnotherSameFigure(figure, fieldX, fieldY);

            if (!sameFigurePosition.equals("0")) {
                char useIfSameFiguresCanDoSame;
                int sameFigurePosX = Character.getNumericValue(sameFigurePosition.charAt(0));
                int sameFigurePosY = Character.getNumericValue(sameFigurePosition.charAt(2));

                board.setField(fieldX, fieldY, "  ");
                control.characterMoves(sameFigurePosX, sameFigurePosY, board);
                control.movementCheckControl(sameFigurePosX, sameFigurePosY);
                board.setField(fieldX, fieldY, figure);

                String move = fieldX + " " + fieldY;

                if (control.getAllMovements().contains(move)) {
                    if (orgY == sameFigurePosY) {
                        useIfSameFiguresCanDoSame = xCoord[orgX];
                    } else {
                        useIfSameFiguresCanDoSame = yCoord[orgY];
                    }

                    if (square.equals("  ")) {
                        this.pgnText = this.pgnText + Character.toUpperCase(figure.charAt(1)) + "" + useIfSameFiguresCanDoSame + "" + yCoord[fieldY] + "" + xCoord[fieldX];
                    } else {
                        this.pgnText = this.pgnText + Character.toUpperCase(figure.charAt(1)) + "" + useIfSameFiguresCanDoSame + "x" + yCoord[fieldY] + "" + xCoord[fieldX];
                    }
                } else {
                    if (square.equals("  ")) {
                        this.pgnText = this.pgnText + Character.toUpperCase(figure.charAt(1)) + "" + yCoord[fieldY] + "" + xCoord[fieldX];
                    } else {
                        this.pgnText = this.pgnText + Character.toUpperCase(figure.charAt(1)) + "x" + yCoord[fieldY] + "" + xCoord[fieldX];
                    }

                }

            } else {

                if (square.equals("  ")) {
                    this.pgnText = this.pgnText + Character.toUpperCase(figure.charAt(1)) + "" + yCoord[fieldY] + "" + xCoord[fieldX];
                } else {
                    this.pgnText = this.pgnText + Character.toUpperCase(figure.charAt(1)) + "x" + yCoord[fieldY] + "" + xCoord[fieldX];
                }
            }
        }

        //add special char at end of move if check or checkmate
        lastChar(figure);
    }
    
    /**
     * use for write when is promotion
     * @param figure is figure, with we moveWriteToPGN
     * @param square is content of the square, where we go
     * @param fieldX x position where figure goes
     * @param fieldY y position where figure goes
     * @param orgY Y position where figure stands
     * @param orgX X position where figure stands
     */
    public void promotionWrite(String figure, String square, int fieldX, int fieldY, int orgY, int orgX){
            round();
            char toFig = Character.toUpperCase(board.getField(fieldX, fieldY).charAt(1));
            
            if (square.equals("  ")) {
                this.pgnText = this.pgnText + yCoord[fieldY] + "" + xCoord[fieldX]+"="+ toFig;
            } else {
                this.pgnText = this.pgnText + yCoord[orgY] + "x" + yCoord[fieldY] + "" + xCoord[fieldX]+"="+ toFig;
            }
            
            lastChar(figure);
    }

    private void round() {
        if (control.getOnTurn() == 'w') {
            this.pgnText = this.pgnText + round + ".";
            round++;
        }
    }

    private String findAnotherSameFigure(String figure, int orgX, int orgY) {
        board.setField(orgX, orgY, "  ");

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getField(i, j).equals(figure)) {
                    board.setField(orgX, orgY, figure);
                    return i + " " + j;
                }
            }

        }

        board.setField(orgX, orgY, figure);

        return "0";
    }

    private void lastChar(String figure) {
        char color = control.opColor(figure.charAt(0));

        if (control.isCheck(color)) {
            if (control.isMate() == true) {
                this.pgnText = this.pgnText + "# ";
            } else {
                this.pgnText = this.pgnText + "+ ";
            }
        } else {
            this.pgnText = this.pgnText + " ";
        }

        pgnArea.setText(pgnText);
    }

    /**
     * write pgn into file
     *
     * @param eventString get from popup window, represents event in pgn
     * notation
     * @param site get from popup window, represents site in pgn notation
     * @param white get from popup window, represents white player in pgn
     * notation
     * @param black get from popup window, represents black player in pgn
     * notation
     * @param fileName how user want to save file
     * @throws IOException io error
     */
    public void savePGNGame(String eventString, String site, String white, String black, String fileName) throws IOException {
        try (FileWriter w = new FileWriter("src/main/resources/pgn/"+fileName+".pgn")) {
            w.write("[ Event \"" + eventString + "\" ]\n");
            w.write("[ Site \"" + site + "\" ]\n");
            w.write("[ White \"" + white + "\" ]\n");
            w.write("[ Black \"" + black + "\" ]\n");
            w.write("\n");

            w.write(pgnText);
            w.close();

        } catch (Exception ex) {
            control.setInfo("game doesn't save\ntry again");
        }
    }

    public void loadPGNGame() throws FileNotFoundException, IOException {

        //File file = new File("src/main/resources/pgn/save.pgn");

        BufferedReader br = new BufferedReader(new FileReader(pgnFile));

        String st;
        
        //move to to the movesString section
        for (int i = 0; i < 5; i++) {
            br.readLine();
        }
        
        String movesString = "";
        //set movesString into string
        while ((st = br.readLine()) != null) {
            movesString = st;
        }
        
        br.close();
        
        String move = "";
        
        
        for (int i = 1; i < movesString.length(); i++) {
            if(movesString.charAt(i-1) == '.' )
                move = "";
            
            if(movesString.charAt(i) == ' '){
                moves.add(move);
                move = "";
            }
            
            if(movesString.charAt(i) != ' ')
                move = move + movesString.charAt(i);
            
        }
        
    }
    
    /**
     * will set into pgnMove next moveWriteToPGN from ArrayList of moves,
     */
    public void nextMove(){
        if(i < moves.size()){
            pgnMove = moves.get(i);
            i++;
        }
    }
    
    public void prevMove(){
        if(i > 1)
            --i;
   }

    public int getI() {
        return i;
    }
    
    

    public String getPgnMove() {
        return pgnMove;
    }
    
    
    /**
     *
     * @param row is second coordination in pgn figure position, which is first parameter in 2 dimensional field
     * @return board is save in array of array, return second parameter of this array
     */
    public char convertRowToFieldX(char row){
        char moveInBoardParametrs ='x';
        
        switch (row){
            case '1':
                moveInBoardParametrs = '7';
            break;
            case '2':
                moveInBoardParametrs = '6';
            break;
            case '3':
                moveInBoardParametrs = '5';
            break;
            case '4':
                moveInBoardParametrs = '4';
            break;
            case '5':
                moveInBoardParametrs = '3';
            break;
            case '6':
                moveInBoardParametrs = '2';
            break;
            case '7':
                moveInBoardParametrs = '1';
            break;
            case '8':
                moveInBoardParametrs = '0';
            break;
        }
        
        return moveInBoardParametrs;
    }   
    
    /**
     *
     * @param column is first coordination in pgn figure position, which is second parameter in 2 dimensional field 
     * @return board is save in array of array, return first parameter of this array
     */
    public char convertColumnToFieldY(char column){
        char moveInBoardParametrs = 'x';

        switch (column){
            case 'h':
                moveInBoardParametrs = '7';
            break;
            case 'g':
                moveInBoardParametrs = '6';
            break;
            case 'f':
                moveInBoardParametrs = '5';
            break;
            case 'e':
                moveInBoardParametrs = '4';
            break;
            case 'd':
                moveInBoardParametrs = '3';
            break;
            case 'c':
                moveInBoardParametrs = '2';
            break;
            case 'b':
                moveInBoardParametrs = '1';
            break;
            case 'a':
                moveInBoardParametrs = '0';
            break;
        }
    
        
        return moveInBoardParametrs;
    }
    
    /**
     *
     * @param move is one moveWriteToPGN in pgn format
     * @return value of figure in the board from pgn moveWriteToPGN
     */
    public String getFigureFromPGNMove(String move){
                
        if(move.length() == 2){
            return "p";
        }
        else if(move.equals("0-0-0") || move.equals("0-0-0") ){
            return "k";
        }
        else{
            switch(move.charAt(0)){
                case 'K':
                    return "k";
                case 'Q':
                    return "q";
                case 'B':
                    return "b";
                case 'N':
                    return "n";
                case 'R':
                    return "r";
            }
        }
            
        return "p";
    }
    
    /**
     * set
     * @param pgnFile for reading pgn files
     */
    public void setPgnFile(File pgnFile) {
        this.pgnFile = pgnFile;
    }

    public ArrayList<String> getMoves() {
        return moves;
    }
    
    
}

