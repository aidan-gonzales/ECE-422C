# ECE 422C: Lab — Shogun's Challenge

**Course:** ECE 422C – Software Design and Implementation II  
**Topic:** Object-Oriented Programming, Inheritance, and Polymorphism
**Due Date:** Feb 9th, 2026 @ 11:59 PM
**Late Due Date:** Feb 10th, 2026 @ 11:59 PM for 10% penalty


---

## Overview

In this lab, you will implement a turn-based combat game called **Shogun's Challenge**. A brave Samurai must battle through chambers of a castle, defeating Oni (demons) and collecting powerful artifacts. The game demonstrates core object-oriented programming concepts including inheritance, encapsulation, and composition.

This lab will help you practice:
- Class design and implementation
- Inheritance hierarchies
- Method overriding
- Working with collections
- File I/O and parsing

---

## Learning Objectives

By completing this lab, you will:

- Design and implement a class hierarchy using inheritance
- Understand the relationship between superclass and subclass methods
- Use composition to build complex objects from simpler ones
- Implement a game loop with turn-based logic
- Practice reading from files and parsing data

---

## Background

### Game Mechanics

The game follows a simple flow:

```
┌─────────────────────────────────────────────────────────┐
│                    GAME START                           │
│         Samurai enters the Shogun's Castle              │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
              ┌────────────────────────┐
              │    Enter Chamber       │◄─────────────┐
              │  Encounter an Oni      │              │
              └────────────────────────┘              │
                           │                          │
                           ▼                          │
              ┌────────────────────────┐              │
              │     COMBAT LOOP        │              │
              │  Faster attacks first  │              │
              │  (Oni wins ties)       │              │
              └────────────────────────┘              │
                     │           │                    │
           ┌─────────┘           └─────────┐          │
           ▼                               ▼          │
    ┌─────────────┐               ┌─────────────┐     │
    │ Samurai     │               │ Oni         │     │
    │ Dies        │               │ Defeated    │     │
    └─────────────┘               └─────────────┘     │
           │                               │          │
           ▼                               ▼          │
    ┌─────────────┐               ┌─────────────┐     │
    │ GAME OVER!  │               │ Collect     │     │
    └─────────────┘               │ Artifact    │     │
                                  └─────────────┘     │
                                          │           │
                                          ▼           │
                                  ┌─────────────┐     │
                                  │ More        │─Yes─┘
                                  │ Chambers?   │
                                  └─────────────┘
                                          │ No
                                          ▼
                                  ┌─────────────┐
                                  │  VICTORY!   │
                                  └─────────────┘
```

### Combat Rules

1. **Turn Order**: The character with higher speed attacks first. If speeds are equal, the Oni attacks first.

2. **Attacking**: When a character attacks, they deal damage equal to their attack stat, reducing the target's health.

3. **Oni Special Ability**: When an Oni attacks, it also reduces the Samurai's speed by its `speedDamage` value.

4. **Victory Condition**: Defeat all Oni in all chambers.

5. **Defeat Condition**: Samurai's health drops to 0 or below.

### Class Hierarchy

```
              GameCharacter
              (base class)
                    │
         ┌─────────┴─────────┐
         ▼                   ▼
      Samurai              Oni
      (hero)             (enemy)
```

---

## Assignment

### Files Provided

| File | Description |
|------|-------------|
| `ShogunChallenge.java` | Main entry point — **DO NOT MODIFY** |
| `GameLoader.java` | Parses input files — **DO NOT MODIFY** |
| `GameCharacter.java` | Base character class — **IMPLEMENT** |
| `Samurai.java` | Hero class — **IMPLEMENT** |
| `Oni.java` | Enemy class — **IMPLEMENT** |
| `Chamber.java` | Room class — **IMPLEMENT** |
| `Artifact.java` | Item class — **IMPLEMENT** |
| `Game.java` | Game logic — **IMPLEMENT** |
| `input1.txt`, `input2.txt` | Sample game configurations |
| `output1.txt`, `output2.txt` | Expected outputs |

### Class Specifications

#### 1. `GameCharacter` (Base Class)

The base class for all characters in the game.

**Instance Variables:**
- `private String name` — Character's name
- `private int health` — Current health points
- `private int attack` — Attack power
- `private int speed` — Speed (determines turn order)

**Methods to Implement:**

| Method | Description |
|--------|-------------|
| `String getName()` | Returns the character's name |
| `int getHealth()` | Returns current health |
| `int getAttack()` | Returns attack value |
| `int getSpeed()` | Returns speed value |
| `boolean isAlive()` | Returns `true` if health > 0 |
| `boolean isDead()` | Returns `true` if health ≤ 0 |
| `void hit(GameCharacter target)` | Attacks target, prints message, reduces target health |
| `void slowDown(int speedDamage)` | Reduces this character's speed |
| `void useArtifact(Artifact artifact)` | Adds artifact's stats to this character |

**hit() Output Format:**
```
[attacker name] attacks [target name], causing [damage] damage.
```

---

#### 2. `Samurai` (extends GameCharacter)

The hero character controlled by the player.

**Constructor:**
```java
public Samurai(String name, int health, int attack, int speed)
```

**Methods to Implement:**

| Method | Description |
|--------|-------------|
| `void fights(Oni oni)` | Attacks the oni using `hit()` |

---

#### 3. `Oni` (extends GameCharacter)

A demon enemy with a speed-draining attack.

**Additional Instance Variable:**
- `private final int speedDamage` — Speed reduction inflicted on samurai

**Constructor:**
```java
public Oni(String name, int health, int attack, int speed, int speedDamage)
```

**Methods to Implement:**

| Method | Description |
|--------|-------------|
| `int getSpeedDamage()` | Returns the speed damage value |
| `void fights(Samurai samurai)` | Hits samurai AND slows them down |

---

#### 4. `Chamber`

Represents a room in the castle.

**Instance Variables:**
- `private String name`
- `private Oni oni`
- `private Artifact artifact`

**Methods to Implement:**

| Method | Description |
|--------|-------------|
| `String getName()` | Returns chamber name |
| `Oni getOni()` | Returns the oni in this chamber |
| `Artifact getArtifact()` | Returns the artifact in this chamber |

---

#### 5. `Artifact`

An item that boosts the samurai's stats.

**Instance Variables:**
- `private String name`
- `private int health`
- `private int attack`
- `private int speed`

**Methods to Implement:**

| Method | Description |
|--------|-------------|
| `String getName()` | Returns artifact name |
| `int getHealth()` | Returns health bonus |
| `int getAttack()` | Returns attack bonus |
| `int getSpeed()` | Returns speed bonus |

---

#### 6. `Game`

Contains the main game logic.

**Method to Implement:**

```java
public void play()
```

The `play()` method must:

1. **Print the welcome banner:**
   ```
          Welcome to Shogun's Challenge!
          ------------------------------
   
   [name] starts with health: [health] speed: [speed] attack: [attack]
   
   ```

2. **For each chamber:**
   - Print encounter: `Chamber: [name]. [samurai] encounters a [oni]`
   - Print oni stats: `    [oni] - health: [h] speed: [s] attack: [a] speed damage: [sd]`
   - Determine turn order (higher speed first; oni wins ties)
   - Combat loop until one character dies:
     - Check if samurai is dead → print `[name] is dead - GAME OVER!` and return
     - Check if oni is dead → print `[oni] is defeated!` and break
     - Execute current turn's attack
     - Switch turns
   - Print artifact found: `[samurai] finds [artifact]`
   - Apply artifact to samurai

3. **Print victory:** `[samurai] wins!`

---

## Input File Format

```
SamuraiName,health,attack,speed
ChamberName,OniName,health,attack,speed,speedDamage,ArtifactName,health,attack,speed
ChamberName,OniName,health,attack,speed,speedDamage,ArtifactName,health,attack,speed
...
```

**Example:**
```
Hanzo,100,15,50
Entrance Gate,Shadow Demon,30,5,30,5,Naginata,10,5,0
Garden Path,River Spirit,25,8,40,10,Healing Herb,20,0,5
```

---

## Sample Output

For the input above:

```
       Welcome to Shogun's Challenge!
       ------------------------------

Hanzo starts with health: 100 speed: 50 attack: 15

Chamber: Entrance Gate. Hanzo encounters a Shadow Demon
    Shadow Demon - health: 30 speed: 30 attack: 5 speed damage: 5
Hanzo attacks Shadow Demon, causing 15 damage.
Shadow Demon attacks Hanzo, causing 5 damage.
Hanzo attacks Shadow Demon, causing 15 damage.
Shadow Demon is defeated!
Hanzo finds Naginata

Chamber: Garden Path. Hanzo encounters a River Spirit
    River Spirit - health: 25 speed: 40 attack: 8 speed damage: 10
Hanzo attacks River Spirit, causing 20 damage.
River Spirit attacks Hanzo, causing 8 damage.
Hanzo attacks River Spirit, causing 20 damage.
River Spirit is defeated!
Hanzo finds Healing Herb

Hanzo wins!
```

---

## Compiling and Running

### Compile All Files

```bash
javac *.java
```

### Run with Input File

```bash
java ShogunChallenge input1.txt
```

### Compare Output

```bash
java ShogunChallenge input1.txt > my_output.txt
diff my_output.txt output1.txt
```

---

## Common Mistakes

### 1. Forgetting to call superclass constructor

```java
// WRONG
public Samurai(String name, int health, int attack, int speed) {
    // Missing super() call!
}

// CORRECT
public Samurai(String name, int health, int attack, int speed) {
    super(name, health, attack, speed);
}
```

### 2. Not using inherited methods

```java
// WRONG - reimplementing in subclass
public void fights(Oni oni) {
    System.out.println(this.getName() + " attacks " + oni.getName() + "...");
    // Duplicating hit() logic
}

// CORRECT - using inherited method
public void fights(Oni oni) {
    hit(oni);  // Use the inherited method!
}
```

### 3. Wrong turn order logic

```java
// WRONG - samurai should NOT go first on ties
boolean samuraisTurn = (samurai.getSpeed() >= oni.getSpeed());

// CORRECT - oni wins ties
boolean samuraisTurn = (oni.getSpeed() >= samurai.getSpeed()) ? false : true;
```

### 4. Checking death after both characters attack

```java
// WRONG - should check after EACH attack
samurai.fights(oni);
oni.fights(samurai);
// Only now checking if someone died...

// CORRECT - check after each action
if (samurai.isDead()) { ... }
if (oni.isDead()) { ... }
```

### 5. Output formatting errors

Pay close attention to:
- Exact spacing in the welcome banner (7 spaces before "Welcome")
- 4 spaces before oni stats
- Empty lines after samurai stats and after finding artifacts

---

## Testing Tips

1. **Test incrementally**: Start with `GameCharacter`, verify it works, then move to subclasses.

2. **Use provided test files**: Run your code against `input1.txt` and `input2.txt` and compare with expected outputs.

3. **Create edge case tests**:
   - Samurai dies in first attack
   - Samurai wins without taking damage
   - Multiple chambers with varying difficulties

4. **Check inheritance**: Make sure `Samurai` and `Oni` properly inherit from `GameCharacter`.

---

## Submission Instructions

1. Complete all required methods in the Java files
2. Test with provided input files
3. Ensure output matches expected format exactly
4. Submit the following files to Gradescope:
   - `GameCharacter.java`
   - `Samurai.java`
   - `Oni.java`
   - `Chamber.java`
   - `Artifact.java`
   - `Game.java`

**Note:** Do not submit `ShogunChallenge.java` or `GameLoader.java` — these are provided by the autograder.

---

## Checklist

### GameCharacter
- [ ] All getter methods implemented
- [ ] `isAlive()` and `isDead()` work correctly
- [ ] `hit()` prints message AND reduces target health
- [ ] `slowDown()` reduces speed
- [ ] `useArtifact()` adds all three stats

### Samurai
- [ ] Constructor calls `super()`
- [ ] `fights()` uses inherited `hit()` method

### Oni
- [ ] Constructor calls `super()` and stores `speedDamage`
- [ ] `getSpeedDamage()` returns correct value
- [ ] `fights()` hits AND slows the samurai

### Chamber & Artifact
- [ ] All getter methods implemented

### Game
- [ ] Welcome banner formatted correctly
- [ ] Turn order determined correctly (oni wins ties)
- [ ] Combat loop checks death after each action
- [ ] Artifact found message printed
- [ ] Artifact applied to samurai
- [ ] Victory/defeat messages correct

---

## Summary

This lab teaches fundamental OOP concepts through game development:

| Concept | How It's Applied |
|---------|------------------|
| **Inheritance** | `Samurai` and `Oni` extend `GameCharacter` |
| **Encapsulation** | Private fields with public getters |
| **Composition** | `Chamber` contains `Oni` and `Artifact` |
| **Polymorphism** | `fights()` methods behave differently in each class |
| **Method Overriding** | Subclasses can override inherited behavior |

Understanding these concepts is essential for designing maintainable, extensible software systems.

Good luck, warrior! 🗡️
