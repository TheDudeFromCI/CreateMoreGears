package me.ci.moregears.foundation;

import net.minecraft.util.math.vector.Vector3d;

public interface ITurretTarget {

    Vector3d getPosition();

    boolean isStillValid();
}
