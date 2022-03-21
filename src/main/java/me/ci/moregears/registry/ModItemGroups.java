package me.ci.moregears.registry;

import me.ci.moregears.CreateMoreGears;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class ModItemGroups {

    public static final ItemGroup DEFAULT_GROUP = new ItemGroup(CreateMoreGears.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return ModBlocks.WORM_GEAR.asStack();
        }
    };

    public static void register() {
        // Load static fields
    }

    private ModItemGroups() {
    }
}
