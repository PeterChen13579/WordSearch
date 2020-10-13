/*
 * WordSearch1.java
 * Created by Peter Chen
 * Created on Sep.26.2018
 * Version 9.0
 * This program accepts a (.txt) file from the user. The (.txt) file should include random words.This
 program will ask the user for a number(n) to randomly generate a word search (n*n) using the random
 words in the text file. If the number the user inputed is too small, this program will throw an error.
 */

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Random;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class WordSearch{
  public static void main(String [] args){
    boolean checkSearch = true;
    int checkLength= 0;
    int numWord =0;
    int wordLengths =0;  //checks total length of strings in textfile. if the total exceeds the grid size, System will throw error.
    Random rand = new Random();
    
    //Create Scanner for user input
    Scanner input = new Scanner(System.in);
    
    //Asks user for their file name and number(n*n) size they want to generate the word search by
    System.out.println("Please enter your text file.");
    String fileName = input.nextLine();
    System.out.println("Please enter the size(n*n) of word search you want by.");
    int sizeGrid = input.nextInt();
    input.close();
    
    //Creates file
    File file = new File (fileName);
    
    try{
      Scanner enter = new Scanner(file);
      //has a num(variable) to determine the # of words the user has in the file and initializes an array;
      while(enter.hasNextLine() && enter.nextLine() != null){
        numWord++;
      }
      enter.close();
      
      //initiate string array based off the # obtained from scanning
      String [] words = new String[numWord];
      
      /* stores all words into the string array. Has a variable(checkLength) to obtain the longest string integer. If
       * the integer the user entered is less than (checkLength) the system will throw an error. Also have a variable that
       * keeps track of total string lengths. If total string length exceeds the size grid the user created, the program will
       * also throw an error.
       */
      
      Scanner read = new Scanner(file);
      
      for(int a=0;a<numWord;a++){
        words[a] = read.nextLine();
        if (a == 0){
          checkLength = words[0].length();
        }
        wordLengths = words[a].length() + wordLengths;
        if (checkLength < words[a].length()){
          checkLength = words[a].length();
        }
      }
      
      //used to check if total wordLength exceeds sizeGrid
      int checkValid = sizeGrid * sizeGrid;
      read.close();
      
      if (checkLength > sizeGrid || wordLengths > checkValid){
        System.out.println("The wordsearch can not be created.");
        System.out.println("You entered a lower integer number than word(s) length itself.");
        System.out.println("Or you have too many words in the textfile.");
        System.out.println("Please try again.");
        checkSearch = false;
      }
      
      //Initialize char array
      char [][] wordSearch = new char [sizeGrid][sizeGrid];
      
      /* First sort the String array in terms of String length (decending order) Program will then place the words into
       * char array starting with the longest words (bubble sorts)
       */
      
      if (checkSearch != false){
        for(int w=1;w<numWord;w++){
          String temp = words[w];
          int i= w-1;
          while(i >= 0 && temp.length() >words[i].length()){
            words[i+1] = words[i];
            i--;
          }
          words[i+1] = temp;
        }
        
        /* If the word is considered a 'long' word. (From lengths (gridSize-2 to grideSize)) the program will place the
         * word horizontally on the top or bottom to make space for the other words. Only non-long words will be placed
         * vertically or diagonally.
         */
              int wordTries = 0;  
        for(int i=0; i<numWord;i++){
          if (words[i].length() >= sizeGrid -2){
            int randDirection = getDirection();
            wordTries =0;
            horizontalWord(sizeGrid, randDirection, words[i], wordSearch, wordTries);
          }
          
          /* generate a random number to determine how to place the word(waysWords)
           * generate a random number '1' or '2' to determine if word is placed forwards or backwards (randDirection)
           */
          if (words[i].length() < sizeGrid-2){
            wordTries = 0;
            int randDirection = getDirection();
            int waysWords = getDirection();
            if (words[i].length() <= sizeGrid/2){
              int verify = rand.nextInt(5) +1;
              if (verify == 1 || verify == 2){
                diagonalWord(sizeGrid,randDirection, words[i], wordSearch, wordTries);
                waysWords = 3;
              }
              if (verify == 3 || verify == 4 || verify == 5){
                checkOverlap(sizeGrid, words[i], wordSearch, wordTries);
                waysWords = 3;
              }
            }
            if (waysWords == 1){
              horizontalWord(sizeGrid, randDirection, words[i], wordSearch, wordTries);
            }
            if (waysWords == 2){
              verticalWord(sizeGrid,randDirection, words[i], wordSearch, wordTries);
            }
          }
        }
        
        //fills in the rest of the 2D character array with random letters
        for (int q=0;q<sizeGrid;q++){
          for (int p=0;p<sizeGrid;p++){
            if (wordSearch[q][p] == 0){
              int tempNum = rand.nextInt(26) + 97;
              wordSearch[q][p] = (char) tempNum;
            }
          }
        }
        
        //starting to write html file;
        FileWriter fw = new FileWriter("wordSearch.html");
        PrintWriter html = new PrintWriter(fw);
        
        /* Prints out the word search solution in a html file with title on the top, 
         * words on the left, and word search on the right
         */
        html.println("<html>");      
        html.println("<head>");
        
        //CSS
        html.println("<style type = \"text/css\">");
        html.println("table {");
        html.println("border: 2px solid #000000;");
        html.println("}");
        //Body
        html.println("body {");
        html.println("background-color: silver;");
        html.println("color: black;");
        html.println("padding: 12px;");
        html.println("text-allign: center");
        html.println("font-family: Arial, Verdana, sans-serif;}");
        //Title
        html.println("title1 {");
        html.println("background-color: silver;");
        html.println("color: red;");
        html.println("font-size: 30px;}");
        //Words
        html.println("h1 {");
        html.println("background-color: silver;");
        html.println("font-size: 20px;");
        html.println("color: yellow;");
        html.println("padding: inherit;}");
        //Letters
        html.println("p {");
        html.println("text-allign: center;");
        html.println("font-family: Times New Roman, Times, serif;");
        html.println("border: 2px solid #000000;");
        html.println("padding: 3px;");
        html.println("margin: 0px;}");

        html.println("</style>");        
        html.println("</head>");       
        html.println("<body>");        
        html.println("<title1 style = \"float:center\">Word Search!</title1>");
        html.println("<h1 style= \"float:left\">");
        for (int i = 0; i < words.length; i++) {
          html.print(words[i] + "<br />");
        }
        html.println("</h1>");
        html.println("</body>");
        html.println("<table style= \"float:center\">");
        for (int i = 0; i < sizeGrid; i++) {
          html.println("<tr>");
          for (int j = 0; j < sizeGrid; j++) {
            html.print("<td><p>" + wordSearch[i][j] + "</p></td>");
          }
          
          html.println("</tr>");
        }
        html.println("</table>");
        html.println("</html>");
        
        html.close();
        //-----------------------------------------------------------
        
        
        //prints to .txt file from the user
        PrintWriter output = new PrintWriter(new FileOutputStream((fileName), true));
        output.println();
        for(int i=0; i<sizeGrid;i++){
          for (int w=0; w<sizeGrid;w++){
            output.print(wordSearch[i][w]);
            if (w != sizeGrid -1){
              output.print(" ");
            }
          }
          output.println();
        }
        System.out.println("A word search has been created and printed in your text file.");
        System.out.println("An HTML file has been created containing the word search and list of words.");
        output.close();
      }
      input.close();
    }catch(IOException ex){
      System.out.println("ERROR");
    } //end of try{catch}
  }
  
  /**
   * getDirection
   * This method generates number '1' or '2' to determine which way to place the words
   * method returns a number
   * @param (nothing)
   * @returns a randomly generated number 1 or 2
   */
  public static int getDirection(){
    Random rand = new Random();
    int r =rand.nextInt(2) +1;
    return r;
  }
  
  /**
   * horizontalWord
   * This method searches the 2D array for a place to place a horizontal word into the 2D character array
   * If it can not be placed, the method will go into the 'verticalWord' method to find a place
   * @The 2D character array holds information of all the words from the textfile. Randomly generated
   * @param takes in gridsize, word, and 2D character array. Will try to place word into the 2D array.
   * horizontally, vertically, or diagonally.
   */
  public static void horizontalWord(int sizeGrid, int wordDirection, String word, char[][] wordSearch, int wordTries){
    int wordLength = word.length();
    Random rand = new Random();
    boolean placeWord = false;
    
    //if there is no place to place a word (very unlikly to happen)
    if (wordTries > 999){
      System.out.println(word + " can not be placed into the word search. Try deleting some words or have a bigger grid size.");
    }
    // if the word is considered a 'long' word (Size ranges from gridLength-2 to gridLength)    
    if (wordLength >= sizeGrid- 2 && wordTries < 999){
      int upDown = rand.nextInt(2) + 1;
      
      //upDown is used to determine if word is to be placed starting at top or bottom
      //'1' will be placing word from top. '2' will be placing word from bottom
      if (upDown == 1){
        
        for (int a=0;a<sizeGrid;a++){ //searches from up to down (rows) to find empty space
          if (wordSearch [a][0] == 0){
            
            if (wordDirection == 1){
              for (int b=0;b<wordLength;b++){
                wordSearch[a][b] = word.charAt(b);  //prints word into the char 2D array
                placeWord = true;
              }
            }
            if (wordDirection == 2){
              int w = 0;
              for (int b=wordLength-1;b>=0;b--){
                wordSearch[a][b] = word.charAt(w);  //prints word into the char 2D array
                w++;
                placeWord = true;
              }
            }
            a = 99999;
          }
        }
      }
      
      if (upDown == 2){
        
        for (int a=sizeGrid-1;a>0;a--){ //searches from down to up (rows) to find empty space
          if(wordSearch [a][0] == 0){
            
            if (wordDirection == 1){
              for (int b=0;b<wordLength;b++){
                wordSearch[a][b] = word.charAt(b);  //prints word into the char 2D array
                placeWord = true;
              }
            }
            if (wordDirection == 2){
              int w =0;
              for (int b=wordLength-1;b>=0;b--){
                wordSearch[a][b] = word.charAt(w);  //prints word into the char 2D array
                w++;
                placeWord = true;
              }
            }
            a = -1;
          }
        }
      }
      //if there is no space to place word, runs into 'verticalWord' method
      if (placeWord == false){
        verticalWord(sizeGrid, wordDirection, word, wordSearch, wordTries);
      }
    }
    
    /* This integer is used to keep track the number of times the program searches to place word Horizontally
     * If it goes over 8,it will go into 'verticalWord' method
     */
    int tryTimes = 0;
    
    //Search entire array for a place to place the word.
    if (wordLength < sizeGrid -2 && wordTries < 999){
      while((placeWord == false) && (tryTimes <8)){
        tryTimes++;
        int findRow = rand.nextInt (sizeGrid) + 0;
        int findSpace = rand.nextInt (sizeGrid-wordLength+1) + 0;
        int checkSpaces = 0;
        
        //check to see if there are empty spaces in the row
        for (int a =findSpace;a<findSpace+wordLength;a++){
          
          if (wordSearch[findRow][a] == 0){
            checkSpaces++;
          }
          
          if (checkSpaces == wordLength){
            if (wordDirection == 1){  //placing word direction forwards
              int w =0;
              a = findSpace;
              for(int c =a;c<a+wordLength;c++){
                wordSearch[findRow][c] = word.charAt(w);
                w++;
              }
              a = 99999;
              placeWord = true;
            }
            if (wordDirection == 2){   //placing word direction backwards
              int w =0;
              a = findSpace;
              for (int c =a+wordLength-1;c>=a;c--){
                wordSearch[findRow][c] = word.charAt(w);
                w++;
              }
              placeWord = true;
              a = 99999;
            }
          }
        }
      }
      
      if (tryTimes == 8 && placeWord == false){
        wordTries = wordTries +8;
        verticalWord(sizeGrid, wordDirection, word, wordSearch, wordTries);
      }
    }
  }   //end of horizontal method
   
  /**
   * verticalWord
   * This method searches the 2D character array for a place to place the word vertically into the 2D array
   * If it can not be placed, the method will go into the 'horizontalWord' method to find a place
   * @param takes in gridsize, word, and 2D character array. Will try to place word into the 2D array.
   * @The 2D character array holds information of all the words from the textfile. Randomly generated
   * horizontally, vertically, or diagonally.
   */
  public static void verticalWord(int sizeGrid, int wordDirection, String word, char[][] wordSearch, int wordTries){
    int wordLength = word.length();
    Random rand = new Random();
    
    /*This integer is used to keep track the number of times the program searches for
     empty spaces to place word. If it goes over 8 times, the program will end search (Vertically)
     and search in other directions. (ie.diagonally, Horizontally etc.) */
    int tryTimes = 0;
    
    /*Searches array to place word horizontally. Horizontal words will only occur for near the right and left sides
     of the word search, not centre. */
    int horizontalPlace = sizeGrid /4;
    boolean placeWord = false; //used to see if word is placed
    
    //Starting to place word horizontally
    while(placeWord == false && tryTimes <8 && wordTries < 999){
      int rightLeft = rand.nextInt(2) +1; // determine to check spot left or right
      int findColumn = 0;
      int checkSpaces = 0;
      tryTimes++;
      int findPlace = rand.nextInt (sizeGrid-wordLength+1) + 0;
      
      //check left side for space to place word horizontally
      if (rightLeft == 1){
        findColumn = rand.nextInt(horizontalPlace+1) + 0;
      }
      if(rightLeft ==2){
        findColumn = rand.nextInt(sizeGrid+1-(sizeGrid-horizontalPlace)) + (sizeGrid-horizontalPlace-1);
      }
      
      for (int a =findPlace;a<findPlace+wordLength;a++){
        if (wordSearch[a][findColumn] == 0){
          checkSpaces++;
        }
        
        if (checkSpaces == wordLength){
          if (wordDirection == 1){  //placing word direction forwards
            int w =0;
            a = findPlace;
            for(int c =a;c<a+wordLength;c++){
              wordSearch[c][findColumn] = word.charAt(w);
              w++;
            }
            a = 99999;
            placeWord = true;
          }
          if (wordDirection == 2){   //placing word direction backwards
            int w =0;
            a = findPlace;
            for (int c =a+wordLength-1;c>=a;c--){
              wordSearch[c][findColumn] = word.charAt(w);
              w++;
            }
            a = 99999;
            placeWord = true;
          }
        }
      }
    }
    if (tryTimes == 8 && placeWord == false && wordTries < 999){
      wordTries = wordTries + 8;
      horizontalWord(sizeGrid, wordDirection, word, wordSearch, wordTries);
    }
  }   //end of placing word vertically method
  
  /**
   * diagonalWord
   * This method searches the 2D character array for a place to place the word diagonally into the 2D array
   * If it can not be placed, the method will go into the 'horizontalWord' method to find a place
   * @param takes in gridsize, wordDirection, word, and 2D character array. Will try to place word into the 2D array.
   * @The 2D character array holds information of all the words from the textfile. Randomly generated
   * horizontally, vertically, or diagonally.
   */
  public static void diagonalWord(int sizeGrid, int wordDirection, String word, char[][] wordSearch, int wordTries){
    int wordLength = word.length();
    Random rand = new Random();
    boolean placeWord = false;
    int tryTimes = 0;
    
    while(placeWord == false && tryTimes <8){
      
      /*diagonalDirection is used to determine how to place the word vertically, either from bottom left
       to top right, or bottom right to top left */
      int diagonalDirection = rand.nextInt(2) +1;
      int checkSpaces = 0;
      tryTimes++;
      
      //checks from bottom left to top right(diagonally)
      if (diagonalDirection == 1){
        //generates random coordinate, then checks diagonally for empty spaces
        int row = rand.nextInt (sizeGrid-wordLength)+(wordLength-1);
        int column = rand.nextInt (sizeGrid-wordLength)+(0);
        
        int w = column;
        for (int i=row; i >row-wordLength;i--){
          if(i < row){
            w++;
          }
          if (wordSearch[i][w] == 0){
            checkSpaces++;
          }
          
          if (checkSpaces == wordLength){
            if (wordDirection == 1){ //forward (word)
              int a = column;
              int c = 0;
              for (int b=row; b >row-wordLength;b--){
                wordSearch[b][a] = word.charAt(c);
                c++;
                a++;
              }
              placeWord = true;
              i = -1;
            }
            if (wordDirection == 2){  //backwards (word)
              int a = column;
              int c = wordLength -1;
              for (int b=row; b >row-wordLength;b--){
                wordSearch[b][a] = word.charAt(c);
                c--;
                a++;
              }
              placeWord = true;
              i = -1;
            }
          }
        }
      }
      
      //checks from bottom right to top left (diagonally)
      if (diagonalDirection == 2){
        int column = rand.nextInt (sizeGrid-wordLength)+(wordLength-1);
        int row = rand.nextInt (sizeGrid-wordLength)+(wordLength-1);
        int w = column;
        for (int i=row; i >row-wordLength;i--){
          if (i != row){
            w--;
          }
          if (wordSearch[i][w] == 0){
            checkSpaces++;
          }
          
          if (checkSpaces == wordLength){
            if (wordDirection == 1){
              int a = column;
              int c = 0;
              for (int b=row; b >row-wordLength;b--){
                wordSearch[b][a] = word.charAt(c);
                c++;
                a--;
              }
              placeWord = true;
              i = -1;
            }
            if (wordDirection == 2){
              int a = column;
              int c = wordLength-1;
              for (int b=row; b >row-wordLength;b--){
                wordSearch[b][a] = word.charAt(c);
                c--;
                a--;
              }
              placeWord = true;
              i = -1;
            }
          }
        }
      }
    }
    
    //if after 8 tries the program can not place a word diagonally, it will run the horizontalWord method
    if (tryTimes >= 8){
      wordTries = wordTries + 8;
      horizontalWord(sizeGrid, wordDirection, word, wordSearch, wordTries);
    }
    
  }  //end of placing word diagonally method
  
  /**
   * checkOverlap
   * This method checks if it is possible to overlap words. If it is, it will then place the word into the 2D character array
   * If it can not be placed, the method will go into 'horizontalWord' method to find a place to place the word
   * @param takes in gridsize, word, and 2D character array. Will try to place word into the 2D array.
   * @The 2D character array holds information of all the words from the textfile. Randomly generated
   * horizontally, vertically, or diagonally.
   */
  public static void checkOverlap(int sizeGrid, String word, char[][] wordSearch, int wordTries){
    int wordLength = word.length();
    char [] wordTemp = new char [wordLength];
    //convert 'word' into a char array
    for (int c=0;c<wordLength;c++){
      wordTemp[c] = word.charAt(c);
    }
    boolean placeWord = false;
    int tryTimes = 0;
    
    //checking the entire array if theres room to place word crossing another word
    while(placeWord == false && tryTimes == 0){
      for (int z=0;z<sizeGrid;z++){
        for (int y=0;y<sizeGrid;y++){
          for(int x=0;x<wordLength;x+=wordLength){
            if (wordSearch [z][y] == wordTemp[x]){
              if (z+1 == sizeGrid){
                if (x == 0 || x == wordLength-1){
                  int checkSpaces =0;
                  
                  //if bottom row has same letter as word
                  for (int w=z;w>z-wordLength;w--){
                    if (wordSearch[w][y] == 0){
                      checkSpaces++;
                    }
                  }
                  if (checkSpaces == wordLength -1){
                    //If there are enough spaces, the program will print the rest of word into 2D array
                    if (x==0){
                      int temp = 0;
                      for (int a=z;a>z-wordLength;a--){
                        wordSearch[a][y] = word.charAt(temp);
                        placeWord = true;
                        temp++;
                      }
                      z--;
                    }
                    if (x==wordLength-1){
                      int temp = wordLength-1;
                      for (int a=z;a<z-wordLength;a--){
                        wordSearch[a][y] = word.charAt(temp);
                        placeWord = true;
                        temp--;
                      }
                      z--;
                    }
                  }
                }
              }  //end of bottom row
              
              //Checks top row
              if (z == 0){
                //If there are enough spaces, the program will print the rest of word into 2D array
                if (x == 0 || x == wordLength-1){
                  int checkSpaces =0;
                  for (int w=z;w<wordLength;w++){
                    if (wordSearch[w][y] == 0){
                      checkSpaces++;
                    }
                  }
                  if (checkSpaces == wordLength -1){
                    if (x==0){
                      int temp = 0;
                      for (int a=z;a<wordLength;a++){
                        wordSearch[a][y] = word.charAt(temp);
                        placeWord = true;
                        temp++;
                      }
                      z++;
                    }
                    if (x==wordLength-1){
                      int temp = wordLength-1;
                      for (int a=z;a<wordLength;a++){
                        wordSearch[a][y] = word.charAt(temp);
                        placeWord = true;
                        temp--;
                      }
                      z++;
                    }
                  }
                }
              }  //end of top row
              
              //Checks left row
              if (y == 0){
                //If there are enough spaces, the program will print the rest of word into 2D array
                if (x == 0 || x == wordLength-1){
                  int checkSpaces =0;
                  for (int w=y;w<wordLength;w++){
                    if (wordSearch[z][w] == 0){
                      checkSpaces++;
                    }
                  }
                  if (checkSpaces == wordLength -1){
                    if (x==0){
                      int temp = 0;
                      for (int a=y;a<wordLength;a++){
                        wordSearch[z][a] = word.charAt(temp);
                        placeWord = true;
                        temp++;
                      }
                    }
                    if (x==wordLength-1){
                      int temp = wordLength-1;
                      for (int a=y;a<wordLength;a++){
                        wordSearch[z][a] = word.charAt(temp);
                        placeWord = true;
                        temp--;
                      }
                    }
                  }
                }
              } //end of left row check
              
              // checks from right side
              if (y+1 == sizeGrid){
                //If there are enough spaces, the program will print the rest of word into 2D array
                if (x == 0 || x == wordLength-1){
                  int checkSpaces =0;
                  for (int w=y;w>y-wordLength;w--){
                    
                    if (wordSearch[z][w] == 0){
                      checkSpaces++;
                    }
                  }
                  if (checkSpaces == wordLength -1){
                    if (x==0){
                      int temp = 0;
                      for (int a=y;a>y-wordLength;a--){
                        wordSearch[z][a] = word.charAt(temp);
                        placeWord = true;
                        temp++;
                      }
                    }
                    if (x==wordLength-1){
                      int temp = wordLength-1;
                      for (int a=y;a<y-wordLength;a--){
                        wordSearch[z][a] = word.charAt(temp);
                        placeWord = true;
                        temp--;
                      }
                    }
                  }
                }
              }
            } //end of check
          }
        }
      }
      if (placeWord == false){
        tryTimes = 1;
      }
    }
    
    // if can not find overlapping words
    if (placeWord == false){
      int randDirection = getDirection();
      horizontalWord(sizeGrid, randDirection, word, wordSearch, wordTries);
    }
  }  //end of check overLap method
}    //end of program