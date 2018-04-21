package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Actor {
    Body body;



    public Actor(Vector2 startPos){
        BodyDef bd = new BodyDef();
        bd.type = getBodyType();
        bd.position.x = startPos.x;
        bd.position.y = startPos.y;
        bd.angle = MathUtils.PI / 2;

        body = LD41.s.gs.world.createBody(bd);
        body.setUserData(this);

        if (hasFixture()) {
            FixtureDef fd = new FixtureDef();
            fd.density = 1;
            PolygonShape ps = new PolygonShape();
            ps.setAsBox(getSize().x * 0.5f, getSize().y * 0.5f);
            fd.shape = ps;
            fd.isSensor = isSensor();

            body.createFixture(fd);
        }
    }

    protected float renderSizeMul(){
        return 1;
    }

    public void update(){}
    public void render(){
        // Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
        //		float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY
        doRender(getTextureRegion());

    }

    protected void doRender(TextureRegion region) {
        LD41.s.batch.draw(region, body.getPosition().x - 0.5f, body.getPosition().y - 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, getSize().x * renderSizeMul(), getSize().y * renderSizeMul(), MathUtils.radiansToDegrees * body.getAngle());
    }

    protected void doRender(Sprite region, Color color) {
        //LD41.s.batch.draw(region, body.getPosition().x - 0.5f, body.getPosition().y - 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, getSize().x, getSize().y, MathUtils.radiansToDegrees * body.getAngle());


        region.setColor(color);
        region.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        region.setOrigin(region.getWidth() / 2, region.getHeight() / 2);
        //region.setCenter();
        region.setBounds(body.getPosition().x - region.getWidth() / 2, body.getPosition().y - region.getHeight() / 2, getSize().x, getSize().y);
        //region.setSize();

        //region.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        //region.setPosition(body.getPosition().x, body.getPosition().y);



        region.draw(LD41.s.batch);
    }

    public boolean keep(){ return true; }

    protected TextureRegion getTextureRegion(){ return LD41.s.solid; }
    protected BodyDef.BodyType getBodyType(){ return BodyDef.BodyType.DynamicBody; }
    protected Vector2 getSize(){ return new Vector2(3,3); }
    protected boolean isSensor(){ return false; }

    protected void collidedWith(Actor a){}
    protected void stopCollideWith(Actor a){}
    protected boolean hasFixture(){ return true; }
    protected int getRenderPass(){ return 2;}

    public void destroyed(){
        LD41.s.gs.world.destroyBody(body);
        body = null;
    }
}
