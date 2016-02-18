package Level.CS;

import java.awt.Image;

public class Hacker 
{
   private int x,y,hitCount;
   private Image img;

   public Hacker(int x, int y, Image img)
   {
      this.x = x;
      this.y = y;
      hitCount = 0;
      this.img = img;
   }

   public Image getImage()
   {
      return img;
   }
   
   public int hitCount() 
   {
      return hitCount;
   }

   public void setHitCount(int hitCount) 
   {
      this.hitCount = hitCount;
   }

   public int getX() 
   {
      return x;
   }

   public void setX(int x) 
   {
      this.x = x;
   }

   public int getY() 
   {
      return y;
   }

   public void setY(int y) 
   {
      this.y = y;
   }

}