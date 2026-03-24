# 🎮 Arkanoid

An Object-Oriented implementation of Arkanoid arcade game written in Java.

## 📸 Gallery

![Main Menu](https://github.com/user-attachments/assets/c3047b71-d5a4-4ea9-a183-0d4661c91844)

<div align="center">
  <img src="https://github.com/user-attachments/assets/5beb3f4e-9fda-4cf5-b435-4db8dd9f8d41" width="400" />
  <img src="https://github.com/user-attachments/assets/9168571e-cc81-496a-9121-a47de43c35bd" width="400" />
</div>
<div align="center">
  <img src="https://github.com/user-attachments/assets/4e544a90-a22a-4c11-ae40-034a349bfb4e" width="400" />
  <img src="https://github.com/user-attachments/assets/8390dae4-bc70-4ca7-99cd-dd3ecbb54dae" width="400" />
</div>
<div align="center">
  <img src="https://github.com/user-attachments/assets/6728b453-2c28-4f29-b8ea-d18ddc0bd53a" width="400" />
</div>

## 🚀 Quickstart

### Prerequisites

- Java **25+**
- Maven

### Run

```bash
git clone https://github.com/niragam/Arkanoid.git
cd Arkanoid
mvn javafx:run
```

## 🎮 Controls

- Main menu: `↑/↓` + `Enter`
- Move paddle: `←/→`
- Launch ball: `Space`
- Pause: `P` (resume with `P` or `Space`, quit to menu with `Q`)
- Debug: `K` skips the current level

## ✨ Key Features

- **Clean Architecture**: Separation of concerns between core logic, rendering, and game entities.
- **Design Patterns**:
  - **Strategy**: Used for adaptable level definitions.
  - **Observer**: Handles game events like collisions and score updates.
  - **Game Loop**: Custom animation loop for smooth gameplay.
- **Physics Engine**:
  - Precise collision detection system.
  - Velocity-based movement with angle calculations.
  - Dynamic paddle bounce mechanics.
- **Gameplay**:
  - **Power-Up System**: Includes Multi-Ball, Extra Life and Paddle-Resize modifiers.
  - **Multiple Levels**: Varied backgrounds and block layouts.
  - **HUD**: Real-time score and lives tracking.

## 🛠️ Technology Stack

- **Language**: Java 25
- **Framework**: JavaFX 25.0.1
- **Build Tool**: Maven
- **Testing**: JUnit 5

## 📐 Project Structure

| Package                          | Purpose                                               |
| :------------------------------- | :---------------------------------------------------- |
| **`arkanoid`**                   | JavaFX app entry points (`Launcher`, `App`).          |
| **`arkanoid.animation`**         | Animation abstractions and per-screen/level runners.  |
| **`arkanoid.core`**              | Game flow controls and main loop.                     |
| **`arkanoid.entity`**            | Game objects (`Ball`, `Paddle`, `Block`).             |
| **`arkanoid.event`**             | Hit events/listeners (scoring, removers, power-ups).  |
| **`arkanoid.geometry`**          | Math primitives (`Point`, `Line`, `Rectangle`).       |
| **`arkanoid.level`**             | Level generation and layouts.                         |
| **`arkanoid.graphics`**          | Rendering utilities and drawing primitives.           |
| **`arkanoid.graphics.painters`** | Per-entity painters used by the renderer.             |
| **`arkanoid.screen`**            | UI screens (menu, pause, etc.).                       |
| **`arkanoid.util`**              | Small shared utilities (counters, input state, etc.). |
