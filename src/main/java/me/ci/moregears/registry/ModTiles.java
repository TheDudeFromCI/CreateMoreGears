package me.ci.moregears.registry;

import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;

import me.ci.moregears.CreateMoreGears;
import me.ci.moregears.tiles.BallistaTile;
import me.ci.moregears.tiles.WormGearTile;

public final class ModTiles {

    private static final CreateRegistrate REGISTRATE = CreateMoreGears.getRegistrate();

    public static final TileEntityEntry<WormGearTile> WORM_GEAR = REGISTRATE
        .tileEntity("worm_gear", WormGearTile::new)
        .instance(() -> SingleRotatingInstance::new)
        .validBlock(ModBlocks.WORM_GEAR)
        .renderer(() -> KineticTileEntityRenderer::new)
        .register();

    public static final TileEntityEntry<BallistaTile> BALLISTA = REGISTRATE
        .tileEntity("ballista", BallistaTile::new)
        .instance(() -> SingleRotatingInstance::new)
        .validBlock(ModBlocks.BALLISTA)
        .renderer(() -> KineticTileEntityRenderer::new)
        .register();

    public static void register() {
        // Just load the static fields
    }

    private ModTiles() {
    }
}
