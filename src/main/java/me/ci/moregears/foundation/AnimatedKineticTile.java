package me.ci.moregears.foundation;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.tileentity.TileEntityType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AnimatedKineticTile extends KineticTileEntity implements IAnimatable {

    private final AnimationFactory animationFactory;

    protected AnimatedKineticTile(TileEntityType<?> typeIn) {
        super(typeIn);
        this.animationFactory = new AnimationFactory(this);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "main", 0, this::animationController));
    }

    @SuppressWarnings({ "unchecked" })
    protected <E extends AnimatedKineticTile> PlayState animationController(AnimationEvent<E> event) {
        event.getController().animationSpeed = getAnimationSpeed();
        return PlayState.CONTINUE;
    }

    @Override
    public boolean shouldRenderNormally() {
        return true;
    }

    protected abstract double getAnimationSpeed();
}
