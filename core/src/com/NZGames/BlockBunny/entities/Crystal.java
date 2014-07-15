package com.NZGames.BlockBunny.entities;

import com.NZGames.BlockBunny.main.BlockBunnyGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by zac520 on 7/14/14.
 */
public class Crystal extends B2DSprite {

    public Crystal (Body body) {

        super(body);


        Texture tex = BlockBunnyGame.res.getTexture("crystal");

        TextureRegion[] sprites = TextureRegion.split(tex, 16, 16)[0];

        setAnimation(sprites, 1 / 12f);
    }

}
