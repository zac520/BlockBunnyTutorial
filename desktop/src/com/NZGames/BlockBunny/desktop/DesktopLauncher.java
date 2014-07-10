package com.NZGames.BlockBunny.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.NZGames.BlockBunny.BlockBunnyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title = BlockBunnyGame.TITLE;
        config.width = BlockBunnyGame.V_WIDTH * BlockBunnyGame.SCALE;
        config.height = BlockBunnyGame.V_HEIGHT * BlockBunnyGame.SCALE;

        new LwjglApplication(new BlockBunnyGame(), config);

    }
}
