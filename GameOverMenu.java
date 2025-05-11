package com.example.termproject2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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

        Text text1 = new Text("Game Over!");
        text1.setFill(Color.WHITE);
        text1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 30));

        bt = new Button("Back to Main Menu");
        bt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
        bt.setStyle("-fx-background-color: #C2B280; -fx-text-fill: #FFFFFF; "+ "-fx-background-radius: 10; -fx-padding: 20 30 20 30;");
        bt.setTranslateY(30);


        VBox vb = new VBox(text1, bt);
        vb.setAlignment(Pos.CENTER);
        vb.setBackground(new Background(new BackgroundFill(Color.DARKORANGE, CornerRadii.EMPTY,null)));
        Image backgroundImage = new Image("background.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        vb.setBackground(new Background(background));

        scene4 = new Scene(vb,800,600);
        primaryStage.setTitle("Game Over");
        primaryStage.setScene(scene4);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();

    }
    public void show(Stage stage) {
        Text text1 = new Text("Game Over!");
        text1.setFill(Color.WHITE);
        text1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 30));

        // Back to main menu button
        bt = new Button("Back to Main Menu");
        bt.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 15));
        bt.setStyle("-fx-background-color: #b06f1a; -fx-text-fill: #FFFFFF; " +
                "-fx-background-radius: 10; -fx-padding: 20 30 20 30;");
        bt.setTranslateY(30);

        bt.setOnAction(e -> {
            try {
                stage.setFullScreen(true);
                stage.setFullScreenExitHint("");
                new MainMenu().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox vb = new VBox(text1, bt);
        vb.setAlignment(Pos.CENTER);
        Image backgroundImage = new Image("background.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        vb.setBackground(new Background(background));

        scene4 = new Scene(vb, 800, 600);
        stage.setTitle("Game Over");
        stage.setScene(scene4);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }
    public static void main(String[] args) {

        launch(args);
    }
}