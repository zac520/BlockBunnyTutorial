package com.NZGames.BlockBunny.states;

import com.NZGames.BlockBunny.handlers.B2DVars;
import com.NZGames.BlockBunny.handlers.GameStateManager;
import com.NZGames.BlockBunny.handlers.MyContactListener;
import com.NZGames.BlockBunny.handlers.MyInput;
import com.NZGames.BlockBunny.main.BlockBunnyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import static com.NZGames.BlockBunny.handlers.B2DVars.BIT_GROUND;
import static com.NZGames.BlockBunny.handlers.B2DVars.BIT_PLAYER;
import static com.NZGames.BlockBunny.handlers.B2DVars.PPM;
/**
 * Created by zac520 on 7/10/14.
 */

public class Play extends GameState{

    private World world;
    private Box2DDebugRenderer b2dr;
    private Body playerBody;
    private MyContactListener cl;

    private OrthographicCamera b2dCam;
    public Play (GameStateManager gsm){

        super (gsm);

        //x and y forces, then inactive bodies should "sleep" (true)
        world = new World(new Vector2(0,-9.81f),true );
        cl = new MyContactListener();
        world.setContactListener(cl);

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
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;//what it can collide with (bitwise operators)

        //create fixture and make a tag setUserData
        body.createFixture(fdef).setUserData("ground");//a tag to identify this later




        //create player
        bdef.position.set(160/PPM, 200/PPM);
        bdef.type = BodyType.DynamicBody;

        //create body
        playerBody = world.createBody(bdef);

        //define Fixture
        shape.setAsBox(5/PPM, 5/PPM);
        fdef.shape = shape;
        fdef.restitution = 0.7f;//1= perfectly bouncy 0 = not at all bouncy
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_GROUND;//what it can collide with (bitwise operators)

        //create fixture
        playerBody.createFixture(fdef).setUserData("box");


        //create foot sensor
        shape.setAsBox(2/PPM, 2/PPM, new Vector2(0, -5/PPM),0);//set the box down
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND;
        fdef.isSensor = true;//make the foot go through ground for easier contact determining
        playerBody.createFixture(fdef).setUserData("foot");

        //set up box2dcam
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, BlockBunnyGame.V_WIDTH /PPM, BlockBunnyGame.V_HEIGHT / PPM);
    }

    public void handleInput(){

        //playerJump
        if(MyInput.isPressed(MyInput.BUTTON1)){
            //System.out.println("pressed Z");
            if(cl.isPlayerOnGround()){
                //force is in newtons
                playerBody.applyForceToCenter(0,200,true);
            }
        }
        if(MyInput.isDown(MyInput.BUTTON2)){
            System.out.println("hold X");
        }
    }
    public void update(float delta){
        //(step, accuracy of collisions (6 or 8 steps recommended), accuracy
        //of setting bodies after collision (2 recommended))
        // clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput();
        //update box2d
        world.step(delta, 6, 2);

    }
    public void render(){
        b2dr.render(world, b2dCam.combined);
    }
    public void dispose(){}
}
