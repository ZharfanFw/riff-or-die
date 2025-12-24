# AGENTS.md - Hide and Seek The Challenge (Riff Or Die)

## 🎮 Game Overview
**Title:** Hide and Seek The Challenge (Gitaris vs Not Balok)  
**Type:** 2D Java Swing GUI Game with MVP Architecture  
**Theme:** Musician (Gitaris) defending music studio from monster attacks  
**Resolution:** 800x600 px  
**FPS:** 60 (16.67ms per frame)  
**Database:** MySQL with JDBC

---

## 📋 Build & Compilation

### Compile (from project root):
```bash
javac -d out -cp lib/mysql-connector-j.jar src/com/riffOrDie/**/*.java
```

### Run:
```bash
java -cp out:lib/mysql-connector-j.jar com.riffOrDie.Main
```

### Clean:
```bash
rm -rf out/
```

---

## 📁 Project Structure (MVP Architecture)

```
src/com/riffOrDie/
├── Main.java                          # Entry point
├── model/                             # Pure business logic (NO UI)
│   ├── GameEngine.java                # Core game loop, collision detection, spawning
│   ├── Player.java                    # Player entity (29x34px, full-screen movement)
│   ├── Monster.java                   # Enemy entity with health & shooting cooldown
│   ├── MonsterType.java               # Enum (EASY/HARD) with stats
│   ├── Bullet.java                    # Projectile (player=blue/down, monster=orange/up)
│   ├── Amplifier.java                 # Obstacle (32x32px, blocks bullets)
│   └── GameScore.java                 # DTO for database (username, skor, peluru_meleset, sisa_peluru)
├── view/                              # Display only (NO business logic)
│   ├── GameFrame.java                 # Main JFrame, panel switching (menu ↔ game)
│   ├── MenuPanel.java                 # Login UI + scoreboard table (implements IMenuView)
│   ├── GamePanel.java                 # Game rendering & loop (implements IGameView)
│   └── GameRenderer.java              # Static drawing methods for all entities
├── presenter/                         # Orchestrate Model ↔ View
│   ├── IGamePresenter.java            # Interface (startGame, playerMove, playerShoot, update, etc)
│   ├── GamePresenter.java             # Implementation (handles game loop, collision, scoring)
│   ├── IGameView.java                 # Interface (updateScore, updateHealth, updateMonsters, etc)
│   ├── IMenuPresenter.java            # Interface (loadScoreboard, onPlayClicked, onQuitClicked)
│   ├── MenuPresenter.java             # Implementation (menu logic, username validation)
│   └── IMenuView.java                 # Interface (updateScoreboard, showError, getUsername)
├── db/                                # Database layer
│   ├── DatabaseConfig.java            # Constants (DB_URL, DB_USER, TABLE_NAME, COLUMN_NAMES)
│   └── DatabaseManager.java           # CRUD (saveScore, getPlayerScore, getAllScores)
└── util/                              # Utilities & configuration
    ├── GameConstants.java             # Game config (SCREEN_WIDTH=800, SCREEN_HEIGHT=600, speeds, etc)
    ├── ImageLoader.java               # Load images (resources/images/player.png)
    ├── InputHandler.java              # Keyboard input (arrows, space) → Presenter calls
    └── AudioManager.java              # Placeholder (optional sound effects)
```

---

## 🏗️ Architecture: MVP (Model-View-Presenter)

### **Model Layer** (No UI Dependencies)
- `GameEngine`: Manages game state, spawning, collision detection
- `Player/Monster/Bullet/Amplifier`: Entity models
- `GameScore`: Data transfer object for database

### **View Layer** (Display Only)
- `GameFrame/MenuPanel/GamePanel`: UI components
- All implement interfaces (`IMenuView`, `IGameView`)
- NO business logic - only render & capture input
- Call presenter methods for user actions

### **Presenter Layer** (Orchestration)
- `GamePresenter`: Bridges GameEngine ↔ GamePanel
- `MenuPresenter`: Bridges menu logic ↔ MenuPanel
- Handles all state transitions & data flow

### **Data Flow:**
```
INPUT (InputHandler.keyPressed)
  ↓
PRESENTER (GamePresenter.playerMove/playerShoot)
  ↓
MODEL (GameEngine.update, collision detection)
  ↓
PRESENTER (update view state)
  ↓
VIEW (GamePanel.paintComponent renders)
```

---

## 🎮 Game Mechanics

### **Entities**
- **Player (Gitaris):** 29x34px, starts bottom-center, can move full screen (TOP ↔ BOTTOM), health=3
- **Monsters (Not Balok):** 
  - EASY: 48x48px, speed=2, health=1, score=100
  - HARD: 48x48px, speed=5, health=2, score=200
  - Spawn randomly at bottom, move up, shoot at player position
- **Bullets:** 
  - Player: 8x8px, blue, move downward, damage=1
  - Monster: 8x8px, orange, move upward, damage=1
- **Amplifiers:** 32x32px, gray, obstacles that destroy bullets

### **Game Loop**
1. Render frame (60 FPS)
2. Update all entities (GameEngine.update)
3. Check collisions (AABB method)
4. Spawn monsters (random interval, gets faster as score increases)
5. Monster AI shoots towards player
6. Remove dead entities
7. Check game over (player.health ≤ 0)

### **Scoring System**
- +points when kill monster (based on type)
- Track "bullets missed" (hit amplifier or off-screen)
- Save to database on game over

### **Database Schema**
```sql
CREATE TABLE tbenfit (
  username VARCHAR(50) PRIMARY KEY,
  skor INT,
  peluru_meleset INT,
  sisa_peluru INT
);
```

---

## 💻 Code Style Guidelines

**Package Structure:** Strict MVP - model, view, presenter, db, util only. NO controller.

**Naming:**
- Classes: `PascalCase` (GameEngine, MenuPresenter)
- Methods/Fields: `camelCase` (playerShoot, directionX)
- Constants: `UPPER_SNAKE_CASE` (SCREEN_WIDTH, BASE_SPAWN_RATE)

**Imports:** Alphabetically organized
```java
import java.*;           // java.* first
import javax.*;          // javax.* second
import com.riffOrDie.*;  // com.* last
```

**Formatting:**
- 4-space indentation
- Max 100 chars per line
- K&R style braces: `if (x) { ... }`
- No trailing whitespace

**Types:**
- Explicit types (no `var`)
- Use primitives for performance (int x, y)
- Use Objects for collections (List<Monster>)

**Comments:**
- Javadoc for PUBLIC methods only
- Inline comments ONLY for non-obvious game logic
- NO TODO comments

**Error Handling:**
- Try-catch for DB operations
- Log errors to console
- Never swallow exceptions silently

**Special Patterns:**
- AABB collision detection: `x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2`
- Remove dead entities: collect-then-removeAll() pattern (NOT break in loops)
- All magic numbers in GameConstants.java
- Getter/setter for private fields
- Copy lists before rendering (prevent ConcurrentModificationException)

---

## 🐛 Known Issues & Fixes

### Fixed Issues
✅ **ClassCastException** - Type checking in collision detection fixed  
✅ **ConcurrentModificationException** - Copy lists before rendering (ArrayList<>(original))  
✅ **Database Connection Closed** - Fresh connection per operation, explicit close in finally block  

### Database Connection Management
```java
// DO: Create fresh connection, explicit close
Connection conn = null;
try {
    conn = getConnection();
    // ... execute queries
} catch (SQLException e) {
    // handle error
} finally {
    if (conn != null && !conn.isClosed()) {
        conn.close();
    }
}

// DON'T: try-with-resources for connection
try (Connection conn = getConnection()) {
    // This auto-closes, but getConnection() may return reused connection!
}
```

---

## 🧪 Testing Checklist

**Menu Screen**
- [ ] Username input field visible
- [ ] Scoreboard table loads from database
- [ ] Play & Quit buttons functional
- [ ] Error dialog on empty username

**Gameplay**
- [ ] Game starts after Play button
- [ ] Player visible at bottom-center
- [ ] Monsters spawn from bottom moving up
- [ ] UP/DOWN/LEFT/RIGHT arrows move player
- [ ] SPACE shoots downward
- [ ] Score increases when kill monster
- [ ] Health decreases when hit
- [ ] Game over dialog when health=0

**Database**
- [ ] Score saved on game over
- [ ] Scoreboard updates after game
- [ ] No SQL injection (PreparedStatement used)

**Performance**
- [ ] 60 FPS stable (watch for lag)
- [ ] No memory leaks (connections closed properly)
- [ ] Rendering smooth (no ConcurrentModificationException)

---

## 📚 Key Files Reference

| File | Purpose | Status |
|------|---------|--------|
| GameEngine.java | Core game logic | ✅ Complete |
| GamePresenter.java | Game orchestration | ✅ Complete |
| GamePanel.java | Game rendering (IGameView impl) | ✅ Complete |
| MenuPresenter.java | Menu logic | ✅ Complete |
| MenuPanel.java | Menu UI (IMenuView impl) | ✅ Complete |
| DatabaseManager.java | DB CRUD operations | ✅ Fixed |
| GameRenderer.java | Drawing methods | ✅ Complete |
| InputHandler.java | Keyboard input | ✅ Complete |

---

## 🚀 Next Steps

1. **Test gameplay thoroughly** - All mechanics verified?
2. **Debug rendering** - Are entities displaying correctly?
3. **Verify database** - Are scores saving/loading?
4. **Optimize performance** - 60 FPS stable?
5. **Add audio** (optional) - AudioManager.java
6. **Polish UI** - Animations, effects, sounds?

---

## 📝 Session Notes

**Latest Session:**
- Fixed 3 critical bugs (ClassCastException, ConcurrentModificationException, DB connection)
- Restructured folders (removed controller/ folder, moved InputHandler to util/)
- Confirmed MVP architecture 100% compliance
- All files compile without errors
- Application runs successfully

**Architecture Verified:** ✅ Strict MVP (no MVC/MVVM confusion)  
**Code Quality:** ✅ Follows style guidelines  
**Test Status:** ✅ Compiles & runs without crashes
