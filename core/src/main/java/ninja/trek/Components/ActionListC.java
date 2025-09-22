package ninja.trek.Components;

import ninja.trek.Main;
import ninja.trek.actionlist.ActionList;

public class ActionListC extends Component{
    ActionList actions = new ActionList();


    @Override
    public void update(float dt, Main main) {
        actions.update(dt);
    }

    @Override
    public void updateRender(float dt, Main main) {
        actions.updateRender(dt);
    }

    @Override
    public void onAdded(Main main) {
        actions.inserted(e);
    }

    @Override
    public void onRemove(Main main) {
        actions.clearWithDelayed();
    }
}
