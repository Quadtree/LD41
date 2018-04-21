package info.quadtree.ld41;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Car extends Actor {
    public float steering;
    public float acceleration;
    public float actualSteering = 0;

    public float traction = 1;

    public Car(Vector2 startPos) {
        super(startPos);
    }

    @Override
    public void update() {
        super.update();

        traction = MathUtils.clamp(traction + 0.02f, 0, 1);

        Vector2 accelVector = new Vector2(acceleration * 2.f, 0);
        accelVector.rotate(body.getAngle());

        body.applyLinearImpulse(accelVector, body.getWorldCenter(), true);



        actualSteering = actualSteering * 0.9f + steering * 0.1f;

        float desiredTurnSpeed = actualSteering * 100f * MathUtils.clamp(body.getLinearVelocity().len() / 4f, 0, 1);
        float turnDelta = desiredTurnSpeed - body.getAngularVelocity();

        body.applyAngularImpulse(turnDelta, true);
        //traction -= turnDelta * 0.05f;
        //System.out.println(turnDelta + " " + desiredTurnSpeed + " " + body.getAngle());

        //if (body.getLinearVelocity().len() > 0) System.out.println(body.getLinearVelocity().len());



        // stop slides
        Vector2 velVector = body.getLinearVelocity().cpy();
        velVector.rotate(-body.getAngle());

        Vector2 stopSlideVector = new Vector2(0, -1);
        stopSlideVector.rotate(body.getAngle());

        Vector2 ssf = stopSlideVector.scl(velVector.y);

        //System.out.println(String.format("SLIDE=%f, Stop slide force %s", velVector.y, ssf));

        body.applyLinearImpulse(ssf, body.getWorldCenter(), true);


        // drag
        body.applyLinearImpulse(body.getLinearVelocity().cpy().scl(-0.1f), body.getWorldCenter(), true);
    }

    protected Vector2 getSize(){ return new Vector2(3,2); }
}
