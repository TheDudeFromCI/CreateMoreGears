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
        reload();
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

    @Override
    public void reload() {
        this.reloadTicks = getReloadTicks();
        this.cooldownTicks = getCooldownTicks();
        setChanged();
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
            this.state = TurretState.RELOADING;
            setChanged();
            reload();
        }
    }

    private void tickUnloadingState() {
        tickUnload();
        if (this.unloadTicks == 0) {
            this.state = TurretState.IDLE;
            this.unloadTicks = getUnloadTicks();
            setChanged();
        }
    }

    private void tickCoolingState() {
        tickCooldown();
        if (this.cooldownTicks == 0) {
            this.state = TurretState.IDLE;
            this.cooldownTicks = getCooldownTicks();
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
            reload();
            return;
        }

        tickReload();
        tickAim(getAimMaxAngle());

        if (this.reloadTicks == 0) {
            lookForTargets();
            if (getAimTarget() != null) {
                this.state = TurretState.AIMMING;
                setChanged();
            } else {
                this.state = TurretState.UNLOADING;
                setChanged();
                reload();
            }
        }
    }

    private void tickAimState() {
        if (!hasAmmo()) {
            this.state = TurretState.UNLOADING;
            setChanged();
            reload();
            return;
        }

        lookForTargets();
        if (getAimTarget() == null) {
            this.state = TurretState.UNLOADING;
            setChanged();
            reload();
            return;
        }

        if (!tickAim(getAimMaxAngle()))
            return;

        fire();
        reload();

        this.state = TurretState.COOLING;
        setChanged();

        if (forgetTargetOnShoot())
            setAimTarget(null);
    }

    private void tickReload() {
        this.reloadTicks--;
        setChanged();
    }

    private void tickCooldown() {
        this.cooldownTicks--;
        setChanged();
    }

    private void tickUnload() {
        this.unloadTicks--;
        setChanged();
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
