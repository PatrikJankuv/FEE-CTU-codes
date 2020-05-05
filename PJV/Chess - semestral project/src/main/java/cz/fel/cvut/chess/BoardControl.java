/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import cz.fel.cvut.chess.time.Clock;
import cz.fel.cvut.chess.figures.Knight;
import cz.fel.cvut.chess.figures.King;
import cz.fel.cvut.chess.figures.Bishop;
import cz.fel.cvut.chess.figures.Pawn;
import cz.fel.cvut.chess.figures.Queen;
import cz.fel.cvut.chess.figures.Rook;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author patrik
 */
public class BoardControl {

    private boolean mate;
    private String info;
    private Board board;
    private String enpassant;
    private char onTurn;
    private SelectionFigure data;
    private Clock blackClock;
    private boolean isPromotion;
    private Clock whiteClock;
    private Thread blackTime;
    private Thread whiteTime;
    private ArrayList<String> allMovements = new ArrayList<>();
    private ArrayList<String> checkMovements = new ArrayList<>();

    public char getOnTurn() {
        return onTurn;
    }

    /**
     *
     * @param board is instance of Board.class
     */
    public BoardControl(Board board) {
        mate = false;
        enpassant = " ";
        onTurn = 'w';
        blackClock = new Clock(180);
        whiteClock = new Clock(180);
        blackTime = new Thread(blackClock);
        whiteTime = new Thread(whiteClock);
        this.board = board;
        this.isPromotion = false;
    }

    /**
     * reset control parameters, for new game
     */
    public void reset() {
        mate = false;
        enpassant = " ";
        onTurn = 'w';
        blackClock = new Clock(180);
        whiteClock = new Clock(180);
        blackTime = new Thread(blackClock);
        whiteTime = new Thread(whiteClock);
        startTime();
    }

    /**
     *
     * @param info is text, which will show on a screen
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     *
     * @param enpassant is pawn which can be take off by en passant move
     */
    public void setEnpassant(String enpassant) {
        this.enpassant = enpassant;
    }

    /**
     * set characterMoves from field with parameters x and y into array
     * allMovements
     *
     * @param x position row on the board
     * @param board is board, method is call from outside
     * @param y position column on the board
     */
    public void characterMoves(int x, int y, Board board) {
        //get figure type from board
        char symbol = board.getField(x, y).charAt(1);

        if (symbol == 'k') {
            King figur = new King(x, y, board);
            allMovements = figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'q') {
            Queen figur = new Queen(x, y, board);
            allMovements = figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'b') {
            Bishop figur = new Bishop(x, y, board);
            allMovements = figur.moves(x, y, board.getColor(x, y));

        }
        if (symbol == 'n') {
            Knight figur = new Knight(x, y, board);
            allMovements = figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'r') {
            Rook figur = new Rook(x, y, board);
            allMovements = figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'p') {
            Pawn figur = new Pawn(x, y, board, enpassant);
            allMovements = figur.moves(x, y, board.getColor(x, y));
        }

    }
    
    /**
     *
     * @return array of available moves just selected figure
     */
    public ArrayList<String> getAllMovements() {
        return allMovements;
    }

    /**
     * similar method as getCharacter moves, but this method is call from
     * methods isCheck and isCheckmat, for checking all possible moves
     *
     * @param x position x on the board
     * @param y position y on the board
     * @param board board
     * @return all movements, do not matter if is good or allowed
     */
    public ArrayList<String> getSelectedCharacterMoves(int x, int y, Board board) {
        //get figure type from board
        char symbol = board.getField(x, y).charAt(1);

        if (symbol == 'k') {
            King figur = new King(x, y, board);
            return figur.moves(x, y, board.getColor(x, y));

        }
        if (symbol == 'q') {
            Queen figur = new Queen(x, y, board);
            return figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'b') {
            Bishop figur = new Bishop(x, y, board);
            return figur.moves(x, y, board.getColor(x, y));

        }
        if (symbol == 'n') {
            Knight figur = new Knight(x, y, board);
            return figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'r') {
            Rook figur = new Rook(x, y, board);
            return figur.moves(x, y, board.getColor(x, y));
        }
        if (symbol == 'p') {
            Pawn figur = new Pawn(x, y, board, enpassant);
            return figur.moves(x, y, board.getColor(x, y));
        } else {
            return null;
        }
    }

    /**
     * will make casting
     *
     * @param toX 1st position in array, which represents board, where king
     * should be move
     * @param toY 2nd position in array, which represents board, where king
     * should be move according :
     * @param nowX 1st position in array, which represents board, where king is
     * before move
     * @param nowY 2nd position in array, which represents board, where king is
     * before move make field empty, because there is king
     */
    public void casting(int toX, int toY, int nowX, int nowY) {
        //black
        if (toY == 2 && toX == 0) {
            //root move
            board.setField(0, 3, board.getField(0, 0));
            board.setField(0, 0, "  ");
            //king move
            board.setField(toX, toY, board.getField(nowX, nowY));
            board.setField(0, 4, "  ");
        }
        if (toY == 6 && toX == 0) {
            //root move
            board.setField(0, 5, board.getField(0, 7));
            board.setField(0, 7, "  ");
            //king move
            board.setField(toX, toY, board.getField(nowX, nowY));
            board.setField(0, 4, "  ");
        }

        //white
        if (toY == 2 && toX == 7) {
            //root move
            board.setField(7, 3, board.getField(7, 0));
            board.setField(7, 0, "  ");
            //king move
            board.setField(toX, toY, board.getField(nowX, nowY));
            board.setField(7, 4, "  ");
        }
        if (toY == 6 && toX == 7) {
            //root move
            board.setField(7, 5, board.getField(7, 7));
            board.setField(7, 7, "  ");
            //king move
            board.setField(toX, toY, board.getField(nowX, nowY));
            board.setField(7, 4, "  ");
        }
    }

    /**
     * change color of player which is on changeOnTurn
     */
    public void changeOnTurn() {
        if (this.onTurn == 'w') {
            this.onTurn = 'b';
        } else {
            this.onTurn = 'w';
        }
    }

    /**
     * check if player with color in parameter of the method, doesn't have check
     *
     * @param color is color of player
     * @return true if player has check, false if not
     */
    public boolean isCheck(char color) {
        char opcolor;
        String king = board.getKing(color);

        opcolor = opColor(color);

        //check all oponent figures, if not containt king in possible movements
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board.isEmpty(i, j)) {
                    if (opcolor == board.getColor(i, j)) {
                        checkMovements = null;
                        checkMovements = getSelectedCharacterMoves(i, j, board);
                        if ((checkMovements != null) && (checkMovements.contains(king))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * @param color
     * @return inverse color of color in parameter
     */
    public char opColor(char color) {
        if (color == 'w') {
            return 'b';
        } else {
            return 'w';
        }
    }

    /**
     * @param color is player we check
     * @return if player of color has check mate return true, else false
     */
    private boolean isCheckMate(char color) {
        //save original copy of board
        String[][] copy = new String[8][8];

        //duplicate board
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board.getBoard()[i], 0, copy[i], 0, 8);
        }

        //check all oponent figures, if not containt king in possible movements
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board.isEmpty(i, j)) {

                    if (color == board.getColor(i, j)) {
                        checkMovements = null;

                        checkMovements = getSelectedCharacterMoves(i, j, board);
                        for (Iterator<String> it = checkMovements.iterator(); it.hasNext();) {
                            String move = it.next();
                            board.setField(Integer.parseInt(String.valueOf(move.charAt(0))), Integer.parseInt(String.valueOf(move.charAt(2))), board.getField(i, j));
                            board.setField(i, j, "  ");

                            if (!isCheck(color)) {
                                //reseat board
                                for (int l = 0; l < 8; l++) {
                                    System.arraycopy(copy[l], 0, board.getBoard()[l], 0, 8);
                                }

                                return false;
                            } else {
                                //reseat board
                                for (int l = 0; l < 8; l++) {
                                    System.arraycopy(copy[l], 0, board.getBoard()[l], 0, 8);
                                }

                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * @param board board
     * @param figure selected by player
     * @param toX x position on board, where figure should be place
     * @param toY y position on board, where figure should be place
     * @return true if move was made, or return false if wasn't made
     *
     */
    public boolean movement(Board board, int fromX, int fromY, String figure, int toX, int toY) {
        String position = board.getPosition(figure);
        char x = position.charAt(0);
        char y = position.charAt(2);

        int posX = x - 48;
        int posY = y - 48;

        //originaFigure will be use if move is illeagal, for return original board situation
        String originalFigure = board.getField(toX, toY);

        board.setField(fromX, fromY, "  ");
        board.setField(toX, toY, figure);

        EnpassantFigure(figure, toX, toY, fromX);

        char figureColor = figure.charAt(0);

        //if player on changeOnTurn has check, control if this move change check
        if (isCheck(figureColor)) {
            board.setField(posX, posY, figure);
            board.setField(toX, toY, originalFigure);
            info = "Can't do this, check";
            return false;
        }

        //promotion setting
        setPromotion(figure, toX, toY, board);

        //checking if after move, has oposite player check mate or check
        // set variable info, which is use for render
        if (isCheck(opColor(figureColor))) {

            if (isCheckMate(opColor(figureColor))) {

                if (opColor(figureColor) == 'w') {
                    info = "White loose\nCHECK MATE";
                    mate = true;
                } else {
                    info = "Black loose\nCHECK MATE";
                }
                mate = true;
            } else {
                if (opColor(figureColor) == 'w') {
                    info = "White has check";
                } else {
                    info = "Black has check";
                }
            }
            return true;
        } else {
            info = "";
        }

        return true;
    }

    /**
     *
     * @return figure which is selected as en passant
     */
    public String getEnpassant() {
        return enpassant;
    }

    /**
     * if move selected by opponent have to remove pawn, which is select as
     * en passant, this method will remove enpassant pawn from board
     */
    public void removeEnpassantFigure() {
        char passantColor = enpassant.charAt(0);
        int passantPosition = (int) enpassant.charAt(2) - 48;

        //check conditions
        if (passantColor == 'w') {
            board.setField(4, passantPosition, "  ");
        } else if (passantColor == 'b') {
            board.setField(3, passantPosition, "  ");
        }
    }

    /**
     * supply promoted pawn, by selected figure
     *
     * @param figure figure
     * @param toX x position on board
     * @param toY is column on the board
     * @param board is board
     */
    public void setPromotion(String figure, int toX, int toY, Board board) {

        if (figure.charAt(1) == 'p' && figure.charAt(0) == 'b' && toX == 7) {
            data = new SelectionFigure();
            // display new window for making choise
            PawnPromotionSelection.display(data);
            figure = "b" + data.getData();

            board.setField(toX, toY, figure);
            setIsPromotion(true);

        } else if (figure.charAt(1) == 'p' && figure.charAt(0) == 'w' && toX == 0) {
            data = new SelectionFigure();
            PawnPromotionSelection.display(data);
            figure = "w" + data.getData();

            board.setField(toX, toY, figure);
            setIsPromotion(true);

        } else {
            setIsPromotion(false);
        }
    }

    /**
     *
     * @return if last turn happen promotion
     */
    public boolean isIsPromotion() {
        return isPromotion;
    }

    /**
     *
     * @param isPromotion inform if happen promotion in last move
     */
    public void setIsPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    /**
     *
     * @param onTurn set which color is on changeOnTurn
     */
    public void setOnTurn(char onTurn) {
        this.onTurn = onTurn;
    }

    /**
     *
     * @return figure selection, from popup
     */
    public String getPromotionSelection() {
        return data.getData();
    }

    /**
     *
     * @return information about game
     */
    public String getInfo() {
        return info;
    }

    /**
     * start run thread with time
     */
    public void startTime() {
        blackTime.start();
        whiteTime.start();

        blackTime.suspend();
        whiteTime.suspend();
    }

    /**
     * this method firstly check if pawn can be en passant and if can, than will
     * set it
     *
     * @param figure is figure
     * @param toX X position on board, where figure will be move
     * @param toY Y position on board, where figure will be move
     */
    public void EnpassantFigure(String figure, int toX, int toY, int fromX) {
        //figure can be enpassat just one round
        if (figure.charAt(0) == enpassant.charAt(0)) {
            enpassant = "   ";
        }

        //set enpassant
        if (figure.charAt(1) == 'p') {
            if (figure.charAt(0) == 'w' && toX == 4 && fromX == 6) {
                enpassant = figure + toY;
            }
            if (figure.charAt(0) == 'b' && toX == 3 && fromX == 1) {
                enpassant = figure + toY;
            }
        }
    }

    /**
     * this method, from array list which contains moves of figure, which is
     * select, remove moves which are illegal, because player after this move
     * will get check
     *
     * @param fromX x position selected figure on board
     * @param fromY y position selected figure on board
     */
    public void movementCheckControl(int fromX, int fromY) {
        String[][] copy = new String[8][8];
        ArrayList<String> toRemove = new ArrayList();

        //duplicate board
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board.getBoard()[i], 0, copy[i], 0, 8);
        }


        try{
        allMovements.stream().map((move) -> {
            board.setField((int) move.charAt(0) - 48, (int) move.charAt(2) - 48, board.getField(fromX, fromY));
            return move;
        }).map((move) -> {
            board.setField(fromX, fromY, "  ");
            if (isCheck(onTurn)) {
                toRemove.add(move);
            }
            return move;
        }).forEachOrdered((_item) -> {
            for (int l = 0; l < 8; l++) {
                System.arraycopy(copy[l], 0, board.getBoard()[l], 0, 8);
            }
        });

        toRemove.forEach((remove) -> {
            allMovements.remove(remove);
        });
        }
        catch(Exception e){
            
        }
    }

    /**
     * change clock after player turn
     */
    public void clockChange() {
        //clock change   
        if (onTurn == 'w') {
            blackTime.suspend();
            whiteTime.resume();
        } else {
            whiteTime.suspend();
            blackTime.resume();
        }

        if (mate) {
            blackTime.stop();
            whiteTime.stop();
        }
    }

    /**
     * stop clock
     */
    public void killClock() {
        blackTime.stop();
        whiteTime.stop();
    }

    /**
     *
     * @return if is mate
     */
    public boolean isMate() {
        return mate;
    }

    /**
     * use for setting mate from outside this class
     *
     * @param mate boolean value if is check mate
     */
    public void setMate(boolean mate) {
        this.mate = mate;
    }

    public Clock getBlackClock() {
        return blackClock;
    }

    public Clock getWhiteClock() {
        return whiteClock;
    }

    public void setBlackClock(Clock blackClock) {
        this.blackClock = blackClock;
    }

    public void setBlackTime(Thread blackTime) {
        this.blackTime = blackTime;
        this.blackTime.setDaemon(true);
    }

    public void setWhiteClock(Clock whiteClock) {
        this.whiteClock = whiteClock;
    }

    public void setWhiteTime(Thread whiteTime) {
        this.whiteTime = whiteTime;
        this.whiteTime.setDaemon(true);
    }
    
    

    public void setAllMovements(ArrayList<String> allMovements) {
        this.allMovements = allMovements;
    }
    
    
    

}
