package com.NZGames.BlockBunny.states;

import com.NZGames.BlockBunny.entities.Crystal;
import com.NZGames.BlockBunny.entities.Player;
import com.NZGames.BlockBunny.handlers.B2DVars;
import com.NZGames.BlockBunny.handlers.GameStateManager;
import com.NZGames.BlockBunny.handlers.MyContactListener;
import com.NZGames.BlockBunny.handlers.MyInput;
import com.NZGames.BlockBunny.main.BlockBunnyGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import static com.NZGames.BlockBunny.handlers.B2DVars.*;

/**
 * Created by zac520 on 7/10/14.
 */

public class Play extends GameState {

    private boolean debug = false;

    private World world;
    private Box2DDebugRenderer b2dr;
    private MyContactListener cl;

    private TiledMap tileMap;
    private float tileSize;
    private OrthogonalTiledMapRenderer tmr;

    private OrthographicCamera b2dCam;

    private Player player;

    private Array<Crystal> crystals;

    public Play(GameStateManager gsm) {

        super(gsm);

        //x and y forces, then inactive bodies should "sleep" (true)
        world = new World(new Vector2(0, -9.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        b2dr = new Box2DDebugRenderer();


        //createPlatform();
        createPlayer();

        //set up box2dcam
        b2dCam = new OrthographicCamera();

        b2dCam.setToOrtho(false, BlockBunnyGame.V_WIDTH / PPM, BlockBunnyGame.V_HEIGHT / PPM);

        createTiles();

        createCrystals();


    }

    public void handleInput() {

        //playerJump
        if (MyInput.isPressed(MyInput.BUTTON1)) {
            //System.out.println("pressed Z");
            if (cl.isPlayerOnGround()) {
                //force is in newtons
                player.getBody().applyForceToCenter(0, 250, true);
            }
        }
        if (MyInput.isDown(MyInput.BUTTON2)) {
            System.out.println("hold X");
        }
    }

    public void update(float delta) {

        handleInput();
        //(step, accuracy of collisions (6 or 8 steps recommended), accuracy
        //of setting bodies after collision (2 recommended))
        world.step(delta, 6, 2);

        //remove crystals if necessary
        Array<Body> bodies = cl.getBodiesToRemove();
        for(int i = 0; i< bodies.size; i++){
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
        }
        bodies.clear();


        player.update(delta);

        for(int i = 0; i<crystals.size; i++){
            crystals.get(i).update(delta);
        }
    }

    public void render() {
        // clear screen
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //draw tilemap
        tmr.setView(cam);
        tmr.render();

        //draw player
        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        //draw crystals

        for(int i = 0; i<crystals.size; i++){
            crystals.get(i).render(sb);
        }
        //draw box2d world
        if(debug) {
            b2dr.render(world, b2dCam.combined);
        }
    }

    public void dispose() {
    }
    public void createPlatform(){
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
        bdef.position.set(160 / PPM, 120 / PPM);
        bdef.type = BodyType.StaticBody;

        //create body
        Body body = world.createBody(bdef);

        //define Fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 5 / PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        //fdef.filter.categoryBits = B2DVars.BIT_GROUND; //what it is
        fdef.filter.maskBits = B2DVars.BIT_PLAYER;//what it can collide with (bitwise operators)

        //create fixture and make a tag setUserData
        body.createFixture(fdef).setUserData("ground");//a tag to identify this later


    }

    public void createPlayer() {
//create player
        //define platform body
        BodyDef bdef = new BodyDef();
        bdef.position.set(75 / PPM, 200 / PPM);
        bdef.type = BodyType.DynamicBody;
        bdef.linearVelocity.set(0.1f,0);
        //create body
        Body body = world.createBody(bdef);

        //define Fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(13 / PPM, 13 / PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        //fdef.restitution = 0.7f;//1= perfectly bouncy 0 = not at all bouncy
        fdef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fdef.filter.maskBits = B2DVars.BIT_BLUE| BIT_CRYSTAL;//what it can collide with (bitwise operators)

        //create fixture
        body.createFixture(fdef).setUserData("box");


        //create foot sensor
        shape.setAsBox(13 / PPM, 13 / PPM, new Vector2(0, -5 / PPM), 0);//set the box down
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_BLUE;
        fdef.isSensor = true;//make the foot go through ground for easier contact determining
        body.createFixture(fdef).setUserData("foot");



        //create player
        player = new Player(body);
        body.setUserData(player);//used to provide reference later

    }

    public void createTiles(){
        /////////////////////////////////////////////////////////////////////
        // load tile map
        tileMap = new TmxMapLoader().load("assets/maps/test2.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);

        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
        createLayer(layer, BIT_BLUE);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
        createLayer(layer, BIT_GREEN);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
        createLayer(layer, BIT_RED);


    }

    public void createLayer(TiledMapTileLayer layer, short bits){
        //for some reason, the guy has rows and columns reversed
        //go through all cells in layer
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {
                //get cell
                Cell cell = layer.getCell(col, row);

                //check if cell exists
                if (cell == null) {
                    continue;
                }
                if (cell.getTile() == null) {
                    continue;
                }

                //create a body and fixture from cell
                bdef.type = BodyType.StaticBody;
                //box2d uses center, not corner positioning, so we add 0.5
                bdef.position.set(
                        (col + 0.5f) * tileSize / PPM,
                        (row + 0.5f) * tileSize / PPM
                );

                //use chainShape to prevent getting stuck between boxes
                ChainShape cs = new ChainShape();

                //we are using 3 corners of the box
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(
                        -tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(
                        -tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2(
                        tileSize / 2 / PPM, tileSize / 2 / PPM);

                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;
                fdef.filter.categoryBits = bits;
                fdef.filter.maskBits = BIT_PLAYER;//default
                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);
            }
        }
    }

    private void createCrystals(){
        crystals = new Array<Crystal>();

        MapLayer layer = tileMap.getLayers().get("crystals");
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject mo: layer.getObjects()){

            bdef.type = BodyType.StaticBody;
            float x=0;
            float y=0;

            if (mo instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) mo).getRectangle();
                x = rect.x / PPM;
                y = rect.y / PPM;
            }
            else if (mo instanceof PolygonMapObject) {
                Polygon polygon = ((PolygonMapObject) mo).getPolygon();
                //these are not right for this shape, but just a starter for if I need it
//                x = polygon.x / PPM;
//                y = polygon.y / PPM;
            }
            else if (mo instanceof PolylineMapObject) {
                Polyline chain = ((PolylineMapObject) mo).getPolyline();
//                x = chain.x / PPM;
//                y = chain.y / PPM;
            }
            else if (mo instanceof CircleMapObject) {
                Circle circle = ((CircleMapObject) mo).getCircle();
                x = circle.x / PPM;
                y = circle.y / PPM;
            }
            else if (mo instanceof EllipseMapObject) {
                Ellipse ellipse = ((EllipseMapObject) mo).getEllipse();
                x = ellipse.x / PPM;
                y = ellipse.y / PPM;
            }



            bdef.position.set(x, y);

            CircleShape cshape = new CircleShape();
            cshape.setRadius(8/PPM);

            fdef.shape = cshape;
            fdef.isSensor = true;
            fdef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
            fdef.filter.maskBits = BIT_PLAYER;

            Body body = world.createBody(bdef);
            body.createFixture(fdef).setUserData("crystal");

            Crystal c = new Crystal(body);
            crystals.add(c);
            body.setUserData(c);


        }

    }

}
