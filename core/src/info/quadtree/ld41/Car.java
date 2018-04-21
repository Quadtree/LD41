package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;

public class Car extends Actor {
    public float steering;
    public float acceleration;
    public float actualSteering = 0;

    public boolean hasAi = true;

    public float traction = 1;

    public float stuckTime = 0;

    public float turnIn = MathUtils.random() * 250;

    public float slideTime = 0;

    public int oilSlickCharge = 0;

    public Color color = Color.WHITE;

    int shootOdds = 1800;

    boolean isAlive = true;

    Set<Actor> collidingWith = new HashSet<Actor>();

    public Car(Vector2 startPos) {
        super(startPos);

        color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(0, 0.6f), 1);
    }



    @Override
    public void update() {
        if (LD41.s.gs.startTime > 0){
            return;
        }

        super.update();

        if (hasAi) runAi();

        if (!hasAi){
            //System.out.println(body.getPosition());
        }

        --shootOdds;

        ++oilSlickCharge;

        if (slideTime < 0.1f) {
            Vector2 accelVector = new Vector2(acceleration * 3.f, 0);
            accelVector.rotateRad(body.getAngle());

            body.applyLinearImpulse(accelVector, body.getWorldCenter(), true);


            actualSteering = actualSteering * 0.9f + steering * 0.1f;

            float desiredTurnSpeed = actualSteering * 100f / 180f * MathUtils.PI * MathUtils.clamp(body.getLinearVelocity().len() / 4f, 0, 1);
            float turnDelta = desiredTurnSpeed - body.getAngularVelocity();

            body.applyAngularImpulse(turnDelta, true);
            //traction -= turnDelta * 0.05f;
            //System.out.println(turnDelta + " " + desiredTurnSpeed + " " + body.getAngle());

            //if (body.getLinearVelocity().len() > 0) System.out.println(body.getLinearVelocity().len());


            // stop slides
            Vector2 velVector = body.getLinearVelocity().cpy();
            velVector.rotateRad(-body.getAngle());

            Vector2 stopSlideVector = new Vector2(0, -1);
            stopSlideVector.rotateRad(body.getAngle());

            Vector2 ssf = stopSlideVector.scl(velVector.y);

            //System.out.println(String.format("SLIDE=%f, Stop slide force %s", velVector.y, ssf));

            body.applyLinearImpulse(ssf, body.getWorldCenter(), true);


        }

        float drag = 0.1f;

        for (Actor a : collidingWith){
            if (a instanceof Car) drag += 0.1f;
        }

        if (slideTime > 0.1f) drag += 0.25f;

        // drag
        body.applyLinearImpulse(body.getLinearVelocity().cpy().scl(-drag), body.getWorldCenter(), true);

        slideTime = Math.max(slideTime - 0.016f, 0);

        if (hitOilSlick){
            for (int i=0;i<3;++i){
                emitSmoke(100, Color.WHITE);
            }
            hitOilSlick = false;
        }
    }

    private void runAi(){
        Vector2 trgDest = new Vector2(body.getPosition().x, body.getPosition().y + 30);

        float leftDst = new Vector2(1, 0).rotateRad(body.getAngle() + 0.02f).add(body.getPosition()).dst2(trgDest);
        float rightDst = new Vector2(1, 0).rotateRad(body.getAngle() - 0.02f).add(body.getPosition()).dst2(trgDest);

        if (leftDst < rightDst){
            steering = 1;
        } else {
            steering = -1;
        }

        if (body.getLinearVelocity().len() < 2f){
            stuckTime += 0.016f;
        } else {
            stuckTime = Math.max(0, stuckTime - .1f);
        }

        //Vector2 delta = trgDest.cpy().sub(body.getPosition());
        if (stuckTime < 0.5f) {
            acceleration = 1;
        } else {
            acceleration = -1;
        }

        if (MathUtils.random(Math.max(1, shootOdds)) == 0) fireOilSlick();
    }

    protected Vector2 getSize(){ return new Vector2(3,2); }

    @Override
    protected void collidedWith(Actor a) {
        super.collidedWith(a);

        if (a instanceof OilSlick && body.getLinearVelocity().len() > 18f && ((OilSlick)a).onGroundTime > 10){
            slideTime = 2;
            body.applyAngularImpulse(MathUtils.random(-200, 200), true);

            ((OilSlick) a).oilLeft -= 50;

            hitOilSlick = true;
        }

        collidingWith.add(a);
    }

    boolean hitOilSlick = false;

    private void emitSmoke(int life, Color color) {
        LD41.s.gs.actors.add(new Spark(body.getPosition().cpy().add(MathUtils.random(-2, 2), MathUtils.random(-2, 2)), 20, LD41.s.smoke, true, 0.03f, 120.f, life, color));
    }

    @Override
    protected void stopCollideWith(Actor a) {
        super.stopCollideWith(a);

        collidingWith.remove(a);
    }

    public void fireOilSlick(){
        if (oilSlickCharge < 240) return;
        oilSlickCharge = 0;
        Vector2 target = new Vector2(0, -10000);
        Vector2 vtp = new Vector2();
        float targetSpeed = 0;

        for (Actor a : LD41.s.gs.actors){
            if (a instanceof Car && a != this){
                vtp.set(a.body.getPosition());
                vtp.add(0, MathUtils.random(3.f));
                if (vtp.y > target.y){
                    target = vtp.cpy();
                    targetSpeed = a.body.getLinearVelocity().len();
                }
            }
        }

        float rangeToTarget = body.getPosition().cpy().dst(target);

        float inaccuracy = rangeToTarget * 0.1f;

        LD41.s.gs.actors.add(new OilBomb(body.getPosition(), target.cpy().add(0, targetSpeed * OilBomb.calcFlightTime(rangeToTarget) * 1.75f).add(0, 15).add(MathUtils.random(-inaccuracy, inaccuracy), MathUtils.random(-inaccuracy, inaccuracy))));
    }

    @Override
    public boolean keep() {
        return isAlive;
    }

    @Override
    public void render() {
        //LD41.s.carBody.setColor();
        doRender(LD41.s.carWheels, Color.WHITE);
        doRender(LD41.s.carBody, color);
        doRender(LD41.s.carCanopy, Color.WHITE);
    }

    @Override
    public void destroyed() {
        if (body.getPosition().dst2(LD41.s.gs.camPos) < (60*60)) {
            LD41.s.explosionSounds.get(MathUtils.random(LD41.s.explosionSounds.size() - 1)).play();
        }

        for (int i=0;i<2;++i){
            emitSmoke(30, Color.ORANGE);
        }
        for (int i=0;i<20;++i){
            emitSmoke(120, Color.WHITE);
        }

        for (int i=0;i<8;++i){
            LD41.s.gs.actors.add(new CollidableSpark(body.getPosition().cpy().add(MathUtils.random(-2, 2), MathUtils.random(-2, 2)), 150, LD41.s.fragment));
        }

        super.destroyed();
    }
}
