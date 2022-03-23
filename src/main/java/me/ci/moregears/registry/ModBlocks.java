package me.ci.moregears.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticBlockModel;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;

import me.ci.moregears.CreateMoreGears;
import me.ci.moregears.blocks.ballista.BallistaBlock;
import me.ci.moregears.blocks.worm_gear.WormGearBlock;
import me.ci.moregears.blocks.worm_gear.WormGearItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {

    private static final CreateRegistrate REGISTRATE = CreateMoreGears.getRegistrate();

    public static final DeferredRegister<Block> REGISTER = DeferredRegister
        .create(ForgeRegistries.BLOCKS, CreateMoreGears.MOD_ID);

    public static final BlockEntry<WormGearBlock> WORM_GEAR = REGISTRATE
        .itemGroup(() -> ModItemGroups.DEFAULT_GROUP)
        .block("worm_gear", WormGearBlock::new)
        .initialProperties(Material.STONE)
        .properties(p -> p.sound(SoundType.WOOD))
        .transform(BlockStressDefaults.setNoImpact())
        .blockstate((c, p) -> BlockStateGen.axisBlock(c, p, b -> p.models()
            .getExistingFile(p.modLoc("block/worm_gear/block"))))
        .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
        .defaultLoot()
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get(), 2)
            .pattern("cc")
            .define('c', AllBlocks.COGWHEEL.get())
            .save(prov))
        .item(WormGearItem::new)
        .build()
        .register();

    public static final RegistryObject<BallistaBlock> BALLISTA = REGISTER
        .register("ballista", BallistaBlock::new);

    private ModBlocks() {
    }
}
