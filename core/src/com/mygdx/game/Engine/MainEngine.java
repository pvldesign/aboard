package com.mygdx.game.Engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;

public class MainEngine extends ApplicationAdapter {
    public PerspectiveCamera cam;
    public Model model;
    public Array<ModelInstance> instances;
    public ModelInstance groundInstance;
    public ModelInstance thingInstance;
    public ModelBatch modelBatch;
    public Environment environment;
    public CameraInputController camController;

    boolean collision_flag;

    btCollisionShape groundShape, thingShape;
    btCollisionObject groundObject, thingObject;
    btCollisionConfiguration collisionConf;
    btDispatcher collisionDispatcher;

    @Override
    public void create () {
        Bullet.init();

        modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0f, 0f, 0f);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();


        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "ground";
        modelBuilder.part("box", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY))).box(10f, 0.1f, 10f);
        modelBuilder.node().id = "ball";
        modelBuilder.part("ball", GL20.GL_TRIANGLES, Usage.Position|Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED))).sphere(1f, 1f, 1f, 20, 20);
        model = modelBuilder.end();

        thingInstance = new ModelInstance(model, "ball");
        thingInstance.transform.setToTranslation(0f, 5f, 0f);
        groundInstance = new ModelInstance(model, "ground");
        instances = new Array<ModelInstance>();
        instances.add(thingInstance);
        instances.add(groundInstance);

        thingShape = new btSphereShape(0.5f);
        groundShape = new btBoxShape(new Vector3(5f, 0.05f, 5f));
        groundObject = new btCollisionObject();
        groundObject.setCollisionShape(groundShape);
        groundObject.setWorldTransform(groundInstance.transform);
        thingObject = new btCollisionObject();
        thingObject.setCollisionShape(thingShape);
        thingObject.setWorldTransform(thingInstance.transform);
        collisionConf = new btDefaultCollisionConfiguration();
        collisionDispatcher = new btCollisionDispatcher(collisionConf);
    }
    @Override
    public void render () {
        Gdx.gl.glViewport ( 0 , 0 , Gdx.graphics.getWidth (), Gdx.graphics.getHeight ());
        Gdx.gl.glClearColor(0.3f, 0.5f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);

        final float delta = Math.min(1f/30f, Gdx.graphics.getDeltaTime());
        if(!collision_flag){
            thingInstance.transform.translate(0f, -delta, 0f);
            thingObject.setWorldTransform(thingInstance.transform);
            collision_flag = CheckCollision();
        }
        modelBatch.begin(cam);
        modelBatch.render(instances, environment);
        modelBatch.end();
    }

    private boolean CheckCollision() {
        CollisionObjectWrapper COWmodel = new CollisionObjectWrapper(thingObject);
        CollisionObjectWrapper COWground = new CollisionObjectWrapper(groundObject);
        btCollisionAlgorithmConstructionInfo constInfo = new btCollisionAlgorithmConstructionInfo();
        constInfo.setDispatcher1(collisionDispatcher);
        btCollisionAlgorithm alg = new btSphereBoxCollisionAlgorithm(null, constInfo, COWmodel.wrapper, COWground.wrapper, false);
        btDispatcherInfo info = new btDispatcherInfo();
        btManifoldResult result = new btManifoldResult(COWmodel.wrapper, COWground.wrapper);
        alg.processCollision(COWmodel.wrapper, COWground.wrapper, info, result);
        boolean res = result.getPersistentManifold().getNumContacts()>0;
        result.dispose();
        info.dispose();
        alg.dispose();
        constInfo.dispose();
        COWmodel.dispose();
        COWground.dispose();
        return res;
    }
    @Override
    public void dispose(){
        thingShape.dispose();
        groundShape.dispose();
        thingObject.dispose();
        groundObject.dispose();
        collisionConf.dispose();
        collisionDispatcher.dispose();
        modelBatch.dispose();
        model.dispose();

    }
}