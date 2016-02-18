package Level;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.Thread;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;

import Main.MAIN;

public class BackImage implements Runnable{

   private Image IMAGE;
   private ArrayList <Image> images = new ArrayList<Image>();
   private int imageCount = 0;
   private int ID;
   private int animatedTimer = 1000;
   public static int cycleAmount = 0;
   public static Thread thread;
   public int levelID;
   
   public BackImage(int id) {
   
      ID = id;   
      loadImage();
      
      levelID = MAIN.currentLevel;
      
      thread = new Thread(this);
   }
   
   public void loadImage()  {
      
      // Images made by paint are .png we can change later
      try {
         for(int a = 0; a < 4; a++) {
            IMAGE = (Image)ImageIO.read(getClass().getResourceAsStream("/Resources/Images/" + ID + ".png"));
            BufferedImage bi = (BufferedImage) IMAGE;
            cycleAmount =  bi.getWidth()/MAIN.block;
            for(int i = 0; i < cycleAmount; i++) {
               Image iTemp = (BufferedImage) bi.getSubimage(i*MAIN.block,0,MAIN.block,MAIN.block);
               images.add(iTemp);
            }
            if(cycleAmount > 1)
               thread.start();
            else
               thread = null;
         }
      }
      catch(Exception e)
      {
         System.out.println("****Problems with IMAGE in BackImage****"); 
         e.printStackTrace();
      }
   }
   
   public Image getImage() {
   
      return IMAGE;
   
   }
   
   public void setImage(Image IMAGE) {
   
      this.IMAGE = IMAGE;
      
   }
   
   public ArrayList <Image> getImages() {
   
      return images;
   
   }
   
   public Image getImages(int i) {
   
      return images.get(i);
   
   }
   
   public int getID() {
      
      return ID;
   }
   
   public void animate()
   {
      IMAGE = images.get(imageCount);
   }
   
   public void run() {
      try{
         while(true && images.size() > 1 && MAIN.currentLevel == levelID)
         {
            Thread.sleep(1000/images.size());
            animate();
            if(imageCount >= images.size())
               imageCount = 0;
         }
      }
      catch(InterruptedException e) {
         System.out.println("****ENTITY ANIMATION ID BROKEN****");
      }
   }
}