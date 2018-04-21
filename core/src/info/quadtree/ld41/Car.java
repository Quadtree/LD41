package info.quadtree.ld41;

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

    public float turnIn = MathUtils.random();

    public float slideTime = 0;

    Set<Actor> collidingWith = new HashSet<Actor>();

    public Car(Vector2 startPos) {
        super(startPos);
    }

    @Override
    public void update() {
        super.update();

        if (hasAi) runAi();

        if (slideTime < 0.1f) {
            Vector2 accelVector = new Vector2(acceleration * 2.f, 0);
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

            float drag = 0.1f;

            for (Actor a : collidingWith){
                if (a instanceof Car) drag += 0.1f;
            }

            // drag
            body.applyLinearImpulse(body.getLinearVelocity().cpy().scl(-drag), body.getWorldCenter(), true);
        }

        slideTime = Math.max(slideTime - 0.016f, 0);
    }

    private void runAi(){
        Vector2 trgDest = new Vector2(MathUtils.clamp(body.getPosition().x * turnIn - body.getPosition().y * 0.25f, 0, 1000), body.getPosition().y + 30);

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
            stuckTime -= 0.1f;
        }

        //Vector2 delta = trgDest.cpy().sub(body.getPosition());
        if (stuckTime < 1.5f) {
            acceleration = 1;
        } else {
            acceleration = -1;
        }
    }

    protected Vector2 getSize(){ return new Vector2(3,2); }

    @Override
    protected void collidedWith(Actor a) {
        super.collidedWith(a);

        if (a instanceof OilSlick){
            System.out.println("COL: " + a);
            slideTime = 5;
            body.applyAngularImpulse(MathUtils.random(-200, 200), true);
        }

        collidingWith.add(a);
    }

    @Override
    protected void stopCollideWith(Actor a) {
        super.stopCollideWith(a);

        collidingWith.remove(a);
    }

    public void fireOilSlick(){
        Vector2 target = new Vector2(0, -10000);
        float targetSpeed = 0;

        for (Actor a : LD41.s.gs.actors){
            if (a instanceof Car){
                if (a.body.getPosition().y > target.y){
                    target = a.body.getPosition();
                    targetSpeed = a.body.getLinearVelocity().len();
                }
            }
        }

        float rangeToTarget = body.getPosition().cpy().dst(target);

        LD41.s.gs.actors.add(new OilBomb(body.getPosition(), target.cpy().add(0, targetSpeed * rangeToTarget / OilBomb.PRJ_SPEED)));
    }
}
