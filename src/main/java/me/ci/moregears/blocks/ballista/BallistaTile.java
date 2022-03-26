package me.ci.moregears.blocks.ballista;

import java.util.List;

import me.ci.moregears.foundation.EntityTurretTarget;
import me.ci.moregears.foundation.TurretTile;
import me.ci.moregears.registry.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class BallistaTile extends TurretTile {

    private final ItemStackHandler inventory;
    private final LazyOptional<BallistaInventoryHandler> capability;

    public BallistaTile() {
        super(ModTiles.BALLISTA.get(), "ballista");

        this.inventory = new ItemStackHandler();
        this.capability = LazyOptional.of(BallistaInventoryHandler::new);
    }

    @Override
    public boolean hasAmmo() {
        return !this.inventory.getStackInSlot(0).isEmpty();
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
    }

    @Override
    public float calculateStressApplied() {
        return 3f;
    }

    @Override
    public double getAimHeight() {
        return 15.95 / 16.0;
    }

    @Override
    protected void lookForTargets() {
        if (getAimTarget() != null)
            return;

        List<EntityTurretTarget> entities = lookForMobTargets(this.level);
        if (entities.isEmpty())
            return;

        setAimTarget(entities.get(0));
    }

    @Override
    protected void fire() {
        this.inventory.extractItem(0, 1, false);
        // TODO Spawn arrow
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap))
            return this.capability.cast();

        return super.getCapability(cap, side);
    }

    private class BallistaInventoryHandler implements IItemHandler {

        @Override
        public int getSlots() {
            return inventory.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (stack.getItem().is(ItemTags.ARROWS))
                return inventory.insertItem(slot, stack, simulate);
            else
                return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return inventory.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return inventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return inventory.isItemValid(slot, stack);
        }
    }
}
