package me.ci.moregears.blocks.ballista;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.simibubi.create.content.contraptions.base.KineticTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class BallistaTile extends KineticTileEntity {

    public static final int RELOAD_TICKS = 60;
    public static final int RANGE = 16;

    private final ItemStackHandler inventory;
    private WeakReference<LivingEntity> targetEntity;
    private int timer;
    private double yaw;
    private double pitch;

    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

    public BallistaTile(TileEntityType<?> typeIn) {
        super(typeIn);
        this.inventory = new ItemStackHandler();
    }

    public boolean hasAmmo() {
        return !this.inventory.getStackInSlot(0).isEmpty();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        // TODO Play bow streching sounds
    }

    @Override
    public void tick() {
        super.tick();
        findEntity();

        if (!canFireArrow()) {
            this.timer = RELOAD_TICKS;
            return;
        }

        tickAim();
        if (this.timer > 0) {
            this.timer--;
            return;
        }

        fireArrow();
        this.targetEntity = null;
        this.timer = RELOAD_TICKS;
    }

    private boolean canFireArrow() {
        return getSpeed() != 0 /* && hasAmmo() */ && getTargetEntity().isPresent();
    }

    public Optional<LivingEntity> getTargetEntity() {
        if (this.targetEntity == null)
            return Optional.empty();

        LivingEntity entity = this.targetEntity.get();
        if (entity == null || !entity.isAlive() || entity.isInvisible() || !canSee(entity)) {
            this.targetEntity = null;
            return Optional.empty();
        } else
            return Optional.of(entity);
    }

    private void findEntity() {
        if (getTargetEntity().isPresent())
            return;

        AxisAlignedBB bounds = new AxisAlignedBB(getBlockPos()).inflate(RANGE);
        Vector3d aim = getAimPos();

        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, bounds)
            .stream()
            .filter(Entity::isAlive)
            .filter(not(Entity::isInvisible))
            .filter(e -> e.distanceToSqr(aim) < RANGE * RANGE)
            .filter(e -> canSee(e, aim))
            .sorted((a, b) -> Double.compare(a.distanceToSqr(aim), b.distanceToSqr(aim)))
            .collect(Collectors.toList());

        if (entities.isEmpty())
            return;

        LivingEntity target = entities.get(0);
        this.targetEntity = new WeakReference<>(target);
    }

    private boolean canSee(Entity e) {
        return canSee(e, getAimPos());
    }

    private boolean canSee(Entity e, Vector3d aim) {
        Vector3d vector3d1 = new Vector3d(e.getX(), e.getEyeY(), e.getZ());
        return this.level.clip(new RayTraceContext(aim, vector3d1, RayTraceContext.BlockMode.COLLIDER,
            RayTraceContext.FluidMode.NONE, null)).getType() == RayTraceResult.Type.MISS;
    }

    private void fireArrow() {
        // TODO Spawn arrow entity
    }

    private void tickAim() {
        Optional<LivingEntity> optionalEntity = getTargetEntity();
        if (!optionalEntity.isPresent())
            return;

        LivingEntity entity = optionalEntity.get();

        Vector3d crossbow = getAimPos();
        Vector3d target = new Vector3d(entity.getX(), entity.getEyeY(), entity.getZ());
        double targetYaw = -calcTargetYaw(crossbow, target);
        double targetPitch = calcTargetPitch(crossbow, target);

        this.yaw = targetYaw;
        this.pitch = targetPitch;
        setChanged();
    }

    private Vector3d getAimPos() {
        BlockPos pos = getBlockPos();
        return new Vector3d(pos.getX() + 0.5, pos.getY() + 14.8 / 16.0, pos.getZ() + 0.5);
    }

    private double calcTargetYaw(Vector3d center, Vector3d target) {
        double d0 = target.x - center.x;
        double d1 = target.z - center.z;
        return (MathHelper.atan2(d1, d0) * (180.0 / Math.PI)) - 90.0;
    }

    private double calcTargetPitch(Vector3d center, Vector3d target) {
        double d0 = target.x - center.x;
        double d1 = target.y - center.y;
        double d2 = target.z - center.z;
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        return -MathHelper.atan2(d1, d3) * (180.0 / Math.PI);
    }

    @Override
    protected void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Iimer", this.timer);
        compound.putDouble("Yaw", this.yaw);
        compound.putDouble("Pitch", this.pitch);
        compound.put("Inventory", this.inventory.serializeNBT());
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        this.timer = compound.getInt("Timer");
        this.yaw = compound.getDouble("Yaw");
        this.pitch = compound.getDouble("Pitch");
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.targetEntity = null;
    }

    public double getYaw() {
        return this.yaw;
    }

    public double getPitch() {
        return this.pitch;
    }

}
