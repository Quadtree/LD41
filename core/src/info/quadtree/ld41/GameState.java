package info.quadtree.ld41;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    List<Actor> actors = new ArrayList<Actor>();

    World world;

    Car pcCar;

    long ticksDone = 0;

    public OrthographicCamera cam = new OrthographicCamera();

    public GameState(){
        world = new World(new Vector2(0,0), true);


    }

    public void init(){
        actors.add(new Car(new Vector2(20, 20)));
        pcCar = (Car)actors.get(0);
    }

    public void render(){


        if (ticksDone < System.currentTimeMillis()){
            update();
            ticksDone += 16;
        }

        cam.setToOrtho(false);
        cam.zoom = 3.f / 32.f;
        cam.position.x = 20;
        cam.position.y = 20;
        cam.update();

        LD41.s.batch.setProjectionMatrix(cam.combined);
        LD41.s.batch.begin();

        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep())
                actors.get(i).render();
        }
        LD41.s.batch.end();
    }

    public void update(){
        if (pcCar != null){
            final float TURN_SPEED = 0.02f;

            pcCar.acceleration = LD41.s.accel ? 1 : (LD41.s.brake ? -1 : 0);
            pcCar.steering = LD41.s.turnLeft ? 1 : (LD41.s.turnRight ? -1 : 0);
        }

        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep())
                actors.get(i).update();
            else
                actors.remove(i--);
        }

        world.step(0.016f, 2, 2);
    }
}
