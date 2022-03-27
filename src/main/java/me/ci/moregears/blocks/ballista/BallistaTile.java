package me.ci.moregears.blocks.ballista;

import java.util.List;

import me.ci.moregears.foundation.EntityTurretTarget;
import me.ci.moregears.foundation.TurretTile;
import me.ci.moregears.registry.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Vector3d;
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
    public int getReloadTicks() {
        return 40;
    }

    @Override
    public int getCooldownTicks() {
        return 23;
    }

    @Override
    public int getUnloadTicks() {
        return 15;
    }

    @Override
    protected float getAimSpeed() {
        return 0.1f;
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
        ItemStack arrowItem = this.inventory.extractItem(0, 1, false);

        Vector3d pos = getAimPos();
        Vector3d target = getAimTarget().getPosition();
        Vector3d delta = target.subtract(pos).normalize();
        pos = pos.add(delta.scale(0.5f));

        ArrowEntity arrow = new ArrowEntity(this.level, pos.x, pos.y, pos.z);
        arrow.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
        arrow.setEffectsFromItem(arrowItem);
        this.level.addFreshEntity(arrow);
        arrow.shoot(delta.x, delta.y, delta.z, 1.1f, 6f);
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
