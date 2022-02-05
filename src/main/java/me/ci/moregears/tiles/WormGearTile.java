package me.ci.moregears.tiles;

import static net.minecraft.state.properties.BlockStateProperties.AXIS;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.content.contraptions.relays.elementary.SimpleKineticTileEntity;

import me.ci.moregears.blocks.WormGearBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;

public class WormGearTile extends SimpleKineticTileEntity {
    public WormGearTile(TileEntityType<? extends SimpleKineticTileEntity> type) {
        super(type);
    }

    @Override
    public float propagateRotationTo(
            KineticTileEntity target,
            BlockState stateFrom,
            BlockState stateTo,
            BlockPos diff,
            boolean connectedViaAxis,
            boolean connectedViaCogs) {

        if (connectedViaAxis) {
            // Default propagation
            return 0;
        }

        if (isWormGearToLargeCog(stateFrom, stateTo, diff)) {
            final float ratio = 1 / 8f;

            boolean flip = nonZeroElement(diff) >= 0;
            Axis axis = stateFrom.getValue(AXIS);

            // Probably not the prettyist way to do this, but...
            if (axis == Axis.X) {
                if (diff.getY() != 0) {
                    return flip ? -ratio : ratio;
                } else {
                    return flip ? ratio : -ratio;
                }
            } else if (axis == Axis.Y) {
                if (diff.getX() != 0) {
                    return flip ? ratio : -ratio;
                } else {
                    return flip ? -ratio : ratio;
                }
            } else {
                if (diff.getX() != 0) {
                    return flip ? -ratio : ratio;
                } else {
                    return flip ? ratio : -ratio;
                }
            }
        }

        return 0;
    }

    private boolean isWormGearToLargeCog(BlockState from, BlockState to, BlockPos diff) {
        if (!(from.getBlockState().getBlock() instanceof WormGearBlock))
            return false;

        if (!ICogWheel.isLargeCog(to))
            return false;

        Axis fromAxis = from.getValue(AXIS);
        Axis toAxis = to.getValue(AXIS);
        if (fromAxis == toAxis)
            return false;

        for (Axis axis : Axis.values()) {
            int axisDiff = axis.choose(diff.getX(), diff.getY(), diff.getZ());

            if (axis == fromAxis || axis == toAxis) {
                if (axisDiff != 0)
                    return false;
            } else if (axisDiff == 0)
                return false;
        }

        return true;
    }

    private static int nonZeroElement(BlockPos pos) {
        if (pos.getX() != 0)
            return pos.getX();
        else if (pos.getY() != 0)
            return pos.getY();
        else
            return pos.getZ();
    }
}
