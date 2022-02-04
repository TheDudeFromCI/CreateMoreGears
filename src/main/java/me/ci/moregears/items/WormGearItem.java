package me.ci.moregears.items;

import static com.simibubi.create.content.contraptions.base.RotatedPillarKineticBlock.AXIS;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import com.simibubi.create.content.contraptions.relays.elementary.CogwheelBlockItem;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.placement.IPlacementHelper;
import com.simibubi.create.foundation.utility.placement.PlacementHelpers;
import com.simibubi.create.foundation.utility.placement.PlacementOffset;
import com.simibubi.create.content.contraptions.base.IRotate;
import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;
import mcp.MethodsReturnNonnullByDefault;
import me.ci.moregears.CreateMoreGears;
import me.ci.moregears.blocks.WormGearBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class WormGearItem extends CogwheelBlockItem {
    public WormGearItem(CogWheelBlock block, Properties builder) {
        super(block, builder);

        // Parent field is private, so we need to use reflection to set this.
        int placementHelper = PlacementHelpers.register(new WormGearHelper());

        try {
            Class<CogwheelBlockItem> cls = CogwheelBlockItem.class;

            Field placementHelperId = cls.getDeclaredField("placementHelperId");
            placementHelperId.setAccessible(true);
            placementHelperId.set(this, placementHelper);
        } catch (Exception e) {
            CreateMoreGears.LOGGER.error("Failed to override placement helpers for Worm Gear!", e);
        }
    }

    public static boolean isWormGearItem(ItemStack test) {
        Item item = test.getItem();
        if (!(item instanceof BlockItem))
            return false;

        return ((BlockItem) item).getBlock() instanceof WormGearBlock;
    }

    @MethodsReturnNonnullByDefault
    private static class WormGearHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return WormGearItem::isWormGearItem;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return ICogWheel::isLargeCog;
        }

        @Override
        public PlacementOffset getOffset(
                PlayerEntity player, World world, BlockState state, BlockPos pos, BlockRayTraceResult ray) {

            if (hitOnShaft(state, ray))
                return PlacementOffset.fail();

            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(
                    pos, ray.getLocation(), state.getValue(AXIS));

            for (Direction dir : directions) {
                BlockPos newPos = pos.relative(dir);

                if (!world.getBlockState(newPos).getMaterial().isReplaceable())
                    continue;

                Axis axis = ((IRotate) state.getBlock()).getRotationAxis(state);
                Axis targetAxis = Axis.values()[3 - (axis.ordinal() ^ dir.getAxis().ordinal())];

                return PlacementOffset.success(newPos, s -> s.setValue(AXIS, targetAxis));
            }

            return PlacementOffset.fail();
        }

        private static boolean hitOnShaft(BlockState state, BlockRayTraceResult ray) {
            return AllShapes.SIX_VOXEL_POLE
                    .get(state.getValue(AXIS))
                    .bounds()
                    .inflate(0.001)
                    .contains(ray.getLocation().subtract(ray.getLocation().align(Iterate.axisSet)));
        }
    }
}
