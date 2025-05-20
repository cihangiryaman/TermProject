# Tower Defense Game

A JavaFX-based tower defense game developed as a term project for the CSE1242 - Computer Programming II course at Marmara University, Spring 2025.

## 🛡️ Overview

This project implements a tower defense game where players strategically place different types of towers to destroy incoming enemies before they reach the end of a predefined path. The game supports various enemy types, tower mechanics, multiple levels, and an engaging UI.

## 🧾 Authors

- **İbrahim Kabadayı** - 150124077
- **Deniz Arda Şanal** - 150123038
- **Cihangir Yaman** - 150123046

Project submitted to **Dr. Öğr. Üyesi Sanem Arslan Yılmaz** on **May 16, 2025**.

## 🎮 Gameplay

- **Objective:** Prevent enemies from reaching the end of the path by strategically placing towers.
- **Controls:**
  - Drag and drop towers onto the map from the right panel.
  - Click on a placed tower to upgrade it (if you have enough money).
- **Game Over:** The game ends if too many enemies reach the endpoint.
- **Victory:** Successfully defend all waves in all levels.

## 🧠 Features

- **Enemy Types:** 
  - Basic `Enemy`
  - `FastEnemy` with increased speed
  - `TankEnemy` with higher health
- **Tower Types:**
  - `SingleShotTower`: Targets one enemy.
  - `LaserTower`: Hits all enemies in range.
  - `TripleShotTower`: Hits up to three enemies.
  - `MissileLauncherTower`: Explosive AoE attacks.
- **Map System:** Custom map configuration using text files.
- **Wave System:** Timed and scaled wave spawning.
- **Difficulty Levels:** Easy, Normal, Hard.
- **Menus:**
  - Main Menu
  - Game Over Menu
  - You Won Menu
- **UI Components:** Health bars, money tracker, countdowns, visual effects.

## 🛠️ Implementation Details

- **Language:** Java
- **Framework:** JavaFX
- **Design Patterns:**
  - OOP: Inheritance for enemies and towers
  - Event-driven architecture for game interactions
- **Game Mechanics:**
  - Leveling system for towers (visual and functional upgrades)
  - Drag-and-drop functionality
  - Dynamic enemy spawning and movement
  - Real-time shooting and damage application
  - Health and money management

## 🧪 Test Cases & Bug Fixes

Detailed in the `example_report.docx`, key challenges included:
- Preserving tower level on drag-and-drop
- Ensuring towers stop shooting after game ends
- Fixing enemy spawn bugs on new levels
- Properly handling explosion animations and positioning

## 🚀 How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/cihangiryaman/TermProject
   cd TermProject
2.Open in your favorite Java IDE (e.g., IntelliJ IDEA, Eclipse).

3.Ensure JavaFX is properly configured.

4.Run MainMenu.java.
