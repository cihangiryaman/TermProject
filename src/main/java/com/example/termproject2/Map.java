package com.example.termproject2;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map extends Application
{
    public static List<Enemy> activeEnemies = new ArrayList<>();
    public static List<Tower> activeTowers = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception
    {
        MapPane pane = new MapPane(new File("level5.txt"));
        GridPane map = pane.getPane();
        map.setAlignment(Pos.CENTER);

        // Lazer efektleri için overlay pane oluşturuyoruz
        Pane laserOverlay = new Pane();
        laserOverlay.setPickOnBounds(false); // Mouse olaylarının alt katmanlara geçmesini sağlar

        double fastEnemyRatio = 0.75;

        int[] waveDelays = pane.textDecoder.waveDelays;
        int[] enemyCountPerWave = pane.textDecoder.enemyCountPerWave;
        double[] enemySpawnDelayPerWave = pane.textDecoder.enemySpawnDelayPerWave;

        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < waveDelays.length; i++) {
            PauseTransition waveDelay = new PauseTransition(Duration.seconds(waveDelays[i]));
            sequentialTransition.getChildren().add(waveDelay);

            for (int j = 0; j < enemyCountPerWave[i]; j++) {

                List<String> enemyQueue = getStrings(fastEnemyRatio, enemyCountPerWave[i]);

                Collections.shuffle(enemyQueue);

                for (String enemyType : enemyQueue)
                {
                    PauseTransition spawnDelay = new PauseTransition(Duration.seconds(enemySpawnDelayPerWave[i]));
                    spawnDelay.setOnFinished(spawnEvent -> {
                        Enemy enemy = switch (enemyType) {
                            case "Fast" -> new FastEnemy(map, 900, 2.5, pane);
                            case "Tank" -> new TankEnemy(map, 1800, 1.2, pane);
                            default -> new FastEnemy(map, 900, 2.5, pane);
                        };
                        activeEnemies.add(enemy);
                        try {
                            enemy.walk(5);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                    sequentialTransition.getChildren().add(spawnDelay);
                }
            }
        }

        sequentialTransition.play();

        BorderPane mainLayout = new BorderPane();
        StackPane layered = new StackPane(map, laserOverlay);
        mainLayout.setCenter(layered);
        MapPane.setOverlayPane(laserOverlay);
        mainLayout.setRight(pane.returnRightPane());

        // Set background image
        try {
            Image backgroundImage = new Image("background.png");
            BackgroundImage background = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            mainLayout.setBackground(new Background(background));
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }

        Scene scene = new Scene(mainLayout);

        stage.setTitle("Level 5");
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    private static List<String> getStrings(double fastEnemyRatio, int enemyCountPerWave) {
        int totalEnemies = enemyCountPerWave;
        int fastEnemies = (int)(totalEnemies * fastEnemyRatio);
        int tankEnemies = totalEnemies - (int)(totalEnemies * fastEnemyRatio);

        List<String> enemyQueue = new ArrayList<>();
        for (int k = 0; k < fastEnemies; k++)
        {
            enemyQueue.add("Fast");
        }
        for (int l = 0; l < tankEnemies; l++)
        {
            enemyQueue.add("Tank");
        }
        return enemyQueue;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}