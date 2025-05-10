package com.example.termproject2;

public class MissileLauncherTower extends Tower
{
    MissileLauncherTower(String ImageName, int price, int damage, int range) {
        super("MissileLauncherTower1.png", ImageName, price, damage, range, 1);
    }

    MissileLauncherTower(int price, int damage, int range) {
        super("MissileLauncherTower1.png", price, damage, range, 1);
    }

    public void levelUp()
    {
        int level = getLevel();
        if (level < 3)
        {
            level++;
            setLevel(level);
            setprice(getPrice() * 2);
            set_damage(getDamage() * 2);
        }
        else
        {

        }
    }
}
