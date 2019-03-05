package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Actors.MainCharacter;
import com.mygdx.game.Actors.TestCharacters;
import com.mygdx.game.Cameras.MainCamera;
import com.mygdx.game.Engine.CheckCollision;
import com.mygdx.game.Environments.MainEnvironment;
import com.mygdx.game.MyGdxGame;


public class TitleScreen implements Screen {

    private Stage stage;
    private Label label;
    private Game game;
    public Model model;

    MainCharacter character = new MainCharacter();
    MainEnvironment environment = new MainEnvironment();
    MainCamera camera = new MainCamera();

    boolean collision_flag;

    float counter = 0;


    public TitleScreen (Game aGame) {


        game = aGame;
        stage = new Stage(new ScreenViewport());

        Texture texture = new Texture(Gdx.files.internal("skin/aboard/titlescreen_background.png"));

        TextureRegion textureRegion = new TextureRegion(texture);

        Image background = new Image(textureRegion);

        textureRegion.setRegion(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setPosition(0,Gdx.graphics.getHeight() - background.getHeight());

        stage.addActor(background);

        Button myButton = new TextButton("PLAY", MyGdxGame.gameSkin,"default");
        myButton.setSize(Gdx.graphics.getWidth()/3,80);
        myButton.setPosition(Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()/3/2,Gdx.graphics.getHeight()/5);

        myButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                label.setText("Press a Button");
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen3d(game));
                return true;
            }
        });

        label = new Label("Press a Button",MyGdxGame.gameSkin,"title");
        label.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getWidth() / 5);
        label.setPosition(0,Gdx.graphics.getHeight()- Gdx.graphics.getWidth() / 12*2);
        label.setAlignment(Align.center);

        stage.addActor(myButton);
        stage.addActor(label);

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render (float delta) {
        Gdx.graphics.getGL20().glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (character.loading)
            if (character.assets.update()) {
                character.model = character.assets.get("skeleton_anim.g3dj", Model.class);
                character.instance = new ModelInstance(character.model);
                character.instance.transform.setToTranslation(0f, 25f, 0f);
                character.controller = new AnimationController(character.instance);
                character.loading = false;
            } else {
                return;
            }

        stage.act();
        stage.draw();

        camera.cam.rotateAround (Vector3.Zero, Vector3.Y, counter);
        camera.cam.update();

        character.modelBatch.begin(camera.cam);
        character.modelBatch.render(character.instance, environment.environment);
        character.controller.update(Gdx.graphics.getDeltaTime());
        character.modelBatch.end();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
    @Override
    public void dispose() {

        stage.dispose();
        character.model.dispose();
        character.modelBatch.dispose();
        model.dispose();
    }

}
