package com.example.termproject2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

//150123038 Deniz Arda Şanal
public class GameOverMenu extends Application {

    Scene scene4;
    Button bt;

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Game Over text
        Text text1 = new Text("Game Over!");
        text1.setFill(Color.WHITE);
        text1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 30));

        //Back to main menu button
        bt = new Button("Back to Main Menu");
        bt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        bt.setStyle("-fx-background-color: #F44336; -fx-text-fill: #FFFFFF; "+ "-fx-background-radius: 10; -fx-padding: 20 30 20 30;");
        bt.setTranslateY(30);


        //Combining them with VBox
        VBox vb = new VBox(text1, bt);
        vb.setAlignment(Pos.CENTER);
        vb.setBackground(new Background(new BackgroundFill(Color.DARKORANGE, CornerRadii.EMPTY,null)));

        scene4 = new Scene(vb,800,600);
        primaryStage.setTitle("Game Over");
        primaryStage.setScene(scene4);
        primaryStage.show();

    }







    public static void main(String[] args) {

        launch(args);

    }
}