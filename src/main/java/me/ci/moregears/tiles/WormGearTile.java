package me.ci.moregears.tiles;

import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.elementary.SimpleKineticTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntityType;

public class WormGearTile extends SimpleKineticTileEntity {
  public WormGearTile(TileEntityType<? extends SimpleKineticTileEntity> type) {
    super(type);
  }

  @Override
  public void addBehaviours(List<TileEntityBehaviour> behaviours) {
    // Prevent bracket generation.
  }

  @Override
  protected boolean canPropagateDiagonally(IRotate block, BlockState state) {
    return false;
  }
}
