package test.com.github.maxstupo.flatengine;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.map.Camera;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main {

    public final Camera camera = new Camera(32);
    public Level level;

    private Main() {
        // try {
        // // level = new Level("d", new File("testmap/010-1.tmx"));
        // } catch (RuntimeException | SAXException | IOException | ParserConfigurationException e) {
        // e.printStackTrace();
        // }
    }

    public static void main(String[] args) {
        JFlatLog.get().setLogLevel(JFlatLog.LEVEL_FINE);

        BasicGameloop loop = new BasicGameloop(60);

        FlatEngine engine = new FlatEngine(loop, JFlatLog.get());

        engine.registerScreen("test", Ingame.class);

        engine.createWindow("Test game", 800, (int) (800f / 16f * 9f), true, FlatEngine.EXIT_ON_CLOSE);
        engine.start();

        Main.get();
        engine.switchTo("test");
    }

    private static Main instance;

    public static Main get() {
        if (instance == null)
            instance = new Main();
        return instance;
    }

}
