package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main extends AbstractScreen {

    public Main(FlatEngine engine, String key) {
        super(engine, key);
    }

    public static void main(String[] args) {

        BasicGameloop loop = new BasicGameloop(60);

        FlatEngine engine = new FlatEngine(loop, JFlatLog.get());

        engine.registerScreen(new Main(engine, "test"));
        engine.switchTo("test");

        engine.createWindow("Test game", 800, 600, true, FlatEngine.EXIT_ON_CLOSE);
        engine.start();

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.red);
        g.fillRect(50, 50, 100, 100);
    }
}
