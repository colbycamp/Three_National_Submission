package Main.Menu;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.lang.Thread;
import java.util.Arrays;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.awt.Image;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.IOException;

import Main.MAIN;

/**
 *Story is a class that is used before each Level is loaded. It loads images
 *from a folder as well as a .txt that has the length that each image has.
 *It gives the game makers a chance to tell the user a story for the game.
 */

public class Story extends JPanel implements KeyListener, Runnable
{
   /**MAIN object to be utilized by class*/
   private MAIN main;
   /**the background Image*/
   private Image bg;
   /**holds each image that will displayed in order*/
   private ArrayList <Image> storyImages = new ArrayList <Image>();
   /**amount of time for each image*/
   private int [] storyTime;
   /**tracks which picture the story is on*/
   private int storyPictureNum = 0;
   /**if no time is presented, this is the defaulted time that the picture will be set on*/
   private int defaultedTime = 1000;
  
   private Thread thread = new Thread(this);
    
   /**
    *@param main
    *Constructor. Requires MAIN Obect.
    *Loads all information into fields.
    *Starts thread to display each image.
    *Sets the focus listener to this object.
    *Sets MAIN object equal Storys' MAIN object
    *Extends JPanel, Implements KeyListener and Runnable
    */
   public Story(MAIN main)
   {
      this.main = main;
      
      addKeyListener(this);
      setFocusable(true);
      
      loadStory();
      
      thread.start();
   }
   
   /**
    *@param g
    *This paint object is only used for 
    */
   public void paint(Graphics g) 
   {
      g.drawImage(storyImages.get(storyPictureNum), 0, 0, this);
      g.setColor(Color.white);
      g.fillRect(0,MAIN.block*16,MAIN.totalWidth,MAIN.totalHeight);
   }
   
   /**
    *@param e
    *Not utilized for this class
    */
   public void keyTyped(KeyEvent e) {}
   
   /**
    *@param e
    *Not utilized for this class
    */
   public void keyPressed(KeyEvent e) {
         
      switch(e.getKeyCode()) 
      {
         case KeyEvent.VK_ENTER:
            storyPictureNum =+ storyImages.size() + 1000;
            main.changePanel(-2);
            break;
      }
   }
   /**
    *@param e
    *Not utilized for this class
    */
   public void keyReleased(KeyEvent e) {}
   
   /**
    *run() is a thread that is runs until storyPictureNum < storyImages.size()
    *Sleeps for designated time that storyTime[] has for each picture
    *If no time is alotted than it uses a defaulted time denoted by field defaultedTime
    */
   public void run() 
   {
      while(storyPictureNum < storyImages.size()){
         try{
            Thread.sleep(storyTime[storyPictureNum]);
         } 
         catch(InterruptedException e) {
            System.out.println("STORY THREAD INTERRUPTED");
            e.printStackTrace();
         }
         catch(ArrayIndexOutOfBoundsException b)
         {
            System.out.println("MISSING STORY TIME FOR LEVEL " + main.currentLevel + ": USING DEFAULT TIME OF " + defaultedTime);
            try{
               Thread.sleep(defaultedTime);
            } 
            catch(InterruptedException e) {
               System.out.println("STORY THREAD INTERRUPTED");
               e.printStackTrace();
            }
         }
         if(storyPictureNum > storyImages.size()-2)
         {
            main.changePanel(-2);
            break;
         }
         storyPictureNum++;
         repaint();
      }
   }
   /**
    *loadStory fills the field storyImages with all images that are going to be presented.
    *Also is responsible for filling the storyTime field with the times that each picture
    *will be used for
    */
   private void loadStory()
   {
      try 
      {
         int i = 1;
         while(i > 0)
         {
            Image image = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Story/" + main.currentLevel + "/" + i + ".png"));
            storyImages.add(image);
            i++;
         }
      }
      catch(IOException e) {
         System.out.println("****STORY PROBLEMS****");
         e.printStackTrace();
      }
      catch(IllegalArgumentException e)
      {
         System.out.println("Loaded all the story");
      }
      try 
      {
         InputStream in = getClass().getResourceAsStream("/Resources/Story/" + main.currentLevel + "/StoryTime.txt");
                           
         BufferedReader info = new BufferedReader(new InputStreamReader(in));
                           
         String line = info.readLine();
         String[] tokens = line.split(",");
         storyTime = new int[tokens.length];
         for(int a = 0; a < tokens.length; a++)
            storyTime[a] = Integer.parseInt(tokens[a]);
      }
      catch(IOException e) {
         System.out.println("****STORY PROBLEMS****");
         e.printStackTrace();
      }
   }
}