package Level.CS;
import java.awt.Image;

public class Ball 
{
   private int x,y,vX,vY;
   private Image img;
   
   public Ball(int x, int y, Image img)
   {
      this.x = x;
      this.y = y;
      this.img = img;
      vX = 0;
      vY = 0;
   }
   
   public Image getImage()
   {
      return img;
   }
   
   public int getXVelocity() 
   {
      return vX;
   }

   public void setXVelocity(int vX) 
   {
      this.vX = vX;
   }

   public int getYVelocity() 
   {
      return vY;
   }

   public void setYVelocity(int vY)
   {
      this.vY = vY;
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