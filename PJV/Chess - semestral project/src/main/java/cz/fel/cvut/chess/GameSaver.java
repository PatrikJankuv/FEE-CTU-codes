/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess;

import cz.fel.cvut.chess.time.Clock;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author patrik
 */
public class GameSaver{
    private static Board board;
    private static BoardControl control;
    
    public GameSaver(Board board, BoardControl control) {
        this.board = board;
        this.control = control;
    }
    
    /**
     * save pgn game into file
     * @throws IOException
     */
    public void  saveGame() throws IOException{
        String[][] boardString = board.getBoard();
        

        try (FileWriter w = new FileWriter("src/main/resources/games/save.txt")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    w.write("[");
                    w.write(boardString[i][j]);
                    w.write("]");
                    if(j == 7){
                        w.write("\n");
                    }
            }
        }
            w.write("\nOn turn: ["+ control.getOnTurn()+"]");
            w.write("\nEn passant: [" + control.getEnpassant() + "]");
            w.write("\nBlack time: [" + control.getBlackClock().getTimeInSec() + "]");
            w.write("\nWhite time: [" + control.getWhiteClock().getTimeInSec() + "]");
            w.close();
            control.setInfo("game save");


        }
        catch(Exception ex){
            control.setInfo("game doesn't save\ntry again");
        }
      
    }
    
    /**
     * load game from file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadGame() throws FileNotFoundException, IOException{
        FileReader r = new FileReader("src/main/resources/games/save.txt");
        
            String figure = null;
            int c =  r.read();
            int i = 0;
            int j = 0;

            
            //read figures on board from begin
            while(i < 8){
                j = 0;
                while (j < 8) {
                    switch (c) {
                        case 91:
                            //91 in ascii [
                            figure = "";
                            break;
                        case 93:
                            board.setField(i, j, figure);
                            j++;
                            break;
                        default:
                            figure = figure + (char) c;
                            break;
                    }

                    c = r.read();
                }
                i++;
                }
            
            c = r.read();
            System.out.println((char)c);
            
            // set on changeOnTurn
            while(c != 91){
                 c = r.read();
            }
            
            if(c == 91){
                c = r.read();
                control.setOnTurn((char)c);
                }
            
            // set en passant 
            while(c != 91){
                 c = r.read();
            }
            
            //set en passant figure
            if(c == 91){
                c = r.read();
                String enpassant = "" + (char)c;
                c = r.read();
                enpassant = enpassant + (char)c;
                c = r.read();
                enpassant = enpassant + (char)c;
                c = r.read();
                control.setEnpassant(enpassant);
                }
            
            
            //set black clocks
            c = r.read();
            while(c != 91){
                 c = r.read();
            }
            
            if(c == 91){
                String time = "";
                while(c != 93){
                    c = r.read();
                    if(c != 93)
                       time = time + (char)c;
                }
                int timeInSec = Integer.parseInt(time);

                control.setBlackClock(new Clock(timeInSec));
                control.setBlackTime(new Thread(control.getBlackClock()));
               
            }
            
            //set white clocks
            c = r.read();
            while(c != 91){
                 c = r.read();
            }
            
            if(c == 91){
                String time = "";
                while(c != 93){
                 c = r.read();
                 if(c != 93)
                    time = time + (char)c;
                }
                 int timeInSec = Integer.parseInt(time);

                control.setWhiteClock(new Clock(timeInSec));
                control.setWhiteTime(new Thread(control.getWhiteClock()));
                
            }
           
            control.startTime();
            r.close();
            }
    
    /**
     * use for load from file edit by player
     * @param file from fileChooser
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadBoard(File file) throws FileNotFoundException, IOException{
            //prevention agains wrong file, file should have 264 bits
            if(file.length() != 264){
                control.setInfo("Wrong file\nformat");
                return;
            }
            
            FileReader r = new FileReader(file);
        
            String figure = null;
            int c =  r.read();
            int i = 0;
            int j = 0;
            
            //read figures on board
            while(i < 8){
                j = 0;
                while (j < 8) {
                    switch (c) {
                        case 91:
                            //91 in ascii [
                            figure = "";
                            break;
                        case 93:
                            board.setField(i, j, figure);
                            j++;
                            break;
                        default:
                            figure = figure + (char) c;
                            break;
                    }

                    c = r.read();
                }
                i++;
                }
            
           
            control.startTime();
            r.close();

               
            }
        
        }

    

