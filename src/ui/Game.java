
package ui;
import java.applet.*;
import java.util.ArrayList;
import java.util.Arrays;
public class Game
{
  private SBDietGameUI grid;
  private int userCol;
  private int msElapsed;
  private int timesGet;
  private int timesAvoid;
  private int userRow;
  private AudioClip introSong;
  private String user;
  private int future;
  private boolean buff;
  private AudioClip gameOver;
  private int start;
  private AudioClip winning;
  private SBDietUI root;
  
  private int calories;
  String foodsArray[] = {"apple.png","bread.png","burger.png","carrot.png",
  "cheese.png","corn.png","egg.png","fish.png","frice.png","lettuce.png"
      ,"milk.png","pie.png","pizza.png","ramen.png","rice.png","salad.png",
"soup.png","spagetti.png","steak.png","sushi.png","tomato.png", "soccer.png", "hola.png",
  "tennis.png", "bicycle.png", "badminton.png", "volleyball.png", "basketball.png"};
  ArrayList<String> foods = new ArrayList<>(Arrays.asList(foodsArray));

  Integer caloriesArray[] = {95,80,270,53,69,85,77,100,350,50,103,296,350,356,110,
  60,118,200,128,50,27, -250, -50, -100, -100, -80, -120, -150};
  ArrayList<Integer> foodCalories = new ArrayList<>(Arrays.asList(caloriesArray));
  
  public Game(SBDietUI root)
  {
    this.root = root;
    grid = new SBDietGameUI(6, 10,"dining2.jpg");
    userCol = 4;
    userRow = grid.getNumRows()-1;
    msElapsed = 0;
    timesGet = 0;
    timesAvoid = 0;
    updateTitle();
    grid.setImage(new Location(5, userCol), "charStand.png");
    grid.setImage(new Location(0, 9), "goal.png");
    introSong = Applet.newAudioClip(this.getClass().getResource("song.wav"));
    user = "charStand.png";
    future = 0;
    buff = false;
    gameOver = Applet.newAudioClip(this.getClass().getResource("game over.wav"));
    start = 0;
    winning = Applet.newAudioClip(this.getClass().getResource("win.wav"));
  }
  
  public void play()
  {
    introSong.loop();
    while (!isGameOver())
    { 
      handleKeyPress();
      grid.pause(100);
      if (msElapsed % 300 == 0)
      {
        scrollLeft();               //Move all the items on the screen downward one cell
        populateRightEdge();        //generate new items on the top of the screen 
      }
      if(buff)              //controlling the buff time and updates
          start+=100;      
      if((double)start/1000>=10){
          buff = false;
          if(grid.getImage(new Location(userRow,userCol)).equals("userFly.png")||grid.getImage(new Location(userRow,userCol)).equals("userFlyR.png")||grid.getImage(new Location(userRow,userCol)).equals("userFlyL.png"))
              grid.setImage(new Location(userRow,userCol),null);
          userRow = grid.getNumRows()-1;
          grid.setImage(new Location(userRow,userCol), "charStand.png");
          user = "charStand.png";
          start = 0;
      }
      updateTitle();            //unpdating information of the top of the stage bar...
      msElapsed += 100;
    }
    
    if(win()){
         introSong.stop();
        winning.play();
        grid.showMessageDialog("Winning!", "windialog.png");  
        root.showWinningEndStory();
    }
    else{                   //Lost
         introSong.stop();
        gameOver.play();
        if(calories < 2000)
            grid.showMessageDialog("Starving!","losedialog1.png");
        else
            grid.showMessageDialog("Binge Eating!","losedialog2.png");  
        root.showLosingEndStory();
    }
    grid.close();
   
  }
  
  public void handleKeyPress()
  {
      int key = grid.checkLastKeyPressed();
      if(key==37&&userCol>0&&userCol<grid.getNumCols()){                //left
          if(userRow!=0)        // if item on top of the char
                handleCollision(new Location(userRow-1,userCol));           
          if(buff)  
              user = "userFlyL.png";          
          handleCollision(new Location(userRow,userCol-1));
          grid.setImage(new Location(userRow,userCol), null);
          userCol--;
          grid.setImage(new Location(userRow,userCol), user);     

      }
      else if(key==39&&userCol>=0&&userCol<grid.getNumCols()-1){
          if(userRow!=0)
                handleCollision(new Location(userRow-1,userCol));
          if(buff)
              user = "userFlyR.png";          
          handleCollision(new Location(userRow,userCol+1));         //right
          grid.setImage(new Location(userRow,userCol), null);
          userCol++;
          grid.setImage(new Location(userRow,userCol), user);

      }
      else if(buff&&key==38&&userRow>0&&userRow<grid.getNumRows()){
          user = "userFly.png";
          handleCollision(new Location(userRow-1,userCol));
          if(userRow!=grid.getNumRows()-1)
              handleCollision(new Location(userRow+1,userCol));         //up
          grid.setImage(new Location(userRow,userCol), null);
          userRow--;
          grid.setImage(new Location(userRow,userCol), user);
      }
      else if(buff&&key==40&&userRow>=0&&userRow<grid.getNumRows()-1){
          user = "userFly.png";
          handleCollision(new Location(userRow+1,userCol));             
          if(userRow!=0)
              handleCollision(new Location(userRow-1,userCol));         //down
          grid.setImage(new Location(userRow,userCol), null);
          userRow++;
          grid.setImage(new Location(userRow,userCol), user);
      }
  }
   
  public void populateRightEdge()
  {
      int rand = (int)(Math.random()*72);
      int pos = (int)(Math.random()*grid.getNumCols());
      if(rand==55)
          grid.setImage(new Location(0,pos),"powerFly.png");
      else if(rand<foods.size())
         grid.setImage(new Location(0,pos), foods.get(rand));
  }

  
  //control items moving
  public void scroll(int c)
  {
      for(int r=grid.getNumRows()-1;r>=0;r--){
          if(grid.getImage(new Location(r,c))!=null&&!grid.getImage(new Location(r,c)).equals(user)){
              if(userRow==0)
                  grid.setImage(new Location(userRow,userCol), user);
              if(c==userCol&&r==userRow-1){//top of user
                  handleCollision(new Location(userRow-1,userCol));
                  grid.setImage(new Location(userRow-1,userCol),null);
              }
              else if(r==grid.getNumRows()-1)//ground
                  grid.setImage(new Location(r,c), null);
              else{//scroll
                  String image = grid.getImage(new Location(r,c));
                  grid.setImage(new Location(r,c),null);
                  r++;
                  grid.setImage(new Location(r,c), image);     
              }
          }
      }
  }
  
  public void scrollLeft()
  {  
      for(int x=0;x<grid.getNumCols();x++)
          scroll(x);  
  }
  
  public void handleCollision(Location loc)
  {
      if(grid.getImage(loc)!=null&&grid.getImage(loc).equals("powerFly.png")){
          buff = true;
          grid.setImage(new Location(userRow,userCol),"userFly.png");
          user = "userFly.png";
      }
      if(grid.getImage(loc)!=null){
          timesGet++;
          if(!grid.getImage(loc).equals("powerFly.png")){
            calories+=foodCalories.get(foods.indexOf(grid.getImage(loc)));
          }
          grid.setImage(new Location(userRow,userCol),"charStand.png");
          user = "charStand.png";
      }  
      grid.setImage(loc, null);
  }
     
  public void updateTitle()
  { 
      grid.setTitle("Total Calories: " + calories +"   Time elaspsed: " + msElapsed/1000 + "s    Get: " + timesGet +"   Buff Time Left: "+(10-start/1000));
  }
  
  public boolean isGameOver()
  {
      if(msElapsed/1000>=61)
          return true;
      return false;
  }
  public boolean win(){
    if(msElapsed/1000>=61 && calories>=2000&&calories<=2300)
        return true;
    return false;
                
  }
}