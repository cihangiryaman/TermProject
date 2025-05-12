package com.example.termproject2;

import javafx.application.Application;
import javafx.event.ActionEvent;
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

public class MainMenu extends Application {
    Button bt1, bt2, bt3, bt4, bt5, bt6;
    Scene scene1, scene2;

    private GameDifficulty selectedDifficulty = GameDifficulty.Normal; // Default to Normal

    // Method to update button styles based on selected difficulty
    private void updateDifficultyButtonStyles() {
        // Default styles
        String defaultStyle = "-fx-background-color: #C2B280; -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-padding: 30 40 30 40;";
        String selectedStyle = "-fx-background-color: #8B4513; -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-padding: 30 40 30 40;";

        bt4.setStyle(selectedDifficulty == GameDifficulty.Easy ? selectedStyle : defaultStyle);
        bt5.setStyle(selectedDifficulty == GameDifficulty.Normal ? selectedStyle : defaultStyle);
        bt6.setStyle(selectedDifficulty == GameDifficulty.Hard ? selectedStyle : defaultStyle);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StageManager.currentStage = stage;
        // Ensure full screen is always set
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        // Başlık
        Text text = new Text("Tower Defence Game");
        text.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 30));
        text.setFill(Color.WHITE);
        text.setTextAlignment(TextAlignment.CENTER);

        // Start Game butonu
        bt1 = new Button("Start Game");
        bt1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bt1.setStyle("-fx-background-color: #C2B280; -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-padding: 30 40 30 40;");

        // Difficulty Buttons
        bt4 = new Button("Easy");
        bt5 = new Button("Normal");
        bt6 = new Button("Hard");

        // Set initial styling and difficulty action handlers
        for (Button difficultyButton : new Button[]{bt4, bt5, bt6}) {
            difficultyButton.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        }

        bt4.setOnAction(e -> {
            selectedDifficulty = GameDifficulty.Easy;
            updateDifficultyButtonStyles();
        });
        bt5.setOnAction(e -> {
            selectedDifficulty = GameDifficulty.Normal;
            updateDifficultyButtonStyles();
        });
        bt6.setOnAction(e -> {
            selectedDifficulty = GameDifficulty.Hard;
            updateDifficultyButtonStyles();
        });

        // Set initial difficulty button styles (Normal selected by default)
        updateDifficultyButtonStyles();

        // How To Play butonu
        bt2 = new Button("How To Play");
        bt2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bt2.setStyle("-fx-background-color: #C2B280; -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-padding: 30 40 30 40;");

        // Exit butonu (diğer sahnede)
        bt3 = new Button("Exit");
        bt3.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        bt3.setStyle("-fx-background-color: #b06f1a; -fx-text-fill: #FFFFFF; -fx-background-radius: 10; -fx-padding: 5 20 5 20;");
        bt3.setTranslateY(30);

        // Difficulty buttons in HBox
        HBox difficultyBox = new HBox(30, bt4, bt5, bt6);
        difficultyBox.setAlignment(Pos.CENTER);

        // Ana merkez kutusu (ortada gözükenler)
        VBox centerBox = new VBox(30, text, bt1, bt2, difficultyBox);
        centerBox.setAlignment(Pos.CENTER);

        // Ortalamak için StackPane
        StackPane centerPane = new StackPane(centerBox);
        centerPane.setAlignment(Pos.CENTER);

        // Arka plan
        Image backgroundImage = new Image("background.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        centerPane.setBackground(new Background(background));

        // How to Play ekranı
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

        VBox vb1 = new VBox(text1, bt3);
        vb1.setAlignment(Pos.CENTER);
        vb1.setBackground(new Background(background));

        // Create scenes with full screen settings
        scene2 = new Scene(vb1);
        scene1 = new Scene(centerPane);

        // Buton aksiyonları
        bt1.setOnAction((ActionEvent arg0) -> {
            try {
                activeTowers.clear();
                activeEnemies.clear();
                new Map(selectedDifficulty).start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        bt2.setOnAction(e -> {
            stage.setScene(scene2);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        });

        bt3.setOnAction(e -> {
            stage.setScene(scene1);
            stage.setFullScreen(true);
            stage.setFullScreenExitHint("");
        });

        // Sahne ayarları
        stage.setTitle("Game Menu");
        stage.setScene(scene1);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}