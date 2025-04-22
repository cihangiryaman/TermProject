package com.example.termproject2;

public class Infantry extends Enemy
{
    Infantry(int initialHealth, int initialSpeed)
    {
        super(initialHealth, initialSpeed);
    }

    Infantry(String imagePath, int initialHealth, int initialSpeed)
    {
        super(imagePath, initialHealth, initialSpeed);
    }
}
