package me.ci.moregears;

import com.simibubi.create.foundation.data.CreateRegistrate;
import me.ci.moregears.registry.ModBlocks;
import me.ci.moregears.registry.ModTags;
import me.ci.moregears.registry.ModTiles;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CreateMoreGears.MOD_ID)
public class CreateMoreGears {
  public static final String MOD_ID = "createmoregears";
  public static final String MOD_NAME = "Create: More Gears";
  public static final Logger LOGGER = LogManager.getLogger();
  public static CreateMoreGears INSTANCE;

  public final ModTiles tiles;
  public final ModTags tags;
  public final ModBlocks blocks;

  public CreateMoreGears() {
    INSTANCE = this;

    @SuppressWarnings("deprecation")
    CreateRegistrate registrate = CreateRegistrate.lazy(CreateMoreGears.MOD_ID).get();

    ItemGroup itemGroup =
        new ItemGroup(CreateMoreGears.MOD_ID) {
          @Override
          public ItemStack makeIcon() {
            return new ItemStack(blocks.wormGear.get());
          }
        };
    registrate = registrate.itemGroup(() -> itemGroup);

    tags = new ModTags();
    blocks = new ModBlocks(registrate, tags);
    tiles = new ModTiles(registrate, blocks);
  }
}
