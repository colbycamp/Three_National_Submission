package Level.Entities;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Main.MAIN;
import Level.*;

public class Entity extends BackImage{
   //x,y = cords. facing = directions 0 = up, 1 = right, 2 = down, 3 = left.
   public int X,Y,facing;
   //mainly implemented only for Testificate
   public static boolean talking = false;
   //same as one above
   private boolean manager = false;
   //cycle is used to determine which picture is being used for the animation
   public int cycle = 0;
   //used to start animation thread
   public boolean animate = false;
   //used to stop threads
   public boolean endThread = false;
   
   public Entity(int x, int y, int id) {
      super(id);
      if(id == 1)
         manager = true;
      X = x;
      Y = y;
      facing = 2;
      
      thread.start();
   }
   
   public int getXLocation() {
   
      return X;
   
   }
   
   public int getYLocation() {
   
      return Y;
   }
   
   public int getGridX() {
   
      return (X/MAIN.block);
   
   }
   
   public int getGridY() {
   
      return (Y/MAIN.block);
   }
   
   public int getFacing() {
   
      return facing;
   
   }
   
   public boolean isManager() {
   
      return manager;
   
   }
   
   public void up(Level l) {
      
      if(!endThread)
      {
         facing = 0;
         setImage(getImages(facing*cycleAmount));
         if(l.imageB[(Y/MAIN.block)-1][X/MAIN.block].getID() < 100)
            for(int i = 0; i < l.entities.length; i++)
            {
               if(l.entities[i].getYLocation() == Y-MAIN.block && l.entities[i].getXLocation() == X)
                  break;
               if(i == l.entities.length-1)
               {
                  animate = true;
                  cycle = 0;
               }
            }
      }
   }
   
   public void down(Level l) {
      
      if(!endThread)
      {
         facing = 2;
         setImage(getImages(facing*cycleAmount));
         if(l.imageB[(Y/MAIN.block)+1][X/MAIN.block].getID() < 100)
            for(int i = 0; i < l.entities.length; i++)
            {
               if(l.entities[i].getYLocation() == Y+MAIN.block && l.entities[i].getXLocation() == X)
                  break;
               if(i == l.entities.length-1)
               {
                  {
                     animate = true;
                     cycle = 0;
                  }
               }
            }           
      }
   }
   public void left(Level l) {
   
      if(!endThread)
      {
         facing = 3;
         setImage(getImages(facing*cycleAmount));
         if(l.imageB[Y/MAIN.block][(X/MAIN.block)-1].getID() < 100)
            for(int i = 0; i < l.entities.length; i++)
            {
               if(l.entities[i].getYLocation() == Y && l.entities[i].getXLocation()+MAIN.block == X)
                  break;
               if(i == l.entities.length-1)
               {
                  animate = true;
                  cycle = 0;
               }
            } 
      }           
   }
   
   public void right(Level l) {
      
      if(!endThread)
      {
         facing = 1;
         setImage(getImages(facing*cycleAmount));
         if(l.imageB[Y/MAIN.block][(X/MAIN.block)+1].getID() < 100)
            for(int i = 0; i < l.entities.length; i++)
            {
               if(l.entities[i].getYLocation() == Y && l.entities[i].getXLocation()-MAIN.block == X)
                  break;
               if(i == l.entities.length-1)
               {
                  animate = true;
                  cycle = 0;
               }
            }
      }
   }
   
   public void animate()
   {
      setImage(getImages((facing*cycleAmount)+cycle));
   }
   
   public void loadImage()  {
      
      // Images made by paint are .png we can change later
      try {
         for(int a = 0; a < 4; a++) {
            setImage((Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Images/" + getID() + "/" + a + ".png")));
            BufferedImage bi = (BufferedImage) getImage();
            cycleAmount =  bi.getWidth()/MAIN.block;
            for(int i = 0; i < cycleAmount; i++) {
               Image iTemp = (BufferedImage) bi.getSubimage(i*MAIN.block,0,MAIN.block,MAIN.block);
               getImages().add(iTemp);
            }
         }
         
         setImage(getImages(6));
      }
      catch(Exception e)
      {
         System.out.println("****Problems with IMAGE in BackImage****"); 
         e.printStackTrace();
      }
   }
   
   public void run() {
      try{
         while(true)
         {
            if(animate && cycle < cycleAmount)
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
            else
               animate = false;
            if(endThread)
               break;
         }
      }
      catch(InterruptedException e) {
         System.out.println("****ENTITY ANIMATION ID BROKEN****");
      }      
   }
   
   public int checkForActionSides(Level l) {
      return 0;
   }
   
   public int checkForActionFacingSide(Level l) {
      return 0;
   }
}