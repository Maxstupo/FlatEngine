package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.hgui.GuiItemContainer;
import com.github.maxstupo.flatengine.hgui.GuiList;
import com.github.maxstupo.flatengine.hgui.GuiSelectionList;
import com.github.maxstupo.flatengine.hgui.GuiWindow;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.item.IItemStack;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.flatengine.util.Util;
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

        list = new GuiSelectionList<>(this, 10, 10, 150, 200);
        list.addListener((executor, actionItem, action) -> {
            System.out.println(actionItem + ", " + action);

            executor.remove(actionItem);
        });
        for (int i = 0; i < 15; i++)
            list.addItem("Item " + i);
        window.add(list);
        guiRoot.add(window);

        // bar.addListener((s, f, b) -> {
        // System.out.println(f + ", " + b);
        // });

        try {
            screenManager.getEngine().getAssetManager().registerSprite("1", Util.createImage("/potion_mana.png"));
            screenManager.getEngine().getAssetManager().registerSprite("2", Util.createImage("/potion_mana2.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        IS holding = new IS(0, 0);
        IS[][] items = new IS[4][5];
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                items[i][j] = new IS(Rand.INSTANCE.nextIntRange(1, 2), Rand.INSTANCE.nextIntRange(0, 10));
            }
        }
        GuiItemContainer<IS> slot = new GuiItemContainer<>(this, 32, 32, 64, 5, items, holding);
        guiRoot.add(slot);
    }

    public static class IS implements IItemStack {

        private int id;
        private int amt;

        public IS(int id, int amt) {
            this.id = id;
            this.amt = amt;
        }

        @Override
        public int getAmount() {
            return amt;
        }

        @Override
        public String getIconId() {
            return id + "";
        }

        @Override
        public String getName() {
            return "Item " + id;
        }

        @Override
        public boolean isEmpty() {
            return getAmount() <= 0;
        }

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
            list.getDefaultItem().getTextNode().setAlignment(Alignment.MIDDLE_LEFT);
            list.setDirty();
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_C)) {
            list.clear();
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_SPACE)) {
            list.addItem("Item - " + Rand.INSTANCE.nextIntRange(0, 100) + "");

        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_S)) {

        }
    }

    @Override
    public void render(Graphics2D g) {

    }

}
