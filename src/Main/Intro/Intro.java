package Main.Intro;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Image;
import javax.imageio.ImageIO;

import Main.MAIN;

public class Intro extends JPanel implements KeyListener
{
   private MAIN main;
   private int current = -1;
   
   private Image five;
   private Image four;
   private Image three;
   private Image two;
   
   private boolean intTog = true;
   
   private Image[] images;
   
   private int time = 800;
      
   private javax.swing.Timer timeLord;

   public Intro(MAIN m)
   {
      main = m;
      main.soundManager.playSound("Intro");
      addKeyListener(this);
      setFocusable(true);
   
      try 
      {
         five = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Intro/5.png"));
         four = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Intro/4.png"));
         three = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Intro/3.png"));
         two = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Intro/2.png"));
         
         Image[] temp = {five, four, three, two};
         images = temp;
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      
      ActionListener updatePos = 
         new ActionListener() 
         {
            public void actionPerformed(ActionEvent ae)
            {
               if (current < 2 && intTog)
                  current++;
               else if (intTog == false && current ==3)
                  current = 2;
               else
               {
                  if(intTog)
                  {
                     current = 3;
                     time = time/8;
                     intTog = false;
                  }
                  else
                     current = 3;
                  time = time - 10;
                  if(time > 10)
                     timeLord.setDelay(time);
               }
               repaint();
            }
         };
      timeLord = new javax.swing.Timer(time,updatePos);
   }

   public void paint(Graphics g) 
   {
      timeLord.start();
      if (current != -1)
         g.drawImage(images[current], 0, 0, MAIN.totalWidth, MAIN.totalHeight, this);
      if( time <= 10)
      {
         g.drawImage(images[2], 0, 0, MAIN.totalWidth, MAIN.totalHeight, this);
         main.soundManager.stopAudio("Intro");
         main.soundManager.playBackgroundAudio();
         main.changePanel(-1);  
      }
   }

   public void keyTyped(KeyEvent e) {}
   
   public void keyPressed(KeyEvent e) {
      switch(e.getKeyCode())
      {
         case KeyEvent.VK_ENTER:
            main.soundManager.stopAudio("Intro");
            main.soundManager.playBackgroundAudio();
            main.changePanel(-1);
            break;
      }
   }
   
   public void keyReleased(KeyEvent e) {}
}