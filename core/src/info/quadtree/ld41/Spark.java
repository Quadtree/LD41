package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Spark extends Actor {
    Sprite sprite;
    boolean expands;
    float drag;

    float fadeTime = 120f;

    int life = 120;

    public Spark(Vector2 startPos, float maxSpeed, Sprite sprite, boolean expands, float drag, float fadeTime) {
        super(startPos);

        this.sprite = sprite;

        Vector2 vel = new Vector2(MathUtils.random(maxSpeed), 0);
        vel.rotate(MathUtils.random(360.f));

        body.setLinearVelocity(vel);
        body.setAngularVelocity(MathUtils.random(-2, 2));
        this.expands = expands;
        this.drag = drag;
        this.fadeTime = fadeTime;
    }

    @Override
    public void update() {
        super.update();

        body.setLinearVelocity(body.getLinearVelocity().cpy().scl(1 - drag));

        --life;
    }

    @Override
    public void render() {
        doRender(sprite, new Color(1,1,1,MathUtils.clamp(life / fadeTime, 0, 1)));
    }

    @Override
    protected boolean hasFixture() {
        return false;
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(2,2);
    }

    @Override
    protected float renderSizeMul() {
        return 1 + (expands ? ((1 - (life / 120.f)) * 4) : 0);
    }

    @Override
    public boolean keep() {
        return life > 0;
    }

    @Override
    protected int getRenderPass() {
        return 4;
    }
}
