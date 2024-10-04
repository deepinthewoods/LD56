package ninja.trek.Components;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import ninja.trek.Main;

public class PhysicsC extends Component{

    private final BodyDef bodyD;
    private final FixtureDef fd;
    public Body body;

    public PhysicsC(BodyDef bodyD, FixtureDef fd){
        this.bodyD = bodyD;
        this.fd = fd;
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
        body.createFixture(fd);
    }

    @Override
    public void onRemove(Main main) {
        main.world.destroyBody(body);
    }
}
