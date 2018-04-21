package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class CollidableSpark extends Spark {
    public CollidableSpark(Vector2 startPos, float maxSpeed, Sprite sprite) {
        super(startPos, maxSpeed, sprite, false, 0);
    }

    @Override
    protected boolean hasFixture() {
        return true;
    }
}
