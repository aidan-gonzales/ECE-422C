# Lab 3 Design Document: Space Station Simulation

**Name: Aidan Gonzales**  
**EID: AGG3498**  
**Date: 2/15/26**  

---

## 1. System Overview

The design goal of this lab is to simulate a 
self-sustaining ecosystem. The space station is populated with
MaintenanceBots, Engineers, Commanders, and PowerCells,
each with a different purpose (described in section 2).

My final simulation achieves a stable equilibrium under a
variety of conditions. I have tested my simulation with
different entity starting counts, seeds, and a very large
amount of steps. I have found that the simulation works
best for most given seed when the entity counts are all
set to 50 at the start. For most seeds, the entity counts
will fluctuate around 50 for tens of thousands of steps.

---

## 2. Class Descriptions


### 2.1 Commander


Commanders act as the apex predator in this ecosystem.
They attack all entities within a two tile radius. If there 
are no entities nearby, they will patrol their territory until
unknowing prey arrive.

I decided to make commanders very aggressive because I was having
issues with populations increasing too much. By having commanders
act as a sort of "crowd control", I was able to maintain a good
equilibrium.

Since commanders gain a lot of energy from winning fights,
they reproduce at a higher than average energy threshold. This
threshold was settled on after much testing to make sure that the
commanders don't overpower the other entities.

### 2.2 Engineer


Engineers play a healing role for the most critical entities:
the maintenance bots. When a maintenance bot is nearby, the
engineer will move to it and repair it, giving both the
maintenance bot and the engineer itself some energy. This creates
a symbiotic relationship between the two.

In my space station, maintenance bots do all the hard work
on the ship. I designed the engineers to be more of a support role
to assist the maintenance bots in their important duties.

Engineers are somewhat fragile to attacks from commanders, so
their reproduction threshold is a little lower than other
entities. Also, they gain energy from repairing maintenance bots,
so the more that they do their job, the longer they will live.



### 2.3 Other Classes (if modified or relevant)


To allow engineers and commanders to know what entities are
around them, I had to add helper functions to the Entity class.
I added getX() and getY() functions so that I could calculate
how close entities were to engineers and commanders. This
allows them to pathfind to specific targets to heal or attack.

---

## 3. UML Class Diagrams

_Provide a UML class diagram for every class you implemented. Each diagram must show:_

- _Class name_
- _Fields (with visibility: `+` public, `-` private, `#` protected)_
- _Methods (with visibility, parameters, and return types)_
- _Inheritance relationships (solid arrow to parent class)_
- _Any associations or dependencies between your classes and the provided classes_

_Include at minimum:_

- [ ] Commander UML diagram
- [ ] Engineer UML diagram
- [ ] A diagram showing the inheritance hierarchy (Entity ← Commander, Entity ← Engineer, Entity ← PowerCell, Entity ← MaintenanceBot)

_You may draw these by hand and scan them, use a tool like draw.io or Lucidchart, or produce them in any other legible format._

---

## 4. Ecosystem Design

### 4.1 Entity Roles


| Entity | Role                                          | Energy Strategy                                 | Reproduction Threshold | Fight Behavior                                 |
|--------|-----------------------------------------------|-------------------------------------------------|------------------------|------------------------------------------------|
| PowerCell | Energy producer                               | Solar charging (+1/step)                        | N/A                    | Never fights                                   |
| MaintenanceBot | Complete vital tasks around the space station | Collect power cells and get healed by engineers | 150                    | Always tries to fight, but unfortunately loses |
| Commander | Crowd control for other entities              | Take energy from winning fights                 | 200                    | Pathfinds to entities and always fights        |
| Engineer | Repair maintenance bots                       | Healing maintenance bots rejuvinates engineers  | 130                    | Always tries to flee                           |

### 4.2 Balance and Tuning

_Describe the process you went through to balance your ecosystem. Answer questions like:_

- _What initial entity counts did you start with?_
- _What happened in your first attempts? (e.g., all entities died, one type dominated, etc.)_
- _What parameters or behaviors did you adjust to fix it?_
- _What seed value and entity counts produce a stable run of 500+ steps?_

### 4.3 Failure Modes

1. **Failure mode 1:** The first main failure that I experienced
was when the populations of maintenance bots and engineers
were growing way too large. All entities started at the
same population, but very quickly these two entities took off.
To fix this, I increased the aggressiveness of commanders.
I changed their awareness radius from 1 tile to 2 tiles. This
helped keep maintenance bots and engineers under control.
2. **Failure mode 2:** The second main failure that I experienced
was when the population of engineers always dropped to zero.
My original design had it so that engineers lost some
energy when repairing maintenance bots, and after some testing, 
I realized that they were dying off because of this feature. To 
fix this, made it so that engineers gain energy when repairing
maintenance bots. This helped keep the engineer population
much more stable.

---

## 5. Testing

_Describe how you tested your implementation beyond the 5 provided tests. What edge cases did you consider? What commands did you run manually to verify behavior? If you wrote any additional test input files, describe what they test._

---

## 6. Challenges and Lessons Learned

_What was the hardest part of this lab? What would you do differently if you started over? What did you learn about object-oriented design from this experience?_
