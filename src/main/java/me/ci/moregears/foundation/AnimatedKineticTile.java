package me.ci.moregears.foundation;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AnimatedKineticTile extends KineticTileEntity implements IAnimatable {

    private final AnimationFactory animationFactory;
    private final AnimationBuilder idleAnimation;
    private AnimationController<AnimatedKineticTile> animationController;

    protected AnimatedKineticTile(TileEntityType<?> typeIn, String tileName) {
        super(typeIn);
        this.animationFactory = new AnimationFactory(this);

        this.idleAnimation = new AnimationBuilder()
            .addAnimation("animation." + tileName + ".idle", true);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.animationFactory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        this.animationController = new AnimationController<>(this, "main", 0, this::animationController);
        data.addAnimationController(this.animationController);
        playIdleAnimation();
    }

    private <E extends AnimatedKineticTile> PlayState animationController(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    @OnlyIn(Dist.CLIENT)
    protected void playAnimation(AnimationBuilder animation) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.animationController.setAnimation(animation));
    }

    @OnlyIn(Dist.CLIENT)
    protected void playIdleAnimation() {
        playAnimation(this.idleAnimation);
    }

    @Override
    public boolean shouldRenderNormally() {
        return true;
    }
}
