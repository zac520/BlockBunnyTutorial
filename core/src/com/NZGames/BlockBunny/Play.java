package com.NZGames.BlockBunny;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by zac520 on 7/10/14.
 */
public class Play extends GameState{
    public Play (GameStateManager gsm){
        super (gsm);
    }

    private BitmapFont font = new BitmapFont();

    public void handleInput(){}
    public void update(float delta){}
    public void render(){
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        font.draw(sb,"Play state", 100,100);
        sb.end();
    }
    public void dispose(){}
}
