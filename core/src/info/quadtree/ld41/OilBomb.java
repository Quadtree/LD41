package info.quadtree.ld41;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class OilBomb extends Actor {
    Vector2 targetPos;

    boolean hasGoneOff = false;

    static final float PRJ_SPEED = 80;

    public OilBomb(Vector2 startPos, Vector2 targetPos) {
        super(startPos);

        this.targetPos = targetPos.cpy();

        body.setLinearVelocity(targetPos.cpy().sub(startPos).nor().scl(PRJ_SPEED));
    }

    @Override
    public void update() {
        super.update();

        if (body.getPosition().dst2(targetPos) < 2*2){
            hasGoneOff = true;
            LD41.s.gs.actors.add(new OilSlick(targetPos));
        }
    }

    @Override
    public boolean keep() {
        return !hasGoneOff;
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(0.1f, 0.1f);
    }

    @Override
    protected boolean hasFixture() {
        return false;


    }
}
