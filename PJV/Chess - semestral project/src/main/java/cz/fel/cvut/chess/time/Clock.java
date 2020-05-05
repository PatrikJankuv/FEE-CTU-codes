/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fel.cvut.chess.time;

/**
 *
 * @author patrik
 */
public class Clock implements Runnable{
    String time = "Time";
    int timeInSec;

    public Clock(int time) {
        timeInSec = time;
        int min = timeInSec / 60;
        int sec = timeInSec % 60;   
            
        this.time = String.format("%02d:%02d",min, sec);
    }
    
    
    
    public void run() {
        int min, sec;
            
        for (int i = timeInSec; i >= 0; i--) {
            min = i / 60;
            sec = i % 60;
            timeInSec--;
            
            
            time = String.format("%02d:%02d",min, sec);
            
            try {
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) {
            }
        }
        time = "No time";

    }

    public String getTime() {
        return time;
    }
    
    public int getTimeInSec() {
        return timeInSec;
    }

    public void setTimeInSec(int timeInSec) {
        this.timeInSec = timeInSec;
    }
    
}
