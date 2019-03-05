package com.mygdx.game.Actors;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;


public class MainCharacter extends InputAdapter {


    public Model model;
    public ModelInstance instance;

    public ModelBatch modelBatch;

    public AssetManager assets;

    public AnimationController controller;

    public boolean loading;

    public MainCharacter() {

        modelBatch = new ModelBatch();

        assets = new AssetManager();
        assets.load("skeleton_anim.g3dj", Model.class);
        loading = true;

    }
}
