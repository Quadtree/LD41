package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class OilSlick extends Actor {
    public int oilLeft = 300;

    public OilSlick(Vector2 startPos) {
        super(startPos);
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
        return super.getSize();
    }

    @Override
    protected boolean isSensor() {
        return true;
    }

    @Override
    public void update() {
        super.update();

        --oilLeft;
    }

    @Override
    public boolean keep() {
        return oilLeft > 0;
    }
}
