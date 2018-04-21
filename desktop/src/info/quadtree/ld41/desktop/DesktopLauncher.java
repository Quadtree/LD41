package info.quadtree.ld41.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import info.quadtree.ld41.LD41;

public class DesktopLauncher {
	public static void main (String[] arg) {
		TexturePacker.processIfModified("../../raw_assets", ".", "main");

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1024;
		config.height = 768;
		new LwjglApplication(new LD41(), config);
	}
}
