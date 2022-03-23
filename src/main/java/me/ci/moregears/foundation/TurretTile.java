package me.ci.moregears.foundation;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.builder.AnimationBuilder;

public abstract class TurretTile extends AnimatedKineticTile implements IReloadable, IAimmable {

    private final AnimationBuilder drawAnimation;
    private final AnimationBuilder fireAnimation;
    private ITurretTarget target;
    private int reloadTicks;
    private double yaw;
    private double pitch;

    protected TurretTile(TileEntityType<?> typeIn, String tileName) {
        super(typeIn, tileName);

        this.drawAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".draw", false)
            .addAnimation("animation." + tileName + ".ready", true);

        this.fireAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".fire", false)
            .addAnimation("animation." + tileName + ".idle", true);

        reload();
    }

    @OnlyIn(Dist.CLIENT)
    protected void playDrawAnimation() {
        playAnimation(this.drawAnimation);
    }

    @OnlyIn(Dist.CLIENT)
    protected void playFireAnimation() {
        playAnimation(this.fireAnimation);
    }

    @Override
    public int getReloadTicksRemaining() {
        return this.reloadTicks;
    }

    @Override
    public void reload() {
        this.reloadTicks = getReloadTicks();
    }

    public boolean canReadyShot() {
        return getSpeed() != 0 /* && hasAmmo() */ && getAimTarget() != null;
    }

    @Override
    public ITurretTarget getAimTarget() {
        if (this.target != null && !this.target.isStillValid())
            this.target = null;

        return this.target;
    }

    @Override
    public void setAimTarget(ITurretTarget target) {
        this.target = target;
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("ReloadTicks", this.reloadTicks);
        compound.putDouble("Yaw", this.yaw);
        compound.putDouble("Pitch", this.pitch);
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        this.reloadTicks = compound.getInt("ReloadTicks");
        this.yaw = compound.getDouble("Yaw");
        this.pitch = compound.getDouble("Pitch");
        this.target = null;
    }

    private void tickAim() {
        ITurretTarget aimTarget = getAimTarget();
        if (aimTarget == null)
            return;

        Vector3d crossbow = getAimPos();
        Vector3d targetPos = aimTarget.getPosition();
        double targetYaw = -calcTargetYaw(crossbow, targetPos);
        double targetPitch = calcTargetPitch(crossbow, targetPos);

        this.yaw = targetYaw;
        this.pitch = targetPitch;
        setChanged();
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
    public void tick() {
        super.tick();
        lookForTargets();

        if (!canReadyShot()) {
            reload();
            return;
        }

        tickAim();
        if (this.reloadTicks > 0) {
            this.reloadTicks--;
            return;
        }

        fire();
        reload();

        if (forgetTargetOnShoot())
            this.target = null;
    }

    @Override
    public boolean isReloading() {
        return this.reloadTicks > 0;
    }

    protected abstract void lookForTargets();

    protected abstract void fire();
}
