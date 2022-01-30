package me.ci.moregears.registry;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {
  private static Tags.IOptionalNamedTag<Block> createForgeTag(String name) {
    return BlockTags.createOptional(new ResourceLocation("forge", name));
  }

  public final Tags.IOptionalNamedTag<Block> woodGear = createForgeTag("gear/wood");
}
