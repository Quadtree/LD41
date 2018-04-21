package info.quadtree.ld41;

import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

public class GameState {
    List<Actor> actors;

    World world;

    long ticksDone = 0;

    public void render(){
        if (ticksDone < System.currentTimeMillis()){
            update();
            ticksDone += 16;
        }

        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep())
                actors.get(i).render();
        }
    }

    public void update(){
        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep())
                actors.get(i).update();
            else
                actors.remove(i--);
        }
    }
}
