package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
        bd.angle = 90;

        body = LD41.s.gs.world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(getSize().x * 0.5f, getSize().y * 0.5f);
        fd.shape = ps;

        body.createFixture(fd);
    }

    public void update(){}
    public void render(){
        // Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
        //		float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY
        LD41.s.batch.draw(getTextureRegion(), body.getPosition().x - 0.5f, body.getPosition().y - 0.5f, 0.5f, 0.5f, 1.0f, 1.0f, getSize().x, getSize().y, body.getAngle());

    }
    public boolean keep(){ return true; }

    protected TextureRegion getTextureRegion(){ return new TextureRegion(LD41.s.img); }
    protected BodyDef.BodyType getBodyType(){ return BodyDef.BodyType.DynamicBody; }
    protected Vector2 getSize(){ return new Vector2(3,3); }
}
