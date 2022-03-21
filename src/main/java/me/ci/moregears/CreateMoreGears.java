package me.ci.moregears;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.NonNullLazyValue;

import me.ci.moregears.registry.ModBlockPartials;
import me.ci.moregears.registry.ModBlocks;
import me.ci.moregears.registry.ModItemGroups;
import me.ci.moregears.registry.ModTiles;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CreateMoreGears.MOD_ID)
public class CreateMoreGears {

    private static final NonNullLazyValue<CreateRegistrate> REGISTRATE = CreateRegistrate.lazy(CreateMoreGears.MOD_ID);

    public static final String MOD_ID = "createmoregears";
    public static final String MOD_NAME = "Create: More Gears";
    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("deprecation")
    public static CreateRegistrate getRegistrate() {
        return REGISTRATE.get();
    }

    public CreateMoreGears() {
        ModItemGroups.register();
        ModBlocks.register();
        ModBlockPartials.register();
        ModTiles.register();
    }
}
