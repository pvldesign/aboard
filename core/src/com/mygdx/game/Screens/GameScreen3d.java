package com.mygdx.game.Screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Actors.MainCharacter;
import com.mygdx.game.Actors.TestCharacters;
import com.mygdx.game.Cameras.MainCamera;
import com.mygdx.game.Engine.CheckCollision;
import com.mygdx.game.Environments.MainEnvironment;
import com.mygdx.game.MyGdxGame;

public class GameScreen3d implements Screen {

    private Stage stage;
    public Game game;
    public Model model;

    MainCharacter character = new MainCharacter();
    TestCharacters testCharacters = new TestCharacters();
    CheckCollision checkCollision = new CheckCollision();
    MainEnvironment environment = new MainEnvironment();
    MainCamera camera = new MainCamera();

    boolean collision_flag;

    float counter = 0;


    public GameScreen3d(Game aGame) {

        game = aGame;
        stage = new Stage(new ScreenViewport());


        Texture texture = new Texture(Gdx.files.internal("skin/aboard/titlescreen_background.png"));

        TextureRegion textureRegion = new TextureRegion(texture);

        Image background = new Image(textureRegion);

        textureRegion.setRegion(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(0, Gdx.graphics.getHeight() - background.getHeight());

        stage.addActor(background);

        Button myButton = new TextButton("PLAY", MyGdxGame.gameSkin,"default");
        myButton.setSize(Gdx.graphics.getWidth()/3,80);
        myButton.setPosition(Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()/3/2,Gdx.graphics.getHeight()/5);

        myButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                character.controller.setAnimation("metarig|Stride Bone BVH_J_Cheerleader_19.001", 1);
                counter = counter -1f;
                character.instance.transform.setToTranslation(0f, 115f, 0f);
                testCharacters.thingInstance.transform.translate(0f, 5, 0f);

                return true;
            }
        });
        Button closeButton = new TextButton("X", MyGdxGame.gameSkin,"default");
        closeButton.setSize(50,50);
        closeButton.setPosition(Gdx.graphics.getWidth()-100,Gdx.graphics.getHeight()-100);

        closeButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));

                return true;
            }
        });

        stage.addActor(myButton);
        stage.addActor(closeButton);

    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
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

        final float deltaY = Math.min(1f/30f, Gdx.graphics.getDeltaTime());

        if(!collision_flag){

            testCharacters.thingInstance.transform.translate(0f, -deltaY-0.3f, 0f);

            testCharacters.thingObject.setWorldTransform(testCharacters.thingInstance.transform);
            checkCollision.thingObject = testCharacters.thingObject;
            checkCollision.collisionObjects = testCharacters.collisionObjects;
            checkCollision.groundObject = testCharacters.groundObject;
            checkCollision.collisionDispatcher = testCharacters.collisionDispatcher;

            collision_flag = checkCollision.CheckCollision();

        }

        camera.cam.rotateAround (Vector3.Zero, Vector3.Y, counter);
        camera.cam.update();

        character.modelBatch.begin(camera.cam);
        character.modelBatch.render(testCharacters.instances, environment.environment);
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

        testCharacters.thingShape.dispose();
        testCharacters.groundShape.dispose();
        testCharacters.thingObject.dispose();
        testCharacters.groundObject.dispose();
        testCharacters.collisionConf.dispose();
        testCharacters.collisionDispatcher.dispose();

        checkCollision.thingObject.dispose();
        checkCollision.groundObject.dispose();
        checkCollision.collisionDispatcher.dispose();
    }
}