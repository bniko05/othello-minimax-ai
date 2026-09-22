# Othello AI: Minimax with Alpha-Beta Pruning

A terminal-based implementation of **Othello (Reversi)** in Java, where you play against an AI opponent powered by the **Minimax algorithm with α-β pruning** and a hand-tuned heuristic evaluation function. The search depth is configurable, so you choose how strong the AI is.

[![Open in GitHub Codespaces](https://github.com/codespaces/badge.svg)](https://codespaces.new/bniko05/othello-minimax-ai)

---

## Play it

### Option 1: In your browser
1. Click the **Open in GitHub Codespaces** button above (requires a free GitHub account).
2. Wait for the environment to load. The code compiles automatically.
3. In the terminal, run:
   ```bash
   ./play.sh
   ```

### Option 2: Download and run
1. Download [`othello.jar`](https://github.com/bniko05/othello-minimax-ai/releases/latest/download/othello.jar) from the latest release.
2. Run it:
   ```bash
   java -jar othello.jar
   ```
   
---

## How to play

- At startup, choose the **maximum search depth** from 1 to 12 (the AI difficulty, 5 recommended) and **who plays first** (`P` for Player, `A` for AI).
- Black discs are shown as `*` and white discs as `o`. Black always moves first, as in the official rules.
- Enter your move in two steps: first the **row number** (1–8), then the **column letter** (A–H, upper or lower case). Invalid input is rejected and you are asked again.
- A move is legal only if it captures at least one opponent disc. If you have no legal moves, your turn is skipped automatically.
- The game ends when neither player can move. The player with the most discs wins.

**Example:** you play `(3, D)` and the AI answers with `(5, C)`:

```
    A   B   C   D   E   F   G   H
1 |   |   |   |   |   |   |   |   | 1
2 |   |   |   |   |   |   |   |   | 2
3 |   |   |   | * |   |   |   |   | 3
4 |   |   |   | * | * |   |   |   | 4
5 |   |   | o | o | o |   |   |   | 5
6 |   |   |   |   |   |   |   |   | 6
7 |   |   |   |   |   |   |   |   | 7
8 |   |   |   |   |   |   |   |   | 8
    A   B   C   D   E   F   G   H
```

---

## How the AI works

### Minimax with α-β pruning
The AI explores the game tree up to the chosen depth. Black is the **maximizing** player and White the **minimizing** player, so the AI takes whichever role corresponds to its color.

- **α** holds the best value the maximizer can guarantee so far, and **β** the best value the minimizer can guarantee.
- When a branch's value crosses these bounds, the opponent would never allow that line of play, so the branch is **pruned**. This returns the same move as plain Minimax while evaluating far fewer positions.

### Heuristic evaluation
Leaf positions are scored from Black's perspective by a weighted linear combination of four features:

```
h = 1·f1 + 6·f2 + 4·f3 + 5·f4
```

| Feature | What it measures | Weight | Why |
|---|---|:---:|---|
| `f1` Disc difference | Black discs minus white discs | 1 | Decides the final result, but is volatile early in the game |
| `f2` Corners | Corners held by Black minus corners held by White | 6 | Corners can never be flipped |
| `f3` Edges | Non-corner edge squares held by Black minus White | 4 | Edge discs can only be captured from 3 directions instead of 8 |
| `f4` Corner access | Whether the player to move can take a corner next | 5 | Anticipates gaining a corner |

The weights were chosen intuitively and fine-tuned through play.

---

## Experimental results

We ran full games with the same heuristic and different maximum depths to measure the cost of deeper search:

| Max depth | Average AI response time | Notes |
|:---:|:---:|---|
| 5 | **~56 ms** | Instant response, already hard to beat |
| 10 | **~11 s** | Noticeably slower |
| 12 | up to **~8 min** per move | Impractical for interactive play |

The number of positions grows exponentially with depth, so **depth 5 is the recommended setting**: it offers a strong opponent with no noticeable delay. Even at low depths, beginners find the AI very hard to beat thanks to the corner- and edge-aware heuristic.

---

## Project structure

```
src/
├── Main.java    # Entry point, creates and starts a Game
├── Game.java    # Setup, input handling, game loop, turn logic and scoring
├── Board.java   # Board state, move validation, disc flipping, move generation, heuristic
├── Ai.java      # Minimax with α-β pruning (max / min)
└── Move.java    # Move data type: coordinates (x, y) and heuristic value
```

If a player has no legal move inside the search tree, the turn passes to the opponent, exactly as in the real game.

Key methods in `Board`:
- `isValidMove(x, y, slotType)` scans all 8 directions for a line of opponent discs closed by one of the player's own discs.
- `getChildren(turn)` returns all legal moves for a player. It drives the search tree and detects skipped turns.
- `turnPieces(x, y, slotType)` flips every captured disc after a move.
- `heuristic(turn)` evaluates a position using the four features above.

---

## Authors

Developed as an assignment for the **Artificial Intelligence** course (2025), Department of Informatics, Athens University of Economics and Business.

- **Vasileios Nikolaou** ([@bniko05](https://github.com/bniko05))
- **Giorgos Papachristos**
- **Efthymis Popolis**
