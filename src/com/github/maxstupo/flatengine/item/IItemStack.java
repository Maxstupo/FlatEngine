package com.github.maxstupo.flatengine.item;

/**
 * @author Maxstupo
 *
 */
public interface IItemStack {

    int getAmount();

    String getIconId();

    String getName();

    IItemStack set(IItemStack stack);

    IItemStack set(int id, int amt);

    int add(IItemStack stack);

    IItemStack setEmpty();

    IItemStack setAmount(int amt);

    int getId();

    boolean decrease(int amt);

    boolean increase(int amt);

    int getMaxAmount();

    boolean isEmpty();

    boolean areItemStacksEqual(IItemStack stack);

    IItemStack copy();
}
