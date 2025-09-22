package ninja.trek.actionlist;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

import ninja.trek.Entity.Entity;
import ninja.trek.Main;


public class ActionList implements Poolable{


	private static final String TAG = "action list";
	//public DoublyLinkedList actions;
	public transient Entity e;
	//public transient World world;
	public DoublyLinkedList actions;// = new DoublyLinkedList<Action>();;
	public int lanes;
    public ActionList(){
		actions = new DoublyLinkedList();
        delayedActions = new BinaryHeap<Action>();
        currentTime = 0f;
    }

    public void update(float dt){
        currentTime += dt;
        processDueDelays();
		lanes = Action.LANE_DELAY;
		//Array.ArrayIterator<Action> iter = actions.iter();
		if (actions.isEmpty()) return;
		Action action = actions.getFirst();
		while (action.getNext() != null){
			Action next = action.getNext();
			if ((lanes & action.lanes) != 0){
				//Gdx.app.log(TAG, "updatedactionnot");
				action = next;
				continue;
			}
			if (action.first){
				action.first = false;
				action.onStart();
			}
			action.update(dt);
			//Gdx.app.log(TAG, "updated");
			if (action.isBlocking)
				lanes |= action.lanes;

			if (action.isFinished){
				action.onEnd();
				actions.remove(action);
				if ((action.lanes & Action.LANE_DELAY) != 0){

	                delayedActions.remove(action);
	                action.lanes = 0;
	            }
                //iter.remove();
			}
			action = next;

		}
        //Gdx.app.log(TAG, "update"+actions.size());
	}
@Override
public String toString() {
	String s = "";
	if (actions.isEmpty()) return "";
	Action action = actions.getFirst();
	while (action.getNext() != null){
		Action next = action.getNext();

		s += action.getClass().getSimpleName() + " ";
		action = next;

	}
    return s;
}
    @Override
    public void reset() {
		// Reset for pooling reuse
		clearWithDelayed();
		if (delayedActions != null) delayedActions.clear();
		if (actions != null) actions.clear();
		currentTime = 0f;
		lanes = 0;
		e = null;

    }

    public void addToStart(Action a){
        actions.addFirst(a);

        a.parent = this;

        //a.onStart();
    }
    public void addToEnd(Action a){
    	actions.addLast(a);
        a.parent = this;


        //a.onStart();

    }

    protected void addBefore(Action a, Action toAdd){

        //a.addBeforeMe(toAdd);
    	actions.addBefore(a, toAdd);
        toAdd.parent = this;
        //toAdd.onStart();

    }
    protected void addAfter(Action a, Action toAdd){
        //a.addAfterMe(toAdd);
    	actions.addAfter(a, toAdd);
        toAdd.parent = this;
        //toAdd.onStart();
    }


    public float currentTime;

    public transient BinaryHeap<Action> delayedActions;



    public void inserted(Entity e) {
	    this.e = e;
	    if (delayedActions == null) delayedActions = new BinaryHeap<Action>();
	    //Array.ArrayIterator<Action> iter = actions.iter();
	    if (actions.isEmpty()) return;
	    Action a = actions.getFirst();
	    while (a.getNext() != null){

	        a.parent = this;
	        //a.onStart();
	        if ((a.lanes & Action.LANE_DELAY) != 0){
	            delayedActions.add(a);
	            Gdx.app.log(TAG, "delayed aadded");
	        }
	        a = a.getNext();
	    }
	    ///Gdx.app.log(TAG, "inserted!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}

	public void removed() {

	}
	public void clearWithDelayed() {
		if (actions.isEmpty()) return;
		Action a = actions.getFirst();
		while (a != null && a.getNext() != null){
			a.onEnd();
            if ((a.lanes & Action.LANE_DELAY) != 0){

                delayedActions.remove(a);
                a.lanes = 0;
            }

            Action next = a.getNext();
            actions.remove(a);

            a = next;

		}

		//clear();
    }


    public boolean containsAction(Class<? extends Action> clas) {
    	Action a = actions.getFirst();
		 while (a.getNext() != null){
            if (clas.isAssignableFrom(a.getClass())) return true;
            a = a.getNext();
        }
        return false;
    }

    public <T extends Action> T getAction(Class<T> clas) {
    	Action a = actions.getFirst();
		 while (a.getNext() != null){
            if (clas.isAssignableFrom(a.getClass())) return (T) a;
            a = a.getNext();
        }
        return null;
    }

    public void remove(Class<? extends Action> clas) {
    	Action a = actions.getFirst();
		 while (a.getNext() != null){
            if (clas.isAssignableFrom(a.getClass())) {
            	actions.remove(a);
            	return;
            }
            a = a.getNext();
        }

    }

    public void addToStart(Class<? extends Action> clas) {
        addToStart(Pools.obtain(clas));
    }

    public void clear() {
		Action a = actions.getFirst();
		 while (a.getNext() != null){
			 Action nxt = a.getNext();
			 actions.remove(a);
			 a = nxt;
       }

	}

	public int size() {

		int c = 0;
		Action a = actions.getFirst();
		 while (a.getNext() != null){

			 Action nxt = a.getNext();
			 c++;
			 a = nxt;
      }
		return c;
	}



    public void updateRender(float dt) {

        currentTime += dt;
        processDueDelays();
		lanes = Action.LANE_DELAY;
		//Array.ArrayIterator<Action> iter = actions.iter();
		if (actions.isEmpty()) return;
		Action action = actions.getFirst();
		while (action.getNext() != null){
			Action next = action.getNext();
			if ((lanes & action.lanes) != 0){
				//Gdx.app.log(TAG, "updatedactionnot");
				action = next;
				continue;
			}
			if (action.first){
				action.first = false;
				action.onStart();
			}
			action.updateRender(dt);
			//Gdx.app.log(TAG, "updated " + action.getClass());
			if (action.isBlocking)
				lanes |= action.lanes;

			if (action.isFinished){
				action.onEnd();
				actions.remove(action);
				if ((action.lanes & Action.LANE_DELAY) != 0){

	                delayedActions.remove(action);
	                action.lanes = 0;
	            }
                //iter.remove();
			}
			action = next;

		}
        //Gdx.app.log(TAG, "update"+actions.size());

	}

    private void processDueDelays() {
        if (delayedActions == null || delayedActions.size == 0) return;
        while (delayedActions.size > 0) {
            Action top = delayedActions.peek();
            if (top.getValue() <= currentTime) {
                delayedActions.pop();
                top.unDelay();
            } else {
                break;
            }
        }
    }

}
