package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;

/**
 * This GUI node is a button it will trigger all listeners when a mouse button is pressed.
 * 
 * @author Maxstupo
 */
public class GuiButton extends GuiContainer {

    private final GuiText text;

    /** The text color when this button is selected. */
    protected Color textColorSelected = Color.WHITE;
    /** The text color when this button is unselected. */
    protected Color textColorUnselected = Color.WHITE;
    /** The outline color when this button is selected. */
    protected Color outlineColorSelected = Color.BLACK;
    /** The outline color when this button is unselected. */
    protected Color outlineColorUnselected = Color.BLACK;
    /** The background color when this button is selected. */
    protected Color backgroundColorSelected = UtilGraphics.changeAlpha(Color.DARK_GRAY, 127);
    /** The background color when this button is unselected. */
    protected Color backgroundColorUnselected = Color.DARK_GRAY;

    /** If true no background or outline will be drawn. */
    protected boolean boxLess;

    /** The mouse button codes that will trigger this button. (Default: Left Mouse Button) */
    protected int[] buttonsMonitored = { Mouse.LEFT_CLICK };

    private final List<IEventListener<GuiButton, Boolean, Integer>> listeners = new ArrayList<>();
    private Object userdata;
    private boolean isMouseOver;

    /**
     * Create a new {@link GuiButton} object.
     * 
     * @param screen
     *            the screen that owns this node.
     * @param text
     *            the button text.
     * @param localX
     *            the local x position.
     * @param localY
     *            the local y position.
     * @param width
     *            the width of this node.
     * @param height
     *            the height of this node.
     */
    public GuiButton(AbstractScreen screen, String text, int localX, int localY, int width, int height) {
        super(screen, localX, localY, width, height);
        this.text = new GuiText(screen, Alignment.CENTER, text);
        this.text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.add(this.text);
    }

    /**
     * Returns the user data. Commonly used within the event listener.
     * 
     * @return the user data object.
     */
    public Object getUserData() {
        return userdata;
    }

    /**
     * Sets the user data. Commonly used within the event listener.
     * 
     * @param userdata
     *            the user data object.
     * @return this object for chaining.
     */
    public GuiButton setUserData(Object userdata) {
        this.userdata = userdata;
        return this;
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {

        if (shouldHandleInput) {
            doInputLogic();
            isMouseOver = isMouseOver();
        } else {
            isMouseOver = false;
        }

        return shouldHandleInput && !isMouseOver();
    }

    @Override
    protected void render(Graphics2D g) {
        text.setColor(isMouseOver ? getTextColorSelected() : getTextColorUnselected());

        if (!isBoxLess()) {
            super.setBackgroundColor(isMouseOver ? getBackgroundColorSelected() : getBackgroundColorUnselected());
            super.setOutlineColor(isMouseOver ? getOutlineColorSelected() : getOutlineColorUnselected());
        } else {
            super.setBackgroundColor(null);
            super.setOutlineColor(null);
        }

        super.render(g);
    }

    /**
     * Checks if any mouse buttons that are being monitored have been clicked.
     */
    protected void doInputLogic() {
        if (buttonsMonitored != null) {
            for (int i = 0; i < buttonsMonitored.length; i++) {
                int code = buttonsMonitored[i];

                if (isMouseClicked(code)) {
                    fireEventListeners(code, true);
                } else if (isMouseUp(code)) {
                    fireEventListeners(code, false);
                }
            }
        }
    }

    /**
     * Triggers all {@link IEventListener listeners} for this button.
     * 
     * @param mouseCode
     *            the mouse button code that triggered this event.
     * @param isPressedDown
     *            true if the event was caused by a mouse being pressed down.
     */
    protected void fireEventListeners(int mouseCode, boolean isPressedDown) {
        for (IEventListener<GuiButton, Boolean, Integer> listener : listeners)
            listener.onEvent(this, isPressedDown, mouseCode);
    }

    /**
     * Adds the given listener to this button.
     * 
     * @param listener
     *            the listener to add.
     * @return this object for chaining.
     */
    public GuiButton addListener(IEventListener<GuiButton, Boolean, Integer> listener) {
        if (listener != null)
            listeners.add(listener);
        return this;
    }

    /**
     * Returns the mouse button codes that will trigger this {@link GuiButton}.
     * 
     * @return the mouse button codes that will trigger this {@link GuiButton}.
     */
    public int[] getButtonsMonitored() {
        return buttonsMonitored;
    }

    /**
     * Sets the mouse button codes that will trigger this {@link GuiButton}.
     * 
     * @param buttonsMonitored
     *            the mouse button codes.
     * @return this object for chaining.
     */
    public GuiButton setButtonsMonitored(int[] buttonsMonitored) {
        this.buttonsMonitored = buttonsMonitored;
        return this;
    }

    /**
     * Returns the text color when this button is selected.
     * 
     * @return the text color when this button is selected.
     */
    public Color getTextColorSelected() {
        return textColorSelected;
    }

    /**
     * Sets the text color of this button when selected.
     * 
     * @param textColorSelected
     *            the text color of the button when selected.
     * @return this object for chaining.
     */
    public GuiButton setTextColorSelected(Color textColorSelected) {
        this.textColorSelected = textColorSelected;
        return this;
    }

    /**
     * Returns the text color when this button is unselected.
     * 
     * @return the text color when this button is unselected.
     */
    public Color getTextColorUnselected() {
        return textColorUnselected;
    }

    /**
     * Sets the text color of this button when unselected.
     * 
     * @param textColorUnselected
     *            the text color of the button when unselected.
     * @return this object for chaining.
     */
    public GuiButton setTextColorUnselected(Color textColorUnselected) {
        this.textColorUnselected = textColorUnselected;
        return this;
    }

    /**
     * Returns the outline color when this button is selected.
     * 
     * @return the outline color when this button is selected.
     */
    public Color getOutlineColorSelected() {
        return outlineColorSelected;
    }

    /**
     * Sets the outline color of this button when selected.
     * 
     * @param outlineColorSelected
     *            the outline color of the button when selected.
     * @return this object for chaining.
     */
    public GuiButton setOutlineColorSelected(Color outlineColorSelected) {
        this.outlineColorSelected = outlineColorSelected;
        return this;
    }

    /**
     * Returns the outline color when this button is unselected.
     * 
     * @return the outline color when this button is unselected.
     */
    public Color getOutlineColorUnselected() {
        return outlineColorUnselected;
    }

    /**
     * Sets the outline color of this button when unselected.
     * 
     * @param outlineColorUnselected
     *            the outline color of the button when unselected.
     * @return this object for chaining.
     */
    public GuiButton setOutlineColorUnselected(Color outlineColorUnselected) {
        this.outlineColorUnselected = outlineColorUnselected;
        return this;
    }

    /**
     * Returns the background color when this button is selected.
     * 
     * @return the background color when this button is selected.
     */
    public Color getBackgroundColorSelected() {
        return backgroundColorSelected;
    }

    /**
     * Sets the background color of this button when selected.
     * 
     * @param backgroundColorSelected
     *            the background color of the button when selected.
     * @return this object for chaining.
     */
    public GuiButton setBackgroundColorSelected(Color backgroundColorSelected) {
        this.backgroundColorSelected = backgroundColorSelected;
        return this;
    }

    /**
     * Returns the background color when this button is unselected.
     * 
     * @return the background color when this button is unselected.
     */
    public Color getBackgroundColorUnselected() {
        return backgroundColorUnselected;
    }

    /**
     * Sets the background color of this button when unselected.
     * 
     * @param backgroundColorUnselected
     *            the background color of the button when unselected.
     * @return this object for chaining.
     */
    public GuiButton setBackgroundColorUnselected(Color backgroundColorUnselected) {
        this.backgroundColorUnselected = backgroundColorUnselected;
        return this;
    }

    /**
     * Returns true if no background or outline will be drawn.
     * 
     * @return true if no background or outline will be drawn.
     */
    public boolean isBoxLess() {
        return boxLess;
    }

    /**
     * Set true to remove the background and outline.
     * 
     * @param boxLess
     *            true for no background or outline.
     * @return this object for chaining.
     */
    public GuiButton setBoxLess(boolean boxLess) {
        this.boxLess = boxLess;
        return this;
    }

    /**
     * Returns an unmodifiable list of listeners.
     * 
     * @see Collections#unmodifiableList(List)
     * @return an unmodifiable list of listeners.
     */
    public List<IEventListener<GuiButton, Boolean, Integer>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Returns the text node of this button.
     * 
     * @return the text node of this button.
     */
    public GuiText getTextNode() {
        return text;
    }

}
