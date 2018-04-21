package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class WallOfDeath extends Actor {
    public WallOfDeath(Vector2 startPos) {
        super(startPos);
    }

    @Override
    public void update() {
        super.update();

        body.setTransform(body.getPosition().cpy().add(0, 0.2f), 0);
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return super.getTextureRegion();
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.KinematicBody;
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(100, 100);
    }

    @Override
    protected void collidedWith(Actor a) {
        super.collidedWith(a);

        if (a instanceof Car){
            ((Car) a).isAlive = false;
        }
    }
}
