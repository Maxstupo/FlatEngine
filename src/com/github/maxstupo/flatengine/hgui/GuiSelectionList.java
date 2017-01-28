package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;

import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.UtilMath;

/**
 * This GUI node is a list that has a built-in scrollbar and will retain a selected item upon clicking it.
 * 
 * @author Maxstupo
 * @param <T>
 *            the entry type stored within this list, the type can implement {@link IListItem} if {@link Object#toString() toString()} is needed for
 *            something else.
 *
 */
public class GuiSelectionList<T> extends GuiList<T> {

    private int selected = -1;
    /** The outline color used for the selected item. */
    protected Color selectedColor = Color.WHITE;

    /**
     * Create a new {@link GuiSelectionList} object.
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
    public GuiSelectionList(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        addListener((executor, actionItem, action) -> {
            selected = action;
            executor.setScrollDirty();
        });
    }

    @Override
    protected void updateItem(GuiButton btn, T t, int index) {
        super.updateItem(btn, t, index);

        if (selected == index) {
            btn.setOutlineColorSelected(selectedColor);
            btn.setOutlineColorUnselected(selectedColor);
        } else {
            btn.setOutlineColorSelected(getDefaultItem().getOutlineColorSelected());
            btn.setOutlineColorUnselected(getDefaultItem().getOutlineColorUnselected());
        }
    }

    @Override
    public GuiList<T> remove(int index) {
        if (index == selected)
            unselect();
        return super.remove(index);
    }

    @Override
    public GuiList<T> remove(T t) {
        if (getItems().indexOf(t) == selected)
            unselect();
        return super.remove(t);
    }

    @Override
    public GuiList<T> clear() {
        unselect();
        return super.clear();
    }

    /**
     * Deselects the currently selected item in the list.
     * 
     * @return this object for chaining.
     */
    public GuiSelectionList<T> unselect() {
        selected = -1;
        return this;
    }

    /**
     * Returns the outline color for the selected item.
     * 
     * @return the outline color for the selected item.
     */
    public Color getSelectedColor() {
        return selectedColor;
    }

    /**
     * Sets the outline color for the selected item.
     * 
     * @param selectedColor
     *            the outline color for the selected item.
     * @return this object for chaining.
     */
    public GuiSelectionList<T> setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        return this;
    }

    /**
     * Sets the currently selected item, if given value is out of bounds it is clamped.
     * 
     * @param index
     *            the index of the new selected item.
     * @return this object for chaining.
     */
    public GuiSelectionList<T> setSelection(int index) {
        selected = UtilMath.clampI(index, 0, getTotalItems() - 1);
        return this;
    }

    /**
     * Returns true if an item is selected.
     * 
     * @return true if an item is selected.
     */
    public boolean hasSelected() {
        return selected != -1;
    }

    /**
     * Returns the index of the item selected.
     * 
     * @return the index of the item selected.
     */
    public int getSelected() {
        return selected;
    }

}
