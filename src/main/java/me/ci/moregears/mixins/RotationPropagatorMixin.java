package me.ci.moregears.mixins;

import com.simibubi.create.content.contraptions.RotationPropagator;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ci.moregears.blocks.worm_gear.WormGearBlock;
import net.minecraft.util.math.BlockPos;

@Mixin(RotationPropagator.class)
public abstract class RotationPropagatorMixin {

    private static boolean isLargeCogToWormGear(KineticTileEntity from, KineticTileEntity to) {
        return ICogWheel.isLargeCog(from.getBlockState()) && to.getBlockState().getBlock() instanceof WormGearBlock;
    }

    @Inject(method = "getRotationSpeedModifier", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void onGetCustomRotationSpeed(KineticTileEntity from, KineticTileEntity to,
        CallbackInfoReturnable<Float> ci) {

        if (isLargeCogToWormGear(from, to)) {
            BlockPos diff = from.getBlockPos().subtract(to.getBlockPos());
            float expected = to.propagateRotationTo(from, to.getBlockState(), from.getBlockState(), diff, false, false);
            ci.setReturnValue(1f / expected);
        }
    }
}
