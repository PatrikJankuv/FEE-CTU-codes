package cz.cvut.fel.pjv;


public class BruteForceAttacker extends Thief {
    int[] numPass;
    char[] chars;
    char[] pass;
    boolean opened = false;

    @Override
    public void breakPassword(int sizeOfPassword) {
        
        this.numPass = new int[sizeOfPassword];
        this.pass = new char[sizeOfPassword];
        
        int numOfchars = getCharacters().length;
        this.chars = new char[numOfchars];
        System.out.println(chars); 
                
        if (sizeOfPassword == 0){
            tryOpen(pass);
        }
        else if (chars.length == 1)
            convertAndTry();
        else{ 
            try {
                tryUntilEnd();
            } 
            catch (RuntimeException e) {
                }
            }
            
    }
   
    public void tryUntilEnd(){
        while(!opened){
                convertAndTry();
                hesloLamac(0);
        }
    }
    

   private void hesloLamac(int indexOf){
                               
            this.numPass[indexOf]++;

            if (this.numPass[indexOf] >= chars.length){
                this.numPass[indexOf++] = 0;
                hesloLamac(indexOf);
               }       
   }
   
   
   public boolean convertAndTry(){
       for (int i = 0; i < this.pass.length; i++){
           this.pass[i] = getCharacters()[this.numPass[i]];
       }
       
       this.opened = tryOpen(pass);
       return this.opened;
   }
    
}
