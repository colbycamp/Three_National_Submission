package Main.Menu;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.awt.Font;
import java.io.InputStreamReader;

import Main.MAIN;
/**
 *Menu is the JPanel that the user is presented with at the begining of the game.
 */
 
public class Menu extends JPanel implements KeyListener, Runnable
{
   //**MAIN object to be utilized by class*/
   private MAIN main;
   /**choices contains the three "choices" the user has*/
   private String [] choices = {"New Game","Credits","Load Game"};
   /**used to track which choice the user is on*/
   private boolean [] way = {true,false,false};
   /**transparency for each choice that the user has*/
   private Color [] transparencyColor = {Color.red,Color.black,Color.black};
   /**tracks the selection that the user is currently on*/
   private int selection = 0;
   /**if it is positive than animation of the chosen choice getting larger occurs*/
   private boolean chosen;
   /**size of that the choices are displayed in*/
   float size = 54f;
   
   /**highest font size for the font size*/
   private int high = 200;
   /**text objects that float around Menu*/
   private Text [] text = new Text[30];
   /**how quickly the screen repaints in milliseconds*/
   private int refresh = 10;
   /**tracks the transparency*/
   private int transparency = 0;
   
   /**
    *@param main
    *Sets the main equal to the main of this class.
    *Adds the keyListener and set the focusable
    *Imports all the background text to be used.
    *Starts the thread for the repain
    */
   public Menu(MAIN main) {
      this.main = main;
      addKeyListener(this);
      setFocusable(true);
      setText();
      repaint();
      Thread thread = new Thread(this);
      thread.start();
   }
  /**
   *run() is used to repaint and keep time.
   */ 
   public void run()
   {
      while(true)
      {
         try{
            Thread.sleep(refresh);
            repaint();
         }
         catch(InterruptedException e)
         {
            System.out.println("THREAD IN MAIN IS NOT WORKING");
            e.printStackTrace();
         }
      }
   }
   /**
    *Fills the text[] with new String objects 
    */
   private void setText()
   {
      try
      {
         InputStream in = getClass().getResourceAsStream("/Resources/Menu/MenuWords.txt");
                           
         BufferedReader info = new BufferedReader(new InputStreamReader(in, "Unicode"));
                           
         String line = info.readLine();
         String [] words = line.split(",");
         for(int i = 0; i < text.length; i++)
         {
            text[i] = new Text();
            text[i].word = words[(int)(Math.random()*words.length)];
         } 
      }
      catch(IOException e) {
         System.out.println("****STORY IN MENUE LOADING THE WORDS:(:(:(****");
         e.printStackTrace();
      }         
   }
   
   /**
    *@param g
    *Fills the background with black.
    *Calls paintText(g) to paint the floating text.
    *Calls paintOptions(g) to paint the options on top.
    */
   public void paint(Graphics g) 
   {
      g.fillRect(0,0,main.totalWidth,main.totalHeight);
      paintText(g);
      paintOptions(g);
   }
   /**
    *@param g
    *Draws the text objects with specific font
    */   
   private void paintText(Graphics g)
   {
      for(int i = 0; i < text.length; i++)
      {
         g.setColor(text[i].color);
         g.setFont(g.getFont().deriveFont((float)(text[i].fontSize)));
         g.drawString(text[i].word,text[i].xlocation,text[i].ylocation);
         text[i].move();
      }   
   }
   /**
    *@param g
    *Draws the Options to choose between
    */   
   private void paintOptions(Graphics g)
   {
      g.setFont(g.getFont().deriveFont(Font.BOLD,54f));
      
      for(int i = 0; i < choices.length; i++)
      {
         if(way[i])
         {
            if(chosen)
            {
               size+=5;
               g.setFont(g.getFont().deriveFont(Font.BOLD,size));
               g.setColor(Color.WHITE);
               g.drawString(choices[i],(MAIN.totalWidth/3)-(int)(size),((MAIN.totalHeight/3)+((MAIN.totalHeight/9)*i)+(MAIN.totalHeight/9))+(int)(size*.5));
               pixelate(g,i);
            }
            else
            {
               g.setColor(Color.WHITE);
            //draws shadow
               g.drawString(choices[i],(MAIN.totalWidth/3)-2,((MAIN.totalHeight/3)+((MAIN.totalHeight/9)*i)+(MAIN.totalHeight/9))+2);
               g.setColor(Color.GREEN);
            //draws actual word
               g.drawString(choices[i],(MAIN.totalWidth/3),(MAIN.totalHeight/3)+((MAIN.totalHeight/9)*i)+(MAIN.totalHeight/9));
            }
         }
         else if(!chosen && !way[i])
         {
            g.setFont(g.getFont().deriveFont(Font.BOLD,54f));
            g.setColor(Color.WHITE);
            //draws actual word
            g.drawString(choices[i],(MAIN.totalWidth/3),(MAIN.totalHeight/3)+((MAIN.totalHeight/9)*i)+(MAIN.totalHeight/9));
         }
      }
   }
   /**
    *Draws the fading out effect to the choice of the user
    */
   private void pixelate(Graphics g, int i)
   {
      g.setColor(new Color(transparencyColor[i].getRed(),transparencyColor[i].getGreen(),transparencyColor[i].getBlue(),transparency));
      g.fillRect(0,0,main.totalWidth,main.totalHeight);
      transparency+=1;
      if(transparency >= 255)
         main.changePanel(selection);
   }
   /**
    *Not utilized by class.
    */
   public void keyTyped(KeyEvent e) {}
   
   /**
    *Tracks user movement if not chosen.
    *Decifers the user options
    */
   public void keyPressed(KeyEvent e) {
      way[selection] = false;
      if(!chosen)
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
            case KeyEvent.VK_ENTER:
               chosen = true;
               break;
            case KeyEvent.VK_ESCAPE:
               System.exit(0);
               break;
            case KeyEvent.VK_1:
               refresh = 10;
               break;
            case KeyEvent.VK_2:
               refresh = 100;
               break;
         }
      
      selection = (selection+3)%3;
      
      way[selection] = true;
   }
   /**
    *Not utilized by class.
    */
   public void keyReleased(KeyEvent e) {}
   
   /**
    *Text class is used to track text objects.
    */
   private class Text
   {
      /**the word that the Text object represents*/
      public String word = "3";
      /**xlocation*/
      public int xlocation = 0;
      /**ylocation*/
      public int ylocation = 0;
      /**fontsize for text object*/
      public int fontSize = 0;
      /**default color of text is white*/
      public Color color = Color.white;
      /** 0 = nothing 1 = moving right/up, 2 = moving left/down*/
      public int xway = 0;
      /** 0 = nothing 1 = moving right/up, 2 = moving left/down*/
      public int yway = 0;
     /**
      *Text object that calls reset.
      */      
      public Text()
      {
         reset();
      }
     /**
      *@s
      *Creates new text object with specific string
      */       
      public Text(String s)
      {
         word += s;
         reset();
      }
     /**
      *Moves the text object
      */ 
      public void move()
      {
         switch(xway)
         {
            case 0:
               break;
            case 1:
               xlocation--;
               break;
            case 2:
               xlocation++;
               break;
         }
         switch(yway)
         {
            case 0:
               break;
            case 1:
               ylocation--;
               break;
            case 2:
               ylocation++;
               break;
         }
         if(ylocation == main.totalHeight+150 && yway == 2)
            reset();
         else if(ylocation < -50 && yway == 1)
            reset();
         if(xlocation == main.totalWidth+150 && xway == 2)
            reset();
         else if(xlocation < -50 && xway == 1)
            reset();
      }
     /**
      *resets new text object somewhere on the screen with new values.
      */
      public void reset()
      {
         xlocation = (int)(Math.random()*main.totalWidth);
         ylocation = (int)(Math.random()*main.totalHeight);
         color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
         fontSize = (int)(Math.random()*high);
         xway = (int)(Math.random()*3);
         yway = (int)(Math.random()*3);
         if(xway == 0 && yway == 0)
            xway = 1;
      }
   }
}