






MARMARA UNIVERSITY
FACULTY OF ENGINEERING
 CSE1242 COMPUTER PROGRAMMING II
 (SPRING 2025)


Tower Defence Game

Submitted to: Dr. Öğr. Üyesi SANEM ARSLAN YILMAZ                Due Date: 16.05.2025 



Student ID Number
Name & Surname
1
150124077
İbrahim Kabadayı
2
150123038
Deniz Arda Şanal
3
150123046
Cihangir Yaman

Table of Contents
PROBLEM DEFINITION	3
IMPLEMENTATION DETAILS	3
1. Enemy Class	4
2. FastEnemy Class	6
3. TankEnemy Class	6
4. Tower Class	7
5. SingleShotTower Class	9
6. LaserTower Class	10
7. TripleShotTower Class	11
8. MissileLauncherTower Class	12
9.  MapPane Class	13
10. Map Class	14
11. TextDecoder Class	15
12. Cell Class	16
13. MainMenu Class	16
14. GameOverMenu Class	17
15. YouWonMenu Class	17
16. StageManager Class	17
GAMEPLAY	18
 TEST CASES	20
1. Wrong Drag and Drop codes from cell to cell	20
2. Level upped castles become level 1 whenever drag starts	21
3. Tower continues to shoot from its old coordinates	22
4. Tower keeps shooting after the game finishes 	24
5. Tower doesn’t shoot after the game over	24
6. Game does not spawn enemies on next level	24
7. After changing the sight of the enemies from circle to image, map was shifting above when the wave started	24
8. Explosion Problem	24







PROBLEM DEFINITION
Our aim is to create a small game in which the player tries to destroy enemies that are walking through a path by using various towers, using Java and JavaFX languages.
Things we will implement while creating this game: creating and visualizing maps and paths depending on the levels, creating different types of enemies and towers for variety, designing suitable sight for better game experience, making money algorithm for tower usage, making live algorithm for enemies to make the game competitive, designing different menus for the transitions between the levels and also the menus, making a health bar in order to see the health of the enemies, designing level up mechanism for towers and explosion effect for enemies, showing the player appropriate scenes when these conditions are met.






IMPLEMENTATION DETAILS


1. Enemy Class
Enemy
-
-
-
-
-
-
-
~
-
-
-
-
-
-
_pane: Pane
_mapPane: MapPane
_health: int
_speed: double
_positionX: double
_positionY: double
_isExploding: boolean
tileSize: double
_image: ImageView
_healthBar: Rectangle
maxHealthWidth: double
maxHealth: int
circle: Circle
image: ImageView
+
+

+
+
+
+
+
+
+
+
+
+
+
+
+
+
+
+
Enemy (pane: Pane, initialHealth: int, initialSpeed: double, mapPane: MapPane)
Enemy (pane: Pane, imageName: String, initialHealth: int, initialSpeed: double, mapPane: MapPane)
walk(level: int): void
explode(): void
randomColor(): Color
getSpeed(): double
setSpeed(speed: int)
getHealth(): int
setHealth(amount: int):void
getPositionX(): double
getPositionY(): double
setPosition(x: double, y: double): void
isExploding(): boolean
setExploding(exploding: boolean): void
stopMovement(): void
getCircle(): Circle
getMapPane(): MapPane
setMapPane(): void


An Enemy object has
A pane that provides to add enemies on the map ,
An initialHealth indicating the initial health of the enemies
An initialSpeed indicating the initial speed of enemies
An imageName for the image of the enemies
Enemy class is the superclass of FastEnemy and TankEnemy classes.

There are five levels in the game. In the walk method, the level is passed as an argument in order to indicate in which path enemies will walk. Instead of dividing
paths into cells and making the enemy walk cell by cell, we decided to do them like a single road for more realistic mechanics.
The explode method is invoked automatically when the enemy is killed. It generates a shockwave before explosion animation.
The randomColor method is invoked inside of the explode method. It generates random colours for the explosion animation based on the Math.random() value. For r < 0.3, it generates dark green, for 0.3 < r < 0.6, it generates green and for 0.6 < r, it generates dark olive green.
The shape of the health bar is implemented in the constructor. Also, the mechanics of the health bar is implemented in the setHealth method.
_PositionX and _PositionY are giving the current x and y coordinates of enemies.
Other getter and setter methods are invoked when they are needed.






2. FastEnemy Class
FastEnemy → Enemy
+
+
FastEnemy(pane: Pane, initialHealth: int, initialSpeed: double, mapPane: MapPane)
FastEnemy(pane: Pane, imageName: String, initialHealth: int, initialSpeed: double, mapPane: MapPane)



FastEnemy class is the subclass of the Enemy class. 
The purpose of this class is to create faster enemies for the game.


3.Tank Enemy
Tank Enemy → Enemy
+
+


TankEnemy(pane: Pane, initialHealth: int, initialSpeed: double, mapPane: MapPane
TankEnemy(pane: Pane, imageName: String, initialHealth: int, initialSpeed: double, mapPane: MapPane)



Tank Enemy is the subclass of the Enemy class.
The purpose of this class is to create enemies that have more health.










4. Tower Class
Tower
-
-
-
-
-
-
-
-
-
-
_name: String
_price: int
_damage: int
_range: int
_positionX: int
_positionY: int
_image: ImageView
_reloadTimeSeconds: double
parentCell: StackPane
level: int
+

+

+
+
+
+
+
+
+
+
+
+
++
+
+
+
+
+
+
+
+
+
+
+
Tower(name: String, price: int, damage: int, range: int, reloadTimeSeconds: double)
Tower(name: String, ImageName: String, price: int, damage: int, range: int, reloadTimeSeconds: double)
shoot(): void
setPrice(price: int): void
set_damage(damage: int): void
setimage(imagePath: String): void
levelUp(): void
getPrice(): int
setRange(range: int): void
getRange(): int
getDamage(): int
setDamage(damage: int): void
setPosition(x: int, y: int): void
getPositionX(): double
getPositionY(): double
getImage(): ImageView
getName(): String
getReloadTimeSeconds(): double
setReloadTimeSeconds(reloadTimeSeconds: double): void
get_imagePath(): String
set_imagePath(_imagePath: String): void
getParentCell(): StackPane
setParentCell(cell: StackPane): StackPane
getLevel(): int
setLevel(level: int): void














Tower class is the superclass of SingleShotTower, LaserTower, TripleShotTower, BomberTower.
A Tower object has
a name for further instantiations,
a price,
a damage of each bullet,
a range of shooting.
positionX and positionY coordinates.
image for its assets.
reloadTimeSeconds for duration between two shoots in seconds.
parentCell for getting its parent cell.
level indicating tower’s level.
The purpose of this class is managing towers in the backend.
Each tower has 3 levels. The method levelUp levels up the tower and every upgrade the price of upgrade gets twice also the damage get twice. We have 3 Tower assets for each tower type and we change the image in MapPane class.
There are getter methods for level, parentCell, imagePath, reloadTimeSeconds, positionX, positionY, name, damage, range and price.
There are setter methods for level, parentCell, imagePath, reloadTimeSeconds, position, damage, range and price.

5. SingleShotTowerClass
SingleShotTower → Tower
+
+
+
+
-
-
SingleShotTower(price: int, damage: int, range: int)
SingleShotTower(ImageName: String, price: int, damage: int, range: int) 
levelUp(): void
shoot(): void
attackFirstEnemyInRange(): void
fireBulletAt(enemy: Enemy): void




This tower shoots a bullet to one enemy.
A SingleShotTower has
a levelUp method
a shoot method for shooting enemies in range
Shoot method has 2 helper methods one of them is attackFirstEnemyInRange method for finding the first enemy, other helper method is fireBulletAt for creating a bullet and shooting.

6.LaserTower Class
LaserTower → Tower
+
+
+
-
+
-
LaserTower(price: int, damage: int, range: int)
LaserTower(ImageName: String, price: int, damage: int, range: int)
levelUp(): void
fireBulletAt(enemy: Enemy): void
shoot(): void
attackEnemiesInRange(): void



This tower shoots lasers to all enemies in the range.
A LaserTower has
a levelUp method
a shoot method for shooting enemies in range
Shoot method has 2 helper methods one of them is attackEnemiesInRange method for finding all enemies, other helper method is fireBulletAt for creating a laser and shooting.

7.TripleShotTower Class
TripleShotTower → Tower
+
+
+
-
+-
TripleShotTower(price: int, damage: int, range: int)
TripleShotTower(ImageName: String, price: int, damage: int, range: int)
levelUp(): void
fireBulletAt(enemy: Enemy): void
shoot(): void
attackEnemiesInRange(): void


This tower shoots bullets to three enemies in the range.
A TripleShotTower has
a levelUp method
a shoot method for shooting enemies in range
Shoot method has 2 helper methods one of them is attackEnemiesInRange method for finding first three enemies, other helper method is fireBulletAt for creating three bullets and shooting.

8.MissileLauncherTower Class
MissileLauncherTower → Tower
+
+

+
+
-
-
-
-
MissileLauncherTower(price: int, damage: int, range: int)
MissileLauncherTower(ImageName: String, price: int, damage: int, range: int )
levelUp(): void
shoot(): void
attackEnemiesInRange(): void
fireExplosiveBulletAt(targetEnemy: Enemy): void
createExplosion(x: double, y: double, overlay: Pane):void
applyAreaDamage(explosionX: double, explosionY: double): void



This tower shoots explosive bombs to an enemy in the range.
A MissileLauncherTower has
a levelUp method
a shoot method for shooting enemies in range
a createExplosion method for creating explosion around the bullet’s explosion point
applyAreaDamage method to give damage to enemies near the explosion, these enemies take the half of the damage.
Shoot method has 2 helper methods one of them is attackEnemiesInRange method for finding the first enemy, other helper method is fireExplosiveBulletAt for creating an explosive bullet and shooting.








9.MapPane Class
MapPane
+
+
+
+
+
+
+
+
-
+
money: int
lives: int
rows: String[]
moneyLabel: Label
livesLabel: Label
textDecoder: TextDecoder
waveCountdownLabel: Label
waveCountDownTime: int
overlayPane: Pane
currentWave: int
+
+
+
-
+
-
-
+
-
+
+
MapPane(levelFile: File)
setOverlayPane(pane: Pane): void
getOverlayPane(): Pane
getTimeline(waveDelay: int[]): TimeLine
getPane(): GridPane
getTower(towerType: String, imagePath: String, cost: int, level: int): Tower
playFadeAnimation(node: Node, row: int, column: int): void
returnRightPane(): VBox
returnCastle(tower: Tower, color: Color): void
getNodeAt(gridPane: GridPane, col: int, row: row): Node
getLevelUpImage(level: int, tower: Tower): Image

	
	
A MapPane object has
money to indicate players initial money
lives to indicate how many lives left
rows array to get every row from level.txt file
moneyLabel to display how much money does the player have
lives label to  display how much live does the player have
textDecoder object to get all the necessary data from the level.txt file
waveCountdown label to display how many seconds left for the next wave
a static transparent pane to display the the shockwave affects, laser beams and explosion effects
a current wave to track the wave of the level
This class is responsible for the layout of the level. It creates the gridPane in the getPane method with level  data that comes from the textDecoder object. 
The countdown to each wave is handled by the getTimeline method. This method creates a timeline that updates every second. This method takes all the wave data with the help of a textdecoder object. It displays “Next Wave in: Xs” while waiting for the next wave or “Wave Started” when the spawn of the enemies still continues. Current wave variable is incremented by one when a wave ends.
 Static method of setOverlayPane displays effects on the map such as the explosion effect of the enemies or the shockwave effect of the bomb of the bomber tower.
The constructor also initialises the default values for lives and the money and updates their labels.
The returnRightPane method is used to return a separate vertical layout(VBox). With the help of the returnCastle method this layout holds all the game info labels (costs, castle names and the image of the castle).
The returnCastle method creates a tower object and puts the data of the object into a stackPane for the further display on the returnRightPane method. This stackPane also starts the first event for the drag and drop event chain (setOnDragDetected). This event takes the important tower data for the other events of the drag on drag event chain.
Other events of the drag and drop event chain are on the getPane method. The setOnDropDetected event takes the copy of the castle image and creates a copy on the dropped green rectangle. This event also takes the level of the dragged castle and according to that level and with the help of the getLevelUpImage method and the levelUp method of the tower class it creates a pre leveled up castle on the map. Because the dragged object can not just come from the right pane it can also come from the dragDetected event which is inside of the for loop that constantly checks for the active towers list that is imported from the map object.
The GetTower method helps for the copying of the dragged castle on the dropDetected event at the getPane method. It creates an exact copy of the dragged castle and returns a tower object that takes place on the initialization of the tower on the map. And it makes sure that the shoot method of the tower class takes place.
The getNodeAt method returns a node which in this case is always a stackPane. This method is used to check if the stackPane that the player wants to put his castle on has no castle on it.
The playFadeAnimation is used for the loading animation of the map. For each castle it has a delay that is proportional to its row and column number by doing this the animation will provide a loading visualisation that starts from the top left up corner to bottom most right corner of the map.

10.Map Class
                                                  Map → Application
+
+
-
activeEnemies: List<Enemy>
activeTowers: List<Tower>
currentLevel: int
+
-
+
start(stage: Stage): void
getStrings(fastEnemyRatio: double, enemyCountPerWave: int): List<String>
main(args: String[])



A Map object has
a static tower list to track all the towers in the map
a static enemies list to track active enemies in the map
a number of the current level 
This class serves as the main entry point of the game by extending the Application class.
This class initialises the main game window with a borderPane by getting the necessary nodes from the MapPane class such as the GridPane for the map and VBox for the right side of the level.
It creates the spawn mechanism of the game based on data that textDecoder class extract.
It maintains the static list of the activeEnemies and activeTower to track all the active elements to later make all the important codes to work for certain events. Such as dragging a castle from the map or making sure that level is over.




11.TextDecoder Class
                                                       TextDecoder
+
++++
cells: ArrayList<Cell>
waveDelays: int[]
enemyCountPerWave:int[]
enemySpawnDelayPerWave: double[]
rows: String[]
+
+
-
-
-
+
TextDecoder(levelFile: File)
getLines(levelFile: File): String[]
getEnemyCountPerWave(): int[]
getEnemySpawnDelayPerWave(): double[]
getDelayToOtherWave(): int[]
getGrayCells(): ArrayList<Cell>




A TextDecoder object has
a list of cells for initializing the map
a wave delays array to store the wave delays of the level
an enemy count array to store the count of the enemies per wave of the level
a spawn delay array to store the delay time between two enemies in a wave of the level
a rows array to store all the rows of the level.txt file
This class reads and decodes data from a level.txt file to initialise the game map and wave settings.
The getLines method gets all the rows from the file and stores them in a String array to later get the width, height, coordinates of the gray cells; enemy counts, spawn delays, and wave delay.
The getGrayCells method extracts the coordinates of the gray cells and puts in an arrayList. It also modifies the cells in the cells arraylist.
The other methods parse-wave related data and store them in different arrays.
The constructor extracts width and height and with a nested loop initializes all cells. 




12. Cell Class
Cell
+
+
+
x: int
y: int
isGray: boolean
+
Cell (x: int, y: int, isGray: boolean


Cell class represents a singular cell in the map that is in the grid pane.
Each cell has its own x and y coordinates and isGray boolean type to determine if the enemies will use this cell as a road or not.



13. MainMenu Class
MainMenu
~~
~~
~
bt1: Button
bt2: Button
bt3: Button
scene1: Scene
scene2: Scene
+
start(stage: Stage): void


MainMenu class is for the main menu scene.
bt1 is for the “Start Game” button, bt2 is for the “How To Play” button and bt3 is for the “Exit” button inside the “How To Play” scene.
Scene1 is the initial scene when you open the game. Scene2 is the scene when you click on the “How To Play” button.



14. GameOverMenu Class
GameOverMenu
~~
bt: Button
scene4: Scene
+
+
start(primaryStage: Stage): void
show(stage: Stage): void


GameOverMenu is for the game over menu scene.
bt is for “Back to Main Menu” button.

15. YouWonMenu Class
YouWonMenu
-
~~
nextLevel: int
bt: Button
scene: Scene
+
+
YouWonMenu(nextLevel: int)
start(stage: Stage): void


YouWonMenu is for the transition between the levels.
bt is for “Continue to Next Level” button.
YouWonMenu constructor is used in the Map class for proper transitions between the levels and different maps.

16. StageManager Class
StageManager
+
currentStage: Stage


 
The purpose of this class is to manage stage transitions and making easier to trigger new scenes, menus.

GAMEPLAY
The purpose of the game is killing enemies and not letting them finish the path. Different types of towers can be used for this situation.



Initially, in the main menu, there are three difficulty buttons on the left corner. Players can choose the difficulty (easy, normal, hard) by clicking on the button. Based on the difficulty, the enemy's speed and health changes. After starting the game by clicking the “Start Game” button, the first level opens.  


In all levels, the game mechanism is the same. There are four towers on the right corner of the scene. Also, there are live, money  and wave countdown presentations for players to see current stats. 


When the wave starts, enemies are leaving the cave one by one based on the wave stats that are given by instructors. 


Players can drag different types of towers on the map depending on the money. After adding a tower on the map, it can be dragged on another cell. Also, when the player clicks on the tower, if the money is sufficient, the tower levels up. The tower’s features improve based on its level. 
Towers are shooting to the nearest enemy they see in their range. Players should combine towers decently in order to kill all enemies and pass the level. 
If one enemy reaches the end of the path, one hearth is decreasing. Also, if five enemies reach  the end, the game ends and the Game Over scene opens.

TEST CASES

1.Wrong Drag and Drop codes from cell to cell
When we first try to implement drag and drop events from cell to cell we encountered many problems such as refund of the money does not work, leveled up castles become level 1 again when dropped into another cell, the map acts like there is still a castle on the old coordinates and continues to shot from there.
These problems were complicated. First we solved the refund problem. This is how we did it: Since we only have dragDetectedOn event on the non gray cells detecting whenever a drag event failed should be easy we thought but when we wrote a code that detects event failed that code never worked properly because these events are only in the non gray cells of the map meaning that any drag event that fails can not be detected inside of our getPane method of the MapPane class. So then we decided to make refund system different then usual, it immediately refunds the money whenever we start dragDetectedOn event that is inside the for loop that checks for the active towers which means the refund will only work if we start dragging the castles on the map

2.  Level upped castles becomes level 1 whenever drag starts
After solving the refund problem we focused on the level up problem. This problem was very complicated. Because we thought our code was true and had no clue why the image doesn't change when drag is over.
To solve this problem we put the level value of the dragged object into the dragboard object at the drag detected event. Then taking this value and leveling up the newly created castle on drop detected event we thought we solved the problem but still it did not show the upgraded png of the castle. Because we did not really write the level up method correctly. This method did change the image of the castle but it was still the object that is being changed which means the dragged image is still the same.
To solve this we wrote another method which is getTower. This method creates a tower object with the parameters type of the tower, the imagePath of the tower cost and the level of the tower. In this method the imagePath is changed according to the level and the type of the tower and after this a new tower object is created and leveled up. After creating the new tower with this method and replacing the imageView of the dragged image with the image of the castle this problem is solved



3. Tower continues to shoot from its old coordinates
In this case, the problem was there was no way to stop the shoot method that starts whenever a tower is dropped on the map.
Initially, we tried manually stopping timelines or removing towers after the drag was complete, but this led to inconsistencies especially if the tower was dropped elsewhere or the drag was canceled
We resolved this issue by introducing a delete() method inside the Tower class. 

4. Tower keep shooting after the game finished
The problem is that; when we saw GameOverMenu, so the game is over, if we click to back to main menu button and start new game old tower keeps shooting in the new game( Picture 1)


Picture 1

To solve this problem we stop all animations and clear the activeTowers list.
5. Tower doesn’t shoot after game over
When the game is over, we start a new game from the MainMenu, tower shoots some bullets to the top-left corner of the map and stops shooting.
It is one of the hardest problems we have encountered. To solve this problem we create the stage management from scratch. We create the StageManager class and a static property for tracking the current stage and use it every scene changes & start functions of menus. 
Most importantly this problem stems from non-stopping animations. Because of the fact that the game over action triggers from enemy class, we can not access all animations naturally. Therefore, we make some animations visible to its packages and replace some animations with newer reusable animations. Also we created some new variables to track the gameStatus, like mapPane.isGameOver variable.

6. Game does not spawn enemies on next level
We added a difficulty system in the game and we passed it as a parameter to Map class. However, when you pass the level the difficulty is null.
To solve this problem we make the difficulty variable static because we pass once and we want to use it everywhere.

7. After changing the sight of the enemies from circle to image, map was shifting above when the wave started
In this situation, while we were testing our code, we saw a little problem, the map was shifting a little bit above when the enemies were leaving the cave. The problem was the size of the enemies that were exceeding the size of the rectangles of the map.
To solve this problem, we adjusted the height and width of the enemies equivalent to rectangles of map.



8. Explosion Problem
In several test cases, the explosion was not occurring on the enemy, but on the top left corner of the map.
After a few tests, we realized that the explosion coordinates were not updating based on the enemy’s coordinates. We solved this problem by creating two explodeX and explodeY variables and assigning them to the enemy’s x and y coordinates. 


