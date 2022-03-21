package me.ci.moregears.tiles;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;

public class BallistaTile extends KineticTileEntity {

    private final ItemStackHandler inventory;

    public BallistaTile(TileEntityType<?> typeIn) {
        super(typeIn);

        this.inventory = new ItemStackHandler();
    }

}
