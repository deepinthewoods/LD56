package ninja.trek.Components;

import ninja.trek.Entity.Entity;
import ninja.trek.Main;

public abstract class Component {
    public Entity e;
    public abstract void update(float dt, Main main);

    public abstract void updateRender(float dt, Main main);

    public abstract void onAdded(Main main);


    public abstract void onRemove(Main main);
}
