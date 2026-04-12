# PROTOCOL.md — Message Protocol

**Name(s):**
**EID(s):**

---

For each message type below, fill in all four fields:
- **Direction** — who sends it and who receives it
- **Fields** — every JSON field, its Java type, and whether it can be null/absent
- **When sent** — the exact condition that triggers this message
- **Receiver action** — what the recipient does upon receiving it

Add rows or sections for any message types you defined beyond the ones listed.

---

## ASSIGN

**Direction:** Server → Client

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"ASSIGN"`) | No |
| `playerNumber` | int | No |

**When sent:**

**Receiver action:**

---

## WAITING

**Direction:** Server → Client (player 1 only)

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"WAITING"`) | No |

**When sent:**

**Receiver action:**

---

## READY

**Direction:** Client → Server

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"READY"`) | No |
| `name` | String | Yes |

**When sent:**

**Receiver action:**

---

## GAME_START

**Direction:** Server → Client

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"GAME_START"`) | No |
| `myBoard` | int\[\]\[\] | No |
| `turn` | int | No |

**When sent:**

**Receiver action:**

---

## FIRE

**Direction:** Client → Server

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"FIRE"`) | No |
| `row` | int | No |
| `col` | int | No |

**When sent:**

**Receiver action:**

---

## SHOT_RESULT

**Direction:** Server → Client (both clients)

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"SHOT_RESULT"`) | No |
| `shooter` | int | No |
| `row` | int | No |
| `col` | int | No |
| `hit` | boolean | No |
| `sunkShip` | String | Yes |

**When sent:**

**Receiver action:**

---

## TURN_CHANGE

**Direction:** Server → Client (both clients)

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"TURN_CHANGE"`) | No |
| `turn` | int | No |

**When sent:**

**Receiver action:**

---

## CHAT (client → server)

**Direction:** Client → Server

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"CHAT"`) | No |
| `text` | String | No |

**When sent:**

**Receiver action:**

---

## CHAT (server → client)

**Direction:** Server → Client (both clients)

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"CHAT"`) | No |
| `from` | String | No |
| `text` | String | No |

**When sent:**

**Receiver action:**

---

## GAME_OVER

**Direction:** Server → Client (both clients)

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"GAME_OVER"`) | No |
| `winner` | int | No |
| `finalBoard` | int\[\]\[\] | No |

**When sent:**

**Receiver action:**

---

## OPPONENT_DISCONNECTED

**Direction:** Server → Client

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"OPPONENT_DISCONNECTED"`) | No |

**When sent:**

**Receiver action:**

---

## ERROR

**Direction:** Server → Client

**Fields:**

| Field | Java type | Nullable? |
|---|---|---|
| `type` | String (`"ERROR"`) | No |
| `message` | String | No |

**When sent:**

**Receiver action:**

---

## Additional message types (if any)

*If you defined message types beyond the ones above, document them here using the same four-field format.*
