package com.example.termproject2;

public class SingleShotTower extends Tower
{
    SingleShotTower(int price, int damage, int range)
    {
        super(price, damage, range);
    }

    SingleShotTower(String ImageName, int price, int damage, int range)
    {
        super(ImageName, price, damage, range);
    }
}
