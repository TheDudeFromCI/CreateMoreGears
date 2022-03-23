package me.ci.moregears.foundation;

import net.minecraft.block.Block;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class ShapeBuilder {
    private VoxelShape shape;

    public ShapeBuilder() {
        this.shape = VoxelShapes.empty();
    }

    public ShapeBuilder add(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.shape = VoxelShapes.or(this.shape, Block.box(x1, y1, z1, x2, y2, z2));
        return this;
    }

    public ShapeBuilder subtract(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.shape = VoxelShapes.join(shape, Block.box(x1, y1, z1, x2, y2, z2), IBooleanFunction.ONLY_FIRST);
        return this;
    }

    public VoxelShape build() {
        return this.shape;
    }
}
