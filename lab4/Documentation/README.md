# ECE 422C — Lab 4 — Spring 2026

## Battleship

**Due March 9, 2026 @ 11:59pm**

---

## General Assignment Requirements

The purpose of this assignment is to design and implement an OO program with multiple classes to play a text-based version of the classic board game called Battleship. You are free to use whatever classes and methods from the Java library you wish.

First, read the Wikipedia article on the game of Battleship at: [Battleship (game)](https://en.wikipedia.org/wiki/Battleship_(game)).

The version of the game you implement will have the following properties:

- The computer will secretly place a fleet of ships on a grid.
- The player will try to sink all ships by firing shots at grid coordinates.
- The player has **50 shots** to sink the entire fleet. This number should be easily changeable by modifying your code via the provided `GameConfiguration` class.
- If the player does not sink all ships within the allotted shots, they lose the game.
- The default board is a **10×10 grid**. Rows are labeled **A–J**, columns are labeled **1–10**. The board size should be configurable.
- The default fleet consists of:
  - **Carrier** (5 cells)
  - **Battleship** (4 cells)
  - **Cruiser** (3 cells)
  - **Submarine** (3 cells)
  - **Destroyer** (2 cells)

  The number of ships, their names, and sizes should be configurable through `GameConfiguration.java`.

- The player enters a target coordinate as a **letter–number pair**, for example: `A5`, `C10`, `J1`.
- After each shot, the result is displayed as **"Hit!"** or **"Miss"**.
- When all cells of a ship have been hit, the player is told which ship was sunk: `"You sunk the <ShipName>!"`
- The player's guesses must be **error-checked**: valid row letter, valid column number, and no repeat shots at the same cell.
- Five special commands may be entered instead of a coordinate:
  - `BOARD` — displays the current board showing hits (`X`), misses (`O`), and unknown water (`~`).
  - `FLEET` — displays each ship's name, size, and whether it is afloat or sunk.
  - `HISTORY` — displays the history of all valid shots and their results in order.
  - `HELP` — displays a list of all available commands and their descriptions.
  - `QUIT` — forfeits the current game immediately. The player's board is displayed, and in test mode the hidden board with remaining ship positions is also shown. The player is then asked if they want to play again.

---

## Sample User Dialogue

Console output is shown in code blocks. User input is marked with `<-- user input`. Do not change the text or format of program output. If you pipe your output to a file instead of the console, the user input part will be missing in the file. We will therefore allow extra or missing newlines in the places where the user inputs text.

### Initial Greeting

```
Welcome to Battleship. Here are the rules.

This is a text version of the classic board game Battleship.

The computer will secretly place a fleet of ships on a 10x10 grid.
The fleet consists of:
  Carrier (5 cells)
  Battleship (4 cells)
  Cruiser (3 cells)
  Submarine (3 cells)
  Destroyer (2 cells)

You try to sink all the ships by firing shots at grid coordinates.
Enter coordinates as a letter (row) followed by a number (column),
for example: A5, C10, J1.
Rows are labeled A-J and columns are labeled 1-10.

After each shot, the result will be displayed as a Hit or Miss.
When all cells of a ship are hit, it sinks and you are notified.

You have 50 shots to sink the entire fleet or you lose.
Type BOARD to see your current board, FLEET to see ship status,
HISTORY to see your shot history, HELP to see available commands,
or QUIT to forfeit the current game.

Are you ready to play? (Y/N):
Y                                                 <-- user input
```

### Game Start (Test Mode)

In test mode (`java Driver 1`), the hidden board is displayed so you can verify ship placement:

```
Deploying ships ...
(TEST MODE) Ship positions:
    1  2  3  4  5  6  7  8  9 10
A   S  S  S  S  S  ~  ~  ~  ~  ~
B   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
C   ~  ~  S  ~  ~  ~  ~  ~  ~  ~
D   ~  ~  S  ~  ~  ~  ~  ~  ~  ~
E   ~  ~  S  ~  ~  S  S  S  ~  ~
F   ~  ~  S  ~  ~  ~  ~  ~  ~  ~
G   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
H   S  ~  ~  ~  ~  ~  ~  ~  ~  ~
I   S  ~  ~  ~  ~  ~  ~  ~  ~  ~
J   S  ~  ~  ~  ~  ~  ~  ~  S  S
```

### Firing Shots — Hits and Misses

```
You have 50 shots remaining. 5 ships afloat.
Enter target coordinate (e.g., A5):
Fire at: A1                                       <-- user input

A1 -> Hit!

You have 49 shots remaining. 5 ships afloat.
Enter target coordinate (e.g., A5):
Fire at: B1                                       <-- user input

B1 -> Miss
```

### Sinking a Ship

When the final cell of a ship is hit:

```
You have 46 shots remaining. 5 ships afloat.
Enter target coordinate (e.g., A5):
Fire at: A5                                       <-- user input

A5 -> Hit! You sunk the Carrier!
```

### Invalid Input Handling

Out-of-range row, out-of-range column, and garbage input:

```
Fire at: Z5                                       <-- user input

Z5 -> Invalid coordinate
Enter a letter A-J followed by a number 1-10 (e.g., A5, J10).

Fire at: A11                                      <-- user input

A11 -> Invalid coordinate
Enter a letter A-J followed by a number 1-10 (e.g., A5, J10).

Fire at: abc                                      <-- user input

abc -> Invalid coordinate
Enter a letter A-J followed by a number 1-10 (e.g., A5, J10).
```

Duplicate shot at an already-targeted cell:

```
Fire at: A1                                       <-- user input

A1 -> Already targeted
```

Invalid shots and commands do **not** cost the player a shot and are **not** recorded in the history.

### BOARD Command

Shows the current state of the player's view:

```
Fire at: BOARD                                    <-- user input

    1  2  3  4  5  6  7  8  9 10
A   X  X  ~  ~  ~  ~  ~  ~  ~  ~
B   O  ~  ~  ~  ~  ~  ~  ~  ~  ~
C   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
D   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
E   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
F   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
G   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
H   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
I   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
J   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
```

Where `X` = Hit, `O` = Miss, `~` = Unknown/Water.

### FLEET Command

Shows which ships are still afloat and which have been sunk:

```
Fire at: FLEET                                    <-- user input

  Carrier (5): SUNK
  Battleship (4): afloat
  Cruiser (3): afloat
  Submarine (3): afloat
  Destroyer (2): afloat
```

### HISTORY Command

Shows all valid shots fired so far and their results, in tabular format (coordinate, two tabs, result):

```
Fire at: HISTORY                                  <-- user input

A1		Hit
B1		Miss
A2		Hit
A3		Hit
A4		Hit
A5		Hit (sunk Carrier)
```

Only valid shots are stored and displayed. Invalid guesses and commands do not appear.

### HELP Command

Displays all available commands:

```
Fire at: HELP                                     <-- user input

Available commands:
  <coordinate>  Fire a shot (e.g., A5, J10)
  BOARD         Show your current board
  FLEET         Show the status of each ship
  HISTORY       Show all previous shots and results
  HELP          Show this help message
  QUIT          Forfeit the current game
```

### QUIT Command

Forfeits the current game immediately. The player's board is displayed, and in test mode the hidden board is also revealed so the autograder can verify remaining ship positions:

```
Fire at: QUIT                                     <-- user input

You have forfeited the game.
Ships remaining: 4

    1  2  3  4  5  6  7  8  9 10
A   X  X  X  ~  ~  ~  ~  ~  ~  ~
B   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
C   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
...

(TEST MODE) Hidden board:
    1  2  3  4  5  6  7  8  9 10
A   X  X  X  S  S  ~  ~  ~  ~  ~
B   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
C   S  S  S  S  ~  ~  ~  ~  ~  ~
...

Are you ready for another game (Y/N):
N                                                 <-- user input
```

The hidden board display on QUIT only appears in test mode. In normal mode, only the player's view board is shown.

### Winning the Game

```
I2 -> Hit! You sunk the Destroyer!

All ships sunk! You win in 17 shots!

    1  2  3  4  5  6  7  8  9 10
A   X  X  X  X  X  ~  ~  ~  ~  ~
B   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
C   X  X  X  X  ~  ~  ~  ~  ~  ~
D   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
E   X  X  X  ~  ~  ~  ~  ~  ~  ~
F   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
G   X  X  X  ~  ~  ~  ~  ~  ~  ~
H   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
I   X  X  ~  ~  ~  ~  ~  ~  ~  ~
J   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~

Are you ready for another game (Y/N):
N                                                 <-- user input
```

### Losing the Game

```
B9 -> Miss

Sorry, you are out of shots. You lose, boo-hoo.
Ships remaining: 5

    1  2  3  4  5  6  7  8  9 10
A   ~  ~  ~  ~  ~  ~  ~  ~  ~  ~
B   ~  ~  ~  ~  ~  ~  ~  ~  O  O
C   ~  ~  ~  ~  O  O  O  O  O  O
D   ~  ~  ~  ~  O  O  O  O  O  O
...

Are you ready for another game (Y/N):
N                                                 <-- user input
```

---

## Testing Harness and the Autograder

To make automated testing possible, the game supports a **deterministic ship placement mode**. The provided `ShipPlacementGenerator` class has two modes:

- **Random mode** (normal play): Ships are placed randomly on the board.
- **Deterministic mode** (testing/autograder): Ship placements are read from standard input so the autograder knows exactly where every ship is located.

### Deterministic Placement Format

In deterministic mode, immediately after the player types `Y` to start a game, the program reads **one line per ship** (in the order they appear in `GameConfiguration.SHIP_NAMES`). Each line contains three space-separated tokens:

```
row col orientation
```

where `row` and `col` are **0-indexed integers** and `orientation` is `H` (horizontal) or `V` (vertical).

For example, for the default 5 ships:

```
0 0 H       -- Carrier at row 0, col 0, placed horizontally (A1-A5)
2 0 H       -- Battleship at row 2, col 0, horizontally (C1-C4)
4 0 H       -- Cruiser at row 4, col 0, horizontally (E1-E3)
6 0 H       -- Submarine at row 6, col 0, horizontally (G1-G3)
8 0 H       -- Destroyer at row 8, col 0, horizontally (I1-I2)
```

### Activating Test Mode

Deterministic mode is activated when the program is run with a command-line argument of `1`:

```
java Driver 1
```

In this mode the hidden board with ship positions is also printed at the start of each game so the tester can verify correct placement.

Normal (random) mode:

```
java Driver
```

### How the Autograder Uses This

Because the autograder controls `stdin`, a test case input file looks like this:

```
Y                          <-- ready to play
0 0 H                      <-- Carrier placement
2 2 V                      <-- Battleship placement
4 5 H                      <-- Cruiser placement
7 0 V                      <-- Submarine placement
9 8 H                      <-- Destroyer placement
A1                          <-- first shot
B1                          <-- second shot
HISTORY                     <-- check history
...
N                           <-- done playing
```

The autograder runs `java Driver 1 < input.txt > actual_output.txt` and then diffs `actual_output.txt` against the expected `output.txt`. Because the ship placements are deterministic and known, the autograder can construct any sequence of shots and predict exactly what the output should be.

### Important

**Do not modify or submit** the `ShipPlacementGenerator` class or `GameConfiguration` class. Use them exactly as provided. Import `ShipPlacementGenerator` and call:

```java
ShipPlacementGenerator.getInstance().generatePlacements(scanner, deterministic)
```

This returns a `ShipPlacements` object containing:
- `board` — the populated `char[][]` with `WATER` and `SHIP` characters
- `shipRows` — 2D array where `shipRows[i][j]` is the row of the j-th cell of ship i
- `shipCols` — 2D array where `shipCols[i][j]` is the column of the j-th cell of ship i

Use the `shipRows`/`shipCols` arrays to track which ship has been hit and detect when a ship is fully sunk.

---

## Program Structure

You must have a class in your program called `Game`, which is nearly at the top level (a `Driver` class with `main()` should call `Game`'s constructor, and is at the top level). The constructor must take a **boolean** value as its first parameter indicating test mode. If test mode is true, the ship positions are revealed at the start of each game, as shown in the sample output. You should read in the first argument to `main`: if the first argument is `1`, set testing mode to true; otherwise testing mode is false.

For example, we will call your program by saying:

```
java Driver 1
```

if we want to set the testing mode to true, and:

```
java Driver
```

or:

```
java Driver <something else>
```

if we want to set the testing mode to false.

Your program must have a **single `Scanner` object** connected to the keyboard (standard input) that is passed to any methods as necessary. You must have only 1 `Scanner` object connected to the keyboard, and it should be created only once during your entire program. It can be created once in `main()` and passed to `Game` as an additional constructor parameter, or created once in the `Game` class's constructor if the `Game` object is created only once in your program.

For example, if the user finishes a game and wishes to play again, a **new `Scanner` object should not be created**. Creation of multiple `Scanner` objects breaks our grading script's functioning.

The `Game` class must also have a method named `runGame` that carries out the actual gameplay. Your `main()` method could create a new `Game` object for each game played, or create one `Game` object that has a loop for multiple games.

Part of the assignment grade will be determined by how easily your program could be altered to allow a **different board size**, **different number of shots**, or **different fleet compositions**. These changes should be possible by modifying only `GameConfiguration.java`.

One of the criteria of the assignment is to **break the problem up into smaller classes** even if you think the problem could be solved more easily with ONE BIG CLASS. For this assignment you should have more than 1 class. Consider having separate classes for:

- **Board** — display and cell tracking (the grid, rendering, firing shots)
- **Fleet** — ship damage tracking and sinking detection
- **Game** — overall control flow, input parsing, game loop

---

## Submission

When finished, upload your files to GradeScope and test your game there with the provided test cases. Include all the source code for all the classes you created, with a header for each source code file. **Do not submit** `ShipPlacementGenerator.java` or `GameConfiguration.java`.

---

## How to Proceed

Recall that when designing a program in an object-oriented way, you should consider implementing a class for each type of entity you see in the problem. For example in Battleship there is a game board, ships, a fleet, shot results, coordinates, and an overall game runner. Some things are so simple you may choose not to implement a class for them (e.g., a coordinate can be a simple `int[]` or two ints).

After deciding what classes you need, implement them one at a time, simplest ones first. Test a class thoroughly before going on to the next class. You may need to alter your design as you implement and test classes. Remember: **Design a little — code a little — test a little. Repeat.**

I recommend you work on this incrementally. Start with a design and try to get that to work. Have a working program at all times and add to it as you implement more features. This will avoid the assignment becoming an all-or-nothing affair. Even if you don't finish you will have a working version with some functionality ready to turn in.

### Suggested Order of Implementation

1. Get the `Driver` to compile and print the greeting, reading `Y`/`N` from the user.
2. Call `ShipPlacementGenerator` to get a board and print it (in test mode).
3. Implement coordinate parsing and validation — make sure `A5`, `J10`, and invalid inputs are handled correctly.
4. Implement the shot logic — mark hits and misses on the board.
5. Implement sinking detection — track per-ship hits and announce when a ship sinks.
6. Implement the `BOARD`, `FLEET`, and `HISTORY` commands.
7. Implement the `HELP` command (display available commands).
8. Implement the `QUIT` command (forfeit the game, display boards, prompt play-again).
9. Implement the win/loss detection and play-again loop.

---

## Design Deliverables

You must upload a UML diagram of your solution and your zipped JavaDoc documentation to Canvas by the due date, as well as submitting to GradeScope.

If you have made significant changes to the methodology described in these, resubmit these with your final code.

---

## Tips and Hints

The main algorithmic challenge in this assignment is **coordinate parsing** and **sinking detection**. Here are some hints:

**Coordinate Parsing:** The player enters coordinates like `A5` or `J10`. The row is the first character (letter), the column is the remaining characters (number). Remember:
- Row `A` = index 0, Row `J` = index 9
- Column `1` = index 0, Column `10` = index 9
- Reject anything that doesn't start with a valid letter or has an invalid number

**Sinking Detection:** When a shot hits, you need to figure out *which ship* was hit and whether all of that ship's cells have now been hit. The `ShipPlacements` object gives you the row and column of every cell of every ship. One approach: for each ship, maintain a count of remaining (unhit) cells. When a hit occurs, scan all ships' cells to find which ship contains that coordinate, then decrement its counter. When the counter reaches zero, announce the sinking.

**Board Display:** Use a 2D `char[][]` for the player's view. Initialize every cell to `~`. On a hit, set it to `X`. On a miss, set it to `O`. Print with row labels on the left and column labels across the top.

---

## Checklist

- [ ] Re-read the requirements after you finished your program to ensure that you meet all of them.
- [ ] Make sure that you use the keyboard `Scanner` in the prescribed way (single instance, passed around)?
- [ ] Make sure that you use the `ShipPlacementGenerator` in the prescribed way?
- [ ] Test with `java Driver 1` (test mode) and `java Driver` (normal mode)?
- [ ] Make up your own test cases?
- [ ] Verify that `BOARD`, `FLEET`, `HISTORY`, `HELP`, and `QUIT` commands work correctly?
- [ ] Verify invalid input handling (bad coordinates, duplicates, empty input)?
- [ ] Verify sinking announcements appear at the right time?
- [ ] Verify win and loss conditions both work?
- [ ] Verify playing multiple games in a row works?
- [ ] Make sure that all your submitted files have the appropriate header?
- [ ] Upload your solution to GradeScope, remembering to include ALL your files?
- [ ] Upload your UML and JavaDoc ZIP file to Canvas?

---
