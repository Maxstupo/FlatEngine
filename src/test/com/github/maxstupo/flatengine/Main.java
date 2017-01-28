package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.hgui.GuiList;
import com.github.maxstupo.flatengine.hgui.GuiWindow;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.flatengine.util.math.Rand;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main extends AbstractScreen {

    public Main(ScreenManager screenManager) {
        super(screenManager);
        guiRoot.setBackgroundColor(Color.LIGHT_GRAY);

        GuiWindow window = new GuiWindow(this, "Inventory", 400, 100, 400, 400 / 16 * 9);
        window.setUsePercentagePositions(true);
        window.setKeepWithinParent(true);

        // GuiButton btn = new GuiButton(this, "Apple", 10, 20, 150, 50);
        // btn.getTextNode().setAlignment(Alignment.CENTER);
        // window.add(btn);

        list = new GuiList<>(this, 10, 10, 150, 200);
        list.addListener((executor, actionItem, action) -> {
            System.out.println(actionItem + ", " + action);
        });
        for (int i = 0; i < 5; i++)
            list.addItem("Item " + i);
        window.add(list);
        guiRoot.add(window);

    }

    GuiList<String> list;

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
        if (getKeyboard().isKeyDown(Keyboard.KEY_1)) {
            list.getDefaultItem().setOutlineColorSelected(Color.red);
            list.setDirty();
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_0)) {
            list.clear();
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_SPACE)) {
            list.addItem(Rand.INSTANCE.nextIntRange(0, 100) + "");
        }
    }

    @Override
    public void render(Graphics2D g) {

    }

}
