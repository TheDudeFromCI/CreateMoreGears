package me.ci.moregears.registry;

import me.ci.moregears.CreateMoreGears;
import me.ci.moregears.blocks.ballista.BallistaRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = CreateMoreGears.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModClient {

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTiles.BALLISTA.get(), BallistaRenderer::new);
    }

    private ModClient() {
    }
}
