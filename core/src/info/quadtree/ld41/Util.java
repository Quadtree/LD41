package info.quadtree.ld41;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Util {
    public static void drawTextCentered(String text, BitmapFont font, float x, float y){
        GlyphLayout gl = new GlyphLayout(font, text);

        font.draw(LD41.s.batch, gl, x - gl.width / 2, y - gl.height / 2);
    }
}
