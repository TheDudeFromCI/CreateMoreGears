package me.ci.moregears.blocks.ballista;

import com.simibubi.create.content.contraptions.base.KineticBlock;
import com.simibubi.create.content.contraptions.relays.elementary.ICogWheel;
import com.simibubi.create.foundation.block.ITE;

import me.ci.moregears.foundation.ShapeBuilder;
import me.ci.moregears.registry.ModTiles;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class BallistaBlock extends KineticBlock implements ITE<BallistaTile>, ICogWheel {

    private static final Properties PROPERTIES = Properties.of(Material.STONE)
        .noOcclusion()
        .sound(SoundType.WOOD)
        .harvestTool(ToolType.AXE);

    private static final VoxelShape SHAPE = new ShapeBuilder()
        .add(0, 0, 0, 16, 6, 16)
        .build();

    public BallistaBlock() {
        super(PROPERTIES);
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTiles.BALLISTA.get().create();
    }

    @Override
    public Class<BallistaTile> getTileEntityClass() {
        return BallistaTile.class;
    }

    @Override
    @Deprecated
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    @Override
    @Deprecated
    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.MEDIUM;
    }
}
