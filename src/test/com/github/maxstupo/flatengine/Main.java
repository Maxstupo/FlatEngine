package test.com.github.maxstupo.flatengine;

import java.awt.Color;
import java.awt.Graphics2D;

import com.github.maxstupo.flatengine.FlatEngine;
import com.github.maxstupo.flatengine.gameloop.BasicGameloop;
import com.github.maxstupo.flatengine.gui.AlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.gui.GuiButton;
import com.github.maxstupo.flatengine.gui.GuiItemContainer;
import com.github.maxstupo.flatengine.gui.GuiList;
import com.github.maxstupo.flatengine.gui.GuiText;
import com.github.maxstupo.flatengine.gui.GuiWindow;
import com.github.maxstupo.flatengine.gui.IListItem;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.item.IItemStack;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.screen.ScreenManager;
import com.github.maxstupo.flatengine.util.math.Rand;
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

        window = new GuiWindow(this, "Inventory", new Vector2i(350, 50), new Vector2i(150, 300));
        window.getTitle().setAlignment(Alignment.CENTER);
        window.setBackgroundColor(Color.lightGray);
        gui.addChild(window);

        int id = 1;
        ItemStack holding = new ItemStack(0, 0);

        ItemStack[][] items = new ItemStack[3][7];

        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                items[i][j] = new ItemStack(id++, Rand.instance.nextIntRange(0, 5));
            }
        }

        newItems = new ItemStack[3][3];
        for (int i = 0; i < newItems.length; i++) {
            for (int j = 0; j < newItems[0].length; j++) {
                newItems[i][j] = new ItemStack(id++, Rand.instance.nextIntRange(0, 5));
            }
        }

        container = new GuiItemContainer<>(this, new Vector2i(10, 10), 32, 5, items, holding);
        // container.getDefaultSlot().setOutlineColor(Color.red);
        window.addChild(container);
    }

    GuiItemContainer<ItemStack> container;
    ItemStack[][] newItems;

    public static class ItemStack implements IItemStack {

        private int id;
        private int amt;

        /**
         * 
         */
        public ItemStack(int id, int amt) {
            this.id = id;// TODO Auto-generated constructor stub
            this.amt = amt;
        }

        @Override
        public int getAmount() {
            return amt;
        }

        @Override
        public String getIconId() {
            return "item";
        }

        @Override
        public boolean isEmpty() {
            return getAmount() <= 0;
        }

        @Override
        public String getName() {
            return "Item " + (id);
        }

        @Override
        public IItemStack set(IItemStack stack) {
            this.id = stack.getId();
            this.amt = stack.getAmount();
            return this;
        }

        @Override
        public IItemStack set(int id, int amt) {
            this.id = id;
            this.amt = amt;
            return this;
        }

        @Override
        public int add(IItemStack stack) {
            if (isEmpty()) {
                set(stack);
                return 0;
            }
            if (!areItemStacksEqual(stack))
                return stack.getAmount();

            int maxCount = getMaxAmount();
            if ((getAmount() + stack.getAmount()) <= maxCount) {
                this.amt += stack.getAmount();
                return 0;
            } else {
                int leftOver = stack.getAmount() - (maxCount - getAmount());
                this.amt = maxCount;
                return leftOver;
            }
        }

        @Override
        public boolean decrease(int amt) {
            setAmount(getAmount() - amt);
            return isEmpty();
        }

        @Override
        public boolean increase(int amt) {
            int a = getAmount() + amt;
            if (a > getMaxAmount()) {
                setAmount(getMaxAmount());
                return true;
            } else {
                setAmount(a);
                return false;
            }
        }

        @Override
        public IItemStack setEmpty() {
            this.amt = 0;
            return this;
        }

        @Override
        public IItemStack setAmount(int amt) {
            this.amt = amt;
            return this;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getMaxAmount() {
            return 100;
        }

        @Override
        public boolean areItemStacksEqual(IItemStack stack) {
            return stack.getId() == getId();
        }

        @Override
        public ItemStack copy() {
            ItemStack stack = new ItemStack(getId(), getAmount());

            return stack;
        }

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
            // window.setVisible(!window.isVisible());
            // list.setVisible(true);
            container.setContents(newItems);
        }
        if (screenManager.getEngine().getKeyboard().isKeyDown(Keyboard.KEY_I)) {
            window.setVisible(!window.isVisible());
        }
    }

    @Override
    public void render(Graphics2D g) {
    }

}
