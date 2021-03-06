package me.ci.moregears.blocks.ballista;

import java.util.List;

import me.ci.moregears.foundation.EntityTurretTarget;
import me.ci.moregears.foundation.TurretTile;
import me.ci.moregears.registry.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
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
        return (int) Math.ceil(40 / getAnimationSpeed());
    }

    @Override
    public int getCooldownTicks() {
        return (int) Math.ceil(30 / getAnimationSpeed());
    }

    @Override
    public int getUnloadTicks() {
        return (int) Math.ceil(13.2 / getAnimationSpeed());
    }

    @Override
    protected float getAimSpeed() {
        return Math.abs(getSpeed()) * 0.1f;
    }

    @Override
    public float getRange() {
        return 16f;
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
        LivingEntity entity = (LivingEntity) ((EntityTurretTarget) getAimTarget()).getEntity();

        Vector3d pos = getAimPos();
        Vector3d target = entity.position();
        Vector3d delta = target.subtract(pos);

        double arc = MathHelper.sqrt(delta.x * delta.x + delta.z * delta.z) * 0.2;
        delta = new Vector3d(delta.x, entity.getY(1 / 3.0) - pos.y + arc, delta.z);
        pos = pos.add(delta.normalize().scale(0.5f));

        ArrowEntity arrow = new ArrowEntity(this.level, pos.x, pos.y, pos.z);
        arrow.pickup = AbstractArrowEntity.PickupStatus.ALLOWED;
        arrow.setEffectsFromItem(arrowItem);
        arrow.shoot(delta.x, delta.y, delta.z, 1.1f, 6f);
        this.level.addFreshEntity(arrow);

        this.level.playSound(null, getBlockPos(), SoundEvents.CROSSBOW_SHOOT, SoundCategory.BLOCKS,
            1.0F, 1.0F / ((float) Math.random() * 0.4F + 0.8F));
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
