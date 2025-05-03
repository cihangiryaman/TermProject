package com.example.termproject2;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MapPane
{
    int money;
    int lives;
    String[] rows;
    Label moneyLabel;
    Label livesLabel;
    Label waveLabel;
    TextDecoder textDecoder;
    Label waveCountdownLabel;
    int waveCountdownTime = 30;

    MapPane(File levelFile)
    {
        textDecoder = new TextDecoder(levelFile);
        lives = 5;
        money = 1000;
        rows = TextDecoder.getLines(levelFile);
        moneyLabel = new Label("Money: " + money + "$");
        livesLabel = new Label("Lives: " + lives );
        waveLabel = new Label("Next wave: " + textDecoder.waveDelays[0] + "s");
        /*Since we don't have an actual method that sets how many seconds have left to the other wave this value is static.*/
        waveCountdownLabel = new Label("Next wave in: " + waveCountdownTime + "s");
        waveCountdownLabel.setFont(Font.font("Arial", 18));
        waveCountdownLabel.setTextFill(Paint.valueOf("red"));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (waveCountdownTime > 0) {
                waveCountdownTime--;
                waveCountdownLabel.setText("Next wave in: " + waveCountdownTime + "s");
            } else {
                waveCountdownLabel.setText("Wave started!");
                // Yeni dalgayı başlatma kodunu buraya entegre edebilirsiniz.
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public GridPane getPane()
    {
        GridPane map = new GridPane();

        ArrayList<Cell> grayCells = textDecoder.getGrayCells();
        ArrayList<Cell> cells = textDecoder.cells;
        for (int i = 0; i < cells.size(); i++)
        {
            StackPane cell = new StackPane();
            Rectangle rectangle = new Rectangle(40,40);

            if (cells.get(i).isGray)
            {

                rectangle.setFill(Color.GRAY);
                cell.getChildren().add(rectangle);

            }
            else
            {
                rectangle.setFill(Color.GOLD);
                cell.getChildren().add(rectangle);

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

                            Tower newTower = new LaserTower(cost, 50, 50);
                            int columnIndex = GridPane.getColumnIndex(cell);
                            int rowIndex = GridPane.getRowIndex(cell);
                            newTower.setPosition(columnIndex * 40 + 20, rowIndex * 40 + 20);
                            Map.activeTowers.add(newTower);
                            newTower.shoot();
                            System.out.println(Map.activeTowers.size());
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
            }
            map.add(cell, cells.get(i).y, cells.get(i).x);
            playFadeAnimation(cell, cells.get(i).y, cells.get(i).x);

        }
        //Gets rid of the lines between cells
        map.setHgap(0);
        map.setVgap(0);
        return map;
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
    public StackPane returnCastle(Tower tower, Color color)//Şu anda Tower class'ında image oluşturma sorunu yüzünden diğer metodu kullanın
    {
        /*Rectangle will act as a container for
        castle image, castle name and castle cost.*/
        Rectangle background = new Rectangle(180,80);
        background.setFill(color);
        background.setStroke(Color.BLACK);
        background.setArcHeight(10);
        background.setArcWidth(10);

        ImageView castleImage = tower.getImage();
        castleImage.setFitHeight(32);
        castleImage.setFitWidth(32);

        Label nameLabel = new Label(tower.getName());
        Label costLabel = new Label(String.valueOf(tower.getPrice()));
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
            content.putString(tower.get_imagePath() + ";" + tower.getPrice());
            dragboard.setContent(content);

            Circle range = new Circle(tower.getRange());
            range.setFill(Color.rgb(255, 0, 0, 0.2));
            range.setStroke(Color.TRANSPARENT);

            ImageView smallView = new ImageView(castleImage.getImage());
            smallView.setFitWidth(32);
            smallView.setFitHeight(32);

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
        StackPane castle1 = returnCastle(new SingleShotTower("Tower.png",50,10,50),Color.WHEAT);
        StackPane castle2 = returnCastle(new LaserTower("Castle.png",120,5,75),Color.WHEAT);
        StackPane castle3 = returnCastle(new TripleShotTower("Castle2.png",150,10,100),Color.WHEAT);
        StackPane castle4 = returnCastle(new MissileLauncherTower("Castle3.png",200,50,125),Color.WHEAT);

        VBox rightPane = new VBox(livesLabel, moneyLabel, waveCountdownLabel, castle1, castle2, castle3, castle4);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setSpacing(5);

        return rightPane;
    }
}
