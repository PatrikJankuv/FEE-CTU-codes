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
public class BoardTest {
    
    public BoardTest() {
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
     * Test of getKing method, of class Board.
     */
    @Test
    public void testGetKing() {
        System.out.println("getKing");
        char color = 'b';
        Board instance = new Board();
        instance.generateNewGame();
        String expResult = "0 4";
        String result = instance.getKing(color);
        assertEquals(expResult, result);
        
        color = 'w';
        String expResult2 = "7 4";
        String result2 = instance.getKing(color);
        assertEquals(expResult2, result2);
        
    }
    
    /**
     * test method, which return positions of figure on the board
     */
    @Test
    public void testGetPositions(){
        Board board = new Board();
        board.generateNewGame();
        ArrayList <String> expResult = new ArrayList<>();
        ArrayList <String> result = new ArrayList<>();

        expResult.add("0 2");
        expResult.add("0 5");

        result = board.getPositionsOfFigure("bb");
        assertEquals(expResult, result);
        
        
        expResult.clear();
        result = board.getPositionsOfFigure("xx");
        assertEquals(expResult, result);

        expResult.add("7 3");

        result = board.getPositionsOfFigure("wq");
        assertEquals(expResult, result);
        
    }


    
}
