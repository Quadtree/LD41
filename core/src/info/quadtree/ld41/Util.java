package info.quadtree.ld41;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

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

    public static void playRandomSound(List<Sound> snds, float x, float y, float atten){
        playRandomSound(snds, x,y,atten, 1);
    }

    public static void playRandomSound(List<Sound> snds, float x, float y, float atten, float vol){
        float distSqr = 10000;
        if (LD41.s.gs.pcCar != null){
            distSqr = new Vector2(x,y).dst2(LD41.s.gs.pcCar.body.getPosition());
        } else {
            distSqr = new Vector2(x, y).dst2(LD41.s.gs.camPos);
        }

        if (distSqr < atten*atten) {
            snds.get(MathUtils.random(snds.size() - 1)).play(MathUtils.clamp(1 - (distSqr / (atten*atten)), 0, 1) * vol);
        }
    }

    public static Runnable takeScreenshot = () -> {};
}
