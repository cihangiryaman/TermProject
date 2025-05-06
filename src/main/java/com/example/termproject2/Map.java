package com.example.termproject2;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
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

        int[] waveDelays = pane.textDecoder.waveDelays;
        int[] enemyCountPerWave = pane.textDecoder.enemyCountPerWave;
        double[] enemySpawnDelayPerWave = pane.textDecoder.enemySpawnDelayPerWave;

        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < waveDelays.length; i++) {
            PauseTransition waveDelay = new PauseTransition(Duration.seconds(waveDelays[i]));
            sequentialTransition.getChildren().add(waveDelay);

            for (int j = 0; j < enemyCountPerWave[i]; j++) {
                PauseTransition spawnDelay = new PauseTransition(Duration.seconds(enemySpawnDelayPerWave[i]));
                spawnDelay.setOnFinished(spawnEvent -> {
                    Enemy enemy = new FastEnemy(map, 900, 2.5);
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

        sequentialTransition.play(); // Tüm işlemleri sıralı olarak başlatır

        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(map);
        mainLayout.setRight(pane.returnRightPane());

        Scene scene = new Scene(mainLayout);

        stage.setTitle("Level 5");
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}