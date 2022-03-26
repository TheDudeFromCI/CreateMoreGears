package me.ci.moregears.foundation;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public abstract class TurretTile extends AimmingTile implements IReloadable {

    public enum TurretState {
        IDLE, RELOADING, AIMMING, FIRING, UNLOADING
    }

    private final AnimationBuilder idleAnimation;
    private final AnimationBuilder drawAnimation;
    private final AnimationBuilder fireAnimation;
    private final AnimationBuilder readyAnimation;
    private final AnimationBuilder unloadingAnimation;

    private int reloadTicks;
    private TurretState state;

    protected TurretTile(TileEntityType<?> typeIn, String tileName) {
        super(typeIn);

        this.idleAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".idle", true);

        this.drawAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".draw", false)
            .addAnimation("animation." + tileName + ".ready", true);

        this.fireAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".fire", false)
            .addAnimation("animation." + tileName + ".idle", true);

        this.readyAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".ready", true);

        this.unloadingAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".unload", false) // TODO Make unloading animation
            .addAnimation("animation." + tileName + ".idle", true);

        this.state = TurretState.IDLE;
        reload();
        resetAim();
    }

    @Override
    public int getReloadTicksRemaining() {
        return this.reloadTicks;
    }

    @Override
    public void reload() {
        this.reloadTicks = getReloadTicks();
        setChanged();
    }

    public boolean canReadyShot() {
        return getSpeed() != 0 && hasAmmo() && getAimTarget() != null;
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("ReloadTicks", this.reloadTicks);
        compound.putInt("TurretState", this.state.ordinal());
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        this.reloadTicks = compound.getInt("ReloadTicks");
        this.state = TurretState.values()[compound.getInt("TurretState")];
    }

    @Override
    public void tick() {
        super.tick();

        switch (this.state) {
            case IDLE:
            case UNLOADING:
            case FIRING:
                tickIdleState();
                break;

            case RELOADING:
                tickReloadState();
                break;

            case AIMMING:
                tickAimState();
                break;
        }
    }

    private void tickIdleState() {
        if (hasAmmo()) {
            this.state = TurretState.RELOADING;
            reload();
        }
    }

    private void tickReloadState() {
        if (!hasAmmo()) {
            this.state = TurretState.IDLE;
            reload();
            return;
        }

        if (!isReloading()) {
            lookForTargets();
            if (getAimTarget() != null) {
                this.state = TurretState.AIMMING;
                resetAim();
            }

            return;
        }

        this.reloadTicks--;
        setChanged();
    }

    private void tickAimState() {
        if (!hasAmmo()) {
            this.state = TurretState.UNLOADING;
            reload();
            resetAim();
            return;
        }

        if (!isAimming()) {
            this.state = TurretState.FIRING;
            fire();
            reload();
            resetAim();

            if (forgetTargetOnShoot())
                setAimTarget(null);

            return;
        }

        tickAim();
        this.aimTicks--;
        setChanged();
        sendData();
    }

    @Override
    public boolean isReloading() {
        return this.reloadTicks > 0;
    }

    @Override
    protected <E extends AnimatedKineticTile> PlayState animationController(AnimationEvent<E> event) {
        switch (this.state) {
            case IDLE:
                event.getController().setAnimation(this.idleAnimation);
                break;

            case RELOADING:
                event.getController().setAnimation(this.drawAnimation);
                break;

            case AIMMING:
                event.getController().setAnimation(this.readyAnimation);
                break;

            case FIRING:
                event.getController().setAnimation(this.fireAnimation);
                break;

            case UNLOADING:
                event.getController().setAnimation(this.unloadingAnimation);
                break;
        }

        return super.animationController(event);
    }

    protected abstract void lookForTargets();

    protected abstract void fire();
}
