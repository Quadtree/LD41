package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
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

        body.setTransform(body.getPosition().cpy().add(0, 0.1f), 0);

        float maxCarY = -1000;

        for (Actor a : LD41.s.gs.actors){
            if (a instanceof Car){
                maxCarY = Math.max(a.body.getPosition().y, maxCarY);
            }
        }

        if (maxCarY - body.getPosition().y > 150){
            body.setTransform(body.getPosition().cpy().add(0, 0.2f), 0);
        }

        if (maxCarY > 750){
            body.setTransform(body.getPosition().cpy().add(0, 1.5f), 0);
        }
    }

    @Override
    public boolean keep() {
        return LD41.s.gs.getCarsLeft() > 1;
    }

    @Override
    protected BodyDef.BodyType getBodyType() {
        return BodyDef.BodyType.KinematicBody;
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(200, 200);
    }

    @Override
    protected void collidedWith(Actor a) {
        super.collidedWith(a);

        if (a instanceof Car){
            ((Car) a).isAlive = false;
        }
    }

    @Override
    protected int getRenderPass() {
        return 5;
    }

    @Override
    protected TextureRegion getTextureRegion() {
        return LD41.s.wallOfDeath;
    }

    @Override
    protected boolean isSensor() {
        return true;
    }
}
