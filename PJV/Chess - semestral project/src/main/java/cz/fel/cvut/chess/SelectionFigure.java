/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

/**
 *
 * @author patrik
 * use for transport data between windows
 */

public class SelectionFigure {
    private String data;

    /**
     * 
     */
    public SelectionFigure() {
    }

    /**
     *
     * @return figure value
     */
    public String getData() {
        return data;
    }

    /**
     *
     * @param data set figure value as data
     */
    public void setData(String data) {
        this.data = data;
    }
    
}
