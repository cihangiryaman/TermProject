package com.example.termproject2;

import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class CastlesAndBars
{
    public static Label moneyLabel = new Label("Money: 150$");
    public static Label livesLabel = new Label("Lives: 5");
    public static Label waveLabel = new Label("Next wave: 5s");


    public static StackPane returnCastle(String name, String cost, String imagePath, Color color,int radius)
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
    public static VBox returnRightPane()
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

