package me.ci.moregears.foundation;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public interface IAimmable {

    double getYaw();

    double getPitch();

    ITurretTarget getAimTarget();

    void setAimTarget(ITurretTarget target);

    Vector3d getAimPos();

    default boolean forgetTargetOnShoot() {
        return false;
    }

    default float getRange() {
        return 16f;
    }

    default double calcTargetYaw(Vector3d center, Vector3d target) {
        double d0 = target.x - center.x;
        double d1 = target.z - center.z;
        return (MathHelper.atan2(d1, d0) * (180.0 / Math.PI)) - 90.0;
    }

    default double calcTargetPitch(Vector3d center, Vector3d target) {
        double d0 = target.x - center.x;
        double d1 = target.y - center.y;
        double d2 = target.z - center.z;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        return -MathHelper.atan2(d1, d3) * (180.0 / Math.PI);
    }

    default List<EntityTurretTarget> lookForMobTargets(World world) {
        Vector3d aim = getAimPos();
        float range = getRange();
        AxisAlignedBB bounds = new AxisAlignedBB(aim, aim).inflate(range);

        return world.getEntitiesOfClass(LivingEntity.class, bounds)
            .stream()
            .filter(Entity::isAlive)
            .filter(e -> !e.isInvisible())
            .filter(e -> e.distanceToSqr(aim) < range * range)
            .map(e -> new EntityTurretTarget(this, e))
            .filter(EntityTurretTarget::canSeeTarget)
            .sorted((a, b) -> Double.compare(a.getEntity().distanceToSqr(aim), b.getEntity().distanceToSqr(aim)))
            .collect(Collectors.toList());
    }
}
