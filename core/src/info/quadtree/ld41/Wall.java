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
        Vector2 cp1 = new Vector2();
        Vector2 add = new Vector2();
        for (int i=0;i<1500/segLength;++i){
            cp.x = (i - 1500/segLength/2) * segLength + segLength / 2;
            cp.y = 0.5f;

            cp.rotateRad(body.getAngle());
            cp.add(body.getPosition());

            // draw the grass FIRST
            cp1.set(cp);

            add.y = segLength / 2;
            add.x = 0;
            add.rotateRad(body.getAngle());
            cp1.add(add);

            for (int j=0;j<24;++j){
                if (Util.isDrawable(cp1.x, cp1.y, 10)) {
                    LD41.s.batch.draw(LD41.s.grass, cp1.x - 0.5f, cp1.y - 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, segLength * 1.01f, segLength * 1.01f, MathUtils.radiansToDegrees * body.getAngle());

                    add.y = segLength;
                    add.x = 0;
                    add.rotateRad(body.getAngle());
                    cp1.add(add);
                }
            }

            if (Util.isDrawable(cp.x, cp.y, 10)) {
                LD41.s.batch.draw(LD41.s.barrier, cp.x - 0.5f, cp.y - 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, segLength * 1.01f, segWidth * 1.01f, MathUtils.radiansToDegrees * body.getAngle());
            }



        }
    }
}
