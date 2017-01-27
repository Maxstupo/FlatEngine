package com.github.maxstupo.flatengine.item;

import com.github.maxstupo.flatengine.gui.GuiItemSlot;
import com.github.maxstupo.flatengine.input.Keyboard;
import com.github.maxstupo.flatengine.input.Mouse;

/**
 * @author Maxstupo
 *
 */
public class SlotLogic implements ISlotLogic {

    @Override
    public <T extends IItemStack> boolean doSlotLogic(GuiItemSlot<T> slot, T item, T holding, boolean takeOnly, Keyboard keyboard) {
        boolean didChange = true;
        if (slot.isMouseClicked(Mouse.LEFT_CLICK)) {

            if (holding.isEmpty() && !item.isEmpty()) { // Take a stack.
                holding.set(item);
                item.setEmpty();
            } else if (!holding.isEmpty() && item.isEmpty() && !takeOnly) { // Place a stack.
                item.set(holding);
                holding.setEmpty();
            } else if (!holding.areItemStacksEqual(item) && !takeOnly) { // Swap item stacks.
                IItemStack newStack = item.copy();
                item.set(holding);
                holding.set(newStack);
            } else if (!takeOnly) { // Try to add the holding stack to the inventory slot.
                int leftOvers = item.add(holding);

                if (leftOvers == 0) {
                    holding.setEmpty();
                } else {
                    holding.setAmount(leftOvers);
                }
            } else if (takeOnly && holding.areItemStacksEqual(item) && !item.isEmpty() && (holding.getMaxAmount() - holding.getAmount()) >= item.getAmount()) {
                holding.add(item);
            } else {
                didChange = false;
            }

        } else if (slot.isMouseClicked(Mouse.RIGHT_CLICK) && !takeOnly) {
            if (holding.isEmpty() && !item.isEmpty()) { // Take half of a stack.
                int amt = (int) Math.ceil((double) item.getAmount() / 2);

                holding.set(item.getId(), amt);
                amt = (int) Math.floor((double) item.getAmount() / 2);
                item.setAmount(amt);
            } else if (!holding.isEmpty() && item.isEmpty()) { // Add one to a slot.
                item.set(holding);
                item.setAmount(1);
                holding.decrease(1);
            } else if (!holding.isEmpty() && !item.isEmpty()) { // Add one to a slot.
                if (holding.areItemStacksEqual(item)) {
                    if (!item.increase(1))
                        holding.decrease(1);
                }

            } else {
                didChange = false;
            }

        } else {
            didChange = false;
        }
        return didChange;
    }

}
