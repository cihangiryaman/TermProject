package com.example.termproject2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import static com.example.termproject2.Map.activeEnemies;
import static com.example.termproject2.Map.activeTowers;

//150123038 Deniz Arda Şanal
public class MainMenu extends Application
{
    Button bt1,bt2,bt3;
    Scene scene1, scene2;

    @Override
    public void start(Stage stage) throws Exception
    {
        Text text = new Text("Tower Defence Game");
        text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 30));
        text.setFill(Color.WHITE);
        text.setTextAlignment(TextAlignment.CENTER);

        //Start Button
        bt1 = new Button("Start Game");
        bt1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bt1.setStyle("-fx-background-color: #C2B280; -fx-text-fill: #FFFFFF; "+ "-fx-background-radius: 10; -fx-padding: 30 40 30 40;");

        //How to Play and Exit buttons
        bt2 = new Button("How To Play");
        bt2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bt2.setStyle("-fx-background-color: #C2B280; -fx-text-fill: #FFFFFF; "+ "-fx-background-radius: 10; -fx-padding: 20 30 20 30;");

        Text text1 = new Text("Place towers on a grid to defend against waves of enemies.\r\n"
                + "•\r\n"
                + "Manage resources to buy towers and stop enemies from reaching the end of a path.\r\n"
                + "•\r\n"
                + "Destroy enemies using bullets, lasers and missiles, triggering explosion effects on elimination.\r\n"
                + "•\r\n"
                + "Survive increasingly difficult waves of enemies.\r\n"
                + "•\r\n"
                + "Win by defeating all waves; lose if 5 enemies reach the endpoint");
        text1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
        text1.setFill(Color.WHITE);
        text1.setTextAlignment(TextAlignment.CENTER);

        bt3 = new Button("Exit");
        bt3.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        bt3.setStyle("-fx-background-color: #b06f1a; -fx-text-fill: #FFFFFF; "+ "-fx-background-radius: 10; -fx-padding: 5 20 5 20;");
        bt3.setTranslateY(30);

        //Combining the How To Play and Exit parts with VBox
        VBox vb1 = new VBox(text1,bt3);
        vb1.setAlignment(Pos.CENTER);
        Image backgroundImage = new Image("background.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        vb1.setBackground(new Background(background));
        scene2 = new Scene(vb1,800,600);

        bt1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                try {
                    activeTowers.clear();
                    activeEnemies.clear();
                    new Map(GameDifficulty.Easy).start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        bt2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                stage.setScene(scene2);
            }
        });

        bt3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                stage.setScene(scene1);
            }
        });
        //Combining both "Start" and "How To Play" buttons with VBox
        VBox vb = new VBox(30,text,bt1,bt2);
        vb.setAlignment(Pos.CENTER);
        Image backgroundImage2 = new Image("background.png");
        BackgroundImage background2 = new BackgroundImage(
                backgroundImage2,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        vb.setBackground(new Background(background2));


        scene1 = new Scene(vb, 800, 600);
        stage.setTitle("Game Menu");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setScene(scene1);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

}
