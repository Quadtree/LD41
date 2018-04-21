package info.quadtree.ld41;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

public class GameState implements ContactListener {
    List<Actor> actors = new ArrayList<Actor>();

    World world;

    Car pcCar;

    long ticksDone = 0;

    public OrthographicCamera cam = new OrthographicCamera();

    Vector2 camPos = new Vector2();

    public GameState(){
        world = new World(new Vector2(0,0), true);

        world.setContactListener(this);
    }

    public void init(){
        actors.add(new Wall(new Vector2(47.3f, 0), MathUtils.PI / 2 + 0.06f));
        actors.add(new Wall(new Vector2(-46.3f, 0), MathUtils.PI / 2 - 0.06f));

        actors.add(new Wall(new Vector2(2.5f, 1499), MathUtils.PI / 2));
        actors.add(new Wall(new Vector2(-1.5f, 1499), MathUtils.PI / 2));

        for (int i=0;i<20;++i){
            for (int j=0;j<5;++j){
                Actor tmp = new Car(new Vector2((i - 10) * 4 + 2, j * 4));
                actors.add(tmp);

                if (j == 0 && i == 10) pcCar = (Car)tmp;
            }
        }

        pcCar.hasAi = false;

        camPos = pcCar.body.getPosition().cpy();
    }

    public void render(){


        if (ticksDone < System.currentTimeMillis()){
            update();
            ticksDone += 16;
        }

        final float CAM_MOVE_SPEED = 0.02f;

        cam.setToOrtho(false);
        cam.zoom = 3.f / 32.f;

        Vector2 camTrg = pcCar.body.getPosition().cpy().add(pcCar.body.getLinearVelocity().cpy().scl(1));

        camPos.x = (camPos.x * (1 - CAM_MOVE_SPEED)) + (CAM_MOVE_SPEED * camTrg.x);
        camPos.y = (camPos.y * (1 - CAM_MOVE_SPEED)) + (CAM_MOVE_SPEED * camTrg.y);
        cam.position.x = camPos.x;
        cam.position.y = camPos.y;
        //cam.position.x = pcCar.body.getPosition().x;
        //cam.position.y = pcCar.body.getPosition().y;
        cam.update();

        LD41.s.batch.setProjectionMatrix(cam.combined);
        LD41.s.batch.begin();

        for (int i=0;i<actors.size();++i){
            if (actors.get(i).keep())
                actors.get(i).render();
        }



        LD41.s.batch.end();

        //Box2DDebugRenderer rnd = new Box2DDebugRenderer();
        //rnd.render(world, cam.combined);
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

    @Override
    public void beginContact(Contact contact) {
        Object oa = contact.getFixtureA().getBody().getUserData();
        Object ob = contact.getFixtureB().getBody().getUserData();

        if (oa instanceof Actor && ob instanceof Actor){
            ((Actor) oa).collidedWith((Actor)ob);
            ((Actor) ob).collidedWith((Actor)oa);
        }
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
}
