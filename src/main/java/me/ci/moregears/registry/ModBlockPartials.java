package me.ci.moregears.registry;

import com.jozufozu.flywheel.core.PartialModel;

import me.ci.moregears.CreateMoreGears;
import net.minecraft.util.ResourceLocation;

public final class ModBlockPartials {

    public static final PartialModel BALLISTA_COGWHEEL = get("ballista/cogwheel");

    private static PartialModel get(String path) {
        ResourceLocation loc = new ResourceLocation(CreateMoreGears.MOD_ID, "block/" + path);
        return new PartialModel(loc);
    }

    public static void register() {
        // Load static fields
    }

    private ModBlockPartials() {
    }
}
