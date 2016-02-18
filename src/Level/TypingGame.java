package Level;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.awt.event.*;

import Main.MAIN;
/**
 *Typing game that is a miny game.
 */
public class TypingGame extends JPanel implements KeyListener
{   
   /**main for object*/
   private MAIN main;
   
   /**lists of all sentences*/
   private String[] sentences;
   /**the amount of sentences completed*/
   private int sentenceCount = 0;
   /**the current sentence*/
   private String currentSentence = "";
   
   /**background for typinggame level*/
   private Image background;
   /**scorescreen for typinggame level*/
   private Image scoreScreen;
   /**font used for level*/
   private Font font;
   
   /**loaded information for the level*/
   private int [] info = new int [4];
   /**the sizes for scorescreen as well as game*/
   private float [] sizes = new float [2];
   /**the textcolor for the typinggame level*/
   private Color textColor = Color.black;
   private Font scoreFont;
    
   private int diffTime;
   /**start time*/
   private long start;
   //private int completeSent = 0;
   private int finalScore = 0;
   //private int numbs[0] = 0;
   //private int correctChars = 0;
   /**total chars,% correct, compl. sent.,cpm,tc] refer to README TG INSTRUCTIONS*/
   private int [] numbs = {0,1,0,1,1};
   /**numbers for the win*/
   private int [] winNumbs = new int [5];
   /**keeps track of all the final scores*/
   private int [] finalScores = {-1,-1,-1,-1,-1};
   /**used to print out each score on score screen*/
   private String [] numbsNames = {"Total Characters","PercentCorrect","Completed Sentences","Chars per minute","Total Correct Chars"};
   /**keep a count of which score has been displayed*/
   private int scoreCount = 1;
   /**used for clock to keep counting down*/
   private Thread clockThread;
   /**time inbetween each repaint*/
   private int sleepTime = 100;
   
   /**tracks for if game is started*/
   private boolean started = false;
   /**tracks for if enter has been hit first*/
   private boolean firstHit = false;
   /**tracks if game has completed*/
   private boolean gameOver = false;
   /**image for typinginstructions*/
   private Image typingInstructions;
   /**
    *Sets the main equal to the main
    */
   public TypingGame(MAIN main)
   {
      this.main = main;
         
      addKeyListener(this);
      setFocusable(true);
      
      populateFields();
      getWords();
      startClockThread();
   }
   /**
    *@param Graphics Object
    */
   public void paint(Graphics g)
   {
      drawBackground(g);
      
      if(started)
      {
         drawWords(g);
         drawInfo(g);
      }
      
      if(gameOver && !started)
         scoreScreen(g);
   }
   /**
    *@param Graphics object
    *Draws two backgrounds
    *Begining screen for instructions and thn the background for the TG
    */
   private void drawBackground(Graphics g)
   {
      if (!started)
         g.drawImage(typingInstructions,0,0,this);
      else
         g.drawImage(background,0,0,this);
   }
   /**
    *@param Graphics object
    *Draws All of Score Scren
    *Stops clock after all scores have been drawn
    */
   private void scoreScreen(Graphics g)
   {
      sleepTime = 500;
      g.setColor(Color.white);
      g.setFont(scoreFont);
      g.drawImage(scoreScreen,0,0,this);
      int totalScore = 5000;
      int tempScore = 0;
      if(scoreCount <= finalScores.length)
      {
         for(int a = 0; a < scoreCount; a++)
         {
            g.setFont(scoreFont.deriveFont(32f));
            if(finalScores[a] == -1)
            {
               main.soundManager.playSound("TG Score Beginning");
               if(a == 1)
               {
                  tempScore = (((int)(((int)(totalScore/winNumbs.length))/winNumbs[a]))*(int)((((double)numbs[4]/(double)numbs[0])*100.0)));
                  if(tempScore > ((int)(totalScore/winNumbs.length)))
                     tempScore = ((int)(totalScore/winNumbs.length));
               }
               else if(a == 3)
               {
                  tempScore = (((int)(((int)(totalScore/winNumbs.length))/winNumbs[a]))*(int)((double)numbs[0]/(((double)diffTime/60.0))));
                  if(tempScore > ((int)(totalScore/winNumbs.length)))
                     tempScore = ((int)(totalScore/winNumbs.length));
               }
               else if(numbs[a] >= winNumbs[a])
                  tempScore = ((int)(totalScore/winNumbs.length));
               else if (numbs[a] < winNumbs[a])
                  tempScore = (((int)(((int)(totalScore/winNumbs.length))/winNumbs[a]))*(numbs[a]));
               finalScores[a] = tempScore;
            }
            g.drawString(numbsNames[a],1,63*(a+1));
            g.drawString(" = " + finalScores[a],375,63*(a+1));
            if(a == winNumbs.length-1 && finalScore == 0)
            {
               for(int b = 0; b < finalScores.length; b++)
                  finalScore += finalScores[b];
               main.soundManager.playSound("TG Final Score");
               g.drawString("           " + finalScore,200,49*(a+4));
               g.drawString("Press Enter to Continue",(main.totalWidth-g.getFontMetrics().stringWidth("Press ENTER to Continue"))/2,50*(a+6));
            }
         }
      }
      if(scoreCount <= finalScores.length)
         scoreCount++;
      if(scoreCount >= finalScores.length)
         sleepTime = 0;
   }
   /**
    *@param Graphics Object
    *Draws the word in the correct spot on ChalkBoard
    *Splits words up to fit on all three lines
    */
   private void drawWords(Graphics g)
   {  
      float f = sizes[0];
      font = font.deriveFont(f);
      g.setFont(font);
      g.setColor(textColor);
   
      if(gameOver)
         currentSentence = "Game is over PRESS ENTER TO CONTINUE to get score screen!";
      String [] full = currentSentence.split(" ");
      int count = 0;
      String firstLine = "";
      String secondLine = "";
      
      for(int i = 0; i < full.length; i++)
      {
         count += full[i].length()+1;
         if(count < 33)
            firstLine += full[i] + " ";
         else if(count < 60)
            secondLine += full[i] + " "; 
      } 
      g.drawString(firstLine,info[0],info[1]);
      g.drawString(secondLine,info[0],info[1]+65);
   }
   
   /**
    *@param Graphics Object
    *Draws the points and time in the correct spot
    */   
   private void drawInfo(Graphics g)
   {
      g.setFont(font);
      g.setColor(textColor);
      float f = sizes[1];
      font = font.deriveFont(f);
      g.setFont(font);
      if((int)(diffTime-((System.nanoTime()-start)/1000000000)) >= 0)
         g.drawString(("Time: " + (diffTime-((System.nanoTime()-start)/1000000000))),info[2],info[3]);
      else
         g.drawString("Time: 0",info[2],info[3]);
      if((int)(diffTime-((System.nanoTime()-start)/1000000000)) <= 0)
         gameOver = true;
   }
   /**
    *Loads all the information for the fields
    */
   private void populateFields()
   {
      //loads the sentences used for this TG
      try {
         InputStream in = getClass().getResourceAsStream("/Resources/TG/Sentences/" + main.currentLevel + ".txt");
         BufferedReader temp = new BufferedReader(new InputStreamReader(in));
         
         sentences = new String[Integer.parseInt(temp.readLine())];
         
         for(int a = 0; a < sentences.length; a++)
            sentences[a] = temp.readLine();
      } 
      catch(IOException e) {
         e.printStackTrace();
      }
      //loads opening instructions screen for TG
      try {     
         InputStream in = getClass().getResourceAsStream("/Resources/TG/Instructions/" + main.currentLevel + ".txt");
                           
         BufferedReader temp = new BufferedReader(new InputStreamReader(in));
      
         for(int a = 1; a <= 6; a++)
            switch(a)
            {
               case 1:
                  winNumbs[0] = Integer.parseInt(temp.readLine());
                  break;
               case 2:
                  winNumbs[1] = Integer.parseInt(temp.readLine());
                  break;
               case 3:
                  winNumbs[2] = Integer.parseInt(temp.readLine());
                  break;
               case 4:
                  winNumbs[3] = Integer.parseInt(temp.readLine());
                  break;
               case 5:
                  winNumbs[4] = Integer.parseInt(temp.readLine());
                  break;
               case 6:
                  diffTime = Integer.parseInt(temp.readLine());
                  break;
            }     
      }
      catch(IOException e){
         e.printStackTrace();
      }
      //loads Background for each level for TG
      try {     
         InputStream in = getClass().getResourceAsStream("/Resources/TG/Backgrounds/" + main.currentLevel + ".txt");
                           
         BufferedReader temp = new BufferedReader(new InputStreamReader(in));
      
         for(int a = 0; a < 7; a++)
         {
            switch(a)
            {
               case 0:
                  info[0] = Integer.parseInt(temp.readLine());
                  break;
               case 1:
                  info[1] = Integer.parseInt(temp.readLine());
                  break;
               case 2:
                  info[2] = Integer.parseInt(temp.readLine());
                  break;
               case 3:
                  info[3] = Integer.parseInt(temp.readLine());
                  break;
               case 4:
                  sizes[0] = Float.parseFloat(temp.readLine());
                  break;
               case 5:
                  sizes[1] = Float.parseFloat(temp.readLine());
                  break;
               case 6:
                  String [] tempC = temp.readLine().split(","); 
                  textColor = new Color(Integer.parseInt(tempC[0]),Integer.parseInt(tempC[1]),Integer.parseInt(tempC[2]));
                  break;
            }
         }     
      }
      catch(IOException e){
         e.printStackTrace();
      }
      //Loads background for TG     
      try {
         background = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/TG/Backgrounds/" + main.currentLevel + ".png"));
      }
      catch(IOException e){
         e.printStackTrace();
      }
      //Loads scoreScreen for TG     
      try {
         scoreScreen = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/TG/ScoreScreens/1.png"));
      }
      catch(IOException e){
         e.printStackTrace();
      }
      //loads opening instructions screen for TG
      try {
         typingInstructions = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/TG/Instructions/" + main.currentLevel + ".png"));
      }
      catch(IOException e){
         e.printStackTrace();
      }
      //loads the font used for the text
      try {
         font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Resources/TG/Fonts/" + main.currentLevel + ".ttf"));
      }
      catch(Exception e){
         e.printStackTrace();
      }
      //loads the font used for the scoreScreen
      try {
         scoreFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Resources/TG/ScoreScreens/score.ttf"));
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
   /**Randomely chooses a sentence and than repaints it
    *sets the new sentence to currentSentence
    */
   private void getWords()
   {
      Random r = new Random();
      int a = r.nextInt(sentences.length);
      currentSentence = sentences[a];
      repaint();
   }
   /**
    *@param e
    *Keeps track of all user entered.
    */
   public void keyTyped(KeyEvent e)
   {
      if(!gameOver && started)
      {
         main.soundManager.playSound("TG HitKey");
         //increments the total amount of inputed characters each time a key is hit during the game
         numbs[0]++;
         //if the correct key is hit, currenct sentence is shrinked by one char
         if(currentSentence.length() > 0 && e.getKeyChar() == currentSentence.charAt(0))
         {
            numbs[4]++;
            currentSentence = currentSentence.substring(1);
         }
         //if the sentence is completed a new one is added and the amount of sentences is incremented
         if(currentSentence.length() == 0)
         {
            numbs[2]++;
            getWords();
            //startWordThread();
         }
         repaint();
      }
   }
   /**
    *not utilized
    */   
   public void keyPressed(KeyEvent e) {}
   /**
    *@param KeyEvent for input characters
    *differentiates what to do with each input key
    */
   public void keyReleased(KeyEvent e) {
      //only uses input if the game is not over
      if(e.getKeyCode() == KeyEvent.VK_ENTER && firstHit)
      {
         if(gameOver && !started)
         {
            sleepTime = 0;
            main.masterScore += finalScore;
            main.changePanel(-2);
         }
         if(!started)
         {
            started = true;
            start = System.nanoTime();
         }
         if(gameOver && started)
            started = false;
         repaint();
      }
      firstHit = true;
   }   
   /**
    *Starts clock counter, repaints every 1/10 of a second
    *Used in score screen to output each score
    */
   public void startClockThread()
   {
      clockThread = 
         new Thread(){
            public void run()
            {
               while(true && sleepTime != 0)
               {                     
                  try
                  {
                     clockThread.sleep(sleepTime);
                     repaint();
                     if((int)(diffTime-((System.nanoTime()-start)/1000000000)) <= 5 && (int)(diffTime-((System.nanoTime()-start)/1000000000)) > 0)
                        main.soundManager.playSound("TG Clock");
                  }
                  catch(Exception e){
                     e.printStackTrace();
                  }
               }
            }
         };
      clockThread.start();
   }
}