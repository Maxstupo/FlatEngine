package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;

/**
 * This GUI node is a list that has a built-in scrollbar.
 * 
 * @author Maxstupo
 * @param <T>
 *            the entry type stored within this list, the type can implement {@link IListItem} if {@link Object#toString() toString()} is needed for
 *            something else.
 *
 */
public class GuiList<T> extends GuiContainer implements IEventListener<GuiButton, String, Integer> {

    private final List<T> items = new ArrayList<>();
    private final List<GuiButton> itemNodes = new ArrayList<>();
    private final GuiButton defaultItem;

    private boolean isItemNodesDirty;
    private boolean isScrollDirty;

    private int spacing = 2;
    private int itemHeight = -1;
    private int scroll;
    private int calculatedItemHeight;

    private List<IEventListener<GuiList<T>, T, Integer>> listeners = new ArrayList<>();

    /**
     * Create a new {@link GuiList} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param width
     *            the width of this node.
     * @param height
     *            the height of this node.
     */
    public GuiList(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);

        this.defaultItem = new GuiButton(null, "", 0, 0, 0, 0);
        this.defaultItem.getTextNode().setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        this.defaultItem.setOutlineColorUnselected(null);
        this.defaultItem.setBackgroundColorSelected(Color.GRAY);

        setBackgroundColor(UtilGraphics.changeAlpha(Color.BLACK, 127));
        setOutlineColor(Color.BLACK);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {

        if (isItemNodesDirty)
            rebuildItemNodes();

        if (isScrollDirty)
            updateScroll();

        if (shouldHandleInput)
            doInputLogic();

        return shouldHandleInput && !isMouseOver();
    }

    /**
     * Handle input for scrolling of the list.
     */
    protected void doInputLogic() {
        if (isMouseOver() && getMouse().didMouseWheelMove()) {
            int dir = getMouse().getWheelRotation();

            if (dir < 0) {
                scrollUp();
            } else if (dir > 0) {
                scrollDown();
            }
        }
    }

    /**
     * Scroll up the list, stop when reached the top.
     * 
     * @return true if the list did scroll up.
     */
    public boolean scrollUp() {
        if (canScrollUp()) {
            scroll--;
            isScrollDirty = true;
            return true;
        }
        return false;
    }

    /**
     * Scroll down the list, stop when reached the bottom.
     * 
     * @return true if the list did scroll down.
     */
    public boolean scrollDown() {
        if (canScrollDown()) {
            scroll++;
            isScrollDirty = true;
            return true;
        }
        return false;
    }

    /**
     * Returns true if the list can scroll up.
     * 
     * @return true if the list can scroll up.
     */
    public boolean canScrollUp() {
        return scroll > 0;
    }

    /**
     * Returns true if the list can scroll down.
     * 
     * @return true if the list can scroll down.
     */
    public boolean canScrollDown() {
        return (items.size() - getItemsVisible()) - scroll > 0;
    }

    /**
     * Updates the {@link GuiButton} list items with the correct names.
     */
    protected void updateScroll() {
        for (int i = 0; i < Math.min(items.size(), getItemsVisible()); i++) {
            GuiButton btn = itemNodes.get(i);

            int newScroll = i + scroll;

            T t = items.get(newScroll);
            btn.getTextNode().setText(getItemText(t));
            btn.setUserData(new Object[] { t, newScroll });
        }
        isScrollDirty = false;
    }

    @Override
    protected void renderFirst(Graphics2D g) {
        super.renderFirst(g);

        int maxHeight = -1;
        for (T t : items) {
            int height = UtilGraphics.getStringBounds(g, getItemText(t)).height;
            maxHeight = Math.max(maxHeight, height);
        }
        calculatedItemHeight = maxHeight;
        isItemNodesDirty = true;
    }

    /**
     * Returns the correct string for the given item. If the item implements {@link IListItem} the method will return {@link IListItem#getItemName()}
     * instead of {@link Object#toString()}.
     * 
     * @param t
     *            the object to get the correct text from.
     * @return the correct text.
     */
    protected String getItemText(T t) {
        if (t instanceof IListItem) {
            return ((IListItem) t).getItemName();
        } else {
            return t.toString();
        }
    }

    /**
     * Removes all item GUI nodes from this list node, and re-adds them from the master list.
     */
    protected void rebuildItemNodes() {
        for (GuiButton b : itemNodes)
            remove(b);
        itemNodes.clear();

        int j = 0;
        int height = (itemHeight == -1) ? calculatedItemHeight : itemHeight;
        for (int i = 0; i < Math.min(items.size(), getItemsVisible()); i++) {

            GuiButton btn = new GuiButton(screen, "<template>", getSpacing(), j * (height + getSpacing()) + getSpacing(), getWidth() - getSpacing() * 2, height) {

                @Override
                protected boolean update(float delta, boolean shouldHandleInput) {
                    super.update(delta, shouldHandleInput);
                    return true;// return true so we can scroll
                }
            };
            btn.getTextNode().setFont(defaultItem.getTextNode().getFont());
            btn.getTextNode().setAlignment(defaultItem.getTextNode().getAlignment());
            btn.setOutlineColorUnselected(defaultItem.getOutlineColorUnselected());
            btn.setOutlineColorSelected(defaultItem.getOutlineColorSelected());
            btn.setBackgroundColorSelected(defaultItem.getBackgroundColorSelected());
            btn.setBackgroundColorUnselected(defaultItem.getBackgroundColorUnselected());
            btn.setTextColorSelected(defaultItem.getTextColorSelected());
            btn.setTextColorUnselected(defaultItem.getTextColorUnselected());
            btn.setBoxLess(defaultItem.isBoxLess());

            btn.addListener(this);
            itemNodes.add(btn);
            add(btn);

            j++;
        }

        isItemNodesDirty = false;
        isScrollDirty = true;
    }

    @Override
    public AbstractNode setSize(int width, int height) {
        super.setSize(width, height);
        isItemNodesDirty = true;
        return this;
    }

    /**
     * Sets the height of the list based on the number of rows given.
     * 
     * @param rows
     *            the rows of items.
     * @return this object for chaining.
     */
    public GuiList<T> setHeightByRows(int rows) {
        int height = (getItemHeight() == -1) ? getCalculatedItemHeight() : getItemHeight();
        setHeight(rows * height);

        isItemNodesDirty = true;
        return this;
    }

    /**
     * Returns the number of items that can fit within the list.
     * 
     * @return the number of items that can fit within the list.
     */
    public int getItemsVisible() {
        int height = (getItemHeight() == -1) ? getCalculatedItemHeight() : getItemHeight();
        if (height == 0)
            return 0;
        return (getHeight() / height) - 1;
    }

    /**
     * Sets the spacing between each item.
     * 
     * @param spacing
     *            the spacing between each item.
     * @return this object for chaining.
     */
    public GuiList<T> setSpacing(int spacing) {
        this.spacing = spacing;
        isItemNodesDirty = true;
        return this;
    }

    /**
     * Clears this list of items.
     * 
     * @return this object for chaining.
     */
    public GuiList<T> clear() {
        items.clear();
        isItemNodesDirty = true;
        return this;
    }

    /**
     * Adds an event listener for this list.
     * 
     * @param listener
     *            the listener.
     * @return this object for chaining.
     */
    public GuiList<T> addListener(IEventListener<GuiList<T>, T, Integer> listener) {
        this.listeners.add(listener);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onEvent(GuiButton executor, String actionItem, Integer action) {
        Object[] arr = (Object[]) executor.getUserData();

        for (IEventListener<GuiList<T>, T, Integer> listener : listeners) {
            listener.onEvent(this, (T) arr[0], (int) arr[1]);
        }

    }

    /**
     * Requests that the {@link GuiButton} items be rebuilt via {@link #rebuildItemNodes()}.
     * 
     * @return this object for chaining.
     */
    public GuiList<T> setDirty() {
        if (itemHeight == -1) {
            setGraphicsCalculationsDirty();
        } else {
            isItemNodesDirty = true;
        }
        return this;
    }

    /**
     * Sets the item height of each item in the list. Set to -1 to use a calculated item height.
     * 
     * @param itemHeight
     *            the item height, or -1 for auto.
     * @return this object for chaining.
     */
    public GuiList<T> setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
        setDirty();
        return this;
    }

    /**
     * Adds a new item to the list.
     * 
     * @param item
     *            the new item.
     * @return this object for chaining.
     */
    public GuiList<T> addItem(T item) {
        items.add(item);
        setDirty();
        return this;
    }

    /**
     * Returns the default button item used for all items within this list.
     * <p>
     * If a modification was made to this button call {@link #setDirty()}.
     * 
     * @return the default button item used for all items within this list.
     */
    public GuiButton getDefaultItem() {
        return defaultItem;
    }

    /**
     * Returns the spacing between each item.
     * 
     * @return the spacing between each item.
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * Returns the user set item height.
     * 
     * @return the user set item height.
     */
    public int getItemHeight() {
        return itemHeight;
    }

    /**
     * Returns the calculated item height, only if {@link #getItemHeight()} is set to -1.
     * 
     * @return the calculated item height, only if {@link #getItemHeight()} is set to -1.
     */
    public int getCalculatedItemHeight() {
        return calculatedItemHeight;
    }

    /**
     * Returns an unmodifiable list of listeners.
     * 
     * @see Collections#unmodifiableList(List)
     * @return an unmodifiable list of listeners.
     */
    public List<IEventListener<GuiList<T>, T, Integer>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

}
