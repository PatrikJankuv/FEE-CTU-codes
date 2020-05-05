package cz.cvut.fel.pjv;

import java.util.Scanner;

public class Lab01 {
    Scanner sc = new Scanner(System.in);
   
   public void start(String[] args) {
       homework();
   }
   
   public void homework(){
       String operation = operationOption();
       operationDirection(operation);
   }
   
   public String operationOption(){
       System.out.println("Vyber operaci (1-soucet, 2-rozdil, 3-soucin, 4-podil):");
       return sc.next();
   }
   
   public void operationDirection(String operation){
       switch(operation){
           case "1":
               output(sum(), "+");
               break;
           case "2":
               output(difference(), "-");
               break;
           case "3":
               output(multi(), "*");
               break;
           case "4":
               output(div(), "/");
               break;
           default: 
               System.out.println("Chybna volba!");
            
       }
   } 

    private double[] sum() {
        double[] element = new double[3];
        System.out.println("Zadej scitanec: ");
        element[0] = sc.nextDouble();
        System.out.println("Zadej scitanec: ");
        element[1] = sc.nextDouble();
        
        element[2] = element[0] + element[1];
        
        return element;
    }

    private double[] difference() {
        double[] element = new double[3];
        System.out.println("Zadej mensenec: ");
        element[0] = sc.nextDouble();
        System.out.println("Zadej mensitel: ");
        element[1] = sc.nextDouble();
        
        element[2] = element[0] - element[1];
        
        return element;
    }

    private double[] multi() {
        double[] element = new double[3];
        System.out.println("Zadej cinitel: ");
        element[0] = sc.nextDouble();
        System.out.println("Zadej cinitel: ");
        element[1] = sc.nextDouble();
        
        element[2] = element[0] * element[1];
        
        return element;
    }

    private double[] div() {
        double[] element = new double[3];
        System.out.println("Zadej delenec: ");
        element[0] = sc.nextDouble();
        System.out.println("Zadej delitel: ");
        element[1] = sc.nextDouble();
        
        if (element[1] == 0){
            System.out.println("Pokus o deleni nulou!");
            System.exit(0);
        }
        
        element[2] = element[0] / element[1];
        
        return element;
    }

    private void output(double[] result, String operationSymbol) {
        System.out.println("Zadej pocet desetinnych mist: ");
        int tenthPlaces = sc.nextInt();
        String neviem = "%." + tenthPlaces + "f";
        if (tenthPlaces >= 0)
            System.out.println(String.format(neviem, result[0]) + " " + operationSymbol + " " + String.format(neviem, result[1]) + " = " + String.format(neviem, result[2]));
        else
            System.out.println("Chyba - musi byt zadane kladne cislo!");
    }
   
   
}