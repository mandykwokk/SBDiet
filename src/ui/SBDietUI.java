/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author hanli
 */
public class SBDietUI {
    // mainPane weight && height
    private int paneWidth = 1500;
    private int paneHeight = 900;
    private Stage primaryStage;

    private BorderPane mainPane;
    private Pane storyPane;
    private Timeline animation;
    private int currentIndex;
    // SplashScreen
    private ImageView splashScreenImageView;
    private Pane splashScreenPane;
    //AboutScreen
    private Pane aboutPane;
    //gamePlayScreen
    private Game game;

    private SBDietEventHandler handler;
    
    public SBDietUI(Stage primaryStage){
        this.primaryStage = primaryStage;
        handler = new SBDietEventHandler(this);
    }
    
    public void startUI(){
        mainPane = new BorderPane();
        mainPane.resize(paneWidth, paneHeight);
        initSplashScreen();
        initAboutScreen();
        mainPane.setCenter(splashScreenPane);
    }
    

    public void initSplashScreen(){
        splashScreenPane = new Pane();
        Image splashScreenImage = loadImage("splashScreenImg.jpg" , paneWidth, paneHeight);
        splashScreenImageView = new ImageView(splashScreenImage);
        
        //Play button
        String buttonImg = "ClosedDoor.png";
        Image buttonImage = loadImage(buttonImg,300,500);
        ImageView buttonImageView = new ImageView(buttonImage);
        Button playButton = new Button("PLAY", buttonImageView);
        playButton.setStyle("-fx-font-size: 50pt; -fx-font-family: Impact; -fx-text-fill: black;-fx-font-weight: bold");
        
        String openedDoorName = "OpenedDoor.png";
        Image openedDoorImage = loadImage(openedDoorName,300,500);
        ImageView openedDoorImageView = new ImageView(openedDoorImage);
        playButton.setOnMouseEntered(e->{
            playButton.setGraphic(openedDoorImageView);
            playButton.setText(null);
        });
        playButton.setOnMouseExited(e->{
            playButton.setGraphic(buttonImageView);
            playButton.setText("PLAY");
            playButton.setStyle("-fx-font-size: 50pt; -fx-font-family: Impact; -fx-text-fill: black;-fx-font-weight: bold");
        });
        
        playButton.setOnAction(e->
            handler.respondToStartGame());
        playButton.setBackground(Background.EMPTY);
        playButton.setLayoutX(450);
        playButton.setLayoutY(300);
        playButton.setContentDisplay(ContentDisplay.CENTER);
        
        
        //about button
        String buttonImg2 = "ClosedDoor.png";
        Image buttonImage2 = loadImage(buttonImg2,300,500);
        ImageView buttonImageView2 = new ImageView(buttonImage2);
        Button aboutButton = new Button("HELP", buttonImageView2);
        aboutButton.setStyle("-fx-font-size: 50pt; -fx-font-family: Impact; -fx-text-fill: black;-fx-font-weight: bold");
        String openedDoorName2 = "AboutOpenedDoor.png";
        Image openedDoorImage2 = loadImage(openedDoorName2,300,500);
        ImageView openedDoorImageView2 = new ImageView(openedDoorImage2);
        aboutButton.setOnMouseEntered(e->{
            aboutButton.setGraphic(openedDoorImageView2);
            aboutButton.setText(null);
        });
        aboutButton.setOnMouseExited(e->{
            aboutButton.setGraphic(buttonImageView2);
            aboutButton.setText("HELP");
            aboutButton.setStyle("-fx-font-size: 50pt; -fx-font-family: Impact; -fx-text-fill: black;-fx-font-weight: bold");
        });
        
        aboutButton.setOnAction(e->{
            mainPane.setCenter(aboutPane);
        });
            
        aboutButton.setBackground(Background.EMPTY);
        aboutButton.setLayoutX(850);
        aboutButton.setLayoutY(300);
        aboutButton.setContentDisplay(ContentDisplay.CENTER);
        
        
        
        //title image
        Image titleImage= loadImage("title.png",700,500);
        ImageView titleImageView= new ImageView(titleImage);
        titleImageView.setLayoutY(0);
        titleImageView.setLayoutX((paneWidth-titleImageView.getFitWidth()) / 3);
        
        
        
        splashScreenPane.getChildren().addAll(splashScreenImageView, playButton,titleImageView, aboutButton);
    }    
    
    public void initAboutScreen(){
        aboutPane = new Pane();
        Image img = loadImage("about.png");
        ImageView aboutIV = new ImageView(img);
        Image img2 = loadImage("returnButton.png");
        ImageView returnIV = new ImageView(img2);
        Button returnButton = new Button(null, returnIV);
        returnButton.setBackground(Background.EMPTY);
        returnButton.setOnAction(e->{
            mainPane.setCenter(splashScreenPane);
        });
        
        aboutPane.getChildren().addAll(aboutIV, returnButton);
    }
    
    //calls the beginning of the story, and its animations
    public void play(){
        storyPane = new Pane();
        Image StoryBackground= loadImage("Story_Walking.png",paneWidth, paneHeight);
        ImageView StoryBackgroundImageView= new ImageView(StoryBackground);
        StoryBackgroundImageView.toBack();
        StoryBackgroundImageView.setOpacity(0.3);
        storyPane.getChildren().add(StoryBackgroundImageView);
        mainPane.setCenter(storyPane);
        showStory();
        animation.play();
    }
    
    
    //shows the beginning of the story.
    public void showStory(){
        Label gameDesc = new Label();       //the label for the story
        storyPane.getChildren().add(gameDesc);
        gameDesc.setStyle("-fx-font-size: 30pt; -fx-font-family: Times New Roman; -fx-text-fill: black;-fx-font-weight: bold");
        
        String s = "";                                  
        gameDesc.setWrapText(true);        
        gameDesc.setPrefWidth(paneWidth- 100);
        gameDesc.setPadding(new Insets(20,20,20,20));
        try {
            Scanner input = new Scanner(new FileReader("data/story.txt"));
                while(input.hasNext()){
                    s +=  input.nextLine()+"\n";
                }
            input.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
        currentIndex= 0;
        final String story = s;
        MediaPlayer mp = new MediaPlayer(new Media(new File("src/ui/bgm_typing.wav").toURI().toString()));
        mp.setCycleCount(story.length());
        EventHandler<ActionEvent> animationHandler = e->{
            
            gameDesc.setText(story.substring(0, currentIndex));         //set the label for the story to the string that we read from scanner
            currentIndex++;
            if(currentIndex == story.length()){
                mp.stop();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SBDietUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                primaryStage.hide();
                game = new Game(this);
                game.play();
            }
        };
        animation = new Timeline(new KeyFrame(Duration.millis(50), animationHandler));
        animation.setCycleCount(story.length());
        mp.play();
    }
    
    
    //same thing, with the ending story when the user wins.
    public void showWinningEndStory(){
           storyPane.getChildren().clear();         //clean the orginial starting story
           primaryStage.show();
           Label gameDesc = new Label();       //the label for the story
           //Set the background for this stage
            Image StoryBackground= loadImage("End_Story_Winning.png",paneWidth, paneHeight);
            ImageView StoryBackgroundImageView= new ImageView(StoryBackground);
            StoryBackgroundImageView.toBack();
            StoryBackgroundImageView.setOpacity(0.3);
            storyPane.getChildren().addAll(StoryBackgroundImageView);
           storyPane.getChildren().add(gameDesc);
           gameDesc.setStyle("-fx-font-size: 30pt; -fx-font-family: Times New Roman; -fx-text-fill: black;-fx-font-weight: bold");
        
           String s = "";                                  
           gameDesc.setWrapText(true);        
           gameDesc.setPrefWidth(paneWidth- 100);
           gameDesc.setPadding(new Insets(20,20,20,20));
           try {
                Scanner input = new Scanner(new FileReader("data/WinningEndStory.txt"));
                while(input.hasNext()){
                        s +=  input.nextLine()+"\n";
                }
                input.close();
           } catch (Exception e) {
                e.printStackTrace();
           }
           currentIndex= 0;
           final String story = s;
            MediaPlayer mp = new MediaPlayer(new Media(new File("src/ui/bgm_typing.wav").toURI().toString()));
            mp.setCycleCount(story.length());
        
           EventHandler<ActionEvent> animationHandler = e->{
               gameDesc.setText(story.substring(0, currentIndex));         //set the label for the story to the string that we read from scanner
                 currentIndex++;
           if(currentIndex == story.length()){
                mp.stop();
                Image returnButtonImg = loadImage("returnButton.png");
                ImageView returnButtonIV= new ImageView(returnButtonImg);
                Button returnButton = new Button(null, returnButtonIV);
                returnButton.setBackground(Background.EMPTY);
                returnButton.toFront();
                returnButton.setOnAction(a->{
                    mainPane.setCenter(splashScreenPane);
                });
                storyPane.getChildren().add(returnButton);
            }
        };
        animation = new Timeline(new KeyFrame(Duration.millis(50), animationHandler));
        animation.setCycleCount(story.length());
        mp.play();
        animation.play();
        
    }
    
    
    // Shows anothe ending story when the user loses. 
        public void showLosingEndStory(){
            storyPane.getChildren().clear();         //clean the orginial starting story
            primaryStage.show();
           Label gameDesc = new Label();       //the label for the story
      
            Image StoryBackground= loadImage("End_Story_Losing.png",paneWidth, paneHeight);
            ImageView StoryBackgroundImageView= new ImageView(StoryBackground);
            StoryBackgroundImageView.toBack();
            StoryBackgroundImageView.setOpacity(0.3);
            storyPane.getChildren().addAll(StoryBackgroundImageView);
           storyPane.getChildren().add(gameDesc);
           gameDesc.setStyle("-fx-font-size: 30pt; -fx-font-family: Times New Roman; -fx-text-fill: black;-fx-font-weight: bold");
        
           String s = "";                                  
           gameDesc.setWrapText(true);        
           gameDesc.setPrefWidth(paneWidth- 100);
           gameDesc.setPadding(new Insets(20,20,20,20));
           try {
                Scanner input = new Scanner(new FileReader("data/LosingEndStory.txt"));
                while(input.hasNext()){
                        s +=  input.nextLine()+"\n";
                }
                input.close();
           } catch (Exception e) {
                e.printStackTrace();
           }
           currentIndex= 0;
           final String story = s;
            MediaPlayer mp = new MediaPlayer(new Media(new File("src/ui/bgm_typing.wav").toURI().toString()));
        mp.setCycleCount(story.length());
           EventHandler<ActionEvent> animationHandler = e->{ 
               gameDesc.setText(story.substring(0, currentIndex));
               currentIndex++;
           if(currentIndex == story.length()){
                mp.stop();
                Image returnButtonImg = loadImage("returnButton.png");
                ImageView returnButtonIV= new ImageView(returnButtonImg);
                Button returnButton = new Button(null, returnButtonIV);
                returnButton.setBackground(Background.EMPTY);
                returnButton.toFront();
                returnButton.setOnAction(a->{
                    mainPane.setCenter(splashScreenPane);
                });
                storyPane.getChildren().add(returnButton);
            }
        };
        animation = new Timeline(new KeyFrame(Duration.millis(50), animationHandler));
        animation.setCycleCount(story.length());
        mp.play();
        animation.play();
        }
    
    
    
    public Image loadImage(String imageName) {
        Image img = new Image("file:images/" + imageName);
        return img;
    }

    public Image loadImage(String imageName, double width, double height) {
        Image img = new Image("file:images/" + imageName, width, height, false, true);
        return img;
    }

    public BorderPane getMainPane() {
        return mainPane;
    }

    public int getpaneWidth() {
        return paneWidth;
    }

    public int getpaneHeight() {
        return paneHeight;
    }
}
