package ninja.trek.Components;

import ninja.trek.Main;

public class CollisionC extends Component{
    @Override
    public void update(float dt, Main main) {

    }

    @Override
    public void updateRender(float dt, Main main) {

    }

    @Override
    public void onAdded(Main main) {
        PhysicsC physicsC = e.get(PhysicsC.class);
        physicsC.body.setUserData(this);
    }

    @Override
    public void onRemove(Main main) {

    }
}
