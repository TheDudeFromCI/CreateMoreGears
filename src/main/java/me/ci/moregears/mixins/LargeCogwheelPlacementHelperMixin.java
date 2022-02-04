package me.ci.moregears.mixins;

import static com.simibubi.create.content.contraptions.base.RotatedPillarKineticBlock.AXIS;

import java.util.List;
import java.util.function.Predicate;

import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.foundation.utility.placement.IPlacementHelper;
import com.simibubi.create.foundation.utility.placement.PlacementOffset;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.ci.moregears.blocks.WormGearBlock;
import me.ci.moregears.items.WormGearItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

@Mixin(targets = "com.simibubi.create.content.contraptions.relays.elementary.CogwheelBlockItem$LargeCogHelper")
public abstract class LargeCogwheelPlacementHelperMixin {

    @Inject(method = "getItemPredicate", at = @At("RETURN"), cancellable = true, remap = false)
    public void onGetItemPredicate(CallbackInfoReturnable<Predicate<ItemStack>> ci) {
        Predicate<ItemStack> pred = WormGearItem::isWormGearItem;
        pred = pred.or(ci.getReturnValue());
        ci.setReturnValue(pred);
    }

    @Inject(method = "getOffset", at = @At("HEAD"), cancellable = true, remap = false)
    public void onGetOffset(PlayerEntity player, World world, BlockState state, BlockPos pos,
            BlockRayTraceResult ray, CallbackInfoReturnable<PlacementOffset> ci) {

        if (WormGearBlock.isWormGear(state)) {
            PlacementOffset offset = getWormGearOffset(world, state, pos, ray);
            ci.setReturnValue(offset);
            ci.cancel();
        }
    }

    private PlacementOffset getWormGearOffset(World world, BlockState state, BlockPos pos,
            BlockRayTraceResult ray) {

        List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.getLocation(),
                state.getValue(AXIS));

        for (Direction dir : directions) {
            BlockPos newPos = pos.relative(dir);

            if (!world.getBlockState(newPos)
                    .getMaterial()
                    .isReplaceable())
                continue;

            Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);
            Axis targetAxis = Axis.values()[3 - (axis.ordinal() ^ dir.getAxis().ordinal())];

            return PlacementOffset.success(newPos, s -> s.setValue(AXIS, targetAxis));

        }

        return PlacementOffset.fail();
    }
}
