package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class OilBomb extends Actor {
    Vector2 targetPos;

    boolean hasGoneOff = false;

    static final float PRJ_SPEED = 80;

    public OilBomb(Vector2 startPos, Vector2 targetPos) {
        super(startPos);

        this.targetPos = targetPos.cpy();

        Vector2 toTarget = targetPos.cpy().sub(startPos).nor();
        float angle = toTarget.angle();

        body.setLinearVelocity(toTarget.scl(PRJ_SPEED));
        body.setTransform(body.getPosition(), angle * MathUtils.degreesToRadians);
    }

    @Override
    public void update() {
        super.update();

        hitSomething();
    }

    private void hitSomething() {
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
        return new Vector2(1.5f, 1.5f);
    }

    @Override
    protected boolean hasFixture() {
        return false;
    }

    @Override
    protected void collidedWith(Actor a) {
        super.collidedWith(a);

        if (a instanceof Wall) hitSomething();
    }

    @Override
    protected int getRenderPass() {
        return 3;
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return LD41.s.oilBomb;
    }
}
