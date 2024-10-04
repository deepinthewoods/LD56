package ninja.trek;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class CutScene {

    public abstract void render(SpriteBatch batch, float deltaTime);


    public abstract void init(Stage stage, TextureAtlas atlas, Main main);
}
