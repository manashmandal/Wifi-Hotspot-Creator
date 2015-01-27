/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wifihotspot;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *
 * @author Manash
 */
public class WifiHotspot extends Application {
    
    public String showPassword = new String();
    public String hidePassword = new String();
    
    @Override
    public void start(Stage primaryStage) {
           HotspotAction obj = new HotspotAction();
        
          //Grid and flowpane declaration
           
        GridPane gridPane = new GridPane();
        FlowPane firstFlowPane = new FlowPane();
        FlowPane secondFlowPane = new FlowPane();
        FlowPane thirdFlowPane = new FlowPane();
        FlowPane statusFlowPane = new FlowPane();
        Label statusLabel = new Label("Idle....");
        statusLabel.setAlignment(Pos.CENTER);
        
        
        statusFlowPane.getChildren().add(statusLabel);
        statusFlowPane.setAlignment(Pos.CENTER);
        
        
        FlowPane radioFlowPane = new FlowPane();
        ToggleGroup toggling = new ToggleGroup();
        
        RadioButton showPass = new RadioButton("Show Password");
        showPass.setSelected(true);
        RadioButton hidePass = new RadioButton("Hide Password");
        
        showPass.setToggleGroup(toggling);
        hidePass.setToggleGroup(toggling);
        
        radioFlowPane.setVgap(5);
        radioFlowPane.setHgap(5);
        radioFlowPane.setAlignment(Pos.CENTER);
        radioFlowPane.getChildren().addAll(showPass, hidePass);
        
        
       
        // Text and pass fields
        TextField SSID = new TextField();
        SSID.setPromptText("Enter Hotspot Name");
        TextField password = new TextField();
        password.setPromptText("Enter Password");
        
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter Password");
        
        firstFlowPane.setPadding(new Insets(11,0, 13, 14));
        
        firstFlowPane.setVgap(5);
        firstFlowPane.setHgap(10);
        firstFlowPane.getChildren().addAll(new Label("Hotspot Name"), SSID);
        
        secondFlowPane.setVgap(5);
        secondFlowPane.setHgap(35);
        secondFlowPane.setPadding(new Insets(11,0,13,14));
        secondFlowPane.getChildren().addAll(new Label("Password"), password);
        
        
        Button aboutMeh = new Button("About Me");
        Button testDevice = new Button("Test Wifi Device");
        Button start = new Button("Start");
        Button stop = new Button("Stop");
        //Button details = new Button("Details >>");
        thirdFlowPane.setPadding(new Insets(11,0,13,14));
        thirdFlowPane.setHgap(7);
        thirdFlowPane.getChildren().addAll(testDevice, start, stop, aboutMeh);
        
        //Children of gridpane
        gridPane.add(firstFlowPane, 0, 0);
        gridPane.add(secondFlowPane, 0, 1);
         gridPane.add(radioFlowPane, 0, 2 );
        gridPane.add(thirdFlowPane, 0, 3);
        gridPane.add(statusFlowPane, 0, 4);
        
        Scene scene = new Scene(gridPane);
        
        
        //Warning Dialog Box for Start Button
         Label warningLabel = new Label();
         final Stage hotspotWarning = new Stage();
   
          hotspotWarning.setTitle("Device Mode");
          hotspotWarning.getIcons().add(new Image("http://png-5.findicons.com/files/icons/1620/crystal_project/128/alert.png"));
          hotspotWarning.initModality(Modality.APPLICATION_MODAL);
          hotspotWarning.initOwner(primaryStage);
          VBox hotspotVbox = new VBox(20);
          hotspotVbox.setAlignment(Pos.CENTER);
          hotspotWarning.setResizable(false);
          
          Scene hotspotWarningScene = new Scene(hotspotVbox, 250, 100);
        
        
       Image warning = new Image("http://png-5.findicons.com/files/icons/1620/crystal_project/128/alert.png");
        ImageView warningView = new ImageView();
        warningView.setImage(warning);
        warningView.setFitHeight(50);
        warningView.setFitWidth(50);
        
        start.setOnAction((event) -> {
            hotspotVbox.getChildren().remove(warningLabel);
            obj.executeCommand("netsh wlan start hostednetwork");
            String activation = "The hosted network couldn't be started.";
            int index = activation.indexOf(obj.outputString);
            
            if (!obj.outputString.contains(activation)){
                
            obj.executeCommand("netsh wlan stop hostednetwork");
            statusLabel.setText("Starting....");
            if (showPass.isSelected()){
                    obj.password = password.getText();
            }
            
            else if (hidePass.isSelected()) {
                obj.password = passField.getText();
            }
            
          //  else if (hidePass.isSelected()){
            //    obj.password = password.getText();
            //}
            
            
            obj.SSID = SSID.getText();
 
            if (obj.SSID != null && obj.password.length() >= 8){
            String startingCommand = "netsh wlan set hostednetwork mode=allow ssid=" + obj.SSID + " key=" + obj.password.toString();
            obj.executeCommand(startingCommand);
            obj.executeCommand("netsh wlan start hostednetwork");
            statusLabel.setText("Wifi Started...");
        }
            
            else {
                statusLabel.setText("Password length minimum 8 and Put a hotspot name");
            }
         }
            
            
            else {
                warningLabel.setText("Please Turn on your Wi-Fi Device");
                hotspotVbox.getChildren().add(warningView);
                hotspotVbox.getChildren().add(warningLabel);          
                hotspotWarning.setScene(hotspotWarningScene);
                hotspotWarning.show();   
            }
        });
        
        
        stop.setOnAction((event) -> {
            obj.executeCommand("netsh wlan stop hostednetwork");
            statusLabel.setText("Wifi Stopped..."); 
        });
        
        
        showPass.setOnAction(e->{
           this.showPassword = passField.getText();
            passField.setText(null);
            secondFlowPane.getChildren().remove(passField);
            secondFlowPane.getChildren().add(password);
            password.setText(showPassword);
            
        });
        
        hidePass.setOnAction(e->{
            
            this.hidePassword = password.getText();
           password.setText(null);
           secondFlowPane.getChildren().remove(password);
           secondFlowPane.getChildren().add(passField);
           passField.setText(hidePassword);
            
        });
        
        //Wifi Device checking dialog
        Image dialogImg = new Image("http://png-2.findicons.com/files/icons/1254/flurry_system/256/get_info.png");
        ImageView dialogImage = new ImageView();
        dialogImage.setImage(dialogImg);
        dialogImage.setFitWidth(50);
        dialogImage.setFitHeight(50);
        dialogImage.setSmooth(true);
        dialogImage.setCache(true);
        
        
         Label label = new Label();
         final Stage dialog = new Stage();
          dialog.getIcons().add(new Image("http://png-2.findicons.com/files/icons/1254/flurry_system/256/get_info.png"));
          dialog.setTitle("Device Mode");
          dialog.initModality(Modality.APPLICATION_MODAL);
          dialog.initOwner(primaryStage);
          VBox dialogVbox = new VBox(20);
          dialogVbox.setAlignment(Pos.CENTER);
          dialogVbox.getChildren().add(dialogImage);
          dialog.setResizable(false);
          
          Scene dialogScene = new Scene(dialogVbox, 250, 100);
        
        
        testDevice.setOnAction(e->{
          dialogVbox.getChildren().remove(label);
          obj.executeCommand("netsh wlan show drivers");
          String deviceAvailability = "Hosted network supported  : Yes";
          //int index = deviceAvailability.indexOf(obj.outputString);
          
         
          if (obj.outputString.contains(deviceAvailability)){
              
              label.setText("Wi-Fi Device Available!");
              dialogVbox.getChildren().add(label);
          }
          
          else {
              label.setText("Wi-Fi Device not Available, Please Install Wi-Fi Driver");
              dialogVbox.getChildren().add(label);
          }
          
          dialog.setScene(dialogScene);
          dialog.show();
        });
        
        
        //About Me Window
       
          
          
      
        aboutMeh.setOnAction(e->{
          final Stage aboutMe = new Stage();
          Image aboutMeImage = new Image("http://png-2.findicons.com/files/icons/653/the_spherical/128/info.png");
          ImageView aboutMeImageView = new ImageView();
          aboutMeImageView.setImage(aboutMeImage);
          aboutMeImageView.setFitWidth(50);
          aboutMeImageView.setFitHeight(50);
          aboutMe.setTitle("Know about me :P");
          aboutMe.getIcons().add(new Image("http://png-2.findicons.com/files/icons/653/the_spherical/128/info.png"));
          aboutMe.initModality(Modality.APPLICATION_MODAL);
          aboutMe.initOwner(primaryStage);
          GridPane aboutMePane = new GridPane();
          aboutMePane.setAlignment(Pos.CENTER);
          aboutMe.setResizable(false);
          Scene aboutMeScene = new Scene(aboutMePane, 300, 300);
            
            
         Text name = new Text("Manash Kumar Mandal");
         Text study = new Text("KUET, EEE - 2nd Year");
         Text about = new Text("Jack of all trades but master of none!\nInterested in : \nLanguage::\n\tC/C++, Java, Python, PHP, Javascript, C#\n\tHardware::Arduino, Raspberry Pi, AVR, PICAXE\n\tFrameworks:: QtC++, .NET\n\tCMS:: Wordpress");
            
            
            
            final String content = "This is CS50!";
            final Text text = new Text(10, 20, "");
            
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(2000));
                }

                protected void interpolate(double frac) {
                    final int length = content.length();
                    final int n = Math.round(length * (float) frac);
                    text.setText(content.substring(0, n));
                }

            };

            animation.play();
            
            aboutMePane.add(aboutMeImageView, 0, 0);
            aboutMePane.add(name,0,1);
            aboutMePane.add(study,0, 2);
            aboutMePane.add(about,0,3);
            aboutMePane.add(text, 0,5);
            
            aboutMe.setScene(aboutMeScene);
            aboutMe.show();
            
        });
        
        
        
        
        primaryStage.getIcons().add(new Image("http://png-1.findicons.com/files/icons/1620/crystal_project/128/wifi.png"));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("CS50 Hotspot 1.0 [Alpha]");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        obj.executeCommand("netsh wlan start hostednetwork");
         
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
