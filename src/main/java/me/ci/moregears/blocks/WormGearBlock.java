package me.ci.moregears.blocks;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;
import me.ci.moregears.CreateMoreGears;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class WormGearBlock extends CogWheelBlock {
    public WormGearBlock(Properties properties) {
        super(false, properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return CreateMoreGears.INSTANCE.tiles.wormGear.create();
    }

    @Override
    public boolean isLargeCog() {
        return false;
    }

    @Override
    public boolean isSmallCog() {
        return false;
    }

    @Override
    public boolean isDedicatedCogWheel() {
        return false;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(
            BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AllShapes.SMALL_GEAR.get(state.getValue(AXIS));
    }
}
