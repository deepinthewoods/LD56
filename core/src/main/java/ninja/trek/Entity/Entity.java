package ninja.trek.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Constructor;
import com.badlogic.gdx.utils.reflect.Field;
import com.badlogic.gdx.utils.reflect.ReflectionException;

import ninja.trek.Components.Component;
import ninja.trek.Main;

public class Entity {
    public float x, y;
    public Array<Component> components = new Array<Component>();
    private boolean hasInit = false;
    public boolean remove = false;
    public Entity(){

    }
    public void init(){
        if (hasInit) return;
        hasInit = true;
        Field[] fields = ClassReflection.getFields(this.getClass());
        for (Field f : fields){
            try {
                Object o = f.get(this);
                if (o == null) {
                    // If the field is null, create a new instance
                    Constructor constructor = ClassReflection.getConstructor(f.getType());
                    o = constructor.newInstance();
                    f.set(this, o);
                    Gdx.app.log("entity", "Created new instance for null field: " + f.getName());
                }
                if (Component.class.isAssignableFrom(f.getType())){
                    Component c = (Component)o;
                    components.add(c);
                    c.e = this;
                   // Gdx.app.log("entity", "add " + o.getClass());
                } //else Gdx.app.log("entity", "not add " + f.getType());
            } catch (ReflectionException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public <CL extends Component> CL get(Class<CL> cl){
        for (int i = 0; i < components.size; i++){
            Component c = components.get(i);
            if (cl.isAssignableFrom(c.getClass())) return (CL)c;
        }
        return null;
    }
    public void update(float dt, Main main){

        for (int i = 0; i < components.size; i++){
            Component c = components.get(i);
            c.update(dt, main);
        }
    }

    public void updateRender(float dt, Main main){

        for (int i = 0; i < components.size; i++){
            Component c = components.get(i);
            c.updateRender(dt, main);
        }
    }

    public void onAdded(Main main){
        for (int i = 0; i < components.size; i++){
            Component c = components.get(i);
            c.onAdded(main);
        }
    }

    public void onRemove(Main main){
        for (int i = 0; i < components.size; i++){
            Component c = components.get(i);
            c.onRemove(main);
        }
    }

}
