package com.github.maxstupo.flatengine.hgui;

/**
 * This interface allows objects to use a separate method other than {@link Object#toString()} when using {@link GuiList}.
 * 
 * @author Maxstupo
 */
public interface IListItem {

    /**
     * Returns the name of the item.
     * 
     * @return the name of the item.
     */
    String getItemName();
}
