LDTemplate — Entity/Component + ActionList Guide
===============================================

This project uses libGDX with a lightweight Entity/Component pattern and an ActionList for sequencing gameplay actions.

Quick Start
-----------

- Desktop run: `./gradlew :lwjgl3:run`
- Build all: `./gradlew build`
- HTML dev server: `./gradlew :html:superDev` → http://localhost:8080/html

Project Layout
--------------

- `core/` shared logic (Java 11), package `ninja.trek`
- `lwjgl3/` desktop launcher
- `android/` Android launcher
- `html/` GWT backend
- `assets/` runtime assets (auto‑generated `assets.txt`)

Entity + Components
-------------------

How it works

- Declare a field on your `Entity` subclass for each `Component` you want to attach.
- `Entity.init()` will:
  - Auto‑instantiate null `Component` fields via a no‑arg constructor.
  - Throw if such a component lacks a no‑arg constructor.
  - Collect all components and set `component.e = this`.
  - Wire component‑to‑component fields by assignability (only if null).
  - Ignore static fields; non‑public fields are supported.
  - Throw if zero or multiple matches exist for a required component reference.
- Lifecycle forwards: `onAdded()`, `update()`, `updateRender()`, `onRemove()` are called on every component.

Simple example

```java
// A simple Component with no-arg constructor (auto-created)
package ninja.trek.Components;
import ninja.trek.Main;
public class PlayerC extends Component {
    @Override public void update(float dt, Main main) {}
    @Override public void updateRender(float dt, Main main) {}
    @Override public void onAdded(Main main) {}
    @Override public void onRemove(Main main) {}
}

// Component that needs constructor args (must be set manually on the Entity)
package ninja.trek.Components;
import com.badlogic.gdx.physics.box2d.*; import ninja.trek.Main;
public class PhysicsC extends Component {
    private final BodyDef bodyD; private final FixtureDef fd; public Body body;
    public PhysicsC(BodyDef bodyD, FixtureDef fd){ this.bodyD = bodyD; this.fd = fd; }
    @Override public void onAdded(Main main){ body = main.world.createBody(bodyD); body.createFixture(fd); }
    @Override public void onRemove(Main main){ main.world.destroyBody(body); }
    @Override public void update(float dt, Main main) { e.x = body.getPosition().x; e.y = body.getPosition().y; }
    @Override public void updateRender(float dt, Main main) {}
}

// Entity: Player — one auto-created (PlayerC) and one manual (PhysicsC)
package ninja.trek.Entity;
import com.badlogic.gdx.physics.box2d.*; import ninja.trek.Components.*;
public class Player extends Entity {
    public PlayerC player;                    // auto-created by Entity.init()
    public PhysicsC physics = new PhysicsC(bd, fd); // provided manually
    static BodyDef bd = new BodyDef(); static FixtureDef fd = new FixtureDef(); static PolygonShape shape = new PolygonShape();
    static { shape.set(new float[]{-.25f,-.5f,-.25f,.5f,.25f,.5f,.25f,-.5f}); fd.shape = shape; bd.type = BodyDef.BodyType.DynamicBody; }
}

// Somewhere in setup (e.g., Main.create):
Player p = add(Player.class); // Pools.obtain + init + onAdded + add to world
```

Tips

- Prefer no‑arg constructors for components you want auto‑created.
- Keep required cross‑references as null fields (e.g., `SpriteRenderC` having `PlayerC player;`) — wiring fills them.
- If multiple components could satisfy a reference, Entity throws to force you to be explicit.

ActionList (Sequenced Actions)
------------------------------

Concept

- `ActionList` manages a doubly‑linked list of `Action` nodes.
- Each `Action` has: `onStart()`, `update(dt)`, `onEnd()`, flags `isBlocking`, `isFinished`, and a `lanes` bitmask.
- Blocking: An action can block lanes to prevent other actions in those lanes from running that frame.
- Delay: `Action.delay(seconds)` sets a DELAY lane and schedules the action to resume via a min‑heap timer. When due, the action is undelayed and continues.
- Use `ActionList.inserted(entity)` after assembling a list to set parents and register any pre‑delayed actions.
- As a component: `ActionListC` wraps an `ActionList` and is updated each frame via the entity lifecycle.

Simple example

```java
// A simple action that waits 1 second then completes
package ninja.trek.actionlist;
public class WaitAction extends Action {
    @Override public void onStart(){ delay(1f); }
    @Override public void update(float dt){ if ((lanes & LANE_DELAY) == 0) isFinished = true; }
    @Override public void onEnd(){}
    @Override public void updateRender(float dt){}
}

// Using ActionList standalone
ActionList list = new ActionList();
list.addToEnd(new WaitAction());
list.inserted(someEntity);
// each frame
list.update(dt);
list.updateRender(dt);
```

Pooling
-------

- Compatible with libGDX `Pools` / `ReflectionPool`.
- `Action.reset()` clears flags, links, lanes, and parent.
- `ActionList.reset()` clears actions (calling `onEnd()`), empties the delay heap, resets `currentTime`, `lanes`, and entity.
- Obtain/free:

```java
WaitAction a = Pools.obtain(WaitAction.class);
// use it
Pools.free(a);
```

Build & Run
-----------

- Desktop run: `./gradlew :lwjgl3:run`
- Build all: `./gradlew build`
- Desktop JAR: `./gradlew :lwjgl3:jar` → `lwjgl3/build/libs/`
- HTML dev: `./gradlew :html:superDev` → http://localhost:8080/html

## Entity + Components

- Purpose: Keep gameplay logic in small `Component` classes attached to an `Entity`.
- Declaration: In your `Entity` subclass (e.g., `ninja.trek.Entity.Player`), declare one field per component, e.g. `public PlayerC player;`.
- Auto‑create: `Entity.init()` finds all non‑static fields whose type extends `ninja.trek.Components.Component` and:
  - Instantiates if null using a no‑arg constructor (throws if missing).
  - Adds every `Component` to `Entity.components` and sets `component.e = this`.
- Auto‑wire: After creation, `init()` scans each component for `Component` fields and wires them to matching components on the same entity:
  - Matches by assignability (interfaces/superclasses allowed), only if the target field is null.
  - Ignores static fields; non‑public fields are supported.
  - Throws if there are zero or multiple possible matches.
- Lifecycle: `Entity.update()`, `updateRender()`, `onAdded()`, `onRemove()` forward to every component.
- Example: `Player` sets `PhysicsC physics = new PhysicsC(bd, fd)` manually (non‑no‑arg). `PlayerC player` is null and is auto‑created by `init()`.

Tips
- Use no‑arg constructors on components you want auto‑created. Set components with required args manually.
- Keep component cross‑references as fields (e.g., `PositionC position;`) and leave them null—`Entity` wiring will populate them.

## Action List

- Types: `ninja.trek.actionlist.ActionList` manages a doubly‑linked list (`DoublyLinkedList`) of `Action` nodes.
- Ticking: `update(dt)` and `updateRender(dt)`
  - Advance `currentTime` and call `processDueDelays()` to release ready delayed actions.
  - Iterate actions: on first tick call `onStart()`; then `update(...)`/`updateRender(...)` every frame.
  - Blocking: if an action is blocking, its `lanes` are added to the active `lanes` mask, preventing other actions in those lanes from running.
  - Finish: when `isFinished` is true, call `onEnd()` and remove the action. If it had `LANE_DELAY`, also remove it from the delay heap.
- Delays: `Action.delay(f)` sets `LANE_DELAY` and schedules via `BinaryHeap<Action>` with value `currentTime + f`. When due, `processDueDelays()` pops and calls `unDelay()` so it can run again.
- Insert: Call `ActionList.inserted(entity)` after you attach the list so each action’s `parent` is set and any pre‑delayed actions are registered.
- Component: `ActionListC` wraps an `ActionList` as a component; it calls `inserted(e)` in `onAdded()` and `clearWithDelayed()` in `onRemove()`.

## Pooling

- Pooling: Classes implement libGDX `Pool.Poolable` and are compatible with `Pools`/`ReflectionPool`.
- Reset:
  - `Action.reset()` clears flags, links (`prev`/`next`), `parent`, `lanes`, and state.
  - `ActionList.reset()` clears actions (calling `onEnd()`), empties the delay heap, resets `currentTime`, `lanes`, and `e`.
- Usage: Obtain with `Pools.obtain(MyAction.class)` and free with `Pools.free(instance)` when done.
