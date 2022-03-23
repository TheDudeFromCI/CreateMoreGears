package me.ci.moregears.registry;

import java.util.function.Supplier;

import me.ci.moregears.CreateMoreGears;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {

    public static final DeferredRegister<Item> REGISTER = DeferredRegister
        .create(ForgeRegistries.ITEMS, CreateMoreGears.MOD_ID);

    public static final RegistryObject<BlockItem> BALLISTA = REGISTER
        .register("ballista", block(ModBlocks.BALLISTA));

    private static <A extends Block> Supplier<BlockItem> block(RegistryObject<A> blockReg) {
        return () -> new BlockItem(blockReg.get(), new Item.Properties().tab(ModItemGroups.DEFAULT_GROUP));
    }

    private ModItems() {
    }
}
