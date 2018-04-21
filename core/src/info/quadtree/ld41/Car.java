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

        Vector2 accelVector = new Vector2(acceleration, 0);
        accelVector.rotate(body.getAngle());

        body.applyLinearImpulse(accelVector, body.getWorldCenter(), true);



        actualSteering = actualSteering * 0.9f + steering * 0.1f;

        float desiredTurnSpeed = actualSteering * 100f;
        float turnDelta = desiredTurnSpeed - body.getAngularVelocity();

        body.applyAngularImpulse(turnDelta, true);
        //traction -= turnDelta * 0.05f;
        System.out.println(turnDelta + " " + desiredTurnSpeed + " " + body.getAngle());
    }
}
