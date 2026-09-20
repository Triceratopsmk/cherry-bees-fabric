package com.cherrybees.entity;

import com.cherrybees.CherryBeesMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

/**
 * A wild boar - a passive forest animal, reusing the pig's model, AI and breeding
 * behaviour but with its own texture, spawn rules and offspring.
 */
public class WildBoarEntity extends PigEntity {
    public WildBoarEntity(EntityType<? extends PigEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public WildBoarEntity createChild(ServerWorld world, PassiveEntity entity) {
        return CherryBeesMod.WILD_BOAR.create(world, SpawnReason.BREEDING);
    }
}
