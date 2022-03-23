package me.ci.moregears.foundation;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class EntityTurretTarget implements ITurretTarget {

    private final IAimmable turret;
    private final Entity entity;

    public EntityTurretTarget(IAimmable turret, Entity entity) {
        this.turret = turret;
        this.entity = entity;
    }

    @Override
    public Vector3d getPosition() {
        return this.entity.getBoundingBox().getCenter();
    }

    public Entity getEntity() {
        return this.entity;
    }

    public IAimmable getTurret() {
        return this.turret;
    }

    @Override
    public boolean isStillValid() {
        return this.entity.isAlive()
            && this.entity.position().distanceTo(this.turret.getAimPos()) < this.turret.getRange()
            && !this.entity.isInvisible()
            && canSeeTarget();
    }

    public boolean canSeeTarget() {
        Vector3d aim = this.turret.getAimPos();
        Vector3d pos = getPosition();

        return this.entity.level.clip(new RayTraceContext(aim, pos, RayTraceContext.BlockMode.COLLIDER,
            RayTraceContext.FluidMode.NONE, null)).getType() == RayTraceResult.Type.MISS;
    }

}
