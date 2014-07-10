package com.NZGames.BlockBunny;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by zac520 on 7/10/14.
 */
public abstract class GameState {
    protected GameStateManager gsm;
    protected BlockBunnyGame game;

    protected SpriteBatch sb;
    protected OrthographicCamera cam;
    protected OrthographicCamera hudCam;


    protected GameState(GameStateManager gsm){
        this.gsm = gsm;
        game = gsm.game();
        sb = game.getSpriteBatch();
        cam = game.getCamera();
        hudCam = game.getHUDCAM();

    }

    public abstract void handleInput();
    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();

}
