package com.example.termproject2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Pane
{
    public static GridPane getPane(File levelFile)
    {
        GridPane map = new GridPane();
        ArrayList<Integer> coordinates = new ArrayList<>();

        String[] rows = getLines(levelFile);
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

                            int currentMoney = Integer.parseInt(CastlesAndBars.moneyLabel.getText().replace("Money: ","").replace("$",""));
                            if (currentMoney >= cost)
                            {
                                int newMoney = currentMoney - cost;
                                CastlesAndBars.moneyLabel.setText("Money: " + newMoney + "$");
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
                }
            }
        }
        map.setHgap(0);
        map.setVgap(0);
        return map;
    }

    public static String[] getLines(File levelFile)
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
}
