package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.flatengine.map.TiledMap;
import com.github.maxstupo.flatengine.map.reader.TiledMapReader;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.BasicShape;
import com.github.maxstupo.flatengine.util.math.Circle;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main extends AbstractScreen {

    Camera camera = new Camera(32);
    TiledMap map;

    public Main(ScreenManager screenManager) {
        super(screenManager);
        guiRoot.setBackgroundColor(Color.LIGHT_GRAY);

        // map = new TiledMap("apple", "Test map", 50, 50);

        try {
            map = TiledMapReader.get().load("asiod", "010-1.tmx");

            //// Tileset ts = new Tileset(0, "ts1", 32, 32, Util.createImage("/Tileset_Woodland.png"));
            // map.getTilesetStore().addTileset(ts, false);

            // Tileset ts1 = new Tileset(42, "apple", 32, 32, Util.createImage("/Tileset_Woodland.png"));
            // map.getTilesetStore().addTileset(ts1, false);

            // map.getTilesetStore().recacheTiles();

            // TileLayer layer = new TileLayer(map, "Background", 1);
            // layer.fillRandom(32, 32, 32, 32, 6, 32, 13, 32, 32, 32, 32, 32, 32, 32, 20);

            // map.addBackgroundLayer(layer);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        JFlatLog.get().setLogLevel(JFlatLog.LEVEL_FINE);

        BasicGameloop loop = new BasicGameloop(60);

        FlatEngine engine = new FlatEngine(loop, JFlatLog.get());

        engine.registerScreen("test", Main.class);

        engine.createWindow("Test game", 800, (int) (800f / 16f * 9f), true, FlatEngine.EXIT_ON_CLOSE);
        engine.start();

        engine.switchTo("test");

    }

    @Override
    public void update(float delta) {
        camera.setViewport(screenManager.getEngine());
        camera.setTileSize(32);

        x += vx * delta;
        y += vy * delta;

        x = UtilMath.clampF(x, 0, map.getWidth());
        y = UtilMath.clampF(y, 0, map.getHeight());

        camera.targetPositionUsingLerp(x, y, delta);
        camera.clamp(map.getWidth(), map.getHeight());

        camera.setSmoothing(0.01f);

        float speed = 2;
        if (getKeyboard().isKeyHeld(Keyboard.KEY_W)) {
            vy = -speed;
        } else if (getKeyboard().isKeyHeld(Keyboard.KEY_S)) {
            vy = speed;
        } else {
            vy = 0;
        }

        if (getKeyboard().isKeyHeld(Keyboard.KEY_A)) {
            vx = -speed;
        } else if (getKeyboard().isKeyHeld(Keyboard.KEY_D)) {
            vx = speed;
        } else {
            vx = 0;
        }
    }

    float x = 38, y = 90;
    float vx, vy;

    @Override
    public void render(Graphics2D g) {
        map.render(g, camera);

        BasicShape r = new Circle(45, 92, 1f);
        BasicShape playerShape = new Circle(x, y, 0.5f);

        boolean c = r.contains(playerShape);

        UtilGraphics.drawShape(g, camera, r, c ? Color.RED : Color.BLUE);
        UtilGraphics.drawShape(g, camera, playerShape, Color.cyan);

    }

}
