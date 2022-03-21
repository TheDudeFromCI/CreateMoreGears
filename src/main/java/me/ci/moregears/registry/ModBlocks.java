package me.ci.moregears.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticBlockModel;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;

import me.ci.moregears.CreateMoreGears;
import me.ci.moregears.blocks.ballista.BallistaBlock;
import me.ci.moregears.blocks.worm_gear.WormGearBlock;
import me.ci.moregears.blocks.worm_gear.WormGearItem;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;

public final class ModBlocks {

    private static final CreateRegistrate REGISTRATE = CreateMoreGears.getRegistrate();

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

    public static final BlockEntry<BallistaBlock> BALLISTA = REGISTRATE
        .itemGroup(() -> ModItemGroups.DEFAULT_GROUP)
        .block("ballista", BallistaBlock::new)
        .initialProperties(Material.WOOD)
        .properties(p -> p.sound(SoundType.WOOD))
        .transform(BlockStressDefaults.setImpact(3.0))
        .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
        .item()
        .transform(ModelGen.customItemModel())
        .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(ctx.get(), 2)
            .pattern("ccc")
            .define('c', AllBlocks.COGWHEEL.get())
            .save(prov))
        .register();

    public static void register() {
        Create.registrate().addToSection(WORM_GEAR, AllSections.KINETICS);
        Create.registrate().addToSection(BALLISTA, AllSections.KINETICS);
    }

    private ModBlocks() {
    }
}
