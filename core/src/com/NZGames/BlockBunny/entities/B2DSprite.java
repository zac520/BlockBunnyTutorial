package com.NZGames.BlockBunny.entities;

import com.NZGames.BlockBunny.handlers.Animation;
import com.NZGames.BlockBunny.handlers.B2DVars;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by zac520 on 7/13/14.
 */
public class B2DSprite {
    protected  Body body;
    protected Animation animation;

    protected float width;
    protected float height;


    public B2DSprite(Body body){
        this.body = body;
        animation = new Animation();

    }

    public void setAnimation(TextureRegion[] reg, float delay){
        animation.setFrames(reg,delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
    }

    public void update (float delta){
        animation.update(delta);
    }

    public void render(SpriteBatch sb){
        //have to set back up to regular size (the PPM multiplyer)
        sb.begin();
        sb.draw(
                animation.getFrame(),
                body.getPosition().x * B2DVars.PPM - width / 2,
                body.getPosition().y * B2DVars.PPM - height /2
        );
        sb.end();
    }

    public Body getBody(){
        return body;
    }

    public Vector2 getPostion(){
        return body.getPosition();
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){

        return height;
    }

}
