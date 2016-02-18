package Level.Entities;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.util.Random;

import Main.MAIN;
import Level.*;

public class Testificate extends Entity implements Runnable
{
   public Level l;
   public static int lineNum;
   
   public Testificate(int x, int y, int id, Level l) 
   {
      super(x,y,id);
   }
   
   public void move(int direction) 
   {
      if(!talking && !endThread)
      {
         switch(direction) 
         {
            case 0:
               up(l);
               break;
            case 1:
               down(l);
               break;
            case 2:
               left(l);
               break;
            case 3:
               right(l);
               break;
         }
         try{
            while(animate && cycle < cycleAmount)
            {
               Thread.sleep((300/cycleAmount));
               animate();
               cycle++;
               
               switch(facing)
               {
                  case 0:
                     Y-= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        Y-=(Y%MAIN.block);
                     break;
                  case 1:
                     X+= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        X+=(MAIN.block-(X%MAIN.block));
                     break;
                  case 2:
                     Y+= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        Y+=(MAIN.block-(Y%MAIN.block));
                     break;
                  case 3:
                     X-= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        X-=(X%MAIN.block);
                     break;
               }
            }
            animate = false;
         }
         catch(InterruptedException e)
         {
            System.out.println("****TESTIFICATE THREAD INTERRUPTED****");
            e.printStackTrace();
         }
      }
   }
   
   public void moveBack(int direction) 
   {
      if(!talking && !endThread)
      {
         try{
            switch(direction) 
            {
               case 0:
                  down(l);
                  break;
               case 1:
                  up(l);
                  break;
               case 2:
                  right(l);
                  break;
               case 3:
                  left(l);
                  break;
            }
         
            while(animate && cycle < cycleAmount)
            {
               Thread.sleep((300/cycleAmount));
               animate();
               cycle++;
               
               switch(facing)
               {
                  case 0:
                     Y-= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        Y-=(Y%MAIN.block);
                     break;
                  case 1:
                     X+= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        X+=(MAIN.block-(X%MAIN.block));
                     break;
                  case 2:
                     Y+= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        Y+=(MAIN.block-(Y%MAIN.block));
                     break;
                  case 3:
                     X-= MAIN.block/cycleAmount;
                     if(cycle == cycleAmount)
                        X-=(X%MAIN.block);
                     break;
               }
            }
            animate = false;
         }
         catch(InterruptedException e)
         {
            System.out.println("****TESTIFICATE THREAD INTERRUPTED****");
            e.printStackTrace();
         }
      }
   }
      
   public void run() 
   {
      while(true && !endThread) 
      {
         try 
         {
            Random r = new Random();
            int time = r.nextInt(5000)+1000;
            int direction = r.nextInt(4);
            Thread.sleep(time);
            move(direction);
            time = r.nextInt(5000)+1000;
            Thread.sleep(time);
            moveBack(direction);
         } 
         catch(InterruptedException e) 
         {
            System.out.println("SOMETHING MESSED UP.");
            e.printStackTrace();
         }
      }
   }
}