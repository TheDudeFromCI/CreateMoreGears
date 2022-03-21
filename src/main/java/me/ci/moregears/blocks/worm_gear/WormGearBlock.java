package me.ci.moregears.blocks.worm_gear;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.relays.elementary.CogWheelBlock;

import me.ci.moregears.registry.ModTiles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class WormGearBlock extends CogWheelBlock {

    public static boolean isWormGear(BlockState blockState) {
        Block block = blockState.getBlock();
        return block instanceof WormGearBlock;
    }

    public WormGearBlock(Properties properties) {
        super(false, properties);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTiles.WORM_GEAR.create();
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
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(
        BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AllShapes.SIX_VOXEL_POLE.get(state.getValue(AXIS));
    }

    @Override
    public float getParticleTargetRadius() {
        return .35f;
    }

    @Override
    public float getParticleInitialRadius() {
        return .125f;
    }
}
