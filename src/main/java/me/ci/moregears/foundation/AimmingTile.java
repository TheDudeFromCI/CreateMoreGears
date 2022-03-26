package me.ci.moregears.foundation;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public abstract class AimmingTile extends AnimatedKineticTile implements IAimmable {

    protected ITurretTarget target;
    protected float yaw;
    protected float pitch;

    protected AimmingTile(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    public ITurretTarget getAimTarget() {
        if (this.target != null && !this.target.isStillValid())
            this.target = null;

        return this.target;
    }

    @Override
    public void setAimTarget(@Nullable ITurretTarget target) {
        this.target = target;
    }

    @Override
    public float getYaw() {
        return this.yaw;
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public void setYaw(float yaw) {
        this.yaw = yaw;
        setChanged();
    }

    @Override
    public void setPitch(float pitch) {
        this.pitch = pitch;
        setChanged();
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("Yaw", this.yaw);
        compound.putFloat("Pitch", this.pitch);
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        this.yaw = compound.getFloat("Yaw");
        this.pitch = compound.getFloat("Pitch");
        this.target = null;
    }

    @Override
    public Vector3d getAimPos() {
        BlockPos pos = getBlockPos();
        return new Vector3d(pos.getX() + 0.5, pos.getY() + getAimHeight(), pos.getZ() + 0.5);
    }

    public double getAimHeight() {
        return 0.5;
    }

    @Override
    public boolean tickAim(float maxAngle) {
        boolean finishedAimming = IAimmable.super.tickAim(maxAngle);
        setChanged();
        sendData(); // TODO Send smaller packets

        return finishedAimming;
    }
}
