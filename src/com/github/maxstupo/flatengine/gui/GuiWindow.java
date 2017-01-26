package com.github.maxstupo.flatengine.gui;

import java.awt.Color;

import com.github.maxstupo.flatengine.IEventListener;
import com.github.maxstupo.flatengine.gui.AlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.input.Mouse;
import com.github.maxstupo.flatengine.screen.AbstractScreen;
import com.github.maxstupo.flatengine.util.UtilGraphics;
import com.github.maxstupo.flatengine.util.math.UtilMath;
import com.github.maxstupo.flatengine.util.math.Vector2i;

/**
 * @author Maxstupo
 *
 */
public class GuiWindow extends GuiNode implements IEventListener<GuiButton, String, Integer> {

    private final GuiText titleText;
    private final GuiNode titleNode;

    private final GuiButton btnClose;

    private final Vector2i clickOrigin = new Vector2i();
    private boolean isDragging;

    public GuiWindow(AbstractScreen screen, String title, Vector2i localPosition, Vector2i size) {
        super(screen, localPosition, size);
        setBackgroundColor(UtilGraphics.changeAlpha(Color.BLACK, 127));

        titleText = new GuiText(screen, Alignment.CENTER, title);

        titleNode = new GuiNode(screen, Vector2i.ZERO, new Vector2i(size.x, 0));
        titleNode.setBackgroundColor(getBackgroundColor());
        titleNode.addChild(titleText);

        addChild(titleNode);

        btnClose = new GuiButton(screen, "X", new Vector2i(size.x, 0), Vector2i.ZERO);
        btnClose.setBoxLess(true);
        btnClose.addListener(this);
        titleNode.addChild(btnClose);
    }

    @Override
    public void onEvent(GuiButton executor, String actionItem, Integer action) {
        setVisible(false);
    }

    @Override
    public boolean update(double delta, boolean shouldHandleInput) {
        super.update(delta, shouldHandleInput);

        if (titleNode.getSize().y != titleText.getSize().y) {
            titleNode.getSize().y = titleText.getSize().y;
            titleNode.setLocalPosition(titleNode.getLocalPositionX(), -titleNode.getSize().y);
            titleText.setTextDirty();// Update text alignment.

            btnClose.getSize().set(titleText.getSize().y, titleText.getSize().y);
            btnClose.setLocalPosition(titleNode.getSize().x - btnClose.getSize().x, 0);
            btnClose.getText().setTextDirty();
        }

        doInputLogic();

        if (isDragging) {
            Vector2i mpos = getMouse().getPosition();

            int x = mpos.x - clickOrigin.x;
            int y = mpos.y - clickOrigin.y;

            Vector2i parentSize = (getParent() != null) ? getParent().getSize() : new Vector2i(screen.getWidth(), screen.getHeight());

            x = UtilMath.clampI(x, 0, parentSize.x - size.x);
            y = UtilMath.clampI(y, titleText.getSize().y, parentSize.y - size.y);

            setLocalPosition(x, y);
        }

        return !isMouseOver() && shouldHandleInput;
    }

    protected void doInputLogic() {

        if (!isDragging && titleNode.isMouseClicked(Mouse.LEFT_CLICK)) {
            Vector2i mpos = getMouse().getPosition();
            Vector2i gpos = getGlobalPosition();

            clickOrigin.set(mpos.x - gpos.x, mpos.y - gpos.y);
            isDragging = true;

        } else if (getMouse().isMouseUp(Mouse.LEFT_CLICK)) {
            isDragging = false;
        }

    }

    @Override
    public void onResize(int width, int height) {
        Vector2i parentSize = (getParent() != null) ? getParent().getSize() : new Vector2i(screen.getWidth(), screen.getHeight());

        int x = UtilMath.clampI(getLocalPositionX(), 0, parentSize.x - size.x);
        int y = UtilMath.clampI(getLocalPositionY(), titleText.getSize().y, parentSize.y - size.y);

        setLocalPosition(x, y);
    }

    @Override
    public AbstractGuiNode addChild(AbstractGuiNode node) {
        node.getSize().set(UtilMath.clampI(node.getSize().x, 0, getSize().x), UtilMath.clampI(node.getSize().y, 0, getSize().y));

        int x = UtilMath.clampI(node.getLocalPositionX(), 0, getSize().x - node.getSize().x);
        int y = UtilMath.clampI(node.getLocalPositionY(), 0, getSize().y - node.getSize().y);

        node.setLocalPosition(x, y);

        return super.addChild(node);
    }

    public GuiNode getTitleNode() {
        return titleNode;
    }

    public GuiText getTitle() {
        return titleText;
    }

    @Override
    public String toString() {
        return String.format("GuiWindow [titleText=%s, titleNode=%s, backgroundColor=%s, outlineColor=%s, screen=%s, localPosition=%s, size=%s, isEnabled=%s, isDebug=%s]", titleText, titleNode, backgroundColor, outlineColor, screen, localPosition, size, isEnabled(), isDebug());
    }

}
