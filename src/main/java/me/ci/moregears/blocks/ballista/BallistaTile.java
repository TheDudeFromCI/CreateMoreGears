package me.ci.moregears.blocks.ballista;

import java.util.List;

import me.ci.moregears.foundation.EntityTurretTarget;
import me.ci.moregears.foundation.TurretTile;
import me.ci.moregears.registry.ModTiles;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class BallistaTile extends TurretTile {

    public static final int RANGE = 16;

    private final ItemStackHandler inventory;

    public BallistaTile() {
        super(ModTiles.BALLISTA.get(), "ballista");
        this.inventory = new ItemStackHandler();
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
        // TODO Auto-generated method stub

    }

}
