package com.example.termproject2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

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

        Enemy testEnemy = new Infantry(300, 0.2);
        activeEnemies.add(testEnemy);
        testEnemy.walk(5,map);
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(map);
        mainLayout.setRight(pane.returnRightPane());

        Scene scene = new Scene(mainLayout);

        stage.setTitle("Level5");
        stage.setWidth(1000);
        stage.setHeight(1000);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}