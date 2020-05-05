/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


/**
 *
 * @author patrik
 */
public class PGNTest {
    
    public PGNTest() {
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
     * Test of convertRowToFieldX method, of class PGN.
     */
    @Test
    public void testConvertRowToFieldX() {
        System.out.println("convertRowToFieldX");
        
        PGN pgn = new PGN(null, null, null);
        
        char row = '1';
        char expResult = '7';
        char result = pgn.convertRowToFieldX(row);
        assertEquals(expResult, result);
        
        char row2 = '4';
        char expResult2 = '4';
        char result2 = pgn.convertRowToFieldX(row2);
        assertEquals(expResult2, result2);
        
        char row3 = '0';
        char expResult3 = 'x';
        char result3 = pgn.convertRowToFieldX(row3);
        assertEquals(expResult3, result3);

    }

    /**
     * Test of convertColumnToFieldY method, of class PGN.
     */
    @Test
    public void testConvertColumnToFieldY() {
        System.out.println("convertColumnToFieldY");
        
        PGN pgn = new PGN(null, null, null);
        
        char column = 'b';
        char expResult = '1';
        char result = pgn.convertColumnToFieldY(column);
        assertEquals(expResult, result);
        
        
        column = 'h';
        expResult = '7';
        result = pgn.convertColumnToFieldY(column);
        assertEquals(expResult, result);
        
        column = '2';
        expResult = 'x';
        result = pgn.convertColumnToFieldY(column);
        assertEquals(expResult, result);
      
    }
    
@Test
@ParameterizedTest
@CsvSource({ "Qh4, q", "Kh4, k"})
public void withNiceName(String word, char number) {
    System.out.println("aspon toto");
    PGN pgn = new PGN(null, null, null);
    assertEquals(word, pgn.getFigureFromPGNMove(word));
}

    /**
     * Test of getFigureFromPGNMove method, of class PGN.
     */
    @Test
    public void testFigureFromPGNMove() {
        System.out.println("getFigureFromPGNMove");
        
        PGN pgn = new PGN(null, null, null);
        
        String move = "Qh4";
        String expResult = "q";
        String result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "Qh4+";
        expResult = "q";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "0-0-0";
        expResult = "k";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "d4";
        expResult = "p";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "d4#";
        expResult = "p";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "Kh4";
        expResult = "k";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "Bxb2";
        expResult = "b";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
        
        move = "Rbxb1#";
        expResult = "r";
        result = pgn.getFigureFromPGNMove(move);
        assertEquals(expResult, result);
    }
    
}
