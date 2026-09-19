package com.cherrybees.entity;

import com.cherrybees.CherryBeesMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CherryBeeEntity extends BeeEntity {
    public CherryBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        List<Goal> toRemove = new ArrayList<>();
        for (PrioritizedGoal prioritizedGoal : this.goalSelector.getGoals()) {
            String name = prioritizedGoal.getGoal().getClass().getSimpleName();
            if (name.equals("PollinateGoal") || name.equals("MoveToFlowerGoal") || name.equals("ValidateFlowerGoal")) {
                toRemove.add(prioritizedGoal.getGoal());
            }
        }
        for (Goal goal : toRemove) {
            this.goalSelector.remove(goal);
        }
        this.goalSelector.add(5, new CherryPollinateGoal(this));
    }

    @Override
    public CherryBeeEntity createChild(ServerWorld world, PassiveEntity entity) {
        return CherryBeesMod.CHERRY_BEE.create(world, SpawnReason.BREEDING);
    }
}
