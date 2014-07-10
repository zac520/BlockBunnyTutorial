package com.NZGames.BlockBunny;

import java.util.Stack;

/**
 * Created by zac520 on 7/10/14.
 */
public class GameStateManager {

    private BlockBunnyGame game;

    private Stack<GameState> gameStates;
    public static final int PLAY = 394092;

    public GameStateManager (BlockBunnyGame game){
        this.game = game;
        gameStates = new Stack<GameState>();
        pushState(PLAY);
    }

    public BlockBunnyGame game(){
        return game;
    }

    public void update(float delta){
        gameStates.peek().update(delta);
    }

    public void render(){
        gameStates.peek().render();

    }

    private GameState getState(int state){
        if (state == PLAY){
            return new Play(this);
        }
        return null;
    }

    public void setState(int state){
        popState();
        pushState(state);
    }

    public void pushState(int state){
        gameStates.push(getState(state));
    }
    public void popState(){
        GameState g = gameStates.pop();
        g.dispose();
    }
}
