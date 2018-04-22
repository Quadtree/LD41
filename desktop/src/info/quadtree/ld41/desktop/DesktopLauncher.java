package info.quadtree.ld41.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import info.quadtree.ld41.LD41;
import info.quadtree.ld41.Util;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Util.takeScreenshot = () -> {
			Gdx.files.external("ld41").mkdirs();

			byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

			for (int i=4;i<pixels.length;i += 4){
				pixels[i - 1] = (byte)255;
			}

			Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
			BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
			PixmapIO.writePNG(Gdx.files.external("ld41/screenshot" + System.currentTimeMillis() + ".png"), pixmap);
			pixmap.dispose();
		};

		TexturePacker.processIfModified("../../raw_assets", ".", "main");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new LD41(), config);
	}
}
