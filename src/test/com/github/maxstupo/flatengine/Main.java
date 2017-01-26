package test.com.github.maxstupo.flatengine;

import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main extends AbstractScreen {

    public Main(ScreenManager screenManager) {
        super(screenManager);

        GuiButton btn = new GuiButton(this, "Hello World", new Vector2i(50, 50), new Vector2i(300, 150));

        gui.addChild(btn);

    }

    public static void main(String[] args) {

        BasicGameloop loop = new BasicGameloop(60);

        FlatEngine engine = new FlatEngine(loop, JFlatLog.get());

        engine.registerScreen("test", Main.class);
        engine.switchTo("test");

        engine.createWindow("Test game", 800, 600, true, FlatEngine.EXIT_ON_CLOSE);
        engine.start();

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render(Graphics2D g) {
    }
}
