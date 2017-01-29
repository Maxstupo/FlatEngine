package com.github.maxstupo.flatengine.hgui;

import com.github.maxstupo.flatengine.hgui.AbstractAlignableGuiNode.Alignment;
import com.github.maxstupo.flatengine.screen.AbstractScreen;

/**
 * @author Maxstupo
 *
 */
public class GuiLabel extends GuiContainer {

    private final GuiText text;

    public GuiLabel(AbstractScreen screen, float localX, float localY, int width, int height) {
        super(screen, localX, localY, width, height);
        this.text = new GuiText(screen, Alignment.CENTER);
        add(text);
    }

    public GuiText getTextNode() {
        return text;
    }

}
