package com.github.maxstupo.flatengine.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.AlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 *
 * @author Maxstupo
 */
public class GuiButton extends AbstractGuiNode {

    private final GuiText text;

    protected Color backgroundColor = Color.GRAY;
    protected Color foregroundColor = Color.WHITE;
    protected Color selectionColor = Color.RED;

    protected boolean boxLess;

    private final Stroke outlineStroke = new BasicStroke(2);

    private boolean isMouseOver;

    protected final List<IEventListener<GuiButton, String, Integer>> listeners = new ArrayList<>();

    public GuiButton(AbstractScreen screen, String text, Vector2i localPosition, Vector2i size) {
        super(screen, localPosition, size);
        this.text = new GuiText(screen, Alignment.CENTER, text);
        this.text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.addChild(this.text);
    }

    @Override
    public boolean update(float delta, boolean shouldHandleInput) {

        if (shouldHandleInput) {
            isMouseOver = isMouseOver();
            doInputLogic();
        } else {
            isMouseOver = false;
        }
        return shouldHandleInput && !isMouseOver;
    }

    protected void doInputLogic() {
        for (int i = 1; i <= Mouse.TOTAL_MOUSE_BUTTONS_MONITORED; i++) {
            if (isMouseClicked(i))
                fireActionEvent(i);
        }
    }

    protected void fireActionEvent(int mouseButton) {
        for (IEventListener<GuiButton, String, Integer> listener : listeners)
            listener.onEvent(this, text.getText(), mouseButton);
    }

    @Override
    public void render(Graphics2D g) {

        Vector2i gpos = getGlobalPosition();

        if (!boxLess) {
            g.setColor(getBackgroundColor());
            g.fill3DRect(gpos.x, gpos.y, size.x, size.y, isEnabled());

            if (isMouseOver) {
                g.setColor(getSelectionColor());

                Stroke defaultStroke = g.getStroke();
                {
                    g.setStroke(outlineStroke);
                    g.drawRect(gpos.x + 1, gpos.y + 1, size.x - 2, size.y - 2);
                }
                g.setStroke(defaultStroke);
            }
            text.setColor(getForegroundColor());
        } else {
            text.setColor(isMouseOver ? getSelectionColor() : getForegroundColor());
        }
    }

    @Override
    public void onResize(int width, int height) {

    }

    public GuiButton setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public GuiButton setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
        return this;
    }

    public GuiButton setSelectionColor(Color selectionColor) {
        this.selectionColor = selectionColor;
        return this;
    }

    public GuiButton setBoxLess(boolean boxLess) {
        this.boxLess = boxLess;
        return this;
    }

    public GuiButton addListener(IEventListener<GuiButton, String, Integer> listener) {
        if (listener != null)
            listeners.add(listener);
        return this;
    }

    public GuiText getText() {
        return text;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public boolean isBoxLess() {
        return boxLess;
    }

    @Override
    public String toString() {
        return String.format("GuiButton [text=%s, backgroundColor=%s, foregroundColor=%s, selectionColor=%s, boxLess=%s, outlineStroke=%s, isMouseOver=%s, listeners=%s, screen=%s, localPosition=%s, size=%s, isEnabled=%s, isDebug=%s]", text, backgroundColor, foregroundColor, selectionColor, boxLess, outlineStroke, isMouseOver, listeners, screen, localPosition, size, isEnabled(), isDebug());
    }

    @Override
    protected void onDispose() {

    }

}
