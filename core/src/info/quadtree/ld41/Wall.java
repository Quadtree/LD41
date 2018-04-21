package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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

    @Override
    public void render() {
        float segLength = 4;
        float segWidth = 1;

        Vector2 cp = new Vector2();
        for (int i=0;i<1500/segLength;++i){
            cp.x = (i - 1500/segLength/2) * segLength;
            cp.y = 0.5f;

            cp.rotateRad(body.getAngle());

            cp.add(body.getPosition());

            LD41.s.batch.draw(LD41.s.barrier, cp.x - 0.5f, cp.y - 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, segLength, segWidth, MathUtils.radiansToDegrees * body.getAngle());
        }
    }
}
