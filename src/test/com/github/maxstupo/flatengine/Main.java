package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.hgui.AlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.hgui.GuiButton;
import com.github.maxstupo.flatengine.hgui.GuiContainer;
import com.github.maxstupo.flatengine.hgui.GuiWindow;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main extends AbstractScreen {

    public Main(ScreenManager screenManager) {
        super(screenManager);
        guiRoot.setBackgroundColor(Color.LIGHT_GRAY);

        GuiContainer container = new GuiContainer(this, .5f, 100, 500, 300);
        container.setUsePercentagePositions(true);
        guiRoot.add(container);

        GuiWindow window = new GuiWindow(this, "Inventory", 400, 100, 400, 400 / 16 * 9);
        window.getTitle().setAlignment(Alignment.CENTER);
        window.setUsePercentagePositions(true);
        window.setKeepWithinParent(true);

        // GuiButton btn = new GuiButton(this, "Apple", -10, 20, 50, 20);
        // window.add(btn);
        guiRoot.add(window);

    }

    GuiButton btn;

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
    public void update(double delta) {
        if (screenManager.getEngine().getKeyboard().isKeyDown(Keyboard.KEY_SPACE)) {

        }

        screenManager.getEngine().setTitle(screenManager.getEngine().getGameloop().getFPS() + "");

    }

    @Override
    public void render(Graphics2D g) {
    }

}
