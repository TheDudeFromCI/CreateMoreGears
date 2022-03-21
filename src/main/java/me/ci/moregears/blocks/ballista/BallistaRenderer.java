package me.ci.moregears.blocks.ballista;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.MatrixTransformStack;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.PartialBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;

import me.ci.moregears.registry.ModBlockPartials;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class BallistaRenderer extends KineticTileEntityRenderer {

    public BallistaRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(KineticTileEntity te) {
        return CreateClient.BUFFER_CACHE
            .renderPartial(ModBlockPartials.BALLISTA_COGWHEEL, te.getBlockState());
    }

    @Override
    public boolean shouldRenderOffScreen(KineticTileEntity pTe) {
        return true;
    }

    @Override
    protected void renderSafe(KineticTileEntity te, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer,
        int light, int overlay) {
        super.renderSafe(te, partialTicks, ms, buffer, light, overlay);

        BallistaTile ballista = (BallistaTile) te;
        BlockState blockState = te.getBlockState();

        IVertexBuilder builder = buffer.getBuffer(RenderType.solid());
        MatrixStack msLocal = new MatrixStack();
        MatrixTransformStack msr = new MatrixTransformStack(msLocal);

        SuperByteBuffer turntable = PartialBufferer.get(ModBlockPartials.BALLISTA_TURNTABLE, blockState).light(light);
        SuperByteBuffer crossbow = PartialBufferer.get(ModBlockPartials.BALLISTA_CROSSBOW, blockState).light(light);

        msr.translate(0.5, 10.5 / 16.0, 0.5);
        msr.rotateY(ballista.getYaw());
        turntable.transform(msLocal).renderInto(ms, builder);

        msr.translateY(4.3 / 16.0);
        msr.rotateX(ballista.getPitch());
        crossbow.transform(msLocal).renderInto(ms, builder);
    }
}
