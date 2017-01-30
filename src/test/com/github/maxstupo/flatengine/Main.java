package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.hgui.GuiItemContainer;
import com.github.maxstupo.flatengine.hgui.GuiList;
import com.github.maxstupo.flatengine.hgui.GuiSelectionList;
import com.github.maxstupo.flatengine.hgui.GuiWindow;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.item.AbstractItemStack;
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

        list = new GuiSelectionList<>(this, 10, 10, 150, 205);
        list.addListener((executor, actionItem, action) -> {
            System.out.println(actionItem + ", " + action);

            executor.remove(actionItem);
        });

        for (int i = 0; i < 14; i++)
            list.addItem("Item " + i);
        window.add(list);

        try {
            screenManager.getEngine().getAssetManager().registerSprite("1", Util.createImage("/potion_mana.png"));
            screenManager.getEngine().getAssetManager().registerSprite("2", Util.createImage("/potion_mana2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        IS holding = new IS(0, 0);

        slot = new GuiItemContainer<>(this, 32, 32, 64, 5, get(), holding);
        guiRoot.add(slot);
        guiRoot.add(window);
    }

    GuiItemContainer<IS> slot;

    public IS[][] get() {
        IS[][] items = new IS[3][5];
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                items[i][j] = new IS(Rand.INSTANCE.nextIntRange(1, 2), Rand.INSTANCE.nextIntRange(0, 10));
            }
        }
        return items;
    }

    public static class IS extends AbstractItemStack {

        public IS(int id, int amt) {
            super(id, amt);
        }

        @Override
        public String getIconId() {
            return getId() + "";
        }

        @Override
        public String getName() {
            return "Item " + getId();
        }

        @Override
        public AbstractItemStack copy() {
            return new IS(getId(), getAmount());
        }

        @Override
        public int getMaxAmount() {
            return 40;
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
            // list.getDefaultItem().getTextNode().setAlignment(Alignment.MIDDLE_LEFT);
            // list.setDirty();
            slot.setEnabled(false);
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_C)) {
            // list.clear();
            slot.setSlotSize(slot.getSlotSize() - 1);
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_SPACE)) {
            // list.addItem("Item - " + Rand.INSTANCE.nextIntRange(0, 100) + "");
            slot.setContents(get());
        }
        if (getKeyboard().isKeyDown(Keyboard.KEY_S)) {
            slot.setSpacing(slot.getSpacing() + 1);
        }
    }

    @Override
    public void render(Graphics2D g) {

    }

}
