package com.example.termproject2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

class Cell
{
    public int x;
    public int y;
    public boolean isGray;
    public Cell(int x,int y,boolean isGray)
    {
        this.x = x;
        this.y = y;
        this.isGray = isGray;
    }
}

public class TextDecoder
{
    ArrayList<Cell> cells = new ArrayList<Cell>();
    int[] waveDelays;
    int[] enemyCountPerWave;
    double[] enemySpawnDelayPerWave;
    String[] rows;

    public TextDecoder(File levelFile)
    {
        rows = getLines(levelFile);
        int width = Integer.parseInt((rows[0].split(":"))[1]);
        int height = Integer.parseInt((rows[1].split(":"))[1]);
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                cells.add(new Cell(i, j, false));
            }
        }
        waveDelays = getDelayToOtherWave();
        enemyCountPerWave = getEnemyCountPerWave();
        enemySpawnDelayPerWave = getEnemySpawnDelayPerWave();
    }

    static String[] getLines(File levelFile)
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

    public ArrayList<Cell> getGrayCells()
    {
        ArrayList<Cell> grayCells = new ArrayList<>();
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
                for (Cell cell : cells)
                {
                    if(cell.x == x && cell.y == y)
                    {
                        cell.isGray = true;
                        grayCells.add(cell);
                    }
                }
            }
        }
        return grayCells;
    }
}
