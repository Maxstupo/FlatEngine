package com.github.maxstupo.flatengine.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.GuiText.TextAlignment;
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
        this.text = new GuiText(screen, null, text);
        this.addChild(this.text);

        this.text.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {

        if (!isEnabled())
            return shouldHandleInput;

        if (shouldHandleInput) {
            isMouseOver = isMouseOver();
            doInputLogic();
        } else {
            isMouseOver = false;
        }
        return shouldHandleInput && !isMouseOver;
    }

    public void doInputLogic() {
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
            g.fill3DRect(gpos.x, gpos.y, size.x, size.y, isEnabled);

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

        if (isDebug) {
            g.setColor(Color.WHITE);
            g.drawRect(gpos.x, gpos.y, size.x, size.y);
        }
    }

    public GuiButton setText(String text) {
        this.text.setText(text);
        return this;
    }

    public GuiButton setFont(Font font) {
        this.text.setFont(font);
        return this;
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

    public GuiButton setTextAlignment(TextAlignment alignment) {
        text.setAlignment(alignment);
        return this;
    }

    public GuiButton addListener(IEventListener<GuiButton, String, Integer> listener) {
        if (listener != null)
            listeners.add(listener);
        return this;
    }

    public Font getFont() {
        return text.getFont();
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

    public TextAlignment getTextAlignment() {
        return text.getAlignment();
    }

    @Override
    public String toString() {
        return String.format("GuiButton [text=%s, backgroundColor=%s, foregroundColor=%s, selectionColor=%s, boxLess=%s, outlineStroke=%s, isMouseOver=%s, listeners=%s]", text, backgroundColor, foregroundColor, selectionColor, boxLess, outlineStroke, isMouseOver, listeners);
    }

    @Override
    protected void onDispose() {

    }

}
