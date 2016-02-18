package Level.CS;

import java.awt.Image;

public class CSPlayer
{
   int x,y;
   private Image img;

   public CSPlayer(int x, int y, Image img)
   {
      this.x = x;
      this.y = y;
      this.img = img;
   }
   
   public Image getImage()
   {
      return img;
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