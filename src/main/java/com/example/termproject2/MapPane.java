package com.example.termproject2;

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

public class MapPane
{
    int money;
    int lives;
    //These values are given at the levels.txt file as the third parameter after the row "Wave_Data:"
    int[] waveDelays;
    //First parameter at the rows after the row "Wave_Data:"
    int[] enemyCountPerWave;
    //Second parameter
    double[] enemySpawnDelayPerWave;
    //This array holds the every row of the .txt file as a string
    String[] rows;
    //This label holds the money at the right up corner of the map
    Label moneyLabel;
    Label livesLabel;
    //This label shows how many seconds has left to other wave
    Label waveLabel;

    MapPane(File levelFile)
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
        /*Since we don't have an actual method that sets how many seconds have left to the other wave this value is static.*/
    }

    public GridPane getPane()
    {
        GridPane map = new GridPane();
        ArrayList<Integer> coordinates = new ArrayList<>();

        int width = Integer.parseInt((rows[0].split(":"))[1]);
        int height = Integer.parseInt((rows[1].split(":"))[1]);

        /*This for loop will find every grey rectangle's coordinates by looking after the second
          row which holds the values of x and y coordinates in the .txt file until the Wave_Data:*/
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

        /*This triple for loop will check every coordinate one by one and also look for if it's coordinates are same as the gray rectangles coordinates
          which we had found these values and put those values into an arrayList as the x values as even and y values as odd.*/

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                boolean isGray = false;

                /*For this loop we increase k value by to because k will as even will give x
                 value of a grey rectangle coordinate and k+1 will give y value of that coordinate*/
                for (int k = 0; k < coordinates.size(); k += 2)
                {
                    if (i == coordinates.get(k) && j == coordinates.get(k+1))
                    {
                        isGray = true;
                    }
                }
                if (isGray)
                {

                    /*We have to use stack pane for each cell because there is a probability that player can put a castle on a cell
                    And yes for grey cell by that cause we don't have to use stack pane on gray ones. But who cares.*/
                    StackPane cell = new StackPane();
                    Rectangle rectangle = new Rectangle(40,40);
                    rectangle.setFill(Color.GRAY);
                    cell.getChildren().add(rectangle);
                    map.add(cell, j, i);

                    /*Any animation must play after the node had initialized
                    otherwise animation cannot be displayed*/
                    playFadeAnimation(cell, j, i);
                }
                else
                {
                    StackPane cell = new StackPane();
                    Rectangle rectangle = new Rectangle(40,40);
                    rectangle.setFill(Color.GOLD);
                    cell.getChildren().add(rectangle);

                    /*There are three event that takes place for drag on drop the one that
                    below is the one event that runs while the player drags the image to the map*/
                    rectangle.setOnDragOver(event ->
                    {
                        //This method will make sure that the dragged object is moving and the original one stays
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        //This method makes sure that event that take place properly ends and does not put itself into a loop
                        event.consume();
                    });
                    rectangle.setOnDragDropped(event ->
                    {
                        //Get the content of the dragged object
                        Dragboard db = event.getDragboard();
                        /*The string in our example is the image path and the cost of the castle.
                         Which means if there is no string there is no object that is being dragged*/
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
                                //Gets the image of the castle and puts on the cell on the map
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
        //Gets rid of the lines between cells
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
        /*First sets the node to be invisible then by a duration sets the node to be visible grade by grade. And the setDelay method
        will make sure that animation will start from top most left corner of the map to the bottom most right corner of the map*/
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(300), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setDelay(Duration.millis((row + column) * 50));
        fade.play();
    }
    public StackPane returnCastle(String name, int cost, String imagePath, Color color,int radius)
    {
        /*Rectangle will act as a container for
        castle image, castle name and castle cost.*/
        Rectangle background = new Rectangle(180,80);
        background.setFill(color);
        background.setStroke(Color.BLACK);
        background.setArcHeight(10);
        background.setArcWidth(10);

        ImageView castleImage = new ImageView(new Image(imagePath));
        castleImage.setFitHeight(32);
        castleImage.setFitWidth(32);

        Label nameLabel = new Label(name);
        Label costLabel = new Label(String.valueOf(cost));
        //Puts labels vertically with 2 bits of spacing
        VBox labelBox = new VBox(nameLabel, costLabel);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.setSpacing(2);
        //Puts image of the castle and labels of it vertically
        VBox contentBox = new VBox(castleImage, labelBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(5);

        StackPane pane = new StackPane(background, contentBox);

        /*This event is the first event that takes place in the drag and drop event series
        and will determinate what starts the event and what will be carried with it.*/
        pane.setOnDragDetected(event ->
        {
            /*The drag board carries dragged object's data. By using clip board
            object we can put these data into the drag board object */
            Dragboard dragboard = castleImage.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            /*We have to put this image path data because when we put
            the object into map we copy the image and out in there*/
            content.putString(imagePath + ";" + String.valueOf(cost).replace("$",""));
            dragboard.setContent(content);

            Circle range = new Circle(radius);
            range.setFill(Color.rgb(255, 0, 0, 0.2));
            range.setStroke(Color.TRANSPARENT);

            ImageView smallView = new ImageView(castleImage.getImage());
            smallView.setFitWidth(32);
            smallView.setFitHeight(32);

            /*VBox cell = (VBox)pane.getParent(); // bulunduğu Cell'i elde ediyoruz
            Tower newTower = new SingleShotTower(cost, 50, 100);
            int columnIndex = GridPane.getColumnIndex(cell);
            int rowIndex = GridPane.getRowIndex(cell);
            newTower.setPosition(columnIndex * 40, rowIndex * 40);
            Map.activeTowers.add(newTower); // haritadaki kulelere ekliyoruz.
            newTower.shoot(cell);
            System.out.println(Map.activeTowers.size());*/

            StackPane visual = new StackPane();
            visual.getChildren().addAll(range,smallView);
            /*These methods and objects will set the dragging view, by using snapshot
            object we can set the inside of the view to be transparent*/
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            Image image = visual.snapshot(params,null);
            /*This one will make sure the cursor is at the center of the image while carrying it*/
            dragboard.setDragView(image, image.getWidth()/2, image.getHeight()/2);
            event.consume();
        });

        return pane;
    }
    //Creates the right pane of the map
    public VBox returnRightPane()
    {
        StackPane castle1 = returnCastle("Single Shot Tower", 50,"Tower.png", Color.WHEAT, 50);
        StackPane castle2 = returnCastle("Laser Tower", 120, "Castle.png",Color.WHEAT, 75);
        StackPane castle3 = returnCastle("Triple Shot Tower", 150, "Castle2.png", Color.WHEAT, 100);
        StackPane castle4 = returnCastle("Missile Launcher Tower", 200, "Castle3.png",Color.WHEAT, 125);

        VBox rightPane = new VBox(livesLabel, moneyLabel, waveLabel, castle1, castle2, castle3, castle4);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(5);

        return rightPane;
    }
}
