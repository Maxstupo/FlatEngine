package com.github.maxstupo.flatengine.item;

import com.github.maxstupo.flatengine.hgui.GuiItemContainer;
import com.github.maxstupo.flatengine.hgui.GuiItemSlot;
import com.github.maxstupo.flatengine.input.Mouse;

/**
 * This class is a general purpose implementation for slot logic that can be used with {@link GuiItemContainer} object.
 * 
 * @see ISlotLogic
 * @author Maxstupo
 */
public class SlotLogic implements ISlotLogic {

    @Override
    public <T extends AbstractItemStack> boolean doSlotLogic(GuiItemSlot<T> slot, T item, T holding, boolean takeOnly, Mouse mouse) {
        boolean didChange = false;

        if (slot.isMouseClicked(Mouse.LEFT_CLICK)) {

            if (holding.isEmpty() && !item.isEmpty()) { // Take a stack.
                holding.set(item);
                item.setEmpty();
                didChange = true;

            } else if (!holding.isEmpty() && item.isEmpty() && !takeOnly) { // Place a stack.
                item.set(holding);
                holding.setEmpty();
                didChange = true;

            } else if (!holding.equals(item) && !takeOnly) { // Swap item stacks.
                AbstractItemStack newStack = item.copy();
                item.set(holding);
                holding.set(newStack);
                didChange = true;

            } else if (!takeOnly) { // Try to add the holding stack to the item slot.
                int leftOvers = item.add(holding);

                if (leftOvers == 0) {
                    holding.setEmpty();
                } else {
                    holding.setAmount(leftOvers);
                }
                didChange = true;

            } else if (takeOnly && holding.equals(item) && !item.isEmpty() && (holding.getMaxAmount() - holding.getAmount()) >= item.getAmount()) {
                // If takeOnly, take from slot if items are the same.
                holding.add(item);
                didChange = true;

            }

        } else if (mouse.isMouseDown(Mouse.RIGHT_CLICK) && !takeOnly) {
            if (holding.isEmpty() && !item.isEmpty()) { // Take half of a stack.
                int amt = (int) Math.ceil((double) item.getAmount() / 2);

                holding.set(item.getId(), amt);
                amt = (int) Math.floor((double) item.getAmount() / 2);
                item.setAmount(amt);
                didChange = true;

            } else if (!holding.isEmpty() && item.isEmpty()) { // Add one to a slot.
                item.set(holding);
                item.setAmount(1);
                holding.decrease(1);
                didChange = true;

            } else if (!holding.isEmpty() && !item.isEmpty()) { // Add one to a slot.
                if (holding.equals(item)) {
                    if (!item.increase(1))
                        holding.decrease(1);
                    didChange = true;
                }

            }
        }
        return didChange;
    }

}
