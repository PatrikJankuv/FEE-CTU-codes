/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import java.util.ArrayList;

/**
 *
 * @author patrik
 */
public class PGNDecoder {
    private GameManager gameManager;
    private final PGN pgn;
    private final Board board;
    private final BoardControl control;


    public PGNDecoder(GameManager gameManager, PGN pgn, Board board, BoardControl control) {
        this.gameManager = gameManager;
        this.pgn = pgn;
        this.board = board;
        this.control = control;
    }
    
    public void decodePGN() {
        ArrayList<String> positionsOfFigure = new ArrayList<>();
        String pgnMove = pgn.getPgnMove();

        String fig = pgn.getFigureFromPGNMove(pgnMove);
        fig = control.getOnTurn() + fig;
        
        if (pgnMove.equals("0-0-0")) {
            if (control.getOnTurn() == 'w') {
                gameManager.manageGame(7, 4);
                gameManager.manageGame(7, 2);
            } else if (control.getOnTurn() == 'b') {
                gameManager.manageGame(0, 4);
                gameManager.manageGame(0, 2);
            }
        } else if (pgnMove.equals("0-0")) {
            if (control.getOnTurn() == 'w') {
                gameManager.manageGame(7, 4);
                gameManager.manageGame(7, 6);
            } else if (control.getOnTurn() == 'b') {
                gameManager.manageGame(0, 4);
                gameManager.manageGame(0, 6);
            }
        }
        
        if (pgnMove.length() == 2) {
            pawnPNGMove(positionsOfFigure, fig, pgnMove);
        } 
        else if (pgnMove.contains("=")) {
            promotionDecode(positionsOfFigure, fig, pgnMove);
        } //if just one figure on board or two but can't rich same field
        else {

            //if moveWriteToPGN has 5 chars
            //exp "Raxc3"
            //exp "axc3#"
            //exp "0-0-0"
            if (pgnMove.length() == 5) {
                StringBuilder sb = new StringBuilder(pgnMove);

                if (pgnMove.charAt(2) == 'x') {
                    sb.deleteCharAt(2);
                } else if (pgnMove.charAt(1) == 'x') {
                    sb.deleteCharAt(1);
                }

                pgnMove = sb.toString();

            }

            //exp "Raxc3#"
            if (pgnMove.length() == 6) {
                StringBuilder sb = new StringBuilder(pgnMove);
                //remove '+' or '#'
                sb.deleteCharAt(5);
                //remove 'x'
                sb.deleteCharAt(2);
                pgnMove = sb.toString();
            }

            // remove x from pgn moveWriteToPGN
            // exp "Rxc4"
            if (pgnMove.length() == 4) {
                // exp 'Nxc4'
                if (pgnMove.charAt(1) == 'x' && fig.charAt(1) != 'p') {
                    StringBuilder sb = new StringBuilder(pgnMove);
                    sb.deleteCharAt(1);
                    pgnMove = sb.toString();
                } //exp 'Rc6x'
                //remove '+' or '#'
                else if (pgnMove.charAt(3) == '+' || pgnMove.charAt(3) == '#') {
                    StringBuilder sb = new StringBuilder(pgnMove);
                    sb.deleteCharAt(3);
                    pgnMove = sb.toString();
                } //exp 'fxe3'
                else if (fig.charAt(1) == 'p') {
                    // exp 'fxe3'                    
                    pawnPGNMoveWhenSelectedOnFirstChar(positionsOfFigure, fig, pgnMove);
                } // 'Bac4' need find figure first and then can moveWriteToPGN
                else {
                    firstFindSecondMoveWithoutEject(positionsOfFigure, fig, pgnMove);
                }

            }

            //exp "f6+" for pawn
            if (pgnMove.charAt(2) == '+' || pgnMove.charAt(2) == '#') {
                StringBuilder sb = new StringBuilder(pgnMove);
                sb.deleteCharAt(2);
                pgnMove = sb.toString();
                pawnPNGMove(positionsOfFigure, fig, pgnMove);
            }

            positionsOfFigure = board.getPositionsOfFigure(fig);

            char column = pgn.convertColumnToFieldY(pgnMove.charAt(1));
            char row = pgn.convertRowToFieldX(pgnMove.charAt(2));

            //one figure on board
            if (positionsOfFigure.size() == 1) {
                justOneFigure(positionsOfFigure, column, row);
            } //two same figures on board
            else if (positionsOfFigure.size() == 2) {
                twoSamaFigure(positionsOfFigure, column, row);

            }
        }
        control.killClock();
    }

    private void promotionDecode(ArrayList<String> positionsOfFigure, String fig, String pgnMove) {
        positionsOfFigure = board.getPositionsOfFigure(fig);

        if(pgnMove.contains("x")){
            StringBuilder gfg = new StringBuilder(pgnMove); 
            int indexOfX = gfg.indexOf("x");
            gfg.deleteCharAt(indexOfX);
            pgnMove = gfg.toString();
        }
        
        if (pgnMove.length() == 4) {
            positionsOfFigure = board.getPositionsOfFigure(fig);
            char column = pgn.convertColumnToFieldY(pgnMove.charAt(0));
            char row = pgn.convertRowToFieldX(pgnMove.charAt(1));
            
            int pomRow = 0;
            if(row == '7'){pomRow = 6;}
            if(row == '0'){pomRow = 1;}

            for (String position : positionsOfFigure) {
                //if length of moveWriteToPGN is 2, for pawn
                int fieldX = Character.getNumericValue(position.charAt(0));
                int fieldY = Character.getNumericValue(position.charAt(2));
                if (position.charAt(2) == column && fieldX == pomRow) {
                    
                    gameManager.manageGame(fieldX, fieldY);

                    board.setField(fieldX, fieldY, gameManager.getFigure().charAt(0)+""+Character.toLowerCase(pgnMove.charAt(3)));

                    fieldX = Character.getNumericValue(row);
                    gameManager.manageGame(fieldX, fieldY);
                }
            }
        }
        else{
            positionsOfFigure = board.getPositionsOfFigure(fig);
            char column = pgn.convertColumnToFieldY(pgnMove.charAt(0));
            char row = pgn.convertRowToFieldX(pgnMove.charAt(2));
            
            int pomRow = 0;
            if(row == '7'){pomRow = 6;}
            if(row == '0'){pomRow = 1;}
            
           
            for (String position : positionsOfFigure) {
                //if length of moveWriteToPGN is 2, for pawn
                int fieldX = Character.getNumericValue(position.charAt(0));
                int fieldY = Character.getNumericValue(position.charAt(2));
                if (position.charAt(2) == column && fieldX == pomRow) {
                    
                    gameManager.manageGame(fieldX, fieldY);

                    board.setField(fieldX, fieldY, gameManager.getFigure().charAt(0)+""+Character.toLowerCase(pgnMove.charAt(4)));
                    
                    column = pgn.convertColumnToFieldY(pgnMove.charAt(1));
                    row = pgn.convertRowToFieldX(pgnMove.charAt(2));
           
                    fieldY = Character.getNumericValue(column);
                    fieldX = Character.getNumericValue(row);

                    gameManager.manageGame(fieldX, fieldY);
                }
            }
        }
    }

    private void pawnPNGMove(ArrayList<String> positionsOfFigure, String fig, String pgnMove) {

        positionsOfFigure = board.getPositionsOfFigure(fig);
        char column = pgn.convertColumnToFieldY(pgnMove.charAt(0));
        
        for (String position : positionsOfFigure) {
            //if length of moveWriteToPGN is 2, for pawn
            if (position.charAt(2) == column) {
                int fieldX = Character.getNumericValue(position.charAt(0));
                int fieldY = Character.getNumericValue(position.charAt(2));

                gameManager.manageGame(fieldX, fieldY);

                char row = pgn.convertRowToFieldX(pgnMove.charAt(1));
                fieldX = Character.getNumericValue(row);
                
                gameManager.manageGame(fieldX, fieldY);
            }
        }
    }

    private void pawnPGNMoveWhenSelectedOnFirstChar(ArrayList<String> positionsOfFigure, String fig, String pgnMove) {
        positionsOfFigure = board.getPositionsOfFigure(fig);
        char column = pgn.convertColumnToFieldY(pgnMove.charAt(0));

        for (String position : positionsOfFigure) {
            //if length of moveWriteToPGN is 2, for pawn
            if (position.charAt(2) == column) {
                int fieldX = Character.getNumericValue(position.charAt(0));
                int fieldY = Character.getNumericValue(position.charAt(2));

                gameManager.manageGame(fieldX, fieldY);

                column = pgn.convertColumnToFieldY(pgnMove.charAt(2));
                fieldY = Character.getNumericValue(column);

                char row = pgn.convertRowToFieldX(pgnMove.charAt(3));
                fieldX = Character.getNumericValue(row);

                gameManager.manageGame(fieldX, fieldY);
            }
        }

    }

    private void firstFindSecondMoveWithoutEject(ArrayList<String> positionsOfFigure, String fig, String pgnMove) {
        char figFrom = pgnMove.charAt(1);
        int fromIsNumber = Character.getNumericValue(figFrom);

        if (fromIsNumber < 9) {

            positionsOfFigure = board.getPositionsOfFigure(fig);
            figFrom = pgn.convertRowToFieldX(figFrom);

            for (String position : positionsOfFigure) {
                //if length of moveWriteToPGN is 2, for pawn
                if (position.charAt(0) == figFrom) {
                    int fieldX = Character.getNumericValue(position.charAt(0));
                    int fieldY = Character.getNumericValue(position.charAt(2));

                    gameManager.manageGame(fieldX, fieldY);

                    char column = pgn.convertColumnToFieldY(pgnMove.charAt(2));
                    fieldY = Character.getNumericValue(column);

                    char row = pgn.convertRowToFieldX(pgnMove.charAt(3));
                    fieldX = Character.getNumericValue(row);

                    gameManager.manageGame(fieldX, fieldY);
                }
            }
        } else {
            positionsOfFigure = board.getPositionsOfFigure(fig);
            figFrom = pgn.convertColumnToFieldY(figFrom);

            for (String position : positionsOfFigure) {
                //if length of moveWriteToPGN is 2, for pawn
                if (position.charAt(2) == figFrom) {
                    int fieldX = Character.getNumericValue(position.charAt(0));
                    int fieldY = Character.getNumericValue(figFrom);

                    gameManager.manageGame(fieldX, fieldY);

                    char column = pgn.convertColumnToFieldY(pgnMove.charAt(2));
                    fieldY = Character.getNumericValue(column);

                    char row = pgn.convertRowToFieldX(pgnMove.charAt(3));
                    fieldX = Character.getNumericValue(row);

                    gameManager.manageGame(fieldX, fieldY);

                }
            }
        }
    }

    private void justOneFigure(ArrayList<String> positionsOfFigure, char column, char row) {
        int fieldX = Character.getNumericValue(positionsOfFigure.get(0).charAt(0));
        int fieldY = Character.getNumericValue(positionsOfFigure.get(0).charAt(2));

        gameManager.manageGame(fieldX, fieldY);

        fieldY = Character.getNumericValue(column);

        fieldX = Character.getNumericValue(row);

        gameManager.manageGame(fieldX, fieldY);

    }

    private void twoSamaFigure(ArrayList<String> positionsOfFigure, char column, char row) {
        for (String position : positionsOfFigure) {
            int fieldX = Character.getNumericValue(position.charAt(0));
            int fieldY = Character.getNumericValue(position.charAt(2));

            control.characterMoves(fieldX, fieldY, board);
            ArrayList<String> figMoves = control.getAllMovements();

            String fieldTo = row + " " + column;

            if (figMoves.contains(fieldTo)) {
                gameManager.manageGame(fieldX, fieldY);
                fieldY = Character.getNumericValue(column);

                fieldX = Character.getNumericValue(row);

                gameManager.manageGame(fieldX, fieldY);
            }
        }

        control.setAllMovements(null);

    }
    

    
}
