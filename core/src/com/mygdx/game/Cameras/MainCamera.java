package com.mygdx.game.Cameras;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;

public class MainCamera {

    public PerspectiveCamera cam;
    public final float[] startPos = {140f, 90f, 180f};

    public MainCamera() {

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(startPos[0], startPos[1], startPos[2]);
        cam.lookAt(0, 10, 0);
        cam.near = 1;
        cam.far = 500f;

    }

}
