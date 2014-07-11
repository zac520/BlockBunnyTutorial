package com.NZGames.BlockBunny.states;

import com.NZGames.BlockBunny.B2DVars;
import com.NZGames.BlockBunny.handlers.GameStateManager;
import com.NZGames.BlockBunny.handlers.MyContactListener;
import com.NZGames.BlockBunny.main.BlockBunnyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import static com.NZGames.BlockBunny.B2DVars.BIT_BALL;
import static com.NZGames.BlockBunny.B2DVars.BIT_GROUND;
import static com.NZGames.BlockBunny.B2DVars.PPM;
/**
 * Created by zac520 on 7/10/14.
 */

public class Play extends GameState{

    private World world;
    private Box2DDebugRenderer b2dr;

    private OrthographicCamera b2dCam;
    public Play (GameStateManager gsm){

        super (gsm);

        //x and y forces, then inactive bodies should "sleep" (true)
        world = new World(new Vector2(0,-9.81f),true );
        world.setContactListener(new MyContactListener());

        b2dr = new Box2DDebugRenderer();

        //create platform
        //to create a body we do 5 steps:
            //1.create world
            //  2. Define Body
            //  3. Create body
            //     4. Define Fixture
            //     5. Create Fixture
        //static body does not move, unaffected by forces
        //kinematic bodies: not affected by world forces, but can change velocities (example: moving platform)
        //dynamic bodies do get affected by forces (example: sprite)


        //define platform body
        BodyDef bdef = new BodyDef();
        bdef.position.set(160 /PPM, 120/PPM);
        bdef.type = BodyType.StaticBody;

        //create body
        Body body = world.createBody(bdef);

        //define Fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50/PPM, 5/PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.filter.categoryBits = B2DVars.BIT_GROUND; //what it is
        fdef.filter.maskBits = B2DVars.BIT_BALL | B2DVars.BIT_BOX;//what it can collide with (bitwise operators)

        //create fixture and make a tag setUserData
        body.createFixture(fdef).setUserData("ground");//a tag to identify this later

        //create falling box
        bdef.position.set(160/PPM, 200/PPM);
        bdef.type = BodyType.DynamicBody;

        //create body
        body = world.createBody(bdef);

        //define Fixture
        shape.setAsBox(5/PPM, 5/PPM);
        fdef.shape = shape;
        fdef.restitution = 0.7f;//1= perfectly bouncy 0 = not at all bouncy
        fdef.filter.categoryBits = B2DVars.BIT_BOX;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;//what it can collide with (bitwise operators)

        //create fixture
        body.createFixture(fdef).setUserData("box");

        //create ball
        bdef.position.set(153/PPM, 220/PPM);
        body = world.createBody(bdef);

        CircleShape cshape = new CircleShape();
        cshape.setRadius(5/PPM);
        fdef.shape = cshape;
        fdef.filter.categoryBits = B2DVars.BIT_BALL;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;//what it can collide with (bitwise operators)

        body.createFixture(fdef).setUserData("ball");


        //set up box2dcam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, BlockBunnyGame.V_WIDTH /PPM, BlockBunnyGame.V_HEIGHT / PPM);
    }

    public void handleInput(){}
    public void update(float delta){
        //(step, accuracy of collisions (6 or 8 steps recommended), accuracy
        //of setting bodies after collision (2 recommended))
        // clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //update box2d
        world.step(delta, 6, 2);

    }
    public void render(){
        b2dr.render(world, b2dCam.combined);
    }
    public void dispose(){}
}
