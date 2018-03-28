package com.github.maxstupo.flatengine.hgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * This GUI node represents a slider knob.
 * 
 * @author Maxstupo
 */
public class GuiSlider extends GuiContainer implements IEventListener<GuiButton, Boolean, Integer> {

    private final GuiButton knob;

    private int spacing = 2;
    private boolean isVertical;
    private boolean isDragging;

    private int clickOrigin;

    private float value;
    private float oldValue;
    private float maxValue = 100;

    private Color backgroundColorUnselected;
    private Color outlineColorUnselected;

    private boolean isSliderDirty;

    private final List<IEventListener<GuiSlider, Float, Boolean>> listeners = new ArrayList<>();
    /** If true dragging the slider will do nothing. */
    protected boolean isDraggingDisabled;

    /**
     * Create a new {@link GuiSlider} object.
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
    public GuiSlider(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        this.knob = new GuiButton(screen, null, spacing, spacing, height - spacing * 2, Math.min(width, height) - spacing * 2) {

            @Override
            protected boolean update(float delta, boolean shouldHandleInput) {
                super.update(delta, shouldHandleInput);
                return shouldHandleInput;
            }
        };
        knob.addListener(this);
        add(knob);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {

        if (isDragging && shouldHandleInput && !isDraggingDisabled()) {
            knob.setBackgroundColorUnselected(knob.getBackgroundColorSelected());
            knob.setOutlineColorUnselected(knob.getOutlineColorSelected());

            if (getMouse().hasMouseMoved()) {
                Vector2i mpos = getMouse().getPosition();
                Vector2i gpos = getGlobalPosition();

                oldValue = getValue();

                if (!isVertical()) {
                    int x = mpos.x - gpos.x - clickOrigin;
                    x = UtilMath.clampI(x, getSpacing(), getWidth() - knob.getWidth() - getSpacing());

                    value = UtilMath.scaleF(x - getSpacing(), getWidth() - knob.getWidth() - getSpacing() * 2, getMaxValue());
                } else {
                    int y = mpos.y - gpos.y - clickOrigin;
                    y = UtilMath.clampI(y, getSpacing(), getHeight() - knob.getHeight() - getSpacing());

                    value = UtilMath.scaleF(y - getSpacing(), getHeight() - knob.getHeight() - getSpacing() * 2, getMaxValue());
                }

                fireEventListeners(true);
                isSliderDirty = true;
            }

            if (!knob.isMouseOver() && getMouse().isMouseReleased(Mouse.LEFT_CLICK))
                stopDragging();
        }

        if (isSliderDirty)
            updateSlider();

        return shouldHandleInput && !isMouseOver() && !knob.isMouseOver();
    }

    /**
     * Updates the slider knob location based on the {@link #getValue()}.
     */
    protected void updateSlider() {

        if (!isVertical()) {
            float x = UtilMath.scaleF(UtilMath.clampF(getValue(), 0, getMaxValue()), getMaxValue(), getWidth() - knob.getWidth() - getSpacing() * 2);
            knob.setLocalPosition(x + getSpacing(), getSpacing());
        } else {
            float y = UtilMath.scaleF(UtilMath.clampF(getValue(), 0, getMaxValue()), getMaxValue(), getHeight() - knob.getHeight() - getSpacing() * 2);
            knob.setLocalPosition(getSpacing() * 2, y + getSpacing());
        }

        int minDimension = Math.min(getWidth(), getHeight());
        knob.setSize(minDimension - spacing * 2, minDimension - spacing * 2);

        isSliderDirty = false;
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        isSliderDirty = true;
    }

    @Override
    public void onEvent(GuiButton executor, Boolean actionItem, Integer action) {
        if (actionItem && !isDraggingDisabled()) {
            isDragging = true;

            Vector2i mpos = getMouse().getPosition();
            Vector2i gpos = knob.getGlobalPosition();
            clickOrigin = !isVertical() ? (mpos.x - gpos.x) : (mpos.y - gpos.y);

            backgroundColorUnselected = knob.getBackgroundColorUnselected();
            outlineColorUnselected = knob.getOutlineColorUnselected();
        } else {
            stopDragging();
        }
    }

    /**
     * Stop updating the slider position, and reset the knob unselected colors.
     */
    protected void stopDragging() {
        isDragging = false;
        if (!isDraggingDisabled()) {
            knob.setBackgroundColorUnselected(backgroundColorUnselected);
            knob.setOutlineColorUnselected(outlineColorUnselected);
        }
    }

    /**
     * Sets the value of this slider.
     * 
     * @param value
     *            a value between 0 and {@link #getMaxValue()}.
     * @return this object for chaining.
     */
    public GuiSlider setValue(float value) {
        oldValue = getValue();
        this.value = UtilMath.clampF(value, 0, getMaxValue());
        fireEventListeners(false);
        isSliderDirty = true;
        return this;
    }

    /**
     * Returns the direction the slider knob has moved.
     * 
     * @return the direction the slider knob has moved.
     */
    public int getDirection() {
        return (int) Math.signum(getValue() - getOldValue());
    }

    /**
     * Returns the old value before the update to move the slider.
     * 
     * @return the old value before the update to move the slider.
     */
    public float getOldValue() {
        return oldValue;
    }

    /**
     * Sets the spacing between the knob and the slider container.
     * 
     * @param spacing
     *            the spacing.
     * @return this object for chaining.
     */
    public GuiSlider setSpacing(int spacing) {
        int minDimension = Math.min(getWidth(), getHeight());
        this.spacing = UtilMath.clampI(spacing, 0, minDimension / 2 - 2);

        isSliderDirty = true;
        return this;
    }

    /**
     * If set true the slider will be displayed vertically.
     * 
     * @param isVertical
     *            true for a vertical slider.
     * @return this object for chaining.
     */
    public GuiSlider setVertical(boolean isVertical) {
        if (isVertical && !isVertical()) {// switching to.
            setSize(getHeight(), getWidth());

            // knob.setLocalPosition(getSpacing(), knob.getLocalPositionX());

        } else if (!isVertical && isVertical()) {// switching from.
            setSize(getHeight(), getWidth());

            // knob.setLocalPosition(knob.getLocalPositionY(), getSpacing());

        }
        this.isVertical = isVertical;
        isSliderDirty = true;
        return this;
    }

    /**
     * Set true to disable sliding when dragging over the slider knob.
     * 
     * @param isDraggingDisabled
     *            true to disable sliding when dragging over the slider knob.
     * @return this object for chaining.
     */
    public GuiSlider setDraggingDisabled(boolean isDraggingDisabled) {
        this.isDraggingDisabled = isDraggingDisabled;
        if (isDraggingDisabled)
            stopDragging();
        return this;
    }

    /**
     * Returns true if dragging the mouse over the slider knob changes the value.
     * 
     * @return true if dragging the mouse over the slider knob changes the value.
     */
    public boolean isDraggingDisabled() {
        return isDraggingDisabled;
    }

    /**
     * Fires all event listeners within this slider.
     * 
     * @param knobMove
     *            true if the events being triggered was from moving the knob with the mouse.
     */
    protected void fireEventListeners(boolean knobMove) {
        for (IEventListener<GuiSlider, Float, Boolean> listener : listeners)
            listener.onEvent(this, getValue(), knobMove);
    }

    /**
     * Returns the max value of this slider.
     * 
     * @return the max value of this slider.
     */
    public float getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the max value {@link #getValue()} can return.
     * 
     * @param maxValue
     *            the max value.
     * @return this object for chaining.
     */
    public GuiSlider setMaxValue(float maxValue) {
        this.maxValue = UtilMath.clampF(maxValue, 0.000001f, Float.MAX_VALUE);
        this.value = UtilMath.clampF(getValue(), 0, maxValue);
        isSliderDirty = true;
        return this;
    }

    /**
     * Returns true if this slider will be displayed vertically.
     * 
     * @return true if this slider will be displayed vertically.
     */
    public boolean isVertical() {
        return isVertical;
    }

    /**
     * Returns the current value of the knob slider.
     * 
     * @return the current value of the knob slider.
     */
    public float getValue() {
        return value;
    }

    /**
     * Returns the node that represents the slider knob.
     * 
     * @return the node that represents the slider knob.
     */
    public GuiButton getKnobNode() {
        return knob;
    }

    /**
     * Returns the spacing between the knob and the slider container.
     * 
     * @return the spacing between the knob and the slider container.
     */
    public int getSpacing() {
        return spacing;
    }

    /**
     * Adds a value change listener to this slider.
     * 
     * @param listener
     *            the listener to add.
     * @return this object for chaining.
     */
    public GuiSlider addListener(IEventListener<GuiSlider, Float, Boolean> listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * Returns an unmodifiable list of listeners.
     * 
     * @return an unmodifiable list of listeners.
     */
    public List<IEventListener<GuiSlider, Float, Boolean>> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * Returns true if the slider is being dragged with the mouse cursor.
     * 
     * @return true if the slider is being dragged with the mouse cursor.
     */
    public boolean isDragging() {
        return isDragging;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((backgroundColorUnselected == null) ? 0 : backgroundColorUnselected.hashCode());
        result = prime * result + clickOrigin;
        result = prime * result + (isDragging ? 1231 : 1237);
        result = prime * result + (isDraggingDisabled ? 1231 : 1237);
        result = prime * result + (isSliderDirty ? 1231 : 1237);
        result = prime * result + (isVertical ? 1231 : 1237);
        result = prime * result + ((knob == null) ? 0 : knob.hashCode());
        result = prime * result + ((listeners == null) ? 0 : listeners.hashCode());
        result = prime * result + Float.floatToIntBits(maxValue);
        result = prime * result + Float.floatToIntBits(oldValue);
        result = prime * result + ((outlineColorUnselected == null) ? 0 : outlineColorUnselected.hashCode());
        result = prime * result + spacing;
        result = prime * result + Float.floatToIntBits(value);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GuiSlider other = (GuiSlider) obj;
        if (backgroundColorUnselected == null) {
            if (other.backgroundColorUnselected != null)
                return false;
        } else if (!backgroundColorUnselected.equals(other.backgroundColorUnselected))
            return false;
        if (clickOrigin != other.clickOrigin)
            return false;
        if (isDragging != other.isDragging)
            return false;
        if (isDraggingDisabled != other.isDraggingDisabled)
            return false;
        if (isSliderDirty != other.isSliderDirty)
            return false;
        if (isVertical != other.isVertical)
            return false;
        if (knob == null) {
            if (other.knob != null)
                return false;
        } else if (!knob.equals(other.knob))
            return false;
        if (listeners == null) {
            if (other.listeners != null)
                return false;
        } else if (!listeners.equals(other.listeners))
            return false;
        if (Float.floatToIntBits(maxValue) != Float.floatToIntBits(other.maxValue))
            return false;
        if (Float.floatToIntBits(oldValue) != Float.floatToIntBits(other.oldValue))
            return false;
        if (outlineColorUnselected == null) {
            if (other.outlineColorUnselected != null)
                return false;
        } else if (!outlineColorUnselected.equals(other.outlineColorUnselected))
            return false;
        if (spacing != other.spacing)
            return false;
        if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value))
            return false;
        return true;
    }

}
