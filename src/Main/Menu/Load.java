package Main.Menu;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.lang.Thread;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.awt.Font;
import java.awt.FontMetrics;

import Main.MAIN;

public class Load extends JPanel implements KeyListener, Runnable
{
   /**main that is utilized by this class*/
   private MAIN main;
   /**current level*/
   private int level;
   /**amount of time before level loads*/
   private int timer = 10;
   /**the thread for the background*/
   private Thread background;
   /**the length for the background rect*/
   private int rectWidth;
   /**boolean for all the rectangles*/
   private boolean way,next,cancelled = false;
   /**alpha value for the transparency in and out*/
   private int alpha = 0;
   /**tracks alpha in and out as well with alpha value*/
   private long alphaCount = 0; 
   /**
    *Default for the Load
    */
   public Load(){}
   
   /**
    *@param g
    *Sets main = main.
    *Sets up the backrgound run as well as rectangleWidth and Loads all info.
    */
   public Load(MAIN m)
   {
      this.main = m;
      addKeyListener(this);
      setFocusable(true);
      
      rectWidth = (int)(Math.random()*main.totalWidth);
      
      loadLevel();
      
      Thread thread = new Thread(this);
      thread.start();
      
      background = 
         new Thread()
         {
            public void run()
            {
               while(timer >= 0)
               {
                  try
                  {
                     Thread.sleep(10);
                     if(!way)
                        rectWidth -= 5;
                     if(way)
                        rectWidth += 5;
                     if(rectWidth <= 0 && !way)
                        way = true;
                     if(rectWidth >= main.totalWidth && way)
                        way = false;
                     if(timer < 3)
                     {
                        alphaCount += 1.085;
                        if(alphaCount >= 1.0)
                        {
                           alpha += 1;
                           alphaCount--;
                           if(alpha >= 255 && !cancelled)
                           {
                              main.currentLevel = level;
                              main.levelReset();
                              main.changePanel(-2);
                           }
                        }
                        next = true;
                     }
                     repaint();
                  }
                  catch(InterruptedException e)
                  {
                     System.out.println("****PROBLEM IN LOAD BACKGROUND****");
                  }
               }
            }   
         };
      background.start();
   }
   /**
    *@param g
    *Paints all the information for the screen
    */
   public void paint(Graphics g)
   {
      Font font = new Font("Font of Some sort",Font.ITALIC,40);
      g.setFont(font);
      
      drawBackground(g);
      
      g.setColor(Color.black);
      g.drawString("Time until level loads...",(main.totalWidth-g.getFontMetrics().stringWidth("Time until level loads..."))/2,main.totalHeight/2);
      g.drawString("" + timer,(main.totalWidth-g.getFontMetrics().stringWidth("" + timer))/2,main.totalHeight/2 + 50);
      g.drawString("Press Enter To cancel...",(main.totalWidth-g.getFontMetrics().stringWidth("Press Enter To cancel..."))/2,main.totalHeight/2 + 100);
      
      if(next)
         transition(g);
   }
   /**
    *@param g
    *Draws the background rectangles.
    */
   private void drawBackground(Graphics g)
   {
      g.setColor(Color.white);
      g.fillRect(0,0,rectWidth,main.totalHeight);
      g.setColor(Color.red);
      g.fillRect(rectWidth,0,main.totalWidth,main.totalHeight);
   }
   /**
    *@param g
    *Makes the fade in and fade out.
    */   
   private void transition(Graphics g)
   {
      g.setColor(new Color(255,0,0,alpha));
      g.fillRect(0,0,main.totalWidth,main.totalHeight);
   }
   /**
    *Runs the timer until level loads.
    */   
   public void run() 
   {
      while(timer >= 0)
      {
         try
         {
            Thread.sleep(1000);
            timer--;
            repaint();
         }
         catch(InterruptedException e)
         {
            System.out.println("****LOAD PROBLEMS IN RUNNABLE RUN METHOD****");
         }
      }
   }
   /**
    *Loads all informations for the fields.
    */
   private void loadLevel()
   {
      try 
      {
         InputStream in = getClass().getResourceAsStream("/Resources/SavedGames/LoadFile.txt");
                           
         BufferedReader info = new BufferedReader(new InputStreamReader(in));
         
         String line = info.readLine();
         System.out.println("LEVEL: " + line);
         level = Integer.parseInt(line);
      }
      catch(IOException e) {
         System.out.println("****LOADING PROBLEMS****");
         e.printStackTrace();
      }
   }
   /**
    *Saves the current level when called to txt
    */
   public void saveGame()
   {
      try
      {
      String s = getClass().getResource("/Resources/SavedGames/LoadFile.txt").getFile();
      System.out.println(s);
      s = s.replaceAll("%20"," ");
      s = s.substring(1);
      File file = new File(s);
      if(file.exists())
         {
         file.delete();
         file.createNewFile();
         System.out.println("FIle exsists recreating");
         }
      else
         file.createNewFile();
      System.out.println("FileWriter now");
      FileWriter f = new FileWriter(file.getAbsoluteFile());
      BufferedWriter b = new BufferedWriter(f);
      System.out.println("Written: "  + main.currentLevel);
      b.write("" + main.currentLevel);
      b.close();
      }
      catch(IOException e)
      {
      System.out.println("****SAVING GAMES****");
      e.printStackTrace();
      }
   }
   /**
    *Not utilized.
    */
   public void keyTyped(KeyEvent e){}  
   /**
    *@param e
    *When hit entered load level cancels
    */
   public void keyPressed(KeyEvent e)
   {
   switch(e.getKeyCode()) {
         case KeyEvent.VK_ENTER:
            cancelled = true;
            main.changePanel(-1);
            break;
      }
   }
   /**
    *Not utilized.
    */   
   public void keyReleased(KeyEvent e){}
}