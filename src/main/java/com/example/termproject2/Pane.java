package com.example.leveldesign;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Pane
{
    int money;
    int lives;
    int[] waveDelays;
    int[] enemyCountPerWave;
    double[] enemySpawnDelayPerWave;
    String[] rows;
    Label moneyLabel;
    Label livesLabel;
    Label waveLabel;

    Pane(File levelFile)
    {
        lives = 5;
        money = 100;
        rows = getLines(levelFile);
        waveDelays = getDelayToOtherWave();
        enemyCountPerWave = getEnemyCountPerWave();
        enemySpawnDelayPerWave = getEnemySpawnDelayPerWave();
        moneyLabel = new Label("Money: " + money + "$");
        livesLabel = new Label("Lives: " + lives );
        waveLabel = new Label("Next wave: " + waveDelays[0] + "s");
    }

    public GridPane getPane()
    {
        GridPane map = new GridPane();
        ArrayList<Integer> coordinates = new ArrayList<>();

        int width = Integer.parseInt((rows[0].split(":"))[1]);
        int height = Integer.parseInt((rows[1].split(":"))[1]);

        for (int i = 2; i < rows.length; i++)
        {
            if(rows[i].equals("WAVE_DATA:"))
            {
                break;
            }
            else
            {
                int x = Integer.parseInt((rows[i].split(","))[0]);
                int y = Integer.parseInt((rows[i].split(","))[1]);
                coordinates.add(x);
                coordinates.add(y);
            }
        }

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                boolean isGray = false;
                for (int k = 0; k < coordinates.size(); k += 2)
                {
                    if (i == coordinates.get(k) && j == coordinates.get(k+1))
                    {
                        isGray = true;
                    }
                }
                if (isGray)
                {
                    StackPane cell = new StackPane();
                    Rectangle rectangle = new Rectangle(40,40);
                    rectangle.setFill(Color.GRAY);
                    cell.getChildren().add(rectangle);
                    map.add(cell, j, i);
                    playFadeAnimation(cell, j, i);
                }
                else
                {
                    StackPane cell = new StackPane();
                    Rectangle rectangle = new Rectangle(40,40);
                    rectangle.setFill(Color.GOLD);
                    cell.getChildren().add(rectangle);

                    rectangle.setOnDragOver(event ->
                    {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    });
                    rectangle.setOnDragDropped(event ->
                    {
                        Dragboard db = event.getDragboard();
                        if (db.hasString())
                        {
                            String data = db.getString();
                            String[] parts = data.split(";");
                            String imagePath = parts[0];
                            int cost = Integer.parseInt(parts[1]);

                            if ( money >= cost)
                            {
                                money -= cost;
                                moneyLabel.setText("Money: " + money + "$");
                                ImageView castleImage = new ImageView(new Image(imagePath));
                                castleImage.setFitWidth(32);
                                castleImage.setFitHeight(32);
                                cell.getChildren().add(castleImage);
                                event.setDropCompleted(true);
                            }
                            else
                            {
                                event.setDropCompleted(false);
                            }
                        }
                        else
                        {
                            event.setDropCompleted(false);
                        }
                        event.consume();
                    });
                    map.add(cell, j, i);
                    playFadeAnimation(cell, j, i);
                }
            }
        }
        map.setHgap(0);
        map.setVgap(0);
        return map;
    }

    private static String[] getLines(File levelFile)
    {
        ArrayList<String> lines = new ArrayList<>();
        try
        {
            Scanner scan = new Scanner(levelFile);
            while(scan.hasNext())
            {
                lines.add(scan.nextLine());
            }
            scan.close();
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        return lines.toArray(new String[0]);
    }
    private int[] getEnemyCountPerWave()
    {
        ArrayList<Integer> waveData = new ArrayList<>();
        for (int i = 15; i < rows.length; i++)
        {
            String[] row = rows[i].split(",");
            if (row.length == 3)
            {
                String enemyCount = row[0].trim();
                waveData.add(Integer.parseInt(enemyCount));
            }
        }
        return waveData.stream().mapToInt(Integer::intValue).toArray();
    }
    private double[] getEnemySpawnDelayPerWave()
    {
        ArrayList<Double> waveData = new ArrayList<>();
        for (int i = 15; i < rows.length; i++)
        {
            String[] row = rows[i].split(",");
            if (row.length == 3)
            {
                String spawnDelay = row[1].trim();
                waveData.add(Double.parseDouble(spawnDelay));
            }
        }
        return waveData.stream().mapToDouble(Double::doubleValue).toArray();
    }
    private int[] getDelayToOtherWave()
    {
        ArrayList<Integer> waveData = new ArrayList<>();
        for (int i = 15; i < rows.length; i++)
        {
            String[] row = rows[i].split(",");
            if (row.length == 3)
            {
                String delay = row[2].trim();
                waveData.add(Integer.parseInt(delay));
            }
        }
        return waveData.stream().mapToInt(Integer::intValue).toArray();
    }
    private void playFadeAnimation(Node node, int row, int column)
    {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis((row + column) * 50));
        fade.play();
    }
    public StackPane returnCastle(String name, String cost, String imagePath, Color color,int radius)
    {
        Rectangle background = new Rectangle(180,80);
        background.setFill(color);
        background.setStroke(Color.BLACK);
        background.setArcHeight(10);
        background.setArcWidth(10);

        ImageView castleImage = new ImageView(new Image(imagePath));
        castleImage.setFitHeight(32);
        castleImage.setFitWidth(32);

        Label nameLabel = new Label(name);
        Label costLabel = new Label(cost);
        VBox labelBox = new VBox(nameLabel, costLabel);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setSpacing(2);

        VBox contentBox = new VBox(castleImage, labelBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(5);

        StackPane pane = new StackPane(background, contentBox);
        pane.setOnDragDetected(event ->
        {
            Dragboard dragboard = castleImage.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(imagePath + ";" + cost.replace("$",""));
            dragboard.setContent(content);

            Circle range = new Circle(radius);
            range.setFill(Color.rgb(255, 0, 0, 0.2));
            range.setStroke(Color.TRANSPARENT);

            ImageView smallView = new ImageView(castleImage.getImage());
            smallView.setFitWidth(32);
            smallView.setFitHeight(32);

            StackPane visual = new StackPane();
            visual.getChildren().addAll(range,smallView);

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image image = visual.snapshot(params,null);

            dragboard.setDragView(image, image.getWidth()/2, image.getHeight()/2);
            event.consume();
        });

        return pane;
    }
    public VBox returnRightPane()
    {
        StackPane castle1 = returnCastle("Single Shot Tower", "50$","Tower.png", Color.WHEAT, 50);
        StackPane castle2 = returnCastle("Laser Tower", "120$", "Castle.png",Color.WHEAT, 75);
        StackPane castle3 = returnCastle("Triple Shot Tower", "150$", "Castle2.png", Color.WHEAT, 100);
        StackPane castle4 = returnCastle("Missile Launcher Tower", "200$", "Castle3.png",Color.WHEAT, 125);

        VBox rightPane = new VBox(livesLabel, moneyLabel, waveLabel, castle1, castle2, castle3, castle4);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(5);

        return rightPane;
    }
}
