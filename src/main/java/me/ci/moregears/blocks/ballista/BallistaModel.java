package me.ci.moregears.blocks.ballista;

import me.ci.moregears.CreateMoreGears;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class BallistaModel extends AnimatedGeoModel<BallistaTile> {

    private float visualYaw;
    private float visualPitch;
    private double lastFrame;

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

        double deltaTime = this.seekTime - this.lastFrame;
        this.lastFrame = this.seekTime;

        float t = (float) Math.min(1, 0.9 * deltaTime);
        float yaw = MathHelper.rotlerp(this.visualYaw, tile.getYaw(), t);
        float pitch = MathHelper.rotlerp(this.visualPitch, tile.getPitch(), t);

        this.visualYaw = yaw;
        this.visualPitch = pitch;

        IBone turntable = getAnimationProcessor().getBone("turntable");
        IBone crossbow = getAnimationProcessor().getBone("crossbow");
        turntable.setRotationY((float) Math.toRadians(yaw));
        crossbow.setRotationX((float) Math.toRadians(pitch));
    }
}
