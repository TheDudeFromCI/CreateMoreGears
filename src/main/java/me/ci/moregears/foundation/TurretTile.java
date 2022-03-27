package me.ci.moregears.foundation;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public abstract class TurretTile extends AimmingTile implements IReloadable {

    public enum TurretState {
        IDLE, RELOADING, AIMMING, COOLING, UNLOADING
    }

    private final AnimationBuilder idleAnimation;
    private final AnimationBuilder drawAnimation;
    private final AnimationBuilder fireAnimation;
    private final AnimationBuilder readyAnimation;
    private final AnimationBuilder unloadingAnimation;

    private int reloadTicks;
    private int cooldownTicks;
    private int unloadTicks;
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
    }

    @Override
    public int getReloadTicksRemaining() {
        return this.reloadTicks;
    }

    @Override
    public int getCooldownTicksRemaining() {
        return this.cooldownTicks;
    }

    @Override
    public int getUnloadTicksRemaining() {
        return this.unloadTicks;
    }

    public boolean canReadyShot() {
        return getSpeed() != 0 && hasAmmo() && getAimTarget() != null;
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("ReloadTicks", this.reloadTicks);
        compound.putInt("CooldownTicks", this.cooldownTicks);
        compound.putInt("UnloadTicks", this.unloadTicks);
        compound.putInt("TurretState", this.state.ordinal());
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        this.reloadTicks = compound.getInt("ReloadTicks");
        this.cooldownTicks = compound.getInt("CooldownTicks");
        this.unloadTicks = compound.getInt("UnloadTicks");
        this.state = TurretState.values()[compound.getInt("TurretState")];
    }

    @Override
    public void tick() {
        super.tick();

        switch (this.state) {
            case IDLE:
                tickIdleState();
                break;

            case UNLOADING:
                tickUnloadingState();
                break;

            case COOLING:
                tickCoolingState();
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
            lookForTargets();
            if (getAimTarget() != null) {
                this.state = TurretState.RELOADING;
                this.reloadTicks = getReloadTicks();
                setChanged();
            }
        }
    }

    private void tickUnloadingState() {
        this.unloadTicks--;
        setChanged();

        if (this.unloadTicks <= 0) {
            this.state = TurretState.IDLE;
            setChanged();
        }
    }

    private void tickCoolingState() {
        this.cooldownTicks--;
        setChanged();

        if (this.cooldownTicks <= 0) {
            this.state = TurretState.IDLE;
            setChanged();
        }
    }

    private float getAimMaxAngle() {
        return Math.abs(getSpeed() * getAimSpeed());
    }

    private void tickReloadState() {
        if (!hasAmmo()) {
            this.state = TurretState.IDLE;
            setChanged();
            return;
        }

        lookForTargets();
        if (getAimTarget() == null) {
            this.state = TurretState.UNLOADING;
            this.unloadTicks = getUnloadTicks();
            setChanged();
            return;
        }

        this.reloadTicks--;
        tickAim(getAimMaxAngle());
        setChanged();

        if (this.reloadTicks <= 0) {
            this.state = TurretState.AIMMING;
            setChanged();
        }
    }

    private void tickAimState() {
        if (!hasAmmo()) {
            this.state = TurretState.UNLOADING;
            this.unloadTicks = getUnloadTicks();
            setChanged();
            return;
        }

        lookForTargets();
        if (getAimTarget() == null) {
            this.state = TurretState.UNLOADING;
            this.unloadTicks = getUnloadTicks();
            setChanged();
            return;
        }

        if (!tickAim(getAimMaxAngle()))
            return;

        fire();

        this.state = TurretState.COOLING;
        this.cooldownTicks = getCooldownTicks();
        setChanged();

        if (forgetTargetOnShoot())
            setAimTarget(null);
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

            case COOLING:
                event.getController().setAnimation(this.fireAnimation);
                break;

            case UNLOADING:
                event.getController().setAnimation(this.unloadingAnimation);
                break;
        }

        return super.animationController(event);
    }

    protected abstract void lookForTargets();

    protected abstract float getAimSpeed();

    protected abstract void fire();
}
