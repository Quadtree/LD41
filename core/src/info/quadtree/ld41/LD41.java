package info.quadtree.ld41;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class LD41 extends ApplicationAdapter implements InputProcessor {
	public SpriteBatch batch;
	public Texture img;

	public static LD41 s;

	public GameState gs;

	public BitmapFont defaultFont;

	public static final boolean CHEATS = true;
	public static final boolean DEBUG_PHYSICS = false;

	public Sprite carWheels;
	public Sprite carBody;
	public Sprite carCanopy;
	public Sprite solid;

	public TextureAtlas atlas;

	Matrix4 origProj = new Matrix4();
	
	@Override
	public void create () {
		LD41.s = this;

		atlas = new TextureAtlas(Gdx.files.internal("main.atlas"));

		carWheels = atlas.createSprite("car1_wheels");
		carBody = atlas.createSprite("car1_body");
		carCanopy = atlas.createSprite("car1_canopy");
		solid = atlas.createSprite("solid");

		batch = new SpriteBatch();
		origProj = batch.getProjectionMatrix().cpy();
		img = new Texture("badlogic.jpg");

		gs = new GameState();
		gs.init();

		defaultFont = new BitmapFont();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {


		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gs.render();

		batch.setProjectionMatrix(origProj.cpy());
		batch.begin();
		defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + " Position: " + gs.getPlayerPosition() + "/" + gs.getCarsLeft(), 40, 40);
		batch.end();


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
		if (keycode == Input.Keys.S){
			brake = true;
			return true;
		}
		if (keycode == Input.Keys.A){
			turnLeft = true;
			return true;
		}
		if (keycode == Input.Keys.D){
			turnRight = true;
			return true;
		}

		if (keycode == Input.Keys.SPACE){
			gs.pcCar.fireOilSlick();
			return true;
		}

		if (CHEATS) {
			if (keycode == Input.Keys.U) {
				gs.pcCar.body.setTransform(gs.pcCar.body.getPosition().cpy().add(0, 200), gs.pcCar.body.getAngle());
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.W){
			accel = false;
			return true;
		}
		if (keycode == Input.Keys.S){
			brake = false;
			return true;
		}
		if (keycode == Input.Keys.A){
			turnLeft = false;
			return true;
		}
		if (keycode == Input.Keys.D){
			turnRight = false;
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
