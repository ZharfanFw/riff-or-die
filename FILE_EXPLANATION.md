# 📁 RIFF OR DIE - FILE STRUCTURE & EXPLANATION

Penjelasan singkat setiap file untuk presentasi. Format: **File → Fungsi Utama → Key Points**

---

## 🎯 MAIN & CONFIG

### `Main.java`
**Fungsi:** Entry point aplikasi  
**Apa yang dilakukan:**
- Membuat JFrame
- Menginisialisasi AudioManager
- Menampilkan MenuPanel

**Line penting:** `new GameFrame()`

---

### `GameConstants.java`
**Fungsi:** Pusat semua konstanta game  
**Apa yang dilakukan:**
- Menyimpan semua nilai: speed, ukuran, volume, timings
- Tidak ada magic numbers di code → mudah di-tweak

**Contoh:**
- `PLAYER_SPEED = 260` (pixel per detik)
- `MONSTER_SHOOT_COOLDOWN = 2000` (milidetik)
- `WAVE_0_SPAWN_RATE = 6000` (milidetik)

**Benefit:** Ubah satu nilai di sini, semua otomatis update!

---

## 🎮 MODEL LAYER (Game Logic)

### `GameEngine.java`
**Fungsi:** Core game logic - jantung game  
**Apa yang dilakukan:**
- Game loop dengan delta time (frame-rate independent)
- Spawn monsters sesuai wave
- Collision detection (player vs amplifier, bullet vs monster/amplifier)
- Update score, wave progression
- Call AudioManager untuk SFX

**Key Methods:**
- `update(deltaTime)` - Jalankan setiap frame
- `playerShoot()` - Buat bullet baru
- `checkCollisions()` - Detect semua tabrakan
- `spawnMonsters()` - Spawn berdasar wave

**Penting:** Tidak ada UI code di sini, pure logic only!

---

### `Player.java`
**Fungsi:** Entity pemain  
**Apa yang dilakukan:**
- Simpan posisi, health, ammo
- Move method (x, y update)
- Collision detection (AABB)
- Ammo management (use, add, get)

**Fields:**
- `x, y` - Posisi
- `health` - 5 HP
- `ammo` - Munisi
- `width, height` - Hitbox (58x64)

---

### `Monster.java`
**Fungsi:** Entity musuh  
**Apa yang dilakukan:**
- Posisi, health, type (EASY/HARD)
- Shoot logic dengan cooldown
- Take damage method
- Getter untuk type info

**Type:**
- **EASY:** 1 HP, 100 pts, 36x56
- **HARD:** 2 HP, 200 pts, 58x56

---

### `MonsterType.java`
**Fungsi:** Enum untuk tipe monster  
**Apa yang dilakukan:**
- Define EASY dan HARD
- Simpan stats: health, score, width, height
- Getter methods

**Pattern:** Enum dengan data → cleaner code!

---

### `Bullet.java`
**Fungsi:** Entity proyektil  
**Apa yang dilakukan:**
- Posisi, velocity (velocityX, velocityY)
- isPlayerBullet() check
- getAngle() untuk rotation
- Collision detection (AABB)

**Key:** Velocity-based movement → smooth diagonal tracking

---

### `Amplifier.java`
**Fungsi:** Entity obstacle  
**Apa yang dilakukan:**
- Posisi, health (3 HP)
- Take damage
- Check alive status
- Collision detection (AABB)

**Regen:** Auto-regenerate setiap 8 detik (di GameEngine)

---

### `GameScore.java`
**Fungsi:** Data Transfer Object (DTO)  
**Apa yang dilakukan:**
- Container untuk data score
- Fields: username, skor, peluru_meleset, sisa_peluru
- Getter/setter

**Digunakan:** Pass data ke/dari database

---

### `DatabaseManager.java`
**Fungsi:** Database CRUD operations  
**Apa yang dilakukan:**
- `saveScore()` - Insert/update ke database
- `getPlayerScore()` - Ambil score player tertentu
- `getAllScores()` - Ambil semua untuk scoreboard
- Connection management (open/close)

**Penting:** PreparedStatement untuk prevent SQL injection!

---

## 🎨 VIEW LAYER (Display)

### `GameFrame.java`
**Fungsi:** Main window container  
**Apa yang dilakukan:**
- JFrame setup (800x600, not resizable)
- CardLayout untuk panel switching (menu ↔ game)
- `switchToGamePanel()` - Menu → Game
- `switchToMenuPanel()` - Game → Menu

**Pattern:** JFrame + CardLayout = clean panel switching

---

### `MenuPanel.java`
**Fungsi:** Login & scoreboard UI  
**Apa yang dilakukan:**
- Username input field
- Scoreboard table (JTable dari database)
- Play & Quit buttons
- Clickable rows → auto-fill username

**Design:**
- Dark theme (#2A2A2A)
- Accent red (#E74C3C)
- RoundedPanel untuk styling

---

### `GamePanel.java`
**Fungsi:** Game rendering & input  
**Apa yang dilakukan:**
- Game loop (`run()` method)
- Input handler (keyboard listener)
- `paintComponent()` - Render semua
- Update HUD (score, health, ammo, enemies, wave)
- Game over dialog
- Wave notification

**Key Methods:**
- `run()` - Game loop dengan 60 FPS
- `paintComponent(Graphics g)` - Render frame
- `keyPressed(KeyEvent)` - Input handling

---

### `RoundedPanel.java`
**Fungsi:** Custom JPanel dengan rounded corners  
**Apa yang dilakukan:**
- Override `paintComponent()` dengan `Graphics2D`
- Draw rounded rectangle shape
- Fill dengan warna custom
- Smooth UI corners (radius 15px)

**Benefit:** Beautiful rounded UI component!

---

### `GameRenderer.java`
**Fungsi:** Static drawing utilities  
**Apa yang dilakukan:**
- `drawPlayer()` - Draw player PNG
- `drawMonsters()` - Draw monster PNG + health bar
- `drawBullets()` - Draw bullet PNG + rotation
- `drawAmplifiers()` - Draw amplifier PNG + health
- `drawAmmo()` - Draw ammo HUD text
- Fallback colored rectangles jika PNG load fail

**Pattern:** Static methods → reusable, no state!

---

## 🎮 PRESENTER LAYER (Orchestration)

### `GamePresenter.java`
**Fungsi:** Bridge GameEngine ↔ GamePanel  
**Apa yang dilakukan:**
- Store reference ke GameEngine & GamePanel
- `playerMove()` - Delegate ke engine
- `playerShoot()` - Delegate ke engine + play audio
- `update()` - Update engine + update view
- `returnToMenu()` - Cleanup + save score + dialog
- `endGame()` - Show game over dialog

**Penting:** ALL user actions go through presenter!

---

### `MenuPresenter.java`
**Fungsi:** Bridge menu logic ↔ MenuPanel  
**Apa yang dilakukan:**
- `loadScoreboard()` - Query database, populate table
- `onPlayClicked()` - Validate username + switch to game
- `onQuitClicked()` - Exit aplikasi

**Validation:** Check username tidak kosong

---

### Interface Files
**`IGamePresenter.java`**
- Contract untuk GamePresenter
- Methods: startGame, playerMove, playerShoot, update, returnToMenu

**`IGameView.java`**
- Contract untuk GamePanel
- Methods: updateScore, updateHealth, updateMonsters, showGameOverDialog

**`IMenuPresenter.java`**
- Contract untuk MenuPresenter
- Methods: loadScoreboard, onPlayClicked, onQuitClicked

**`IMenuView.java`**
- Contract untuk MenuPanel
- Methods: updateScoreboard, showError, getUsername

**Pattern:** Interface → loose coupling, easy testing!

---

## 🔧 UTILITY FILES

### `AudioManager.java`
**Fungsi:** Centralized audio management  
**Apa yang dilakukan:**
- Load semua WAV files di `initialize()`
- `playPlayerShoot()` - Player bullet sound
- `playMonsterHit()` - Monster hit sound
- `playMonsterShoot()` - Monster shoot sound
- `playAmplifierHit()` - Amplifier hit sound
- `playBackgroundMusic()` - BGM loop
- Smart volume control (fallback MASTER_GAIN → VOLUME)

**Audio Behavior:**
- Player bullet & monster hit: **Overlap** (multiple simultaneous)
- Amplifier hit: **Interrupt** (single instance)
- Monster shoot: **Overlap**

---

### `InputHandler.java`
**Fungsi:** Keyboard input handling  
**Apa yang dilakukan:**
- Implement KeyListener
- `keyPressed()` - Detect key down
- Route input ke presenter:
  - **Z** → `presenter.playerShoot()`
  - **Arrow keys** → `presenter.playerMove()`
  - **SPACE** → `presenter.returnToMenu()`

**Pattern:** Separate input handler → clean separation!

---

### `ImageLoader.java`
**Fungsi:** Load PNG assets  
**Apa yang dilakukan:**
- `loadImage(path)` - Load PNG file
- Return BufferedImage atau null jika fail
- Graceful fallback jika asset missing

**Assets:**
- player.png, monster-easy.png, monster-hard.png, amplifier.png
- bullet-player.png, bullet-monster.png

---

## 📊 ARCHITECTURE SUMMARY

### MVP Pattern Flow

```
INPUT (Z key)
  ↓
InputHandler.keyPressed()
  ↓
Presenter.playerShoot()
  ↓
GameEngine.playerShoot()
  ↓
Presenter.update()
  ↓
GameEngine.update()
  ↓
View.paintComponent()
  ↓
RENDER (draw bullet on screen)
```

### Layer Responsibilities

| Layer | Tanggung Jawab | Files |
|-------|---|---|
| **Model** | Pure logic, NO UI | GameEngine, Player, Monster, Bullet, Amplifier, DatabaseManager |
| **View** | Display only, NO logic | GamePanel, MenuPanel, GameFrame, RoundedPanel, GameRenderer |
| **Presenter** | Orchestrate M ↔ V | GamePresenter, MenuPresenter, + interfaces |
| **Config** | Constants & config | GameConstants, DatabaseConfig |
| **Util** | Helper utilities | AudioManager, InputHandler, ImageLoader |

---

## 🎯 KEY DESIGN PATTERNS

### 1. MVP (Model-View-Presenter)
- Clear separation of concerns
- Easy to test, maintain, extend

### 2. Enum (MonsterType)
- Type-safe monster types
- Encapsulate related data

### 3. Interface (I*Presenter, I*View)
- Define contracts
- Loose coupling
- Easy mocking for tests

### 4. Singleton-like (AudioManager, DatabaseManager)
- Static methods
- Global access
- Single instance per component

### 5. Observer (implicit)
- InputHandler → Presenter → Engine → View
- Event-driven updates

---

## 📈 DATA FLOW EXAMPLE: Player Shoots

1. **User presses Z key**
   - InputHandler.keyPressed() → calls presenter.playerShoot()

2. **Presenter orchestrates**
   - GamePresenter.playerShoot() → calls engine.playerShoot()
   - AudioManager.playPlayerShoot()

3. **Engine updates game state**
   - GameEngine.playerShoot() → creates new Bullet

4. **Next frame: Presenter updates view**
   - GamePresenter.update() → calls view.updateScore(), etc.

5. **View renders**
   - GamePanel.paintComponent() → calls GameRenderer.drawBullets()

6. **User sees bullet on screen!**

---

## 💡 WHY THIS ARCHITECTURE?

✅ **Testability** - Mock interfaces, test each layer independently  
✅ **Maintainability** - Change logic without touching UI  
✅ **Reusability** - GameRenderer used by multiple views  
✅ **Scalability** - Add features without refactoring  
✅ **Clean Code** - Clear responsibilities, no spaghetti code  

---

## 🚀 COMPILE & RUN

```bash
# Compile
javac -d out -cp lib/mysql-connector-j.jar src/riffOrDie/**/*.java

# Run
java -cp out:lib/mysql-connector-j.jar riffOrDie.Main
```

---

**Total Files:** 22  
**Total Lines:** ~4000  
**Pattern:** MVP + SOLID principles  
**Quality:** Production-ready! 🎉

