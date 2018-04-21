package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class Wall extends Actor {
    public Wall(Vector2 startPos, float angle) {
        super(startPos);
        body.setTransform(body.getPosition(), angle);
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return super.getTextureRegion();
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.StaticBody;
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(1500, 1);
    }
}
