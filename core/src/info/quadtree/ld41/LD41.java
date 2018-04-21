package info.quadtree.ld41;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class LD41 extends ApplicationAdapter implements InputProcessor {
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
		gs.init();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		if (gs.pcCar != null){
			gs.pcCar.acceleration = accel ? 1 : (brake ? -1 : 0);
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gs.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	boolean accel = false;
	boolean brake;
	boolean turnLeft;
	boolean turnRight;

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.W){
			accel = true;
			return true;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W){
			accel = false;
			return true;
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
