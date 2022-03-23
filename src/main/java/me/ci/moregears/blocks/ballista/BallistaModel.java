package me.ci.moregears.blocks.ballista;

import me.ci.moregears.CreateMoreGears;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class BallistaModel extends AnimatedGeoModel<BallistaTile> {

    @Override
    public ResourceLocation getAnimationFileLocation(BallistaTile animatable) {
        return new ResourceLocation(CreateMoreGears.MOD_ID, "animations/block/ballista.animation.json");
    }

    @Override
    public ResourceLocation getModelLocation(BallistaTile object) {
        return new ResourceLocation(CreateMoreGears.MOD_ID, "geo/block/ballista.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(BallistaTile object) {
        return new ResourceLocation(CreateMoreGears.MOD_ID, "textures/block/ballista.png");
    }

    @Override
    @SuppressWarnings({ "rawtypes" })
    public void setLivingAnimations(BallistaTile tile, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(tile, uniqueID, customPredicate);

        IBone turntable = getAnimationProcessor().getBone("turntable");
        IBone crossbow = getAnimationProcessor().getBone("crossbow");

        BallistaTile ballista = tile;
        turntable.setRotationY((float) Math.toRadians(ballista.getYaw()));
        crossbow.setRotationX((float) Math.toRadians(ballista.getPitch()));

    }
}
