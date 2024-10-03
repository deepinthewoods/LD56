package ninja.trek;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ScreenUtils;

import ninja.trek.Entity.Entity;
import ninja.trek.Entity.Player;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private Array<Entity> entities = new Array<Entity>();
    public World world;
    private Vector2 gravity = new Vector2(0, -20);
    private ContactListener contactListener;


    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        add(Player.class);
        Gdx.app.log("main", "create");

        world = new World(gravity, true);
        contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        world.setContactListener(contactListener);
    }

    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        world.step(deltaTime, 2, 2);
        for (int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            e.update(deltaTime, this);
        }
        for (int i = entities.size-1; i >= 0; i--){
            Entity e = entities.get(i);
            if (e.remove){
                entities.removeIndex(i);
                e.onRemove(this);
                e.remove = false;
                Pools.free(e);
            }
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        for (int i = 0; i < entities.size; i++){
            Entity e = entities.get(i);
            e.updateRender(deltaTime, this);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }

    public void add(Class<? extends Entity> cl){
        Entity e = Pools.obtain(cl);
        e.init();
        e.onAdded(this);
        entities.add(e);
    }
}
