package com.github.maxstupo.flatengine.gui;

import java.awt.Color;

/**
 * @author Maxstupo
 *
 */
public interface IListItem {

    String getListItemText();

    /**
     * Returns the color of the {@link #getListItemText()} string. If null is returned default text color will be used.
     * 
     * @return The text color of {@link #getListItemText()}, or null.
     */
    Color getListItemTextColor();
}
