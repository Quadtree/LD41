package info.quadtree.ld41;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LD41 extends ApplicationAdapter {
	public SpriteBatch batch;
	public Texture img;

	public static LD41 s;

	public GameState gs;
	
	@Override
	public void create () {
		LD41.s = this;
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		gs = new GameState();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
