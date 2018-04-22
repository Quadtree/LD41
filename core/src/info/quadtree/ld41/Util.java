package info.quadtree.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Util {
    public static void drawTextCentered(String text, BitmapFont font, float x, float y){
        GlyphLayout gl = new GlyphLayout(font, text);

        font.draw(LD41.s.batch, gl, x - gl.width / 2, y - gl.height / 2);
    }

    public static boolean isDrawable(float x, float y, float padding){
        float sw = Gdx.graphics.getWidth() * LD41.s.gs.cam.zoom;
        float sh = Gdx.graphics.getHeight() * LD41.s.gs.cam.zoom;

        if (x > LD41.s.gs.camPos.x + sw / 2 + padding) return false;
        if (x < LD41.s.gs.camPos.x - sw / 2 - padding) return false;
        if (y > LD41.s.gs.camPos.y + sh / 2 + padding) return false;
        if (y < LD41.s.gs.camPos.y - sh / 2 - padding) return false;

        return true;
    }
}
