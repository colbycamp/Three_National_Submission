package Main.Menu;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.lang.Thread;
import java.util.ArrayList;

import Main.MAIN;
/**
 *Finale is used at the end of the game. It is a JPanel whos main use
 *is to inform the user what placement he/she  has recieved.
 */
public class Finale extends JPanel implements Runnable
{
   /**main is to be utilized by class*/
   private static MAIN main;
   /**thread is to be utilized by class mainly as a time keeper*/
   private Thread thread;
   /**finalPic displays the final picture*/
   private Image finalPic;
   /**transparency is used to keep track of the fading in and out of the picture*/
   private int transparency = 255;
   /**rolor object is used to track which color the fading in and out is*/
   private Color color = new Color(173,255,47,transparency);
   /**refresh is used to track how long the class repaints*/
   private int refresh = 10;
   /**fadeIn tracks if the program has faded in alread*/
   private boolean fadeIn = true;
   /**font is the font used to write in the class*/
   private Font font;
   /**tempf is a float object that states what sizes the font should be (is growing during program)*/
   private float tempf = 0;
   /**counter tracks which name it is on for the cycling of end*/
   private int counter = 0;
   /**fontCounter tracks the size of the font so far*/
   private int fontCounter = 0;
   /**end holds the list of all names*/
   private ArrayList <String> end = new ArrayList<String>();
   
   /**
    *@param main
    *Sets classes main equal to that of the current main.
    *Loads all data for finale.
    *Starts thread.
    */
   public Finale(MAIN main)
   {
      this.main = main;
      
      loadFinale();
      
      thread = new Thread(this);
      thread.start();
   }
   /**
    *Loads all data for finalPic.
    *Loads data for the names in the end.
    *Loads the font to be used.
    */
   private void loadFinale()
   {   
      try {
         finalPic = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Finale/Finale.png"));
      }
      catch(IOException e) {
         e.printStackTrace();
      }
      
      try
      {
         InputStream in = getClass().getResourceAsStream("/Resources/Finale/finale.txt");
                           
         BufferedReader info = new BufferedReader(new InputStreamReader(in));
         
         String line = "";
         
         while(!line.equals("end"))
         {
            line = info.readLine();
            System.out.println(line);
            end.add(line);
         }
      }
      catch(IOException e) {
         System.out.println("****CREDITS PROBLEMS****");
         e.printStackTrace();
      }
      
      try {
         font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Resources/Finale/finale.ttc"));
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
   
   /**
    *@param g
    *Paint is used to paint the fadeIn and Out as well as the rank
    */
   public void paint(Graphics g) 
   {
      if(fadeIn)
         fadeIn(g);
      else
      {
         if(tempf < 30)
            rank(g);
         else
            fadeOut(g);
      }
   }
   
   /**
    *@param g
    *FadeIn decrements the transparency with a lime green color.
    *Draws rect withs specific color over all of the JFrame.
    *Once transparency == 0 than fadeIn becomes false
    */
   private void fadeIn(Graphics g)
   {
      color = new Color(173,255,47,transparency);
      g.setColor(color);
      g.drawImage(finalPic,0,0,this);
      g.fillRect(0,0,main.totalWidth,main.totalHeight);
      transparency--;
      if(transparency == 0)
         fadeIn = false;
   }
   /**
    *@param g
    *FadeOut imcrement the transparency with black color.
    *Draws rect withs specific color over all of the JFrame.
    *Once transparency >= 255 than switchs to credits
    */   
   private void fadeOut(Graphics g)
   {
      if(refresh > 300)
         refresh -= 200;
      else if(refresh > 10)
         refresh -= 5;
      else if(refresh > 1)
         refresh--;
      color = new Color(0,0,0,transparency);
      g.setColor(color);
      g.fillRect(0,0,main.totalWidth,main.totalHeight);
      transparency++;
      if(transparency >= 255)
         main.changePanel(1);
   }
   
   /**
    *@param g
    *Draws specific rank for score that is denoted by main.masterScore.
    *"Spins" through all of the ranks possible.
    *Lands on specific rank.
    */
   private void rank(Graphics g)
   {
      g.drawImage(finalPic,0,0,this);
      tempf = 10+fontCounter;
      g.setFont(font.deriveFont(tempf));
      if(refresh <= 1000)
      {
         g.drawString("You are a..." + end.get(counter),(main.totalWidth-g.getFontMetrics().stringWidth("You are a..." + end.get(counter)))/2,400);
         if(refresh > 300 && refresh < 1000 && tempf < 31)
         {
            fontCounter+=3;
            refresh+=100;
         }
         else
            refresh+=5;
      }
      counter++;
      if(counter >= end.size())
         counter = 0;
      if(tempf >= 30)
      {
         g.drawImage(finalPic,0,0,this);
         String s = "You are a..." + calculate();
         g.drawString(s,(main.totalWidth-g.getFontMetrics().stringWidth(s))/2,400);
         refresh -= 200;
      }
   }
   /**
    *@return String
    *Calculates specific score and returns rank as String.
    */   
   private String calculate()
   {
      if(main.masterScore == 75000)
         return end.get(0);
      else if(main.masterScore > 70000)
         return end.get(1);
      else if(main.masterScore > 65000)
         return end.get(2);
      else if(main.masterScore > 60000)
         return end.get(3);
      else if(main.masterScore > 55000)
         return end.get(4);
      else if(main.masterScore > 50000)
         return end.get(5);
      else if(main.masterScore > 45000)
         return end.get(6);
      else if(main.masterScore > 40000)
         return end.get(7);
      else
         return end.get(8); 
   }
   /**
    *Keeps timing of the repaint method.
    *Changes panel when refresh <= 1.
    */      
   public void run()
   {
      while(true)
      {
         try{
            if(refresh <= 1)
            {
               main.changePanel(1);
               break;
            }
            Thread.sleep(refresh);
            repaint();
         }
         catch(InterruptedException e)
         {
            System.out.println("FINALE THREAD INTERRUPTED");
         }
      }
   }
   
   public static void main(String [] args)
   {
      new MAIN();
   }
}