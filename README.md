# Compact Combat

Compact Combat is a lightweight Minecraft Forge 1.20.1 combat overhaul focused on RPG-style melee systems that remain performant on laptop hardware.

The long-term goal is to build a compact RPG engine for Minecraft with responsive combat, weapon-specific combos, stamina, skill trees, loot, bosses, and large optimized dungeon systems.

## Current Features

- Combat mode toggle
- Stamina system
- Dodge movement
- 3+ hit combo system
- Weapon-specific combo behavior
- Basic melee hit detection
- Knockback scaling
- Lightweight combat particles
- Stamina HUD foundation

## Tech Stack

- Minecraft 1.20.1
- Forge 47.x
- Java 17
- Gradle

## Project Goals

Compact Combat is designed around one principle:

> Make RPG combat feel ambitious without wasting performance.

Instead of relying on heavy systems, the mod focuses on:

- lightweight state machines
- reusable combat managers
- simple client effects
- low-overhead weapon data
- future-ready architecture for multiplayer and server-side combat

## Roadmap

### Milestone 1 — Core Combat

- [x] Forge project setup
- [x] Combat mode
- [x] Stamina
- [x] Dodge
- [x] Combo attacks
- [x] Hit detection
- [x] Weapon behavior

### Milestone 2 — Combat Feel

- [ ] Improved HUD
- [ ] Camera shake
- [ ] Hit stop
- [ ] Forward lunge
- [ ] Better slash effects

### Milestone 3 — Weapon Framework

- [ ] Custom weapon classes
- [ ] Weapon-specific animations
- [ ] Combo finishers
- [ ] Stat-based weapon balancing

### Milestone 4 — RPG Systems

- [ ] Skill trees
- [ ] Loot rarity
- [ ] Affixes
- [ ] Boss mechanics

### Milestone 5 — Compact RPG Engine

- [ ] Modular dungeon generation
- [ ] Boss framework
- [ ] JSON-driven content
- [ ] Multiplayer-safe combat

## Development

Build the project:

bash ./gradlew build

Run the Minecraft client:

bash ./gradlew runClient

## Status

This project is in early prototype development.

Current version:

txt v0.0.1 — Core combat prototype

## Author

Built by Taahirah Denmark.
