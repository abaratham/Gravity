package anandgames.gravity.desktop;

import anandgames.gravity.Gravity;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.width = 1280;
		cfg.height = 720;
		cfg.fullscreen = true;
		cfg.vSyncEnabled = true;
		new LwjglApplication(new Gravity(Gravity.DESKTOP), cfg);
	}
}
