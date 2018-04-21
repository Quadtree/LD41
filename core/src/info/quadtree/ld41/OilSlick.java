package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class OilSlick extends Actor {
    public int oilLeft = 300;

    public OilSlick(Vector2 startPos) {
        super(startPos);
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return LD41.s.oilSlick;
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

    @Override
    protected int getRenderPass() {
        return 1;
    }

    @Override
    protected float renderSizeMul() {
        return 1.3f;
    }

    @Override
    public void render() {
        doRender(LD41.s.oilSlick, new Color(1,1,1, MathUtils.clamp(oilLeft / 45.f, 0.f, 1.f)));
    }
}
