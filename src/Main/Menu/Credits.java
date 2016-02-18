package Main.Menu;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import java.awt.Font;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.lang.Thread;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.FontMetrics;

import Main.MAIN;

public class Credits extends JPanel implements KeyListener,Runnable
{
   /**main that is utilized by this object*/
   private static MAIN main;
   
   /**all of the images used by the credits*/
   private ArrayList<Image> images = new ArrayList<Image>();
   /**all the setences that will displayed in the credits*/
   private ArrayList<String> words = new ArrayList<String>();
   /**tracks which slide is on*/
   private int slide = 0;
   /**contains all the information for the current slide*/
   private Slide slideInfo = new Slide();
   /**the refresh speed of*/
   private int creditSpeed = 7;
   /**sets the font for the level*/
   private Font font;
   /**the fontMetrics for the current level*/
   private static FontMetrics fontMetrics;
   /**if the slideInfo has started*/
   private boolean started;
   /**currentPicture that is on*/
   private int currentPicture = 0;
   /**sets the background*/
   private Color background = Color.black;
   /**calculates the Size of three in credits*/
   private int threeSize = 0;
   
   /**
    *@param main
    *Sets main equal to this classes main field.
    *Loads all info.
    *Starts the thread.
    */
   public Credits(MAIN main)
   {
      this.main = main;
      
      addKeyListener(this);
      setFocusable(true);
      
      repaint();
      
      loadCreditInfo();
      
      Thread thread = new Thread(this);
      thread.start();
   }
   /**
    *run() cycles through all pictures and sets repaint speed
    */   
   public void run()
   {
      while(true)
      {
         try{
            Thread.sleep(creditSpeed);
            repaint();
            slideInfo.waitPixels--;
            if(slideInfo.waitPixels == 1)
            {
               while(slideInfo.height > 0)
               {
                  Thread.sleep(creditSpeed);
                  slideInfo.waitPixels--;
                  slideInfo.height--;
                  repaint();
               }
            }
         }  
         catch(InterruptedException e)
         {
            System.out.println("CREDITS THREAD INTERRUPTED");
         }
      }
   }
   /**
    *@param g
    *Paints all the stuff and updates that other stuff
    */     
   public void paint(Graphics g) 
   {
      g.setFont(font);
      fontMetrics = g.getFontMetrics();
      if(slideInfo.waitPixels <= 0 && slideInfo.height <= 0)
      {
         slide++;
         if(slide == words.size()-1)
            main.changePanel(-1);
         try
         {
            currentPicture = Integer.parseInt(words.get(slide));
            slideInfo = new Slide(images.get(currentPicture));
         }
         catch(NumberFormatException e)
         {
            slideInfo = new Slide(words.get(slide));
         }
      }
      g.setColor(background);
      g.fillRect(0,0,main.totalWidth,main.totalHeight);
      
      
      if(slideInfo.height > 5)
      {
      g.setFont(new Font("Serif",Font.BOLD,threeSize));
      FontMetrics fm = g.getFontMetrics();
      g.setColor(new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
      for(int i = 0; i < threeSize; i++)
          g.drawString("3",((main.totalWidth-fm.stringWidth("3"))/2),(int)((main.totalWidth-fm.getHeight())/1.5));
      if(threeSize > 0)
         threeSize--;
      }
          
      g.setFont(font);
      g.setColor(Color.white);
      //if the text has a number then that picture is displayed
      try
      {
         int novalue = Integer.parseInt(words.get(slide));
         g.drawImage(images.get(currentPicture),slideInfo.xPlacement,slideInfo.waitPixels,this);
      }
      catch(NumberFormatException e)
      {
         g.drawString(words.get(slide),slideInfo.xPlacement,slideInfo.waitPixels);
      }   
   }
   /**
    *not utilized.
    */      
   public void keyTyped(KeyEvent e) {}
   /**
    *tracks each input from user
    */   
   public void keyPressed(KeyEvent e) {
      switch(e.getKeyCode())
      {
      case KeyEvent.VK_UP:
         if(creditSpeed <= 40)
            creditSpeed++;
         break;
      case KeyEvent.VK_DOWN:
         if(creditSpeed > 5)
            creditSpeed--;
         break;
      case KeyEvent.VK_SPACE:
         background = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
         break;
      case KeyEvent.VK_3:
         if(threeSize < 300)
            threeSize += 7;
         break;
      }
   }
   /**
    *not utilized.
    */       
   public void keyReleased(KeyEvent e) {}
   /**
    *Loads all information for the fields.
    */       
   private void loadCreditInfo()
   {
      try 
      {
         int i = 1;
         while(i > 0)
         {
            Image image = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Credits/Photos/" + i + ".jpg"));
            images.add(image);
            i++;
         }
      }
      catch(IOException e) {
         System.out.println("****CREDITS PROBLEMS****");
         e.printStackTrace();
      }
      catch(IllegalArgumentException e)
      {
         System.out.println("Loaded all credit info");
      }
      
      try
      {
         InputStream in = getClass().getResourceAsStream("/Resources/Credits/Credits.txt");
                           
         BufferedReader info = new BufferedReader(new InputStreamReader(in));
         
         String line = "";
         
         while(!line.equals("end"))
         {
            line = info.readLine();
            words.add(line);
         }
      }
      catch(IOException e) {
         System.out.println("****CREDITS PROBLEMS****");
         e.printStackTrace();
      }
      
      try {
         font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Resources/Credits/CreditFont.ttf"));
         font = font.deriveFont(25f);
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
   /**
    *Keeps all the information Slide and which way its heading and such.
    */   
   private static class Slide
   {
      /**amount of pixels that need to wait before loading next slide*/
      public static int waitPixels = 0;
      /**height of slide object*/
      public static int height = 0;
      /**word that is represented*/
      public static String word = "";
      /**image for object if present*/
      public static Image image;
      /**current xLocation*/
      public static int xPlacement = 0;
      /**current yLocation*/
      public static int yPlacement = 0;
      /**
       *Slide object
       */
      public Slide()
      {}
      /**
       *Slide object for word
       */
      public Slide(String s)
      {
         word += s;
         waitPixels = main.totalHeight;
         wait(s);
         placement(s);
      }
      /**
       *Slide object for image
       */
      public Slide(Image i)
      {
         image = i;
         waitPixels = main.totalHeight;
         wait(i);
         placement(i);
      }
      /**
       *wait for string before next slide
       */      
      private void wait(String s)
      {
         height = fontMetrics.getHeight()+1;
      }
      /**
       *wait for image before next slide
       */      
      private void wait(Image i)
      {
         BufferedImage image = (BufferedImage)i;
         height = image.getHeight()+1;
      }
      /**
       *current placement of s
       */      
      private void placement(String s)
      {
         xPlacement = ((main.totalWidth-fontMetrics.stringWidth(s))/2);
      }
      /**
       *Slide object for image
       */      
      private void placement(Image i)
      {
         BufferedImage image = (BufferedImage)i;
         int x = image.getWidth();
         xPlacement = ((main.totalWidth-x)/2);
      }
   }
}