/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess.figures;

import java.util.ArrayList;

/**
 *
 * @author patrik
 * this interface has all basic properties of figure
 */
public interface figure {

    /**
     *
     * @param x position x on board
     * @param y position y on board
     * @param color of figure
     * @return array list of all possible moves 
     */
    public ArrayList moves(int x, int y, char color);

/**
     *
     * @param x position x on board
     * @param y position y on board
     * @param history is previous field in this direction 
     * @param color of figure
     * @return array list of all possible moves 
     */
    public int isValid(int x, int y, int history, char color);
 }
