package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;

public class TestCharacters{

    public Model model;
    public Array<ModelInstance> instances;
    public Array<btCollisionObject> collisionObjects;
    public ModelInstance groundInstance, thingInstance;
    public btCollisionObject groundObject, thingObject;

    public btCollisionShape groundShape, thingShape;

    public btCollisionConfiguration collisionConf;
    public btDispatcher collisionDispatcher;

    public TestCharacters () {

        Bullet.init();

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        modelBuilder.node().id = "ground";
        modelBuilder.part("box", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position| VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GREEN))).box(100f, 0.1f, 100f);
        modelBuilder.node().id = "ball";
        modelBuilder.part("ball", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position| VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.RED))).sphere(10f, 10f, 10f, 20, 20);
        model = modelBuilder.end();

        thingInstance = new ModelInstance(model, "ball");
        thingInstance.transform.setToTranslation(10f, 35f, 0f);
        groundInstance = new ModelInstance(model, "ground");

        instances = new Array<ModelInstance>();
        instances.add(thingInstance);
        instances.add(groundInstance);

        thingShape = new btSphereShape(5.5f);
        groundShape = new btBoxShape(new Vector3(105f, 0.05f, 105f));

        groundObject = new btCollisionObject();
        groundObject.setCollisionShape(groundShape);
        groundObject.setWorldTransform(groundInstance.transform);

        thingObject = new btCollisionObject();
        thingObject.setCollisionShape(thingShape);
        thingObject.setWorldTransform(thingInstance.transform);

        collisionObjects = new Array<btCollisionObject>();
        collisionObjects.add(groundObject);
        collisionObjects.add(thingObject);

        collisionConf = new btDefaultCollisionConfiguration();
        collisionDispatcher = new btCollisionDispatcher(collisionConf);

    }
}
