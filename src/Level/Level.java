package Level;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.Thread;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import Main.MAIN;
import Level.Entities.*;

public class Level extends JPanel implements KeyListener, Runnable {

   //2D array for all background images
   public static BackImage[][] imageB = new BackImage[MAIN.height][MAIN.width];
   //1D array for all foreground 
   public static Entity[] entities;
   private int block = MAIN.block;
   private int levelID;
   private MAIN main;
   private boolean up, down, left, right, action, quit;
   private boolean loadMiniGame = false;
   private boolean spaceCheck = false;
   
   private boolean answer = false;
   private String [] answers = {"HI!","Welcome to floor " + main.currentLevel,"Where you from","Do you think you will become a CEO?","Whats your favorite color?","How are you?","I like the color of your shirt!","Keep up the good work!","I heard you have been doing a great job!"};
   private int number = -1;
   
   private String [] choices = {"Cyber Security","Community Service","Word Proccessing"};
   private boolean [] way = {true,false,false};
   private int selection = 0;
   
   public boolean playedCS = false;
   public boolean playedComService = true;
   public boolean playedTG = true;
   
   private int transparency = 0;
   private Color transparencyColor = Color.black;
   
   private int repaintTimer = 1;
   
   public Level(MAIN main) {
      this.main = main;
      levelID = this.main.currentLevel;
      loadLevel(levelID);
      
      addKeyListener(this);
      setFocusable(true);
      
      Thread thread = new Thread(this);
      thread.start();
   }
   
   public void paint(Graphics g) 
   {
      
      if(quit)
         pixelate(g);
      else if(entities != null)
         populatePanel(g);
   }
   
   public void populatePanel(Graphics g) {
   
      //double for-loop draws all of the background images
      for(int x = 0; x < imageB[0].length; x++) {
      
         for(int y = 0; y < imageB.length; y++){
         
            g.drawImage(imageB[y][x].getImage(),x*block,y*block,this);
         }
      }
      //single for-loop draws all of the foreground entities
      
      for(int i = 0; i < entities.length; i++)
         g.drawImage(entities[i].getImage(),entities[i].getXLocation(),entities[i].getYLocation(),this);
      if(loadMiniGame)
      {
         g.setColor(new Color(0, 0, 0, 175));
         g.fillRect(0, 0,  MAIN.totalWidth, MAIN.totalHeight);
         g.setColor(Color.BLACK);
         g.setFont(g.getFont().deriveFont(54f));
      
         for(int i = 0; i < choices.length; i++)
         {
            if(way[i])
               g.setColor(Color.GREEN);
            else
               g.setColor(Color.WHITE);
            g.drawString(choices[i],(main.totalWidth-g.getFontMetrics().stringWidth(choices[i]))/2,(MAIN.totalHeight/3)+((MAIN.totalHeight/9)*i)+(MAIN.totalHeight/9));
            
         }
         
         g.setColor(Color.BLUE);
         if(playedCS && selection == 0)
         {
            g.drawString("Completed!",(main.totalWidth-g.getFontMetrics().stringWidth("Completed!"))/2,(MAIN.totalHeight/3)- 60+(MAIN.totalHeight/9));
            spaceCheck = false;
         } 
         if(playedComService && selection == 1)
         {
            g.drawString("Completed!",(main.totalWidth-g.getFontMetrics().stringWidth("Completed!"))/2,(MAIN.totalHeight/3)- 60+(MAIN.totalHeight/9));
            spaceCheck = false;
         }      
         if(playedTG && selection == 2)
         {
            g.drawString("Completed!",(main.totalWidth-g.getFontMetrics().stringWidth("Completed!"))/2,(MAIN.totalHeight/3)- 60+(MAIN.totalHeight/9));
            spaceCheck = false;
         }      
      }
      if(!loadMiniGame && answer)
      {
         g.setColor(Color.white);
         g.setFont(g.getFont().deriveFont(30f));
         if(number == -1)
            number = (int)(Math.random()*answers.length);
         if(number > -1)
            g.drawString(answers[number],(main.totalWidth-g.getFontMetrics().stringWidth(answers[number]))/2,(MAIN.totalHeight/3)- 60+(MAIN.totalHeight/9));
      }
      
   }
   
   private void pixelate(Graphics g)
   {
      repaintTimer = 30;
      if(transparency <= 255)
      {
         g.setColor(new Color(transparencyColor.getRed(),transparencyColor.getGreen(),transparencyColor.getBlue(),transparency));
         g.fillRect(0,0,main.totalWidth,main.totalHeight);
         transparency+=1;
      }
      if(transparency > 75)
      {
         g.setColor(Color.white);
         g.setFont(g.getFont().deriveFont(80f));
         g.drawString("Saving Data...",(main.totalWidth-g.getFontMetrics().stringWidth("Saving Data..."))/2,main.totalHeight/2);
      }
   }
   
   public void loadLevel(int ln) 
   {
      loadBackground(ln);
      loadEntities(ln);
   }
   
   public void loadBackground(int ln)
   {
      try 
      {
         InputStream in = getClass().getResourceAsStream("/Resources/LevelMaps/" + ln + ".txt");
                           
         BufferedReader info = new BufferedReader(new InputStreamReader(in));
                           
         for(int a = 0; a < MAIN.height; a++) 
         {
            String line = info.readLine();
            String[] tokens = line.split(",");
            
            for(int b = 0; b < MAIN.width; b++) 
            {
               imageB[a][b] = new BackImage(Integer.parseInt(tokens[b]));
            }
         }
      }
      catch(IOException e) {
         System.out.println("****LEVEL " + ln + " NOT FOUND****");
         e.printStackTrace();
      }
   }
   
   public void loadEntities(int ln)
   {
      try{
         InputStream in = getClass().getResourceAsStream("/Resources/Entities/" + ln + ".txt");
         BufferedReader info = new BufferedReader(new InputStreamReader(in));
         //AOE = amount of entities, it is in the file right before the
         //entity data is entered
         //Each new line in the file = a NEW ENTITY
         int AOE = Integer.parseInt(info.readLine());
         entities = new Entity[AOE];
         for(int i = 0; i < AOE; i++) {
            String line = info.readLine();
            String[] tokens = line.split(",");
            if(i == 0)
               entities[i] = new Player(Integer.parseInt(tokens[0])*MAIN.block,Integer.parseInt(tokens[1])*MAIN.block,Integer.parseInt(tokens[2]));
            else
            {
               if(i == 1)
                  entities[i] = new Testificate(Integer.parseInt(tokens[0])*MAIN.block,Integer.parseInt(tokens[1])*MAIN.block,1,this);
               else
                  entities[i] = new Testificate(Integer.parseInt(tokens[0])*MAIN.block,Integer.parseInt(tokens[1])*MAIN.block,Integer.parseInt(tokens[2]),this);
            }
         }
      } 
      catch(IOException e) {
         System.out.println("****LEVEL " + ln + " NOT FOUND****");
         e.printStackTrace();
      }
   }
   
   public void nextLevel() {
      up = down = left = right = action = quit = false;
      shutDownThreads();
      main.changeLevel(levelID+1);
   }
   
   private void shutDownThreads()
   {
      if(entities != null && entities.length > 0)
         for(int i = 0; i < entities.length; i++)
         {
            System.out.println(i);
            entities[i].endThread = true;
            entities[i] = null;
         }
      entities = null;
      System.out.println("Threads Ended");
   }
   
   public void keyTyped(KeyEvent e){}  
   
   public void keyPressed(KeyEvent e)
   {
      if(!loadMiniGame)
      {
         switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
               up = true;
               answer = false;
               number = -1;
               break;
            case KeyEvent.VK_DOWN:
               down = true;
               answer = false;
               number = -1;
               break;
            case KeyEvent.VK_LEFT:
               left = true;
               answer = false;
               number = -1;
               break;
            case KeyEvent.VK_RIGHT:
               right = true;
               answer = false;
               number = -1;
               break;
            case KeyEvent.VK_ESCAPE:
               quit = true;
               answer = false;
               number = -1;
               break;
            case KeyEvent.VK_SPACE:
               if(entities[0].checkForActionFacingSide(this) == 1)
               {
                  for(int i = 0; i < entities.length; i++)
                  {
                     if(entities[i].isManager())
                        entities[i].talking = true;
                  }
                  loadMiniGame = true;
               }
               if(entities[0].checkForActionFacingSide(this) > 1 && entities[0].checkForActionFacingSide(this) < 20)
                  answer = true;
               break;
         }
      }
      else
      {
         way[selection] = false;
         switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
               selection--;
               break;
            case KeyEvent.VK_DOWN:
               selection++;
               break;
            case KeyEvent.VK_LEFT:
               selection--;
               break;
            case KeyEvent.VK_RIGHT:
               selection++;
               break;
            case KeyEvent.VK_0:
               playedCS = true;
               break;
            case KeyEvent.VK_ENTER:
               if(selection == 0 && !playedCS)
               {
                  main.changePanel(5);
                  playedCS = true;
               }
               else if(selection == 0 && playedCS)
                  spaceCheck = true;
               
               if(selection == 1 && !playedComService)
               {
                  main.changePanel(4);
                  playedComService = true;
               }
               else if(selection == 1 && playedComService)
                  spaceCheck = true;
               
               if(selection == 2 && !playedTG)
               {
                  main.changePanel(3);
                  playedTG = true;
               }
               
               else if(selection == 2 && playedTG)
                  spaceCheck = true;
               break;
            case KeyEvent.VK_ESCAPE:
               for(int i = 0; i < entities.length; i++)
                  if(entities[i].isManager())
                     entities[i].talking = false;
               loadMiniGame = false;
               break;
         }
      }
      
      selection = (selection+3)%3;
      
      way[selection] = true;
      
      repaint();
      
      if(e.getKeyCode() == KeyEvent.VK_SPACE)
      {
         int i = entities[0].checkForActionFacingSide(this); 
         System.out.println(i);
      }
   }
   
   public void keyReleased(KeyEvent e) {
      switch(e.getKeyCode()) {
         case KeyEvent.VK_UP:
            up = false;
            break;
         case KeyEvent.VK_DOWN:
            down = false;
            break;
         case KeyEvent.VK_LEFT:
            left = false;
            break;
         case KeyEvent.VK_RIGHT:
            right = false;
            break;
         case KeyEvent.VK_SPACE:
            action = false;
            break;
      }
   }
   
   public void run() {
      while(levelID == main.currentLevel && entities != null) {
         try {
            if(entities[0].animate == false)
            {
               if(up && entities[0].getYLocation() > 0) {
                  entities[0].up(this);
               }
               if(down && entities[0].getYLocation() < MAIN.totalHeight-MAIN.block) {
                  entities[0].down(this);
               }
               if(left && !up && !down && entities[0].getXLocation() > 0) {
                  entities[0].left(this);
               }
               if(right && !up && !down && entities[0].getXLocation() < MAIN.totalWidth-MAIN.block) {
                  entities[0].right(this);
               }
            }
            if(quit) {
               if(transparency > 255)
               {
                  shutDownThreads();
                  main.changePanel(-1);
                  main.levelReset();
                  break;
               }
            }
            Thread.sleep(repaintTimer);
            repaint();
         } 
         catch(InterruptedException e) {
            System.out.println("THREAD IN LEVELS INTERRUPTED");
            e.printStackTrace();
         }
      }
   }
}