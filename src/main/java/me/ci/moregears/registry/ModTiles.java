package me.ci.moregears.registry;

import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;
import me.ci.moregears.tiles.WormGearTile;

public class ModTiles {
  public final TileEntityEntry<WormGearTile> wormGear;

  public ModTiles(CreateRegistrate registrate, ModBlocks blocks) {
    wormGear =
        registrate
            .tileEntity("worm_gear", WormGearTile::new)
            .instance(() -> SingleRotatingInstance::new)
            .validBlock(blocks.wormGear)
            .renderer(() -> KineticTileEntityRenderer::new)
            .register();
  }
}
