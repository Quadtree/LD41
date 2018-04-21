package info.quadtree.ld41;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class CollidableSpark extends Spark {
    public CollidableSpark(Vector2 startPos, float maxSpeed, Sprite sprite) {
        super(startPos, maxSpeed, sprite, false, 0, 15.f, 120, Color.WHITE);
    }

    @Override
    protected boolean hasFixture() {
        return true;
    }

    @Override
    protected Vector2 getSize() {
        return new Vector2(0.5f, 0.5f);
    }
}
