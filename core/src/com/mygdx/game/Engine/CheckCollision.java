package com.mygdx.game.Engine;

import com.badlogic.gdx.physics.bullet.collision.CollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithm;
import com.badlogic.gdx.physics.bullet.collision.btCollisionAlgorithmConstructionInfo;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDispatcherInfo;
import com.badlogic.gdx.physics.bullet.collision.btManifoldResult;
import com.badlogic.gdx.physics.bullet.collision.btSphereBoxCollisionAlgorithm;
import com.badlogic.gdx.utils.Array;


public class CheckCollision {

    public btCollisionObject thingObject, groundObject;
    public btDispatcher collisionDispatcher;
    public Array<btCollisionObject> collisionObjects;


    public boolean CheckCollision() {

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
}
