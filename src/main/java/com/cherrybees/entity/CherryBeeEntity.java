package com.cherrybees.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;

public class CherryBeeEntity extends BeeEntity {
    public CherryBeeEntity(EntityType<? extends BeeEntity> entityType, World world) {
        super(entityType, world);
    }
}

