package Level;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.event.*;
import java.util.*;

import Main.MAIN;

public class CleanUp extends JPanel implements KeyListener, Runnable
{//talk to the master to start the game then move and collect the trash
   final static int block = 32;//size of one block
   final static int width = 22;//width of screen in blocks
   final static int height = 17;//height of screen in blocks
   final static int totalWidth = width*block;//pixels of screen width
   final static int totalHeight = height*block;//pixels of screen height
   int trashnum;//amount of trash in level (10,20,25,40,50)
   static Image[][] back = new Image[height][width];//image array of background
   static Image[][] trash = new Image[height][width];//image array of trash
   static Image[][] obstacles = new Image[height][width];//image array of obstacles
   int playerX = 0;//player location X
   int playerY = 0;//player location Y
   int mastX;//master location X
   int mastY;//master location Y
   String mast;//text the master says
   Image NULL = getImage(0,-1);//completely blank image for placeholder
   Image PLAYER = getImage(1,-1);//player location
   double time = 60.00;//time
   int score = 0;//score
   int scoremod;//one trashnumth of 5000
   boolean drawMT,timer,started,finished;//booleans to dictate how game works (NO LONGER NEED UP DOWN LEFT RIGHT ACTION)
   
   String backgroundtext;
   String obstacletext;
   
   /*
      public CommunityService (Main main)
      this.main = main;
      main.masterScore += score;
   */
   MAIN main;
   public CleanUp(MAIN main)
   {//constructor of game
      this.main = main;
      readGame(main.currentLevel);//reads the text file of the level
      loadGame();//loads the game
      addKeyListener(this);//adds keylistener
      setFocusable(true);//makes keylistener work
      Thread t = new Thread(this);//makes thread
      t.start();//starts the thread
   }
   
   public void readGame(int level)
   {//reads the game from text file
      switch(level)
      {
         case 1:
            trashnum = 10;
            break;
         case 2:
            trashnum = 20;
            break;
         case 3:
            trashnum = 25;
            break;
         case 4:
            trashnum = 40;
            break;
         case 5:
            trashnum = 50;
            break;
         default:
            trashnum = 25;
            break;
      }
      mast = "You have to clean up "+trashnum+" pieces of trash. Avoid obstacles!";
      scoremod = 5000/trashnum;
      try
      {
         BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Resources/LevelDesign/"+level+"B.txt")));
         try
         {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
         
            while (line != null)
            {
               sb.append(line);
               sb.append("\n");
               line = br.readLine();
            }
            backgroundtext = sb.toString();
         }
         finally
         {
            br.close();
         }
         br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Resources/LevelDesign/"+level+"O.txt")));
         try
         {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
         
            while (line != null)
            {
               sb.append(line);
               sb.append("\n");
               line = br.readLine();
            }
            obstacletext = sb.toString();
         }
         finally
         {
            br.close();
         }
      }
      catch(Exception e)
      {
         System.err.println(e);
      }
   }   
   public void loadGame()
   {//loads the game
      loadBackground();//loads the background array
      loadObstacles();//loads the obstacles array
      loadMaster();//loads the master into the obstacles array
   }
   
   public void loadBackground()
   {//loads the background
      for (int i = 0; i < height; i++)//iterates through the first section of array
      {
         for (int j = 0; j < width; j++)//iterates through the second section of array
         {
            back[i][j] = getImage((int)backgroundtext.charAt(i*width+j)-48,0);//sets back[i][j] equal to image 1.png in Background (0)
         }
      }
   }
   
   public void loadMaster()
   {//loads the master into the obstacle array
      String str = obstacletext.substring(0,obstacletext.indexOf("\n")+1);
      mastX = Integer.parseInt(str.substring(0,str.indexOf(",")));//master's X location
      mastY = Integer.parseInt(str.substring(str.indexOf(",")+1,str.lastIndexOf(",")));//master's Y location
      obstacles[mastY][mastX] = getImage(Integer.parseInt(str.substring(str.lastIndexOf(",")+1,str.length()-1)),1);//sets master[mastX][mastY] equal to -1.png in Obstacles(1)
   }
   
   public void loadTrash()
   {//randomly generates trash into the trash array
      for (int i = 0; i < trash.length; i++)//iterates through the first section of array
      {
         for (int j = 0; j < trash[0].length; j++)//iterates through the second section of array
         {
            trash[i][j] = NULL;//makes array full of blank images
         }
      }
      int h = (int)(Math.random()*height);//generates random y location of trash
      int w = (int)(Math.random()*width);//generates random x location of trash
      for (int i = 0; i < trashnum; i++)//iterates trashnum amount of times
      {
         while (!trash[h][w].equals(NULL) || (h == playerX && w == playerY) || !obstacles[h][w].equals(NULL))//makes sure trash isnt on top of something
         {
            h = (int)(Math.random()*height);//regenerates random y location of trash
            w = (int)(Math.random()*width);//regenerates random x location of trash
         }
         trash[h][w] = getImage((int)(Math.random()*3+1),2);//sets trash[h][w] equal to 1.png in Trash(2)
      }
   }
   
   public void loadObstacles()
   {//loads the obstacles array
      for (int i = 0; i < obstacles.length; i++)//iterates through the first section of array
      {
         for (int j = 0; j < obstacles[0].length; j++)//iterates through the second section of array
         {
            obstacles[i][j] = NULL;//makes array full of blank images
         }
      }
      String str = obstacletext.substring(obstacletext.indexOf("\n")+1);
      while(str.length() > 0)
      {
         int x = Integer.parseInt(str.substring(0,str.indexOf(",")));
         str = str.substring(str.indexOf(",")+1);
         int y = Integer.parseInt(str.substring(0,str.indexOf(",")));
         str = str.substring(str.indexOf(",")+1);
         int t = Integer.parseInt(str.substring(0,str.indexOf("\n")));
         str = str.substring(str.indexOf("\n")+1);
         obstacles[y][x] = getImage(t,1);//sets obstacles[12][5] equal to 1.png in Obstacles(1)
      }
   }
   public void performAction()
   {//when spacebar is pressed
      if (!drawMT)//if master's text is not on screen
      {
         if (playerX >= mastX-1 && playerX <= mastX+1)//checks if player is within one x direction of master
         {
            if (playerY >= mastY-1 && playerY <= mastY+1)//checks if player is within one y direction of master
            {
               drawMT = true;//makes master's text on screen
               loadTrash();
            }
         }
      }
      else//if master's text is on screen
      {
         drawMT = false;//takes away master's text
         timer = true;//starts the timer
      }
   }
   
   public void paint(Graphics g)
   {//paints the panel
      if(!finished)
      {
         if(!started)
            g.drawImage(getImage(-1,-1),0,0,this);
         else
         {
            drawBackground(g);//draws the background
            drawObstacles(g);//draws the obstacles
            drawPlayer(g);//draws the player
            if (drawMT&&!timer)//checks if game should draw master's text on screen
               drawMasterText(g);//draws master text on screen
            if (timer)//if timer is started
            {
               drawMT = !timer;//makes sure player cannot freeze next to master
               drawTime(g);//draws time on the screen
               drawTrash(g);//draws trash on the screen
               drawScore(g);//draws the score on the screen
               drawLeft(g);//draws the amount of trash left on the screen
            }
         }
      }
      else
      {
         g.setColor(Color.black);
         g.fillRect(0,0,getWidth(),getHeight());
         g.setColor(Color.white);//makes the text white
         g.setFont(new Font("Monospaced", Font.PLAIN, 100));//changes font and size of font
         g.drawString("FINAL SCORE:",0,getHeight()/2-50);
         if(score<0)score=0;
         g.drawString("    "+score,0,getHeight()/2+50);
      }
   }
   
   public void drawLeft(Graphics g)
   {//draws the amount of trash left on the screen
      g.setColor(Color.white);//makes the text white
      g.setFont(new Font("Monospaced", Font.PLAIN, block/3*2));//changes font and size of font
      g.drawString("Trash: "+trashnum,width/2*block-2*block,block);//draws amount of trash left in a section of screen
   }
   
   public void drawScore(Graphics g)
   {//draws the player's score
      g.setColor(Color.white);//makes the text white
      g.setFont(new Font("Monospaced", Font.PLAIN, block/3*2));//changes font and size of font
      g.drawString("Score: "+score,block,block);//draws score in a section of the screen
   }
   
   public void drawTime(Graphics g)
   {//draws the time left on the screen
      g.setColor(Color.white);//makes the text white
      g.setFont(new Font("Monospaced", Font.PLAIN, block/3*2));//changes font and size of font
      g.drawString("Time: "+Math.floor(time * 10) / 10,width*block - 5*block,block);//draws the time in a section of the screen
   }
   
   public void drawMasterText(Graphics g)
   {//draws the text that the master says on the screen
      g.setColor(Color.black);//makes a rectangle color black
      g.fillRect(0,block*(height-2),block*width,block*2);//makes a black rectangle on the screen
      g.setColor(Color.white);//changes the font color to white
      g.setFont(new Font("Monospaced", Font.PLAIN, block/3*2));//changes font and size of font
      g.drawString(mast,block/2,block*height-block);//draws the master's text in a section of the screen
   }
   
   public void drawBackground(Graphics g)
   {//draws the background on the panel
      for (int i = 0; i < back.length; i++)//iterates through the first section of the array
      {
         for (int j = 0; j < back[0].length; j++)//iterates through the second section of the array
         {
            g.drawImage(back[i][j],j*block,i*block,this);//draws the specific background image on the screen
         }
      }
   }
   
   public Image getImage(int i,int t)//returns an image
   {//t is which array it came from -1- for NULL&PLAYER 0-back 1-obst 2-trash
      String str = "Resources/";//folder where subfolder's lie
      try
      {//tries
         switch(t)
         {//switches
            case 0://when t = 0
               str += "Background/";//subfolder background
               break;//breaks
            case 1://when t = 1
               str += "Obstacles/";//subfolder obstacles
               break;//breaks
            case 2://when t = 2
               str += "Trash/";//subfolder trash
               break;//breaks
         }
         str += ""+i;//selects image from folder
         return (Image)ImageIO.read(getClass().getResourceAsStream(str + ".png"));//returns specific image
      }
      catch(Exception e)//catches
      {
         System.err.println(e);//prints error
      }
      return NULL;//returns blank image if image does not exist
   }
   
   public void drawPlayer(Graphics g)
   {//draws the player on the screen
      g.drawImage(PLAYER,playerX*block,playerY*block,this);//draws the player on the screen
   }
   
   public void drawTrash(Graphics g)
   {//draws the trash
      for (int i = 0; i < trash.length; i++)//iterates through the first section of the array
      {
         for (int j = 0; j < trash[0].length; j++)//iterates through the second section of the array
         {
            g.drawImage(trash[i][j],j*block,i*block,this);//draws trash image at location
         }
      }
   }
   
   public void drawObstacles(Graphics g)
   {//draws the obstacles
      for (int i = 0; i < obstacles.length; i++)//iterates through the first section of the array
      {
         for (int j = 0; j < obstacles[0].length; j++)//iterates through the second section of the array
         {
            g.drawImage(obstacles[i][j],j*block,i*block,this);//draws the obstacle at location
         }
      }
   }
   
   public void keyReleased(KeyEvent e)
   {//what happens when you release a key
      
   }
   
   public void keyPressed(KeyEvent e)
   {//what happens when you press a key
      if(!started)
      {
         started = true;
      }
      else
      {
         if(finished)
         {
            finished = false;
         }
         else
         {
            try
            {//tries
               if (!drawMT)//if master's text is not on the screen
               {
                  if (e.getKeyCode() == KeyEvent.VK_UP && (playerY > 0 ))//checks if player can move
                  {
                     if (obstacles[playerY-1][playerX].equals(NULL))
                        playerY--;//moves the player
                     else
                        if (timer)
                           score -=100;
                  }
                  else if (e.getKeyCode() == KeyEvent.VK_DOWN && (playerY < height-1 ))//checks if player can move
                  {
                     if (obstacles[playerY+1][playerX].equals(NULL))
                        playerY++;//moves the player
                     else
                        if (timer)
                           score -=100;
                  }
                  else if (e.getKeyCode() == KeyEvent.VK_LEFT && (playerX > 0 ))//checks if player can move
                  {
                     if (obstacles[playerY][playerX-1].equals(NULL))
                        playerX--;//moves the player
                     else
                        if (timer)
                           score -=100;
                  }
                  else if (e.getKeyCode() == KeyEvent.VK_RIGHT && (playerX < width-1))//checks if player can move
                  {
                     if (obstacles[playerY][playerX+1].equals(NULL))
                        playerX++;//moves the player
                     else
                        if (timer)
                           score -=100;
                  }
               }
               if (e.getKeyCode() == KeyEvent.VK_SPACE)//if pressed spacebar
                  performAction();//perform an action
            }
            catch (Exception x)
            {//catches
               System.err.println(x);//prints an error
            }
         }
      }
   }
   
   public void keyTyped(KeyEvent e)
   {//what happens when you type a key (unreliable)
   }
   
   public void gameOver()
   {//what happens when game is over
   }
   
   public void run()
   {//runs
      while(time>0 && trashnum>0)//while game is active
      {
         try
         {//tries               
            if (timer&&!trash[playerY][playerX].equals(NULL))//if player is on same tile as trash
            {
               score += scoremod;//adds onetrashnumth of 5000 to score
               trash[playerY][playerX] = NULL;//removes the trash
               trashnum--;//changes the amount of trash left
            }
         
            Thread.sleep(50);//sleeps
            if (timer)//if clock is on
               time -= 0.05;//remove .05 seconds from time
            repaint();//repaints the screen
         }
         catch (Exception e)//catches
         {
            System.err.print(e);//prints out error
         }
      }
      finished = true;
      repaint();
      while(finished)
      {
         repaint();
      }
      finished = true;
      repaint();
      main.masterScore += score;
      main.changePanel(-2);
   }
}