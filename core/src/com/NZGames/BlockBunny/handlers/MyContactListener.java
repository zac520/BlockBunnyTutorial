package com.NZGames.BlockBunny.handlers;

import com.badlogic.gdx.physics.box2d.*;

/**
 * Created by zac520 on 7/10/14.
 */
public class MyContactListener implements ContactListener {

    //called when two fixtures begin to collide
    public void beginContact (Contact c){
        //System.out.println("Begin Contact");
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        System.out.println("contact between " + fa.getUserData() + " and " + fb.getUserData());

    }

    //called when two fixtures no longer collide
    public void endContact (Contact c) {
        //System.out.println("End Contact");

    }

    //collision detection
    //collision handling
    public void preSolve (Contact c, Manifold m) {}

    //whatever happens after
    public void postSolve (Contact c, ContactImpulse ci) {}
}
