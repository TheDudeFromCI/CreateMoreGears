package me.ci.moregears.registry;

import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;

import me.ci.moregears.CreateMoreGears;
import me.ci.moregears.blocks.ballista.BallistaTile;
import me.ci.moregears.blocks.worm_gear.WormGearTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModTiles {

    private static final CreateRegistrate REGISTRATE = CreateMoreGears.getRegistrate();

    public static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister
        .create(ForgeRegistries.TILE_ENTITIES, CreateMoreGears.MOD_ID);

    public static final TileEntityEntry<WormGearTile> WORM_GEAR = REGISTRATE
        .tileEntity("worm_gear", WormGearTile::new)
        .instance(() -> SingleRotatingInstance::new)
        .validBlock(ModBlocks.WORM_GEAR)
        .renderer(() -> KineticTileEntityRenderer::new)
        .register();

    public static final RegistryObject<TileEntityType<BallistaTile>> BALLISTA = REGISTER
        .register("ballista", () -> TileEntityType.Builder.of(BallistaTile::new,
            ModBlocks.BALLISTA.get()).build(null));

    private ModTiles() {
    }
}
