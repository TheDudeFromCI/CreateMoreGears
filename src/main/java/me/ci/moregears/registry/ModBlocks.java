package me.ci.moregears.registry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.AllSections;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedKineticBlockModel;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.providers.RegistrateRecipeProvider;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import me.ci.moregears.blocks.WormGearBlock;
import me.ci.moregears.items.WormGearItem;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.data.ShapedRecipeBuilder;

public class ModBlocks {
    public final BlockEntry<WormGearBlock> wormGear;

    public ModBlocks(CreateRegistrate registrate, ModTags tags) {
        wormGear = registrate
                .block("worm_gear", WormGearBlock::new)
                .initialProperties(Material.STONE)
                .properties(p -> p.sound(SoundType.WOOD))
                .transform(BlockStressDefaults.setNoImpact())
                .blockstate(
                        (c, p) -> BlockStateGen.axisBlock(
                                c, p, (b) -> p.models().getExistingFile(p.modLoc("block/worm_gear/block"))))
                .onRegister(CreateRegistrate.blockModel(() -> BracketedKineticBlockModel::new))
                .tag(tags.woodGear)
                .defaultLoot()
                .recipe(
                        (ctx, prov) -> {
                            ShapedRecipeBuilder.shaped(ctx.get(), 2)
                                    .pattern("cc")
                                    .define('c', AllBlocks.COGWHEEL.get())
                                    .unlockedBy(
                                            "has_cogwheel",
                                            RegistrateRecipeProvider.hasItem(AllBlocks.COGWHEEL.get()))
                                    .save(prov);
                        })
                .item(WormGearItem::new)
                .build()
                .register();

        Create.registrate().addToSection(wormGear, AllSections.KINETICS);
    }
}
