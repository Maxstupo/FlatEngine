package com.github.maxstupo.flatengine.item;

import com.github.maxstupo.flatengine.gui.GuiItemSlot;
import com.github.maxstupo.flatengine.input.Keyboard;

/**
 * @author Maxstupo
 *
 */
public interface ISlotLogic {

    <T extends IItemStack> boolean doSlotLogic(GuiItemSlot<T> slot, T item, T holding, boolean takeOnly, Keyboard keyboard);

}
