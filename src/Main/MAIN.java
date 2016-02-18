package Main;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.imageio.ImageIO;

import Level.*;
import Main.Menu.*;
import Main.Intro.*;
import Level.CS.*;

public class MAIN extends JFrame 
{
   public final static int block = 32;
   public final static int width = 22;
   public final static int height = 17;
   public final static int totalWidth = width*block;
   public final static int totalHeight = height*block;
   public final static SoundManager soundManager = new SoundManager();
   public static int currentLevel = 1;
   public Level level = new Level(this);
   public Load load = new Load();
   public int masterScore = 0;

   public MAIN()
   {
      super("3");
      GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
      setLocation((gd.getDisplayMode().getWidth()-totalWidth)/2,(gd.getDisplayMode().getHeight()-totalHeight)/2);
      setIconImage(icon());
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setUndecorated(true);
      setResizable(false);
      add(new Intro(this));
      getContentPane().setPreferredSize(new Dimension(totalWidth, totalHeight));
      setResizable(true);
      pack();
      setVisible(true);
   }
   
   public void changePanel(int choice)
   {
      getContentPane().removeAll();
      switch(choice)
      {
         case -2:
            getContentPane().add(level);
            getRootPane().revalidate();
            repaint();
            level.requestFocusInWindow();
            break;
         case -1:
            Menu menu = new Menu(this);
            getContentPane().add(menu);
            getRootPane().revalidate();
            repaint();
            menu.requestFocusInWindow();
            break;
         case 0:
            Story story = new Story(this);
            getContentPane().add(story);
            getRootPane().revalidate();
            repaint();
            story.requestFocusInWindow();
            break;
         case 1:
            Credits credits = new Credits(this);
            getContentPane().add(credits);
            getRootPane().revalidate();
            repaint();
            credits.requestFocusInWindow();
            break;
         case 2:
            load = new Load(this);
            getContentPane().add(load);
            getRootPane().revalidate();
            repaint();
            load.requestFocusInWindow();
            break;
         case 3:
            TypingGame tg = new TypingGame(this);
            getContentPane().add(tg);
            getRootPane().revalidate();
            repaint();
            tg.requestFocusInWindow();
            break;
         case 4:
            CleanUp hd = new CleanUp(this);
            getContentPane().add(hd);
            getRootPane().revalidate();
            repaint();
            hd.requestFocusInWindow();
            break;
         case 5:
            CyberSecurity cs = new CyberSecurity(currentLevel, this);
            getContentPane().add(cs);
            getRootPane().revalidate();
            repaint();
            cs.requestFocusInWindow();
            break;
         case 6:
            Finale finale = new Finale(this);
            getContentPane().add(finale);
            getRootPane().revalidate();
            repaint();
            finale.requestFocusInWindow();
            break;
      }
      getRootPane().revalidate();
   }
   
   public void levelReset()
   {  
      level = null;
      level = new Level(this);
   }
   
   public void changeLevel(int next) 
   {
      load.saveGame();
      level = null;
      currentLevel++;
      
      if(currentLevel < 6) 
      {
         getContentPane().removeAll();
         level = new Level(this);
         Story story = new Story(this);
         getContentPane().add(story);
         getRootPane().revalidate();
         repaint();
         level.requestFocusInWindow();
      }
      else
      {
         level = null;
         changePanel(6);
      }
   }
   
   public Image icon()
   {
      try 
      {
         return (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Credits/Photos/7.jpg"));
      }
      catch(Exception e)
      {
         System.out.println("****Problems IMAGE for Icon****"); 
         e.printStackTrace();
      }
      return null;
   }
   
   public static void main(String[]args)
   {
      new MAIN();
   }
}