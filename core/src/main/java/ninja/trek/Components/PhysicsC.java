package ninja.trek.Components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import ninja.trek.Main;

public class PhysicsC extends Component{

    private final BodyDef bodyD;
    public Body body;

    public PhysicsC(BodyDef bodyD){
        this.bodyD = bodyD;
    }
    @Override
    public void update(float dt, Main main) {

    }

    @Override
    public void updateRender(float dt, Main main) {

    }

    @Override
    public void onAdded(Main main) {
        body = main.world.createBody(bodyD);
    }

    @Override
    public void onRemove(Main main) {
        main.world.destroyBody(body);
    }
}
