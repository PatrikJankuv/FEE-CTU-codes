/*
 * File name: Lab06.java
 * Date:      2014/08/26 21:39
 * Author:    @author
 */

package cz.cvut.fel.pjv;

import java.util.Scanner;

public class Lab02 {
    Scanner sc = new Scanner(System.in);
    double[] numbers = new double[10];
    int line = 0;
    int indexOfNum = 0;

  
   public void start(String[] args) {
           homework();
   }
   
   public void homework(){
       
       while(sc.hasNextLine()){
        this.line++;
               
        String tmp = sc.nextLine();

        try{
            numbers[indexOfNum] = Double.parseDouble(tmp);
            indexOfNum++;}
        catch(NumberFormatException e){
            System.err.println("A number has not been parsed from line " + line);
        }
        
        
        
        if (indexOfNum >= 10){
            calculate(10);
            indexOfNum = 0;
        }
        
       }
       
       System.err.println("End of input detected!");

       if (indexOfNum > 1) {
           calculate(indexOfNum);
           //System.exit(0);
        }
   }
   
   private void print(double mean, double sd, int len){
        System.out.println(String.format("%2d %.3f %.3f", len, mean, sd));
   }
   
   private boolean calculate(int len){
       double mean = calcMean(len);
       double sd = calcSd(len, mean);

       print(mean, sd, len);
       return true;
   }
   
   private double calcMean(int len){
       double sum = 0;
       
       for (int i = 0; i < len; i++)
           sum += numbers[i];
       
       return sum/len;
   }
   
   
   private double calcSd(int len, double mean) {
        double sd = 0;

        for (int i = 0; i < len; i++) {
            sd += Math.pow(numbers[i] - mean, 2);
        }

        return Math.sqrt(sd / len);
    }
}

/* end of Lab02.java */
