package com.example.termproject2;

import javafx.animation.AnimationTimer;
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

public class Map extends Application {
    public static List<Enemy> activeEnemies = new ArrayList<>();
    public static List<Tower> activeTowers = new ArrayList<>();
    private int currentLevel = 1;
    public static GameDifficulty Difficulty;
    public static SequentialTransition sequentialTransition;

    public Map(int level){
        activeTowers.clear();
        this.currentLevel = level;
    }
    public Map(GameDifficulty difficulty)
    {
        activeTowers.clear();
        Difficulty = difficulty;
    }

    @Override
    public void start(Stage stage) throws Exception {
        StageManager.currentStage = stage;
        MapPane pane = new MapPane(new File("level" + currentLevel + ".txt"));
        GridPane map = pane.getPane();
        map.setAlignment(Pos.CENTER);

        // Lazer efektleri için overlay pane oluşturuyoruz
        Pane laserOverlay = new Pane();
        laserOverlay.setPickOnBounds(false); // Mouse olaylarının alt katmanlara geçmesini sağlar

        double fastEnemyRatio = 0.75;

        int[] waveDelays = pane.textDecoder.waveDelays;
        int[] enemyCountPerWave = pane.textDecoder.enemyCountPerWave;
        double[] enemySpawnDelayPerWave = pane.textDecoder.enemySpawnDelayPerWave;

        sequentialTransition = new SequentialTransition();

        for (int i = 0; i < waveDelays.length; i++) {
            // 1. Wave başlamadan önce bekleme süresi
            PauseTransition waveDelay = new PauseTransition(Duration.seconds(waveDelays[i]));
            sequentialTransition.getChildren().add(waveDelay);

            // 2. Düşmanları sırala (tek sefer)
            List<String> enemyQueue = getStrings(fastEnemyRatio, enemyCountPerWave[i]);
            Collections.shuffle(enemyQueue);

            // 3. Kademeli olarak düşman spawnlama
            Duration cumulativeDelay = Duration.ZERO;

            for (String enemyType : enemyQueue) {
                PauseTransition spawnDelay = new PauseTransition(cumulativeDelay);

                spawnDelay.setOnFinished(spawnEvent -> {
                    Enemy enemy = switch (enemyType) {
                        case "Fast" -> new FastEnemy(map, 30* Difficulty.getDifficulty(), 2 + Difficulty.getDifficulty()/5, pane);
                        case "Tank" -> new TankEnemy(map, 90* Difficulty.getDifficulty(), 0.75 + Difficulty.getDifficulty()/5, pane);
                        default -> new FastEnemy(map, 30* Difficulty.getDifficulty(), 2 + Difficulty.getDifficulty()/5, pane);
                    };
                    activeEnemies.add(enemy);
                    try {
                        enemy.walk(currentLevel);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                sequentialTransition.getChildren().add(spawnDelay);

                // Bir sonraki düşmanın gecikmesini ayarla
                cumulativeDelay = cumulativeDelay.add(Duration.seconds(enemySpawnDelayPerWave[i]));
            }
        }

        sequentialTransition.play();


        sequentialTransition.play();
        sequentialTransition.setOnFinished(e -> {
            // Düşmanlar spawnlandı, şimdi aktif düşmanlar bitene kadar bekle
            AnimationTimer checkWaveEnd = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (activeEnemies.isEmpty()) {
                        this.stop(); // kontrolü durdur
                        for(Tower tower : activeTowers){
                            tower.delete();
                        }
                        activeTowers.clear();
                        currentLevel++;
                        if (currentLevel <= 5) {
                            new YouWonMenu(currentLevel).start(new Stage()); //sonraki level
                            stage.close();
                        } else {
                            try {
                                new GameOverMenu().start(new Stage()); //bitiş
                                stage.close();
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            };
            checkWaveEnd.start();
        });


        BorderPane mainLayout = new BorderPane();
        StackPane layered = new StackPane(map, laserOverlay);
        mainLayout.setCenter(layered);
        MapPane.setOverlayPane(laserOverlay);
        mainLayout.setRight(pane.returnRightPane());

        Image backgroundImage = new Image("background.png");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        mainLayout.setBackground(new Background(background));

        Scene scene = new Scene(mainLayout);

        stage.setTitle("Level " + currentLevel);
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    private static List<String> getStrings(double fastEnemyRatio, int enemyCountPerWave) {
        int totalEnemies = enemyCountPerWave;
        int fastEnemies = (int) (totalEnemies * fastEnemyRatio);
        int tankEnemies = totalEnemies - (int) (totalEnemies * fastEnemyRatio);

        List<String> enemyQueue = new ArrayList<>();
        for (int k = 0; k < fastEnemies; k++) {
            enemyQueue.add("Fast");
        }
        for (int l = 0; l < tankEnemies; l++) {
            enemyQueue.add("Tank");
        }
        return enemyQueue;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
