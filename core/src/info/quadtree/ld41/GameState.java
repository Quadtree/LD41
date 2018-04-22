package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameState implements ContactListener {
    List<Actor> actors = new ArrayList<Actor>();

    World world;

    Car pcCar;

    long ticksDone = 0;

    public OrthographicCamera cam = new OrthographicCamera();

    Vector2 camPos = new Vector2();

    float startTime = 1.5f;
    boolean started = false;

    boolean hasWon = false;
    boolean hasLost = false;

    float msg1Phase = 0;
    float msg2Phase = 0;

    public GameState(){
        world = new World(new Vector2(0,0), true);

        world.setContactListener(this);
    }

    public void init(){
        actors.add(new Wall(new Vector2(47.3f, 0), MathUtils.PI / 2 + 0.06f + MathUtils.PI));
        actors.add(new Wall(new Vector2(-46.3f, 0), MathUtils.PI / 2 - 0.06f));

        actors.add(new Wall(new Vector2(2.35f, 1498), MathUtils.PI / 2 + MathUtils.PI));
        actors.add(new Wall(new Vector2(-1.35f, 1498), MathUtils.PI / 2));

        int n = 0;

        for (int i=0;i<20;++i){
            for (int j=0;j<5;++j){
                Actor tmp = new Car(new Vector2((i - 10) * 4 + 2, j * 4));
                actors.add(tmp);

                //((Car)tmp).hasAi = false;

                if (MathUtils.random(n++) == 0) pcCar = (Car)tmp;
            }
        }

        //pcCar = (Car)actors.stream().filter(it -> it instanceof Car).reduce((a, b) -> MathUtils.randomBoolean() ? a : b).get();

        pcCar.hasAi = false;
        pcCar.color = new Color(0,0,1,1);

        camPos = pcCar.body.getPosition().cpy();

        actors.add(new WallOfDeath(new Vector2(0, -200)));

        ticksDone = System.currentTimeMillis();
    }

    public void render(){
        int updates = 0;

        while (ticksDone < System.currentTimeMillis() && updates < 10){
            update();
            ticksDone += 16;
            ++updates;
        }

        final float CAM_MOVE_SPEED = 0.02f;

        cam.setToOrtho(false);
        cam.zoom = 3.f / 32.f;

        if (pcCar != null) {
            Vector2 camTrg = pcCar.body.getPosition().cpy().add(new Vector2(0, 10));

            //camPos.x = (camPos.x * (1 - CAM_MOVE_SPEED)) + (CAM_MOVE_SPEED * camTrg.x);
            //camPos.y = (camPos.y * (1 - CAM_MOVE_SPEED)) + (CAM_MOVE_SPEED * camTrg.y);
            camPos.set(camTrg);
        }
        cam.position.x = camPos.x;
        cam.position.y = camPos.y;
        //cam.position.x = pcCar.body.getPosition().x;
        //cam.position.y = pcCar.body.getPosition().y;
        cam.update();

        LD41.s.batch.setProjectionMatrix(cam.combined);
        LD41.s.batch.begin();

        float tileSize = 128 * cam.zoom;
        float yBase = ((int)((camPos.y - 10 * tileSize) / tileSize)) * tileSize;

        for (int x=-20;x<20;x++){
            for (int y=-20;y<20;++y){
                LD41.s.batch.draw(LD41.s.road, x * tileSize, y * tileSize + yBase, tileSize * 1.001f, tileSize * 1.001f);
            }
        }

        for (int pass=0;pass<6;++pass) {
            for (int i = 0; i < actors.size(); ++i) {
                if (actors.get(i).getRenderPass() == pass)
                    actors.get(i).render();
            }
        }



        LD41.s.batch.end();

        if (LD41.DEBUG_PHYSICS) {
            Box2DDebugRenderer rnd = new Box2DDebugRenderer();
            rnd.render(world, cam.combined);
        }
    }

    public void update(){
        if (pcCar != null){
            final float TURN_SPEED = 0.02f;

            pcCar.acceleration = LD41.s.accel ? 1 : (LD41.s.brake ? -1 : 0);
            pcCar.steering = (LD41.s.turnLeft ? 1 : (LD41.s.turnRight ? -1 : 0)) * (pcCar.isGoingForward() ? 1 : -1);
        }

        if (started) {
            for (int i = 0; i < actors.size(); ++i) {
                if (actors.get(i).keep())
                    actors.get(i).update();
                else {
                    actors.get(i).destroyed();
                    if (actors.get(i) == pcCar) pcCar = null;
                    actors.remove(i--);
                }
            }

            world.step(0.016f, 2, 2);
        }

        if (getCarsLeft() == 1 && pcCar != null){
            hasWon = true;
        }

        if (pcCar == null) hasLost = true;

        if (started) startTime -= 0.016f;

        if (started){
            if (startTime > 0.25f){
                msg1Phase = Math.min(msg1Phase + 0.05f, 0.5f);
                if (!getReadySoundPlayed){ LD41.s.getReadySound.play(0.5f); getReadySoundPlayed = true; }
            } else if (startTime > -0.25f) {
                if (!goSoundPlayed){ LD41.s.goSound.play(0.5f); goSoundPlayed = true; }
                msg1Phase = Math.min(msg1Phase + 0.05f, 1.5f);
                msg2Phase = Math.min(msg2Phase + 0.05f, 0.5f);
            } else {
                msg2Phase = Math.min(msg2Phase + 0.05f, 1.5f);
            }
        }

        if (pcCar != null){
            float speed = pcCar.body.getLinearVelocity().len();

            if (Math.abs(speed) < 1){
                if (engineNoiseInd != -1){
                    LD41.s.engine.stop(engineNoiseInd);
                    engineNoiseInd = -1;
                }
            } else {
                if (engineNoiseInd == -1) engineNoiseInd = LD41.s.engine.loop();

                LD41.s.engine.setVolume(engineNoiseInd, MathUtils.clamp(speed / 10, 0, 1) * 0.35f);
                LD41.s.engine.setPitch(engineNoiseInd, MathUtils.clamp(0.5f + speed / 20, 0.5f, 2));
            }
        } else {
            if (engineNoiseInd != -1){
                LD41.s.engine.stop(engineNoiseInd);
                engineNoiseInd = -1;
            }
        }
    }

    long engineNoiseInd = -1;

    boolean getReadySoundPlayed = false;
    boolean goSoundPlayed = false;

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
        Object oa = contact.getFixtureA().getBody().getUserData();
        Object ob = contact.getFixtureB().getBody().getUserData();

        if (oa instanceof Actor && ob instanceof Actor){
            ((Actor) oa).stopCollideWith((Actor)ob);
            ((Actor) ob).stopCollideWith((Actor)oa);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public int getPlayerPosition(){
        List<Actor> list = actors.stream().filter(it -> it instanceof Car).sorted(Comparator.comparingDouble(a -> -a.body.getPosition().y)).collect(Collectors.toList());

        return list.indexOf(pcCar) + 1;
    }

    public int getCarsLeft(){
        return (int)actors.stream().filter(it -> it instanceof Car).count();
    }
}
