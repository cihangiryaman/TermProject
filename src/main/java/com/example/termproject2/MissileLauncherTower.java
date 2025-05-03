package com.example.termproject2;

public class MissileLauncherTower extends Tower
{
    MissileLauncherTower(String ImageName, int price, int damage, int range) {
        super("Missile Launcher Tower", ImageName, price, damage, range, 1);
    }

    MissileLauncherTower(int price, int damage, int range) {
        super("Missile Launcher Tower", price, damage, range, 1);
    }
}
