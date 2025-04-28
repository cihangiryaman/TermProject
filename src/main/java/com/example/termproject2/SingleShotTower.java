package com.example.termproject2;

public class SingleShotTower extends Tower
{
    SingleShotTower(int price, int damage, int range)
    {
        super("Single Shot Tower", price, damage, range);
    }

    SingleShotTower(String ImageName, int price, int damage, int range)
    {
        super("Single Shot Tower",ImageName, price, damage, range);
    }
}
