package me.ci.moregears.blocks.ballista;

import com.jozufozu.flywheel.backend.instancing.Instancer;
import com.jozufozu.flywheel.backend.material.MaterialManager;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.RotatingData;
import com.simibubi.create.content.contraptions.base.SingleRotatingInstance;

public class BallistaCog extends SingleRotatingInstance {

    public BallistaCog(MaterialManager<?> modelManager, KineticTileEntity tile) {
        super(modelManager, tile);
    }

    // @Override
    // protected Instancer<RotatingData> getModel() {
    // return getRotatingMaterial().getModel(ModBlockPartials.BALLISTA_COGWHEEL,
    // tile.getBlockState());
    // }

}
