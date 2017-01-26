package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.gui.AlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiList;
import com.github.maxstupo.flatengine.gui.GuiText;
import com.github.maxstupo.flatengine.gui.GuiWindow;
import com.github.maxstupo.flatengine.gui.IListItem;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.flatengine.util.math.Vector2i;
import com.github.maxstupo.jflatlog.JFlatLog;

/**
 * @author Maxstupo
 *
 */
public class Main extends AbstractScreen {

    public static class Item implements IListItem {

        private int i = 0;

        public Item(int i) {
            this.i = i;
        }

        @Override
        public String getListItemText() {
            return "Item " + i;
        }

    }

    GuiWindow window;
    GuiList<Item> list;

    public Main(ScreenManager screenManager) {
        super(screenManager);

        GuiButton btn = new GuiButton(this, "Click me", new Vector2i(25, 270), new Vector2i(300, 100));

        gui.addChild(btn);

        GuiText text = new GuiText(this, new Vector2i(450, 50), "HELLO WORLD");

        gui.addChild(text);

        window = new GuiWindow(this, "Inventory", new Vector2i(350, 50), new Vector2i(170, 200));
        window.getTitle().setAlignment(Alignment.CENTER);
        window.setBackgroundColor(Color.lightGray);
        gui.addChild(window);

        list = new GuiList<>(this, new Vector2i(20, 10), new Vector2i(130, 170));
        for (int i = 0; i < 20; i++)
            list.addEntry(new Item(i));
        window.addChild(list);

    }

    public static void main(String[] args) {
        JFlatLog.get().setLogLevel(JFlatLog.LEVEL_FINE);

        BasicGameloop loop = new BasicGameloop(60);

        FlatEngine engine = new FlatEngine(loop, JFlatLog.get());

        engine.registerScreen("test", Main.class);
        engine.switchTo("test");

        engine.createWindow("Test game", 800, (int) (800f / 16f * 9f), true, FlatEngine.EXIT_ON_CLOSE);
        engine.start();

    }

    @Override
    public void update(double delta) {
        if (screenManager.getEngine().getKeyboard().isKeyDown(Keyboard.KEY_SPACE)) {
            window.setVisible(!window.isVisible());
            // list.setVisible(true);
        }
    }

    @Override
    public void render(Graphics2D g) {
    }

}
