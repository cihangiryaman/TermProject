package com.example.leveldesign;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;

public class Map extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        Pane pane = new Pane(new File("level5.txt"));
        GridPane map = pane.getPane();
        map.setAlignment(Pos.CENTER);

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