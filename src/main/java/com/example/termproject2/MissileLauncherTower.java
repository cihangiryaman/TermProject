package com.example.termproject2;

public class MissileLauncherTower extends Tower
{
    MissileLauncherTower(String name, String ImageName, int price, int damage, int range, double reloadTimeSeconds) {
        super(name, ImageName, price, damage, range, reloadTimeSeconds);
    }

    @Override
    public void levelUp() {

    }

    MissileLauncherTower(String name, int price, int damage, int range, double reloadTimeSeconds) {
        super(name, price, damage, range, reloadTimeSeconds);
    }
}
