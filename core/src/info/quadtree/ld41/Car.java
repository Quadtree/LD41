package info.quadtree.ld41;

import com.badlogic.gdx.math.Vector2;

public class Car extends Actor {
    public float steering;
    public float acceleration;

    public Car(Vector2 startPos) {
        super(startPos);
    }

    @Override
    public void update() {
        super.update();

        Vector2 accelVector = new Vector2(acceleration, 0);
        accelVector.rotateRad(body.getAngle());

        body.applyLinearImpulse(accelVector, body.getWorldCenter(), true);

        body.applyAngularImpulse(steering, true);
    }
}
