# Labyrinth Escape Game

**Author:** Saeed Fathallah Khanloobrise  
**Course:** Programming Technology – Semester 3  
**University Project**

## 🧩 Description
Labyrinth is a GUI-based strategy game where the player must escape a randomly generated maze while avoiding a patrolling dragon. The player starts at the bottom-left and must reach the top-right. The dragon moves in random straight lines until hitting walls. If it gets too close, the player dies.

The game includes:
- Procedural maze generation using dynamic graphs and backtracking traversal algorithms.
- Limited visibility (3 units of sight).
- High score system saved in a database.
- Restart and high score viewing options in the menu.
- Full GUI interface (Java Swing).
- JUnit-based unit testing.
- Documented with Javadoc.

## 🎮 Gameplay Rules
- Movement: Arrow keys or GUI buttons (Up, Down, Left, Right).
- Goal: Reach the top-right corner without being caught by the dragon.
- Dragon: Randomly spawned, moves until hitting a wall, then picks a new direction.
- Vision: Limited to a 3-tile radius around the player.
- Death: Occurs if dragon enters a neighboring tile.
- Each solved maze increases your score.
- On death, your name and score are saved in the local database.

## 🧠 Technologies
- **Java (Swing)** – GUI
- **Graph algorithms** – for path generation
- **Backtracking** – for escape routes
- **JDBC** – for storing highscores
- **JUnit** – for testing
- **Javadoc** – for code documentation

## 📦 How to Run
```bash
mvn clean install
java -jar target/LabyrinthGame.jar
```

## 🧪 Run Tests
```bash
mvn test
```

## 📈 View High Scores
Use the "High Scores" option from the game menu.

---

**Enjoy escaping... if you can. 🐉**
