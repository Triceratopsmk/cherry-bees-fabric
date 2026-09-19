package com.cherrybees.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;

/**
 * Cherry bees only pollinate fallen cherry petals (pink_petals),
 * never regular flowers.
 */
public class CherryPollinateGoal extends Goal {
    private static final int SEARCH_RADIUS = 6;
    private static final int SEARCH_HEIGHT = 4;
    private static final double PICKUP_DISTANCE_SQ = 4.0;
    private static final int POLLINATE_TICKS_REQUIRED = 60;

    private final CherryBeeEntity bee;
    private int pollinateTicks;

    public CherryPollinateGoal(CherryBeeEntity bee) {
        this.bee = bee;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.bee.hasNectar() || this.bee.hasFlower()) {
            return false;
        }
        BlockPos pos = findNearestPetals();
        if (pos == null) {
            return false;
        }
        this.bee.setFlowerPos(pos);
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (this.bee.hasNectar() || !this.bee.hasFlower()) {
            return false;
        }
        BlockPos pos = this.bee.getFlowerPos();
        return pos != null && isPetals(this.bee.getEntityWorld(), pos);
    }

    @Override
    public void start() {
        this.pollinateTicks = 0;
    }

    @Override
    public void stop() {
        this.bee.clearFlowerPos();
        this.pollinateTicks = 0;
    }

    @Override
    public void tick() {
        BlockPos pos = this.bee.getFlowerPos();
        if (pos == null) {
            return;
        }
        double targetX = pos.getX() + 0.5;
        double targetY = pos.getY() + 0.3;
        double targetZ = pos.getZ() + 0.5;
        double distanceSq = this.bee.squaredDistanceTo(targetX, targetY, targetZ);
        if (distanceSq > PICKUP_DISTANCE_SQ) {
            this.bee.getNavigation().startMovingTo(targetX, targetY, targetZ, 1.0);
        } else {
            this.pollinateTicks++;
            if (this.pollinateTicks >= POLLINATE_TICKS_REQUIRED) {
                this.bee.setHasNectar(true);
                this.bee.resetPollinationTicks();
            }
        }
    }

    private static boolean isPetals(World world, BlockPos pos) {
        return world.getBlockState(pos).isOf(Blocks.PINK_PETALS);
    }

    private BlockPos findNearestPetals() {
        World world = this.bee.getEntityWorld();
        BlockPos center = this.bee.getBlockPos();
        BlockPos closest = null;
        double closestDistanceSq = Double.MAX_VALUE;
        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dy = -SEARCH_HEIGHT; dy <= SEARCH_HEIGHT; dy++) {
                for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                    BlockPos pos = center.add(dx, dy, dz);
                    if (isPetals(world, pos)) {
                        double distanceSq = pos.getSquaredDistance(center);
                        if (distanceSq < closestDistanceSq) {
                            closestDistanceSq = distanceSq;
                            closest = pos;
                        }
                    }
                }
            }
        }
        return closest;
    }
}
