/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess.figures;

import cz.fel.cvut.chess.Board;
import java.util.ArrayList;
import static junit.framework.Assert.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author patrik
 */
public class FigureTest {
    
    public FigureTest() {
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
     * Test of moves method, of class Knight.
//     */
    @Test
    public void testKnightMoves() {
        System.out.println("moves");
        int x = 0;
        int y = 0;
        char color = ' ';
        Board board = new Board();
        board.generateNewGame();
        Knight kn = new Knight(0, 1, board);
        ArrayList<String> expResult = new ArrayList<>();
        
        expResult.add("2 1");
        expResult.add("1 2");
        
        ArrayList result = kn.moves(x, y, color);
        System.out.println(result);
        assertEquals(expResult, result);

    }
    
    /**
     * Test of moves method, of class King.
     */
    @Test
    public void testKingMoves() {
        System.out.println("moves");
        int x = 0;
        int y = 0;
        char color = ' ';
         
        Board board = new Board();
        board.generateNewGame();
        King king = new King(4, 4, board);
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add("5 5");
        expResult.add("5 3");
        expResult.add("3 5");
        expResult.add("3 3");
        expResult.add("5 4");
        expResult.add("4 3");
        expResult.add("3 4");
        expResult.add("4 5");
        
        ArrayList result = king.moves(4, 4, color);
        assertEquals(expResult, result);
    }
    
       /**
     * Test of moves method, of class Rook.
     */
    public void testRootMoves() {
        System.out.println("moves");
        Board board = new Board();
        board.generateNewGame();
        Rook rook = new Rook(0, 0, board);
        ArrayList<String> expResult = new ArrayList<>();
        
        ArrayList result = rook.moves(0, 0, 'b');
        
        System.out.println(result);
        assertEquals(expResult, result);
   }
    
           /**
     * Test of moves method, of class Rook.
     */
    public void testQueenMoves() {
        System.out.println("moves");
        Board board = new Board();
        board.generateNewGame();
        Queen queen = new Queen(0, 3, board);
        ArrayList<String> expResult = new ArrayList<>();
        
        ArrayList result = queen.moves(0, 0, 'b');
        
        System.out.println(result);
        assertEquals(expResult, result);
   }
    
}
