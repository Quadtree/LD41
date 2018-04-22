package info.quadtree.ld41;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class LD41 extends ApplicationAdapter implements InputProcessor {
	public SpriteBatch batch;
	public Texture img;

	public static LD41 s;

	public GameState gs;

	public BitmapFont defaultFont;
	public BitmapFont bigFont;

	public static final boolean CHEATS = false;
    public static final boolean DEBUG_PHYSICS = false;

	public static final boolean MUSIC = true;

	public static final boolean ENGINE_NOISE = true;

	public Sprite carWheels;
	public Sprite carBody;
	public Sprite carCanopy;
	public Sprite solid;
	public Sprite wallOfDeath;
	public Sprite road;
	public Sprite barrier;
	public Sprite grass;
	public Sprite oilSlick;
	public Sprite oilBomb;
	public Sprite fragment;
	public Sprite smoke;
	public Sprite gradient;

	public List<Sound> explosionSounds = new ArrayList<>();

	public String[] musicNames = {
			"carefree_racing",
			"drum_drive",
			"gotta_go_fast",
			"ride_for_your_lives",
			"swift_driving",
			"swiftness"
	};

	public List<Sound> oilSounds = new ArrayList<>();
	public List<Sound> slipSounds = new ArrayList<>();

	public List<Sound> hitSounds = new ArrayList<>();

	public Sound getReadySound;
	public Sound goSound;
	public List<Music> engine = new ArrayList<>();

	Music currentMusic;

	Map<String, Music> musicCache = new HashMap<>();

	public TextureAtlas atlas;

	boolean showingTitleScreen = true;

	Matrix4 origProj = new Matrix4();
	
	@Override
	public void create () {
		LD41.s = this;

		atlas = new TextureAtlas(Gdx.files.internal("main.atlas"));

		carWheels = atlas.createSprite("car1_wheels");
		carBody = atlas.createSprite("car1_body");
		carCanopy = atlas.createSprite("car1_canopy");
		solid = atlas.createSprite("solid");
		wallOfDeath = atlas.createSprite("wall_of_death");
		road = atlas.createSprite("road");
		barrier = atlas.createSprite("barrier1");
		grass = atlas.createSprite("grass1");
		oilSlick = atlas.createSprite("oil_slick");
		oilBomb = atlas.createSprite("oil_bomb");
		fragment = atlas.createSprite("fragment1");
		smoke = atlas.createSprite("smoke2");
		gradient = atlas.createSprite("gradient");

		for (int i=0;i<4;++i){
			explosionSounds.add(Gdx.audio.newSound(Gdx.files.internal("exp" + i + ".wav")));
		}

		for (int i=0;i<3;++i){
			oilSounds.add(Gdx.audio.newSound(Gdx.files.internal("oil" + i + ".wav")));
		}

		for (int i=0;i<3;++i){
			slipSounds.add(Gdx.audio.newSound(Gdx.files.internal("slip" + i + ".wav")));
		}

		for (int i=0;i<5;++i){
			hitSounds.add(Gdx.audio.newSound(Gdx.files.internal("hit" + i + ".wav")));
		}

		getReadySound = Gdx.audio.newSound(Gdx.files.internal("get_ready.wav"));
		goSound = Gdx.audio.newSound(Gdx.files.internal("go.wav"));

		if (ENGINE_NOISE) {
			for (int i = 0; i < 7; ++i) {
				engine.add(Gdx.audio.newMusic(Gdx.files.internal("engine" + i + ".ogg")));
			}
		}


		batch = new SpriteBatch();
		origProj = batch.getProjectionMatrix().cpy();
		img = new Texture("badlogic.jpg");

		gs = new GameState();
		gs.init();

		defaultFont = new BitmapFont(Gdx.files.internal("aldrich_24.fnt"));
		bigFont = new BitmapFont(Gdx.files.internal("aldrich_90.fnt"));

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		if (MUSIC && (currentMusic == null || !currentMusic.isPlaying())){
			String nextMusic = musicNames[MathUtils.random(musicNames.length - 1)];

			if (currentMusic == null) nextMusic = "gotta_go_fast";

			if (!musicCache.containsKey(nextMusic)){
				musicCache.put(nextMusic, Gdx.audio.newMusic(Gdx.files.internal(nextMusic + ".mid.ogg")));
			}

			currentMusic = musicCache.get(nextMusic);
			currentMusic.setVolume(0.25f);
			currentMusic.play();

			System.out.println("Playing " + nextMusic);
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gs.render();

		batch.setProjectionMatrix(origProj.cpy());
		batch.begin();

		if (showingTitleScreen){
			batch.draw(LD41.s.gradient, 0, Gdx.graphics.getHeight() - 350, Gdx.graphics.getWidth(), 350);

			int y = 400;
			Util.drawTextCentered("Last Car Standing", bigFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + y);
			Util.drawTextCentered("Outrun the wall of doom and be the last car standing!", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + (y -= 130));
			Util.drawTextCentered("WASD/Arrow Keys to move, Space to shoot oil bombs, R to restart", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + (y -= 30));
			Util.drawTextCentered("The bomb will auto-target at the car in the lead,", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + (y -= 30));
			Util.drawTextCentered("and is only effective against cars going fast.", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + (y -= 30));
			Util.drawTextCentered("Press any key to start", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + (y -= 60));

			Util.drawTextCentered("Made by Quadtree for Ludum Dare 41", defaultFont, Gdx.graphics.getWidth() - 290, 45);
		}

		if (gs.hasLost){
			Util.drawTextCentered("You have lost!", bigFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 120);
			Util.drawTextCentered("Press R to restart", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		}

		if (gs.hasWon){
			Util.drawTextCentered("You have won!", bigFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 + 120);
			Util.drawTextCentered("Press R to restart", defaultFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		}

		if (CHEATS) defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 40, 40);

		//defaultFont.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond() + " Position: " + gs.getPlayerPosition() + "/" + gs.getCarsLeft() + " Speed: " + (int)(gs.pcCar != null ? gs.pcCar.body.getLinearVelocity().len() : 0), 40, 40);
		if (gs.pcCar != null && !showingTitleScreen) {
			if (gs.pcCar.isOilSlickReady()) batch.draw(LD41.s.oilBomb, Gdx.graphics.getWidth() - 70, Gdx.graphics.getHeight() - 24, 8, 8, 16, 16, 1, 1, 90);
			defaultFont.draw(batch, Objects.toString((int) (gs.pcCar != null ? gs.pcCar.body.getLinearVelocity().len() : 0)), Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 5);
			defaultFont.draw(batch, gs.getPlayerPosition() + "/" + gs.getCarsLeft(), 5, Gdx.graphics.getHeight() - 5);
		}

		if (gs.msg1Phase < 0.99f) Util.drawTextCentered("Get ready!", bigFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - (0.5f - gs.msg1Phase) * 800);
		if (gs.msg2Phase < 0.99f) Util.drawTextCentered("Go!", bigFont, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2 - (0.5f - gs.msg2Phase) * 800);


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
		if (showingTitleScreen){
			showingTitleScreen = false;
			gs.started = true;
		}

		if (keycode == Input.Keys.W || keycode == Input.Keys.UP){
			accel = true;
			return true;
		}
		if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN){
			brake = true;
			return true;
		}
		if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT){
			turnLeft = true;
			return true;
		}
		if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT){
			turnRight = true;
			return true;
		}
		if (keycode == Input.Keys.R){
			gs = new GameState();
			gs.init();
			gs.started = true;
			return true;
		}

		if (keycode == Input.Keys.SPACE){
			gs.pcCar.fireOilSlick();
			return true;
		}

		if (keycode == Input.Keys.F12){
			Util.takeScreenshot.run();
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
		if (keycode == Input.Keys.W || keycode == Input.Keys.UP){
			accel = false;
			return true;
		}
		if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN){
			brake = false;
			return true;
		}
		if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT){
			turnLeft = false;
			return true;
		}
		if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT){
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
