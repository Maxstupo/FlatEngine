package com.github.maxstupo.flatengine.item;

import com.github.maxstupo.flatengine.hgui.GuiItemSlot;
import com.github.maxstupo.flatengine.input.Mouse;

/**
 * This interface defines a common method to handle item slot logic, this can include actions such as swapping two item slots, adding items to a item
 * slot, etc.
 * 
 * @author Maxstupo
 */
public interface ISlotLogic {

    /**
     * Do logic for the given item slot.
     * 
     * @param slot
     *            the item slot currently hovered over or selected.
     * @param item
     *            the item that the slot contains.
     * @param holding
     *            the item stack.
     * @param takeOnly
     *            true if the slot logic should only allow taking items.
     * @param mouse
     *            the mouse handler used to check mouse buttons.
     * @return true if a change to an item slot or holding item occurred.
     */
    <T extends AbstractItemStack> boolean doSlotLogic(GuiItemSlot<T> slot, T item, T holding, boolean takeOnly, Mouse mouse);

}
