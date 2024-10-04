package ninja.trek.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import ninja.trek.Main;

public class SpriteRenderC extends Component{
    private String name;
    private Sprite sprite;

    public SpriteRenderC(String name){
        this.name = name;
    }
    @Override
    public void update(float dt, Main main) {
//        Gdx.app.log("spr", "upd");
    }

    @Override
    public void updateRender(float dt, Main main) {
        sprite.setPosition(e.x, e.y);
        sprite.draw(main.batch);
//        Gdx.app.log("spr", "draw2");
    }

    @Override
    public void onAdded(Main main) {
        sprite = main.atlas.createSprite(name);
    }

    @Override
    public void onRemove(Main main) {

    }
}
