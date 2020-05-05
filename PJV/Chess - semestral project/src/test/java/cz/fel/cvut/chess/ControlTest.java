/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author patrik
 */
public class ControlTest {
    
    public ControlTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getOnTurn method, of class BoardControl.
     */
    @Test
    public void testGetOnTurn() {
        System.out.println("getOnTurn");
        Board board = new Board();

        BoardControl instance = new BoardControl(board);
        char expResult = 'w';
        char result = instance.getOnTurn();
        assertEquals(expResult, result);

    }


    /**
     * Test of characterMoves method, of class BoardControl.
     */
    @Test
    public void testCharacterMoves() {
        System.out.println("characterMoves");
        int x = 0;
        int y = 0;
        Board board = new Board();
        BoardControl instance = new BoardControl(board);
        
        //root test
        board.generateNewGame();
        instance.characterMoves(x, y, board);
        ArrayList<String> expResult = new ArrayList();
        ArrayList<String> result = instance.getAllMovements();
        assertEquals(expResult, result);
        
        
        //knight test
        expResult.add("2 2");
        expResult.add("2 0");              
        instance.characterMoves(0, 1, board);
        result = instance.getAllMovements();

        assertEquals(expResult, result);


    }


    /**
     * Test of casting method, of class BoardControl.
     */
    @Test
    public void testCasting() {
        System.out.println("casting");
  
        Board board = new Board();
        board.generateNewGame();
        BoardControl instance = new BoardControl(board);
        
        board.setField(0, 6, "  ");
        board.setField(0, 5, "  ");

        instance.casting(0, 6, 0, 4);
        String result1 = board.getField(0, 5);
        String expResult1 = "br";
        assertEquals(expResult1, result1);
        
        String result2 = board.getField(0, 6);
        String expResult2 = "bk";
        assertEquals(expResult2, result2);
        
        String result3 = board.getField(0, 4);
        String expResult3 = "  ";
        assertEquals(expResult3, result3);
      
       
    }


    /**
     * Test of changeOnTurn method, of class BoardControl.
     */
    @Test
    public void testChangeOnTurn() {
        System.out.println("turn");
 
        Board board = new Board();
        board.generateNewGame();
        BoardControl instance = new BoardControl(board);
        
        instance.changeOnTurn();
        char result = instance.getOnTurn();
        char expResult = 'b';
        
        assertEquals(expResult, result);
        
    }

    /**
     * Test of isCheck method, of class BoardControl.
     */
    @Test
    public void testIsCheck() {
        System.out.println("isCheck");
        
        char color = 'b';
        char color2 = 'w';
        
        Board board = new Board();
        board.generateNewGame();
        BoardControl instance = new BoardControl(board);
        
        boolean expResult = false;
        boolean result = instance.isCheck(color);
        assertEquals(expResult, result);
        
        boolean expResult2 = false;
        boolean result2 = instance.isCheck(color2);
        assertEquals(expResult2, result2);
        
        board.setField(5, 4, "bk");
        board.setField(0, 4, "  ");
        board.setField(4, 4, "wq");
        board.setField(7, 3, "bk");
        
        boolean expResult3 = true;
        boolean result3 = instance.isCheck(color);
        assertEquals(expResult3, result3);

    }

    /**
     * Test of opColor method, of class BoardControl.
     */
    @Test
    public void testOpColor() {
        System.out.println("opColor");
        char color = 'w';
        Board board = new Board();
        BoardControl instance = new BoardControl(board);
        char expResult = 'b';
        char result = instance.opColor(color);
        assertEquals(expResult, result);
        
        
        char result2 = instance.opColor(expResult);
        assertEquals(color, result2);
    }

    /**
     * Test of movement method, of class BoardControl.
     */
    @Test
    public void testMovement() {
        System.out.println("movement");

        Board board = new Board();
        board.generateNewGame();
        BoardControl instance = new BoardControl(board);
        
        int fromX = 6;
        int fromY = 2;
        String figure = "wp";
        int toX = 4;
        int toY = 2;

        boolean expResult = true;
        boolean result = instance.movement(board, fromX, fromY, figure, toX, toY);
        assertEquals(expResult, result);
    }


    /**
     * Test of removeEnpassantFigure method, of class BoardControl.
     */
    @Test
    public void testRemoveEnpassant() {
        System.out.println("makeEnpassant");
        
        Board board = new Board();
        board.generateNewGame();
        BoardControl instance = new BoardControl(board);
        
        instance.setEnpassant("wp4");
        board.printBoard();
        instance.removeEnpassantFigure();

        String result = board.getField(4, 4);
        String expResult = "  ";
        
        assertEquals(expResult, result);
    }


    /**
     * Test of EnpassantFigure method, of class BoardControl.
     */
    @Test
    public void testEnpassantFigure() {
        System.out.println("EnpassantFigure");
        String figure = "bp";
        int toX = 3;
        int toY = 3;
        int fromX = 1;
        
        Board board = new Board();
        board.generateNewGame();
        BoardControl instance = new BoardControl(board);
        
        instance.EnpassantFigure(figure, toX, toY, fromX);
        String result = instance.getEnpassant();
        String expResult = "bp3";
                
        assertEquals(expResult, result);
    }
    
}
