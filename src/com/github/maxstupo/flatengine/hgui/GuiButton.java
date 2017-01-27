package com.github.maxstupo.flatengine.hgui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.hgui.AlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiButton extends AbstractNode {

    private final GuiText text;

    protected Color backgroundColor = Color.LIGHT_GRAY;
    protected Color foregroundColor = Color.WHITE;
    protected Color selectionColor = backgroundColor.darker().darker();
    protected Stroke outlineStroke = new BasicStroke(2);

    protected boolean boxLess;

    private boolean isMouseOver;

    protected final List<IEventListener<GuiButton, String, Integer>> listeners = new ArrayList<>();

    public GuiButton(AbstractScreen screen, String text, int localX, int localY, int width, int height) {
        super(screen, localX, localY, width, height);
        this.text = new GuiText(screen, Alignment.CENTER, text);
        this.text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        this.add(this.text);
    }

    @Override
    protected boolean update(float delta, boolean shouldHandleInput) {

        if (shouldHandleInput) {
            isMouseOver = isMouseOver();
            doInputLogic();
        } else {
            isMouseOver = false;
        }
        return shouldHandleInput && !isMouseOver;
    }

    @Override
    protected void render(Graphics2D g) {
        Vector2i gpos = getGlobalPosition();

        if (!boxLess) {
            g.setColor(getBackgroundColor());
            g.fill3DRect(gpos.x, gpos.y, getWidth(), getHeight(), isEnabled());

            if (isMouseOver) {
                g.setColor(getSelectionColor());

                Stroke defaultStroke = g.getStroke();
                {
                    g.setStroke(getOutlineStroke());
                    g.drawRect(gpos.x + 1, gpos.y + 1, getWidth() - 2, getHeight() - 2);
                }
                g.setStroke(defaultStroke);
            }
            text.setColor(getForegroundColor());
        } else {
            text.setColor(isMouseOver ? getSelectionColor() : getForegroundColor());
        }
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

    public GuiButton addListener(IEventListener<GuiButton, String, Integer> listener) {
        if (listener == null)
            return this;
        listeners.add(listener);
        return this;
    }

    public GuiText getText() {
        return text;
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

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Stroke getOutlineStroke() {
        return outlineStroke;
    }

    public GuiButton setOutlineStroke(Stroke outlineStroke) {
        this.outlineStroke = outlineStroke;
        return this;
    }

    public boolean isBoxLess() {
        return boxLess;
    }

    public GuiButton setBoxLess(boolean boxLess) {
        this.boxLess = boxLess;
        return this;
    }
}
