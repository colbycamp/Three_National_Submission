package Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.File;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.lang.Thread;
import java.net.URL;

/**
 *Soundmanager is in charge of all music being played during the game.
 *Automatically sets up the background audio and keeps it going.
 */
public class SoundManager implements Runnable
{
   /**all used audio occuring right now*/
   private ArrayList<Audio> audio = new ArrayList<Audio>();
   /**path for all background*/
   private String backgroundPath = "/Resources/Sound/BackgroundMusic/31.wav";
   /**all background audio*/
   private URL backgroundURL;
   /**the clip that is being used as background currently*/
   private Clip currentBackgroundClip;
   
   /**
    *Starts by loading all the background audio.
    *Starts thread to change background audio.
    */
   public SoundManager()
   {
      loadBackground();
      
      Thread thread = new Thread(this);
      thread.start();
   }
   /**
    *loads all background audio into background audio.
    */   
   public void loadBackground()
   {
      backgroundURL = getClass().getResource(backgroundPath);
   }
   /**
    *@param soundPath
    *If the audio is already played than it will replace the old and start it up.
    */ 
   public void playSound(String soundPath)
   {
      boolean contains = false;
      
      for(Audio a: audio)
      {
         if(a.audioName.equals(soundPath))
         {
            int i = audio.indexOf(a);
            audio.get(i).playAudio();
            contains = true;
         }
      }
      if(!contains)
      {
         try
         {
            URL url = getClass().getResource("/Resources/Sound/" + soundPath + ".wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
         
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            audio.add(new Audio(soundPath,clip));
            audio.get(audio.size()-1).playAudio();
         }
         catch(IOException e) {
            System.out.println("****SOUND PROBLEMS****");
            e.printStackTrace();
         }
         catch(UnsupportedAudioFileException e)
         {
            System.out.println("****DID NOT ADD WRITE TYPE OF FILE*****");
            e.printStackTrace();
         }
         catch(LineUnavailableException e)
         {
            System.out.println("****LINE UNAVAILABLE SOUNDMANAGER*****");
            e.printStackTrace();
         }
      }
   }
   /**
    *@param soundPath that should be played.
    *Stops the current audio being played.
    */
   public void stopAudio(String soundPath)
   {
      for(Audio a: audio)
      {
         if(a.audioName.equals(soundPath))
         {
            int i = audio.indexOf(a);
            audio.get(i).stopAudio();
         }
      }
   }
   /**
    *Starts the background audio by choosing it randomely.
    */   
   public void playBackgroundAudio()
   {
      try
      {
         System.out.println(backgroundURL);
         AudioInputStream audioStream = AudioSystem.getAudioInputStream(backgroundURL);
            
         Clip clip = AudioSystem.getClip();
         clip.open(audioStream);
         currentBackgroundClip = clip;
         currentBackgroundClip.start();
      }
      catch(UnsupportedAudioFileException e)
      {
         System.out.println("****DID NOT ADD WRITE TYPE OF FILE*****");
         e.printStackTrace();
      }
      catch(LineUnavailableException e)
      {
         System.out.println("****LINE UNAVAILABLE SOUNDMANAGER*****");
         e.printStackTrace();
      }
      catch(IOException e) {
         System.out.println("****BACKGROUND PROBLEMS OR MOST LIKELY ALL OF THE MUSIC HAS BEEN DOWNLOADED****");
      }
      System.out.println("STARTED PLAYING");
   }
   /**
    *Stops whatever background audio is being played.
    */
   public void stopBackgroundAudio()
   {
      if(currentBackgroundClip.isRunning())
         currentBackgroundClip.stop();
   }
   /**
    *Responsible for changing the audio for the background.
    */
   public void run()
   {
      while(true)
      {       
         try
         {
            Thread.sleep(1000);
            if(currentBackgroundClip != null && !currentBackgroundClip.isRunning())
               playBackgroundAudio();
         }
         catch(InterruptedException e)
         {
            System.out.println("****PROBLEM WITH BACKGROUND SOUND THREAD****");
         }
      }
   }
   /**
    *Inner class that holds information for the clip.
    */
   private class Audio
   {
      /**name of file for the clip*/
      public String audioName = "";
      /**clip that is used for this audio*/
      public Clip clip;
      /**
       *@param soundPath
       *@param clip
       */
      public Audio(String soundPath,Clip clip)
      {
         this.audioName += soundPath;
         this.clip = clip;
      }
      /**
       *@return audio
       */
      public Audio getAudio()
      {
         return this;
      }
      /**
       *plays audio if not running.
       */      
      public void playAudio()
      {
         if(clip.isRunning())
            clip.stop();
         clip.setFramePosition(0);
         clip.start();
      }
      /**
       *stops the audio if it is running.
       */      
      public void stopAudio()
      {
         if(clip.isRunning())
            clip.stop();
      }
   }
}