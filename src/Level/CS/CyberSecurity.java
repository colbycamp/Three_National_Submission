package Level.CS;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.MAIN;

public class CyberSecurity extends JPanel implements KeyListener
{
   //initializes entities
   private Hacker[] hackers;
   private Ball ball;
   private CSPlayer player;
   
   //initializes variables and constants     
   private int rightAcc, leftAcc, level, hackNum, hackerX, hackerY, ballX, ballY, playerX, playerY, destroyedHackers, lives;
   private Image controls,background, hackerImg, ballImg, playerImg;
   
   private double score = 0;
   
   private boolean left, right;
   private boolean space = true;
   private boolean endTog = true;
   private boolean controlTog = true;

   
   private final int maxSpeed = 6;
   private final int xBound = 704;
   private final int yBound = 544;
   private int hackerTimer = 0; 
   
   private MAIN main;

   public CyberSecurity(int level, MAIN main)
   {
      //sets level 
      this.level = level;
      this.main = main;
      
      //sets up hackers 
      hackNum = 14 + level*14;
      hackers = new Hacker[hackNum];
      
      //set up the lives
      
      lives = 6 - level; 
   
      //allows for keyboard input
      addKeyListener(this);
      
      //gets focus
      this.requestFocusInWindow();
      this.setFocusable(true);
   
      //initiates images 
      controls = getImageFile("/Resources/CS/C"+level+".png");
      background = getImageFile("/Resources/CS/B"+level+".png");
      hackerImg = getImageFile("/Resources/CS/"+level+".png");
      ballImg = getImageFile("/Resources/CS/Ball"+level+".png");
      playerImg = getImageFile("/Resources/CS/CSPlayer.png");
      
      //gets image constants 
      hackerX = ((BufferedImage)hackerImg).getWidth();
      hackerY = ((BufferedImage)hackerImg).getHeight();
      ballX = ((BufferedImage)ballImg).getWidth();
      ballY = ((BufferedImage)ballImg).getHeight();
      playerX = ((BufferedImage)playerImg).getWidth();
      playerY = ((BufferedImage)playerImg).getHeight();     
   
      //creates hackers 
      int tempCount = 0;   
      for (int currentRow = 0; currentRow < level+1; currentRow++)
      {
         for(int i = 0; i < xBound/hackerX - 2; i++)
         {
            Hacker temp;
            temp = new Hacker(i*hackerX, currentRow*hackerY,hackerImg);
            
            hackers[tempCount] = temp;
            tempCount++;
         }
      }
   
      //creates ball 
      ball = new Ball(xBound/2 - ballX/2, yBound - 110, ballImg);
   
      //creats player 
      player = new CSPlayer(xBound/2 - playerX/2, yBound - 90, playerImg);
      
      //timer for updates 
      ActionListener animate = 
         new ActionListener() 
         {
            public void actionPerformed(ActionEvent ae)
            {
               collision();
               if(hackerTimer == 110)
                  updateHackerPos();
               if(score < 0)
                  score = (double)0;
               updateBallPos();
               updateCSPlayer();
               hackerTimer++;
               repaint();           
            }
         };
      // Change time for repaint here. #Dr. Who
      javax.swing.Timer timeLord = new javax.swing.Timer(12,animate);
      timeLord.start();
   }
   
   //gets image file 
   private Image getImageFile(String filename)
   {
      try
      {
         System.out.println(filename);
         return (Image)ImageIO.read(getClass().getResourceAsStream(filename));
      }
      
      catch(Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }
   
   // updates player position
   private void updateCSPlayer()
   {
      if(left && player.getX() > 0)
      {
         if(leftAcc < 8)
            leftAcc++;
            
         player.setX(player.getX() - leftAcc);
      }
      
      if(right && player.getX() + playerX < xBound)
      {   
         if(rightAcc < 8)
            rightAcc++;
            
         player.setX(player.getX() + rightAcc);
      }
       
      if(player.getX() < 0)
         player.setX(0);
         
      if(player.getX() > xBound - playerX)
         player.setX(xBound - playerX);
         
      if((ball.getX() == xBound/2 - ballX/2 && ball.getY() == yBound - 110))
      {
         player.setX(xBound/2 - playerX/2);
         player.setY(yBound - 90);
      }
   }
   
   // updates hacker position
   private void updateHackerPos()
   {
      for (int i = 0; i < hackNum; i++)
      {
         if(hackers[i].getX() < xBound - hackerX)
            hackers[i].setX(hackers[i].getX()+hackerX);
         else     
            hackers[i].setX(0);
      }
      hackerTimer = 0;
   }
   
   // updates ball position
   private void updateBallPos()
   {
      ball.setX(ball.getX() + ball.getXVelocity());
      ball.setY(ball.getY() + ball.getYVelocity());  
   }
   
   private void reset()
   {
      score -= (double)500;
      ball.setX(xBound/2 - ballX/2);
      ball.setY(yBound - 110);
      ball.setXVelocity(0);
      ball.setYVelocity(0);
      player.setX(xBound/2 - playerX/2);
      player.setY(yBound - 90);
   }
   
   private void collision()
   {
      //side walls collision detection
      if(ball.getX() + ballX >= xBound || ball.getX() <= 0)
      {
         ball.setXVelocity(-1 * ball.getXVelocity());
         main.soundManager.playSound("CSHitWall");
      }  
      //upper wall collision detection
      if(ball.getY() < 0)
      {
         ball.setYVelocity(-1 * ball.getYVelocity());
         main.soundManager.playSound("CSHitWall");
      }
   
      //player collision detection
      if(ball.getY() + ballY >= player.getY() && (ball.getX() >= player.getX() && ball.getX() <= (player.getX() + playerX)) && (ball.getY() + ballX <= player.getY() + playerY))
      {
         ball.setXVelocity(ball.getXVelocity() + rightAcc - leftAcc);
                            
         if(ball.getXVelocity() > maxSpeed)
            ball.setXVelocity(maxSpeed);
            
         if(ball.getXVelocity() < -1*maxSpeed)
            ball.setXVelocity(-1* maxSpeed);
               
         ball.setYVelocity(-1 * ball.getYVelocity());
         
         main.soundManager.playSound("CSHitWall");

      }
      
      //lower wall collision detection
      if(ball.getY()> player.getY() - ballY/1.5)
      {
         lives--;
         space = true;
         reset();
      }
   
      //hacker collision detection
      for(int i = 0; i < hackers.length; i++)
      {
         if(ball.getX() + ballX >= hackers[i].getX() && ball.getX() <= hackers[i].getX() + hackerX)
         {
            if(ball.getY() + ballY >= hackers[i].getY() && ball.getY() <= hackers[i].getY() + hackerY)
            {
               if(hackers[i].hitCount() == 0)
               {
                  if(ball.getX() + ballX - ball.getXVelocity() <= hackers[i].getX() || ball.getX() - ball.getXVelocity() >= hackers[i].getX() + hackerX)
                  {
                     ball.setXVelocity(-1 * ball.getXVelocity());
                     hackers[i].setHitCount(hackers[i].hitCount() + 1);
                  }
                  
                  else
                  {
                     ball.setYVelocity(-1 * ball.getYVelocity());
                     hackers[i].setHitCount(hackers[i].hitCount() + 1);
                  }
                  
                  main.soundManager.playSound("CSHitHacker");
                  score += (double)((double)5000 / (double)hackers.length);
               }
            }
         }
         
         //counts destroyed hackers
         if(hackers[i].hitCount() > 0)
            destroyedHackers++;
      }
      
      //win   
      if(endTog && destroyedHackers == hackers.length)
      {
         if(score < 0)
            score = (double)0;
         main.masterScore += (int)Math.ceil(score);
         System.out.println("Win: "+ main.masterScore);
         endTog = false;
         main.changePanel(-2);
      }
         
      //lose
      if(endTog && lives == 0)
      {
         if(score < 0)
            score = (double)0;
         main.masterScore += (int)Math.ceil(score);
         System.out.println("Lose: "+ main.masterScore);
         endTog = false;      
         main.changePanel(-2);
      }             
      
      destroyedHackers = 0;
   }
   
   public void paint(Graphics g)
   {
      //paints background
      g.drawImage(background,0,0,this);
      
      //paints hackers 
      for(int i = 0; i < hackers.length; i++)
      {
         if(hackers[i].hitCount() == 0)
            g.drawImage(hackers[i].getImage(),hackers[i].getX(), hackers[i].getY(),this);
      }
   
      //paints player
      g.drawImage(player.getImage(),player.getX(), player.getY(),this);
   
      //paints ball
      g.drawImage(ball.getImage(),ball.getX(), ball.getY(),this);
      
      //paints score and lives
      g.setColor(Color.BLACK);
      g.fillRect(0,yBound - 40, xBound, 40);
      
      g.setColor(Color.GRAY);
      g.drawRect(0,yBound - 40, xBound-1, 38);
            
      g.setColor(Color.WHITE);
      g.setFont(g.getFont().deriveFont(28f));
      g.drawString("Lives: "+ lives,xBound - 130,yBound-10);
      g.drawString("Score: "+ (int)(Math.ceil(score)),20,yBound-10); 
      
      //Paints contols 
      if(controlTog)
            g.drawImage(controls,0,0,this);
   }
   
   public void keyPressed(KeyEvent event)
   {       
      switch(event.getKeyCode())
      {
         //moves player left 
         case KeyEvent.VK_LEFT:
            if(!(ball.getX() == xBound/2 - ballX/2 && ball.getY() == yBound - 110))
               left = true;
            else
               right = false;
            break;
      
         //moves player right 
         case KeyEvent.VK_RIGHT:
            if(!(ball.getX() == xBound/2 - ballX/2 && ball.getY() == yBound - 110))
               right = true;
            else
               right = false;
            break;
          
         //starts game
         case KeyEvent.VK_SPACE:
         if(!controlTog)
         {
            if(space && ball.getX() == xBound/2 - ballX/2 && ball.getY() == yBound - 110)
            {
               space = false;
               ball.setYVelocity(-1*maxSpeed+2);
               ball.setXVelocity(-1*maxSpeed+2);
            }
         }
         else
            controlTog = false;
            break;
      }
   }
   
   public void keyReleased(KeyEvent event) 
   {
      switch(event.getKeyCode())
      {
         //stops left movement 
         case KeyEvent.VK_LEFT:
            left = false;
            leftAcc = 0;
            break;
            
         //stops right movement    
         case KeyEvent.VK_RIGHT:
            right = false;
            rightAcc = 0;
            break;
      }
   }
   public void keyTyped(KeyEvent event) {}
}
