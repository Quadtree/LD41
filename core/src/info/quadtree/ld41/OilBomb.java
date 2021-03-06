package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class OilBomb extends Actor {
    Vector2 targetPos;

    boolean hasGoneOff = false;

    static final float PRJ_SPEED = 60;

    float x = -0.5f;
    float rtt;

    public OilBomb(Vector2 startPos, Vector2 targetPos) {
        super(startPos);

        this.targetPos = targetPos.cpy();

        Vector2 toTarget = targetPos.cpy().sub(startPos);
        rtt = toTarget.len();
        toTarget.nor();
        float angle = toTarget.angle();

        body.setLinearVelocity(toTarget.scl(PRJ_SPEED));
        body.setTransform(body.getPosition(), angle * MathUtils.degreesToRadians);

        //xChangeRate = rtt / PRJ_SPEED * 0.016f * 4;
    }

    public static float calcFlightTime(float range){
        return range / PRJ_SPEED;
    }

    @Override
    public void update() {
        super.update();

        hitSomething();

        float rrtt = body.getPosition().cpy().dst(targetPos) - 2;
        x = 0.5f - (rrtt / rtt);

        if (x > 0.6f) System.out.println("x=" + x);
    }

    private float getAltitude(){
        return (0.25f - (x*x)) * 4;
    }

    private void hitSomething() {
        if (body.getPosition().dst2(targetPos) < 2*2){
            hasGoneOff = true;
            LD41.s.gs.actors.add(new OilSlick(targetPos));

            Util.playRandomSound(LD41.s.oilSounds, body.getPosition().x, body.getPosition().y, 25);
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
    protected int getRenderPass() {
        return 3;
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return LD41.s.oilBomb;
    }

    @Override
    protected float renderSizeMul() {
        return getAltitude() * 3 + 1;
    }

    @Override
    public void render() {



        float opac = MathUtils.lerp(1, 0.05f, MathUtils.clamp(getAltitude() * 1.25f, 0, 1));

        //System.out.println(x + " - " + opac);

        //System.err.println(opac);

        doRender(LD41.s.oilBomb, new Color(1, 1, 1, opac));
    }
}
